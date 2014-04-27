package com.dhs.portglass.services;


import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Manages a ThreadPoolExecutor to take advantage of hardware 
 * concurrency and improve tasks by providing parallelization; 
 * rather than stopping the main application thread. The 
 * ThreadPoolExecutor kills idle threads aggressively, since the 
 * application may go long periods of time without the need of 
 * these threads. The shutdown() method will shut down the thread
 * pool.
 * @author Manuel R Saldana
 *
 */
public class ThreadPoolManager 
{
	public static final int PROCESSORS =
			Runtime.getRuntime().availableProcessors();
	public static final int THREAD_TIMEOUT_SECONDS = 10;
	
	
	private volatile static ThreadPoolExecutor threadPoolExecutor ;
			
	private volatile ScheduledExecutorService scheduledExecutor;
	
	//Singleton instance of the ThreadPoolController
	private static final ThreadPoolManager singleton = new ThreadPoolManager();
	
	private ThreadPoolManager(){
		super();
		threadPoolExecutor =
				newThreadPoolExecutor(PROCESSORS+1, THREAD_TIMEOUT_SECONDS);
		scheduledExecutor =
				Executors.newSingleThreadScheduledExecutor();
	}
	
	/**
	 * Ensures that only one instance of the ThreadPoolController is made.
	 * Done to avoid other thread pools to be created.
	 * @return
	 */
	public static ThreadPoolManager getInstance(){
		return singleton;
	}
	
	/**
	 * Returns the ThreadPoolExecutor object created by the ThreadPoolController.
	 * This can be passed to other classes to realize asynchronous tasks by 
	 * methods that implement Runnable.
	 * @return
	 */
	public ThreadPoolExecutor getThreadPoolExecutor(){
		return threadPoolExecutor;
	}
	
	
	/**
	 * Returns a single thread for Scheduled events created by the ThreadPoolController.
	 * This can be passed to other classes to realize asynchronous scheduled tasks by 
	 * methods that implement Runnable.
	 * @return
	 */
	public ScheduledExecutorService getScheduledExecutor(){
		return scheduledExecutor;
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
	 * Shutdown all threads.
	 */
	public void shutdown() {
		System.out.println("Thread Pool: SHUTTING DOWN");
		if (scheduledExecutor != null) { scheduledExecutor.shutdown(); }
		if (threadPoolExecutor != null) { threadPoolExecutor.shutdownNow(); }
	}

	
	public static void main(String[] args)
	{
		/*
		Account testUser = new Account();
  		testUser.setEmail("noreply.portglass@gmail.com");
  	  	testUser.setFirstName("Noreply");
  	  	testUser.setPassword("password");
  	  	testUser.setType("General User");
  	  	testUser.setLastName("Portglass");
		
		ThreadPoolController.getInstance().getThreadPoolExecutor().execute(MailManager.getInstance()
				.sendNewAsyncNewAccountEmail(testUser));
		System.out.println("test");
		*/
	}
}


