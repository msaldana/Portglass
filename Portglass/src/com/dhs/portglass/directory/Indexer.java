package com.dhs.portglass.directory;


import static java.nio.file.Files.exists;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import static com.dhs.portglass.IO.IO.deserialize;
import static com.dhs.portglass.IO.IO.serialize;
import static com.dhs.portglass.directory.Indexer.IndexAction.ADD;
import static com.dhs.portglass.directory.Indexer.IndexAction.MODIFY;
import static com.dhs.portglass.directory.Indexer.IndexAction.DELETE;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.dhs.portglass.IO.PropertyConfig;
import com.dhs.portglass.directory.DirectoryWatcher;
import com.dhs.portglass.services.SensorManager;
import com.dhs.portglass.services.ThreadPoolManager;
  


/**
 * Indexes all sensor documents (.txt) and hyperspectral images (.png) in a specified
 * directory and automatically indexes any documents that are added to the directory
 * during execution. The indexing process consists of adding all data to the database
 * and do any image conversion to appropriate sizes. The close() method must be called 
 * to shutdown all threads, flush all changes to disk and release all resources.
 * 
 * @author Manuel R Saldana
 */
public class Indexer {
	/**
	 * Set<Integer> indexedDocuments contains the hashCodes of documents that
	 * have already been indexed to avoid doing costly index accesses. Any time
	 * a document is indexed or deleted, indexedDocuments is be updated to
	 * reflect these changes. The hashCode should be obtained by calling the
	 * getHashCode(Path) function. The Set is serialized whenever index changes
	 * are flushed to disk to ensure consistency. If the deserialized Set
	 * contains a different number of entries as the index, the Set is cleared
	 * and repopulated through a call to repairSet(). This Set is a
	 * ConcurrentSkipListSet, which is both threadsafe and scalable.
	 * 
	 * Since indexing is a CPU-bound operation, a ThreadPoolExecutor is used to
	 * take advantage of hardware concurrency and improve indexing times. It
	 * also allows indexing to occur in the background rather than stopping
	 * the main application thread. The ThreadPoolExecutor kills idle threads
	 * aggressively (set a low keep-alive time) since the application may go
	 * long periods of time without indexing new documents. The Indexer's
	 * shutdown() method will shutdown the thread pool. 
	 * 
	 * A ScheduledExecutorService access method is provided for implementing
	 * scheduled flushes to the system, so that the indexer updates the the files
	 * that have been indexed to the serialization file.
	 * Calling shutdown() will also shutdown this executor.
	 */
	
	public static final int PROCESSORS =
			Runtime.getRuntime().availableProcessors();
	public static final int THREAD_TIMEOUT_SECONDS = 10;
	
	private volatile Path documentsPath;
	private volatile Path indexPath;
	private volatile Path serializedFile;
	private volatile ConcurrentMap<Integer, Long> indexCache;
	private volatile ThreadPoolExecutor threadPoolExecutor = ThreadPoolManager.getInstance().getThreadPoolExecutor();
	private volatile ScheduledExecutorService scheduledExecutor =
			Executors.newSingleThreadScheduledExecutor();

	
	/**
	 * Creates a new Indexer and indexes any unindexed documents. An exception
	 * will be thrown if the directory with the sensor text files doesn't exist.
	 *  The index directory need not exist and will be created if necessary.
	 * @param documentsPath The directory where the documents to be indexed are
	 * @exception IOException The usual cause is that the document directory
	 * 	doesn't exist.
	 */
	public Indexer(Path documentsPath) throws IOException,
			ClassNotFoundException {
		try {
			init(documentsPath);
			System.out.print("Indexer Starting..");
		} catch (Exception e) {
			shutdown();
			throw e;
		}
	}
	
	/**
	 * Initializes the Indexer instance by starting a DirectoryWatcher instance
	 * with a PoolThreadExecutor. It also creates an 'indexedDocuments.serialized'
	 * file on the given path. The DirectoryWatcher will monitor this directory
	 * and all changes will be reflected upon the serialized file. Thus, when
	 * a file is added, the database is updated and the file is serialized so the
	 * DirectoryWatcher knows that is has been indexed. When alterations on the file
	 * or deletion of the file is detected by the DirectoryWatcher, the file is
	 * updated in the serialization.
	 * @param documentsPath Path to data to be monitored.
	 * @throws IOException Document Directory doesn't exist.
	 * @throws ClassNotFoundException
	 */
	private void init(Path documentsPath) throws
			 IOException, ClassNotFoundException {
		if (!exists(documentsPath)) {
			throw new FileNotFoundException(
					"Document directory doesn't exist.");
		}
		
		if (documentsPath.isAbsolute()) {
			
			this.documentsPath = Paths.get("").toRealPath().
					relativize(documentsPath);
			
		} else {
			
			this.documentsPath = documentsPath;
		
		}
		
		this.indexPath = documentsPath;
		this.indexPath = indexPath.normalize();
	
		serializedFile =
				this.indexPath.resolve("indexedDocuments.serialized");
		System.out.println(documentsPath);
		indexCache = getIndexedDocuments();
		System.out.println(documentsPath);
		indexNewFiles();
		
		// Start scheduled tasks
		
		scheduledExecutor.scheduleAtFixedRate(new FlushToDiskRunnable(),
				10, 60, TimeUnit.SECONDS);
		
		// Start DirectoryWatcher
		threadPoolExecutor.execute(new DirectoryWatcher(this.documentsPath,
				new IndexingFilter(), threadPoolExecutor));
	}

	/**
	 * Shutdown all threads and close index-related resources.
	 */
	public void shutdown() {
		System.out.println("Indexer: SHUTTING DOWN");
		if (scheduledExecutor != null) { scheduledExecutor.shutdown(); }
		if (threadPoolExecutor != null) { threadPoolExecutor.shutdownNow(); }
	}
	

	

	/**
	 * If indexedDocuments had previously been serialized, deserializes it.
	 * Otherwise, creates a new Set.
	 */
	@SuppressWarnings("unchecked")
	private ConcurrentMap<Integer, Long> getIndexedDocuments()
			throws IOException, ClassNotFoundException {
		ConcurrentHashMap<Integer, Long> indexedDocuments;
		if (exists(serializedFile)) {
			indexedDocuments = (ConcurrentHashMap<Integer, Long>)
					deserialize(serializedFile);
			
		} else {
			indexedDocuments = new ConcurrentHashMap<Integer, Long>();
		}
		//Serialize the Set to test for writing-related IOExceptions.
		serialize(indexedDocuments, serializedFile);
		return indexedDocuments;
	}
	

	
	/**
	 * Indexes all text files that aren't already indexed.
	 */
	private void indexNewFiles() throws IOException {
		
		SimpleFileVisitor<Path> fileVisitor = new IndexingFileVisitor(indexCache);
		System.out.println(documentsPath);
		Files.walkFileTree(documentsPath, fileVisitor);
	}
	
	/**
	 * Returns the hashCode used for indexing.
	 * @throws IOException 
	 */
	private int getHashCode(Path file) throws IOException {
		Path relativePath = documentsPath.relativize(file);
		return relativePath.hashCode();
	}
	
	
	
	/**
	 * Commits all index changes to disk and serializes the set of indexed docs.
	 */
	private class FlushToDiskRunnable implements Runnable {
		@Override public void run() {
			try {
				
				serialize(indexCache, serializedFile);
			} catch (IllegalStateException e) {
				System.err.format("Indexer: OUT OF MEMORY (%s)%n", e);
				System.err.println("Indexer: ATTEMPTING TO RESTART...");
				shutdown();
				try {
					init(documentsPath);
				} catch (ClassNotFoundException | IOException e1) {
					System.err.format("Indexer: FATAL ERROR, " +
							"SHUTTING DOWN (%s)%n ", e1);
				}
			}
			catch (IOException e) {
				System.err.println("Indexer: Couldn't write to disk!");
				e.printStackTrace();
			}
		}
	}
	
	public static enum IndexAction {
		ADD, MODIFY, DELETE;
	}
	
	/**
	 * Indexes a single sensor text file.
	 */
	private class SensorIndexingRunnable implements Runnable {
		private final Path file;
		private final IndexAction action;
		
		public SensorIndexingRunnable(Path file, IndexAction action) {
			this.file = file;
			this.action = action;
		}
		
		@Override public void run() {
			try {
				int hashCode = getHashCode(file);
				
				long modifiedTime = 0;
				
				if (action == ADD || action == MODIFY) {
					String name = file.getFileName().toString();
					// Strip last four characters (.txt)
					name = name.substring(0, name.length()-4);
					modifiedTime = Files.getLastModifiedTime(file).toMillis();
					indexCache.put(hashCode, modifiedTime);
					String[] parsedFileContents = PropertyConfig.loadProperties(file.toFile());
					SensorManager.getInstance().indexAddEvent(parsedFileContents,
							hashCode, modifiedTime);
					
				} else if (action == DELETE) {
					SensorManager.getInstance().indexDeleteEvent(hashCode);
					indexCache.remove(hashCode);
				}
				System.out.format("Indexer: %s %s%n", action, file);
			} catch (IOException e) {
				System.err.format("Indexer: Couldn't %s %s (%s)%n", action,
						file, e);
			}
		}
	}
	
	/**
	 * File visitor for recursively indexing all Sensor Text files in a directory.
	 */
	private class IndexingFileVisitor extends SimpleFileVisitor<Path> {
		
		HashMap<Integer, Long> cacheCopy =
				new HashMap<Integer, Long>();
		
		public IndexingFileVisitor(ConcurrentMap<Integer, Long> cache) {
			cacheCopy.putAll(cache);
		}
		
		@Override public FileVisitResult visitFile(Path file,
				BasicFileAttributes attrs) throws IOException {
			int hashCode = getHashCode(file);
			boolean isTxt = file.toString().endsWith(".txt");
			boolean indexed = cacheCopy.containsKey(hashCode);
			if (isTxt && indexed) {
				long modifiedTime = Files.getLastModifiedTime(file).toMillis();
				if (modifiedTime != cacheCopy.get(hashCode)) {
					threadPoolExecutor.execute(
							new SensorIndexingRunnable(file, MODIFY));
				}
				// Remove existing files so only deleted files remain
				cacheCopy.remove(hashCode);
			} else if (isTxt && !indexed) {
				threadPoolExecutor.execute(new SensorIndexingRunnable(file, ADD));
			}
			return FileVisitResult.CONTINUE;
		}
		
		@Override public FileVisitResult postVisitDirectory(Path dir,
				IOException exc) {
			try {
				if (Files.isSameFile(dir, documentsPath)) {
					for (int hashCode: cacheCopy.keySet()) {
						//"hashCode",String.valueOf(hashCode);
						//indexWriter.deleteDocuments(term);
						System.out.println("Indexer: File with hash code " +
								hashCode + " missing. Removing from index.");
					}
				}
			} catch (IOException e) {
				System.err.format("Indexer: Couldn't remove deleted files " +
						"from index (%s)%n", e);
			}
			return FileVisitResult.CONTINUE;
		}
	}
	
	public class IndexingFilter  implements WatchEventFilter{
		private Runnable NULL_RUNNABLE = new Runnable() {
			 public void run() {}
		};
		
		
		public Runnable getOverflowRunnable(WatchEvent<Object> overflowEvent) {
			return NULL_RUNNABLE;
		}

		
		public Runnable getRunnable(WatchEvent<Path> event, Path contextPath) {
			Kind<Path> eventKind = event.kind();
			System.out.format("DirectoryWatcher: %s %s%n", eventKind,
					contextPath);
			if (contextPath.toString().endsWith(".txt")) {
				if (eventKind == ENTRY_CREATE) {
					return new SensorIndexingRunnable(contextPath, ADD);
				} else if (eventKind == ENTRY_MODIFY) {
					return new SensorIndexingRunnable(contextPath, MODIFY);
				} else if (eventKind == ENTRY_DELETE) {
					return new SensorIndexingRunnable(contextPath, DELETE);
				}
			}
			return NULL_RUNNABLE;
		}
	}
}
