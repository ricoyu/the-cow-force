package com.cowforce.ratelimit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.TimeUnit.*;

/**
 * 滑动窗口限流算法
 * 
 * 它将时间窗口划分为更小的时间片段，每过一个时间片段，时间窗口就会往右滑动一格，每个时间片段都有独立的计数器。
 * 我们在计算整个时间窗口内的请求总数时会累加所有的时间片段内的计数器。
 * 时间窗口划分的越细，那么滑动窗口的滚动就越平滑，限流的统计就会越精确
 *
 * 作者：ershuai8614
 * 链接：https://www.jianshu.com/p/9cb6aa788520
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 * <p>
 * Copyright: (C), 2022-11-18 10:41
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class SlidingWindowRateLimiter implements RateLimiter, Runnable {
	
	private AtomicLong totalCount = new AtomicLong();
	
	//private Long lastVisitMillis = System.currentTimeMillis();
	
	/**
	 * 每秒最多通过几个请求
	 */
	private final long maxVisitPerSecond;
	
	/**
	 * 默认划分成几个小的时间窗口
	 */
	private static final int DEFAULT_BLOCK = 10;
	
	/**
	 * 默认没秒限制访问5次
	 */
	private static final int DEFAULT_ALLOWED_VISIT_PER_SECOND = 5;
	
	/**
	 * 表示分成多少个小的窗口
	 */
	private final int block;
	
	/**
	 * 数组里每个AtomicLong记录的是每个小时间窗口内的访问次数
	 */
	private final AtomicLong[] countPerBlock;
	
	/**
	 * 记录一秒内总的访问次数
	 */
	private AtomicLong count;
	
	/**
	 * 记录处于哪个小的时间窗口
	 */
	private volatile int index;
	
	/**
	 * 
	 * @param block 划分成几个时间窗口
	 * @param maxVisitPerSecond 每秒最多放行多少个请求
	 */
	public SlidingWindowRateLimiter(int block, long maxVisitPerSecond) {
		this.block = block;
		this.maxVisitPerSecond = maxVisitPerSecond;
		countPerBlock = new AtomicLong[block];
		for (int i = 0; i < block; i++) {
			countPerBlock[i] = new AtomicLong();
		}
		count = new AtomicLong(0);
	}
	
	public SlidingWindowRateLimiter() {
		this(DEFAULT_BLOCK, DEFAULT_ALLOWED_VISIT_PER_SECOND);
	}
	
	@Override
	public boolean isOverlimit() {
		return currentQps() > maxVisitPerSecond;
	}
	
	@Override
	public long currentQps() {
		return count.get();
	}
	
	@Override
	public boolean visit() {
		countPerBlock[index].incrementAndGet();
		count.incrementAndGet();
		boolean overlimit = isOverlimit();
		System.out.println("第"+totalCount.incrementAndGet()+"次访问: " + (overlimit? "限流" : "放行"));
		return overlimit;
	}
	
	/**
	 * run由定时任务驱动, 每100毫秒执行一次
	 */
	@Override
	public void run() {
		//System.out.println("当前QPS: " + currentQps());
		//System.out.println("当前时间窗口: "+index);
		//run没100毫秒执行一次, 每次移动到下一个时间窗口
		index = (index+1)%block;
		//拿到该时间窗口的当前计数, 然后把该时间窗口计数清零
		long val = countPerBlock[index].getAndSet(0);
		//
		count.addAndGet(-val);
	}
	
	public static void main(String[] args) {
		SlidingWindowRateLimiter rateLimiter = new SlidingWindowRateLimiter(10, 100);
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleAtFixedRate(rateLimiter, 100, 100, MILLISECONDS);
		new Thread(() -> {
			while (true) {
				rateLimiter.visit();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		new Thread(() -> {
			while (true) {
				rateLimiter.visit();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
