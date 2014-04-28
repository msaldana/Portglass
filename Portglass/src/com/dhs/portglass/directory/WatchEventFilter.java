package com.dhs.portglass.directory;


import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * Implements a filter that inspects a WatchEvent and the time since the last
 * event and returns the appropriate Runnable object. If the event should be
 * ignored, return a Runnable that does nothing. 
 * @author Manuel R Saldana  
 */
public interface WatchEventFilter {
	public Runnable getOverflowRunnable(WatchEvent<Object> overflowEvent);
	public Runnable getRunnable(WatchEvent<Path> event, Path contextPath);
}
