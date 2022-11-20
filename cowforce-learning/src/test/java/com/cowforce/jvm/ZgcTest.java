package com.cowforce.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * Java 17默认G1
 * 默认G1: -Xmx2G -XX:+PrintGCDetails
 * PS:    -XX:+UseParallelGC -Xmx2G -XX:+PrintGCDetails
 * ZGC:   -XX:+UseZGC -Xmx2G -XX:+PrintGCDetails
 * <p>
 * Copyright: (C), 2022-11-05 20:02
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ZgcTest {
	
	public static class FillListTest extends Thread {
		
		List<byte[]> list = new ArrayList<>();
		
		@Override
		public void run() {
			try {
				while (true) {
					if (list.size()*512/1024/1024 >=900) {
						list.clear();
						System.out.println("list is cleared");
					}
					byte[] bl;
					for (int i = 0; i < 100; i++) {
						bl = new byte[1024];
						list.add(bl);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		FillListTest myThread = new FillListTest();
		myThread.start();
	}
}
