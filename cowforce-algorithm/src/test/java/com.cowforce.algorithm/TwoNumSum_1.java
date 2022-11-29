package com.cowforce.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target  的那 两个 整数，并返回它们的数组下标。
 *
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
 *
 * 你可以按任意顺序返回答案。
 * <p>
 * Copyright: (C), 2022-11-29 10:29
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class TwoNumSum_1 {
	
	public int[] twoSum(int[] nums, int target) {
		int[] result = new int[2];
		for (int i = 0; i < nums.length; i++) {
			for (int j = i+1; j < nums.length; j++) {
				if (nums[i] + nums[j] == target) {
					result[0] = i;
					result[1] = j;
					return result;
				}
			}
		}
		return result;
	}
	
	public int[] twoSum2(int[] nums, int target) {
		Map<Integer, Integer> storeNums = new HashMap<>();
		int[] result = new int[2];
		for (int i = 0; i < nums.length; i++) {
			int another = target - nums[i];
			Integer anotherIndex = storeNums.get(another);
			if (null != anotherIndex) {
				result[0] = anotherIndex;
				result[1] = i;
				break;
			} else {
				storeNums.put(nums[i], i);
			}
		}
		return result;
	}
}
