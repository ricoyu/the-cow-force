package com.cowforce.ratelimit;

import com.cowforce.common.lang.ratelimit.RateLimits;
import com.cowforce.common.lang.ratelimit.SlidingWindow;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.TimeUnit.*;

/**
 * <p>
 * Copyright: (C), 2022-11-18 15:32
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class RateLimitTest {
	
	@Test
	public void test() {
		AtomicLong counter = new AtomicLong(0);
		System.out.println(counter.incrementAndGet() + "aaa");
		new Thread(() -> {
			while (true) {
				System.out.println(counter.incrementAndGet());
			}
		}).start();
	}
	
	@Test
	public void testSlidingWindow() {
		AtomicLong counter = new AtomicLong(0);
		SlidingWindow slidingWindow = RateLimits.slidingTimeWindow(10L, SECONDS)
				.limit(10l)
				.precision(10)
				.build();
		new Thread(() -> {
			//try {
				System.out.println("上来先休眠9秒, 模拟前面没有请求, 到最后一秒请求大量来到");
				/*
				 * 休眠9秒, 那么会创建9个count是0的slot
				 */
				//SECONDS.sleep(9);
			//} catch (InterruptedException e) {
			//	throw new RuntimeException(e);
			//}
			while (true) {
				try {
					//一下放行10次, 然后限流
					if (slidingWindow.canPass()) {
						long count = counter.incrementAndGet();
						log.info("第 {} 次访问, 放行!", count);
						//SECONDS.sleep(1);
						//MILLISECONDS.sleep(500);
					} else {
						log.info("第 {} 次访问被限流", counter.incrementAndGet());
						SECONDS.sleep(1);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
