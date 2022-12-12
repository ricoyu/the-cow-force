package com.cowforce.algorithm.sort;

/**
 * <p>
 * Copyright: (C), 2022-12-08 12:39
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class PrintArray {
	
	public final static int[] SRC = {86, 39, 77, 23, 32, 45, 58, 63, 93, 4, 37, 22};
	
	public static void print(int[] array) {
		for (int i : array) {
			System.out.print(i + "  ");
		}
		System.out.println("");
	}
	
	public static void printIndex(int[] array,int begin ,int end){
		for(int i=begin;i<=end;i++){
			System.out.print(array[i]+"  ");
		}
		System.out.println("");
	}
}
