package com.dhs.portglass.directory;

import static com.dhs.portglass.util.IO.deserialize;
import static com.dhs.portglass.util.IO.serialize;

import static java.nio.file.Files.exists;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.dhs.portglass.directory.DirectoryWatcher;



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
	 * A ScheduledExecutorService is used to periodically perform two tasks:
	 * reopening IndexSearchers so changes to the index will be visible to new
	 * searches, and flushing index updates to disk. These tasks are separate
	 * and need not run at similar intervals. Calling shutdown() will also shut
	 * down this executor.
	 */
	
	public static final int PROCESSORS =
			Runtime.getRuntime().availableProcessors();
	public static final int THREAD_TIMEOUT_SECONDS = 10;
	
	private volatile Path documentsPath;
	private volatile Path indexPath;
	private volatile Path serializedFile;
	private volatile ConcurrentMap<Integer, Long> indexCache;
	private volatile ThreadPoolExecutor threadPoolExecutor =
			newThreadPoolExecutor(PROCESSORS+1, THREAD_TIMEOUT_SECONDS);
	private volatile ScheduledExecutorService scheduledExecutor =
			Executors.newSingleThreadScheduledExecutor();

	
	/**
	 * Creates a new Indexer and indexes any unindexed documents. An exception
	 * will be thrown if the directory with the images and text files doesn't exist.
	 *  The index directory need not exist and will be created if necessary.
	 * @param documentsPath The directory where the documents to be indexed are
	 * @param indexPath The directory where the index will be stored
	 * @exception IOException The usual cause is that the document directory
	 * 		doesn't exist.
	 */
	public Indexer(Path documentsPath, Path indexPath) throws IOException,
			ClassNotFoundException {
		try {
			init(documentsPath, indexPath);
		} catch (Exception e) {
			shutdown();
			throw e;
		}
	}
	
	private void init(Path documentsPath, Path indexPath) throws
			 IOException, ClassNotFoundException {
		if (!exists(documentsPath)) {
			throw new FileNotFoundException(
					"Document directory doesn't exist.");
		}
		
		if (documentsPath.isAbsolute()) {
			this.documentsPath = Paths.get(".").toAbsolutePath().
					relativize(documentsPath);
		} else {
			this.documentsPath = documentsPath;
		}
		
		
		this.indexPath = indexPath.normalize();
		serializedFile =
				this.indexPath.resolve("indexedDocuments.serialized");
		
		
		indexNewFiles();
		// Start scheduled tasks
		//scheduledExecutor.scheduleAtFixedRate(new MaybeReopenRunnable(),
			//	10, 30, TimeUnit.SECONDS);
		scheduledExecutor.scheduleAtFixedRate(new FlushToDiskRunnable(),
				10, 60, TimeUnit.SECONDS);
		
		// Start DirectoryWatcher
		//threadPoolExecutor.execute(new DirectoryWatcher(this.documentsPath,
			//	new IndexingFilter(), threadPoolExecutor));
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
	 * Returns a ThreadPoolExecutor with an unbounded queue and the specified
	 * pool size and keep-alive time. The number of threads can be altered by
	 * calling setCorePoolSize(). Because the queue is unbounded, there will
	 * never be more threads than the core pool size; the max pool size has
	 * no effect.
	 * @param threads The maximum number of threads in the pool
	 * @param timeout The keep-alive time for idle threads, measured in seconds
	 */
	private static ThreadPoolExecutor newThreadPoolExecutor(int threads,
			long timeout) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(threads,
				Integer.MAX_VALUE, timeout, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
		executor.allowCoreThreadTimeOut(true);
		return executor;
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
	 * Indexes all PDFs that aren't already indexed.
	 */
	private void indexNewFiles() throws IOException {
		SimpleFileVisitor<Path> fileVisitor = new IndexingFileVisitor(indexCache);
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
					init(documentsPath, indexPath);
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
	 * Indexes a single PDF file.
	 */
	private class PDFIndexingRunnable implements Runnable {
		private final Path file;
		private final IndexAction action;
		
		public PDFIndexingRunnable(Path file, IndexAction action) {
			this.file = file;
			this.action = action;
		}
		
		@Override public void run() {
			try {
				int hashCode = getHashCode(file);
				
				long modifiedTime = 0;
				
				if (action == ADD || action == MODIFY) {
					String name = file.getFileName().toString();
					// Strip last four characters (.pdf)
					name = name.substring(0, name.length()-4);
					modifiedTime = Files.getLastModifiedTime(file).toMillis();
					
					
				}
				
				
				if (action == MODIFY || action == DELETE) {
					//"hashCode", String.valueOf(hashCode);
				}
				
				if (action == ADD) {
					
					indexCache.put(hashCode, modifiedTime);
				} else if (action == MODIFY) {
					;
					indexCache.put(hashCode, modifiedTime);
				} else if (action == DELETE) {
					
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
	 * File visitor for recursively indexing all PDFs in a directory.
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
			boolean isPdf = file.toString().endsWith(".pdf");
			boolean indexed = cacheCopy.containsKey(hashCode);
			if (isPdf && indexed) {
				long modifiedTime = Files.getLastModifiedTime(file).toMillis();
				if (modifiedTime != cacheCopy.get(hashCode)) {
					threadPoolExecutor.execute(
							new PDFIndexingRunnable(file, MODIFY));
				}
				// Remove existing files so only deleted files remain
				cacheCopy.remove(hashCode);
			} else if (isPdf && !indexed) {
				threadPoolExecutor.execute(new PDFIndexingRunnable(file, ADD));
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
	
	private class IndexingFilter  {
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
			if (contextPath.toString().endsWith(".pdf")) {
				if (eventKind == ENTRY_CREATE) {
					return new PDFIndexingRunnable(contextPath, ADD);
				} else if (eventKind == ENTRY_MODIFY) {
					return new PDFIndexingRunnable(contextPath, MODIFY);
				} else if (eventKind == ENTRY_DELETE) {
					return new PDFIndexingRunnable(contextPath, DELETE);
				}
			}
			return NULL_RUNNABLE;
		}
	}
}
