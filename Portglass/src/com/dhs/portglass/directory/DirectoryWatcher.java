package com.dhs.portglass.directory;



import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread that monitors a directory and executes tasks according to an
 * EventFilter and Executor. This class assumes all tasks can run independently
 * and concurrently - multiple threads can respond to events from the same
 * directory.
 * 
 * @author Manuel R Saldana
 */
public class DirectoryWatcher implements Runnable {
	
	/*
	 * Important observations concerning the WatchService API:
	 * WatchService.take() blocks until a WatchKey is queued
	 * WatchKey.pollEvents() does NOT block if no events are queued, it simply
	 * returns an empty list. All events retrieved will be dequeued.
	 * A WatchKey can receive more events between the calls to pollEvents() and
	 * reset(), and they can be retrieved by calling pollEvents() again.
	 * Calling reset() merely allows the WatchKey to be requeued.
	 */
	
	private static final Logger logger =
			Logger.getLogger(DirectoryWatcher.class.getName());
	private final WatchService watchService;
	private final boolean recursive;
	private final boolean shutdownExecutor;
	private final WatchEventFilter eventFilter;
	private final ExecutorService executor;
	private final Map<WatchKey, Path> paths = new HashMap<WatchKey, Path>();
	private volatile boolean running = true;
	
	/**
	 * Creates a new DirectoryWatcher. The executor will NOT be shut down if
	 * this constructor throws an exception.
	 * @param directoryPath The path to the directory that will be watched
	 * @param recursive True if sub-directories should be watched as well
	 * @param eventFilter Returns the appropriate Runnable for each event
	 * @param executor Manages the threads for the tasks
	 * @throws IOException The directory doesn't exist.
	 */
	public DirectoryWatcher(Path directoryPath, boolean recursive,
			WatchEventFilter eventFilter, ExecutorService executor,
			boolean shutdownExecutor) throws IOException {
		this.recursive = recursive;
		this.eventFilter = eventFilter;
		this.executor = executor;
		this.shutdownExecutor = shutdownExecutor;
		watchService = FileSystems.getDefault().newWatchService();
		if (recursive) {
			recursiveRegister(directoryPath);
		}
		else {
			register(directoryPath);
		}
	}
	
	/**
	 * Creates a recursive DirectoryWatcher that won't shut down its executor.
	 */
	public DirectoryWatcher(Path directoryPath, WatchEventFilter eventFilter,
			ExecutorService executor) throws IOException {
		this(directoryPath, true, eventFilter, executor, false);
	}
	
	@Override
	public void run() {
		try {
			while (running) {
				loopOnce();
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "DirectoryWatcher encountered an " +
					"IOException it could not recover from.", e);
		} catch (InterruptedException e) { /* Do nothing */ }
		finally {
			shutdown();
		}
	}
	
	public void shutdown() {
		logger.log(Level.WARNING, "DirectoryWatcher shutting down.");
		running = false;
		if (shutdownExecutor) {
			executor.shutdown();
		}
	}

	/**
	 * Package-private helper method for unit testing run()
	 * @throws InterruptedException
	 *             If the thread is interrupted while waiting for events
	 * @throws IOException
	 *             If all directories being watched are deleted
	 */
	@SuppressWarnings("unchecked")
	void loopOnce() throws InterruptedException, IOException {
		// Wait for an event to occur in a registered directory
		WatchKey watchKey = watchService.take();
		
		// Get a Runnable object for each event and execute them asynchronously
		for (WatchEvent<?> event : watchKey.pollEvents()) {
			Runnable runnable;
			Kind<?> eventKind = event.kind();
			
			// Check for event queue overflows
			if (eventKind == OVERFLOW) {
				WatchEvent<Object> overflowEvent = (WatchEvent<Object>) event;
				runnable = eventFilter.getOverflowRunnable(overflowEvent);
			} else {
				WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
				Path contextPath = getContextPath(watchKey, pathEvent);
				runnable = eventFilter.getRunnable(pathEvent, contextPath);
				
				// Check if a new directory has been created
				boolean newDirectory = (eventKind == ENTRY_CREATE) &&
						Files.isDirectory(contextPath, NOFOLLOW_LINKS);
				if (recursive && newDirectory) {
					// Register the new directory and all subdirectories
					recursiveRegister(contextPath);
				}
			}
			executor.execute(runnable);
		}
		
		resetKey(watchKey);
	}

	/**
	 * Recursively registers the given directory and all sub-directories.
	 * @param rootDirectory
	 * @throws IOException
	 */
	private void recursiveRegister(Path rootDirectory) {
		try {
			Files.walkFileTree(rootDirectory, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir,
						BasicFileAttributes attrs) throws IOException {
					register(dir);
					return FileVisitResult.CONTINUE;
				}
			});
		}
		catch (IOException e) {
			logger.log(Level.WARNING, "Couldn't register " + rootDirectory + 
					" for watching.", e);
		}
	}
	
	/**
	 * Registers the specified directory and adds it to the paths Map.
	 * @param directory
	 * @throws IOException
	 */
	private void register(Path directory) throws IOException {
		WatchKey watchKey = directory.register(watchService, ENTRY_CREATE,
				ENTRY_DELETE, ENTRY_MODIFY);
		paths.put(watchKey, directory);
	}
	
	/**
	 * Returns the path of the created/modified/deleted file
	 */
	private Path getContextPath(WatchKey watchKey, WatchEvent<Path> event) {
		Path directoryPath = paths.get(watchKey);
		return directoryPath.resolve(event.context());
	}
	
	/**
	 * Resets the WatchKey so it can be requeued. If it's no longer valid,
	 * (the directory was deleted) remove its Path from the paths Map.
	 * @throws IOException All directories have been deleted
	 */
	private void resetKey(WatchKey watchKey) throws IOException {
		boolean keyIsValid = watchKey.reset();
		if (!keyIsValid) {
			paths.remove(watchKey);
			if (paths.isEmpty()) {
				throw new IOException("No more directories left to watch. " +
						"Did someone delete them?");
			}
		}
	}
}