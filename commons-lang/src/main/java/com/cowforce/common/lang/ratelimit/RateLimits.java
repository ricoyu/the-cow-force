package com.cowforce.common.lang.ratelimit;

import java.util.concurrent.TimeUnit;

/**
 * 限流组件
 * <p>
 * Copyright: (C), 2022-11-18 15:29
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public final class RateLimits {
	
	/**
	 * 滑动时间窗口限流
	 * @param timeWindow
	 * @param timeUnit
	 * @return SlidingWindowRateLimiterBuilder
	 */
	public static SlidingWindowRateLimiterBuilder slidingTimeWindow(Long timeWindow, TimeUnit timeUnit) {
		return new SlidingWindowRateLimiterBuilder(timeWindow, timeUnit);
	}
}
