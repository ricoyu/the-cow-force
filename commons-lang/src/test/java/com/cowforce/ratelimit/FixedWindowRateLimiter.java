package com.cowforce.ratelimit;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定窗口算法, 该算法的有点是简单, 但是存在临界值问题: 加上限制1分钟的访问次数不能超过100个
 * 如果用户在0-50秒没有调用，在50-60秒一次性调用了100次请求，那么请求是可以通过的。然后用户在60-70秒内又调用了100次请求，也可以成功，这就突破了1s100个请求的限制。
 * <p>
 * Copyright: (C), 2022-11-18 8:40
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class FixedWindowRateLimiter implements RateLimiter {
	
	/**
	 * 记录
	 */
	private Long lastVisitAt = System.currentTimeMillis();
	/**
	 * 默认没秒限制访问5次
	 */
	private static final int DEFAULT_ALLOWED_VISIT_PER_SECOND = 5;
	
	private final int maxVisitPerSecond;
	
	private AtomicInteger count;
	
	public FixedWindowRateLimiter() {
		this(DEFAULT_ALLOWED_VISIT_PER_SECOND);
	}
	
	public FixedWindowRateLimiter(int maxVisitPerSecond) {
		this.maxVisitPerSecond = maxVisitPerSecond;
		this.count = new AtomicInteger();
	}
	
	@Override
	public boolean isOverlimit() {
		return count.get() > maxVisitPerSecond;
	}
	
	public long currentQps() {
		return count.get();
	}
	
	@Override
	public boolean visit() {
		long now = System.currentTimeMillis();
		synchronized (lastVisitAt) {
			long diff = now - lastVisitAt;
			System.out.println(currentQps() + (isOverlimit() ? "限流" : "放行"));
			if (diff > 1000) {
				lastVisitAt = now;
				count.set(0);
			}
		}
		count.incrementAndGet();
		return isOverlimit();
	}
	
	public static void main(String[] args) {
		FixedWindowRateLimiter rateLimiter = new FixedWindowRateLimiter();
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
