package com.cowforce.ratelimit;

/**
 * <p>
 * Copyright: (C), 2022-11-18 8:44
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public interface RateLimiter {
	
	/**
	 * 是否超过限流阈值了
	 * @return
	 */
	public boolean isOverlimit();
	
	/**
	 * 当前的QPS
	 * @return
	 */
	public long currentQps();
	
	/**
	 * 是否允许访问
	 * @return
	 */
	public boolean visit();
}
