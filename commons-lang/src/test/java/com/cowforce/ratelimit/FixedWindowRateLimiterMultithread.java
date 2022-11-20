package com.cowforce.ratelimit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定窗口算法, 多线程版本
 * <p>
 * Copyright: (C), 2022-11-18 8:40
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class FixedWindowRateLimiterMultithread implements RateLimiter, Runnable {
	
	/**
	 * 默认没秒限制访问5次
	 */
	private static final int DEFAULT_ALLOWED_VISIT_PER_SECOND = 5;
	
	private final int maxVisitPerSecond;
	
	private AtomicInteger count;
	
	public FixedWindowRateLimiterMultithread() {
		this.maxVisitPerSecond = DEFAULT_ALLOWED_VISIT_PER_SECOND;
		this.count = new AtomicInteger();
	}
	
	public FixedWindowRateLimiterMultithread(int maxVisitPerSecond) {
		this.maxVisitPerSecond = maxVisitPerSecond;
	}
	
	@Override
	public boolean isOverlimit() {
		return currentQps() > maxVisitPerSecond;
	}
	
	public long currentQps() {
		return count.get();
	}
	
	@Override
	public boolean visit() {
		count.incrementAndGet();
		System.out.println(isOverlimit()? "限流了": "放行");
		return isOverlimit();
	}
	
	@Override
	public void run() {
		System.out.println(currentQps());
		count.set(0);
	}
	
	public static void main(String[] args) {
		ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		FixedWindowRateLimiterMultithread rateLimiter = new FixedWindowRateLimiterMultithread();
		//每秒清零计数器
		scheduledExecutorService.scheduleAtFixedRate(rateLimiter, 0, 1, TimeUnit.SECONDS);
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					rateLimiter.visit();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					rateLimiter.visit();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
