package com.zzt.springmvc.contactlist.thread;

import java.util.List;

public class Consume implements Runnable {
	private List<Object> container = null;
	private int count;

	public Consume(List<Object> container) {
		super();
		this.container = container;
	}

	public void run() {

		while (true) {
			synchronized (container) {
				if (container.size() == 0) {
					try {
						container.wait();// 容器为空，放弃锁，等待生产
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				container.remove(0);
				container.notify();
				System.out.println("我吃了" + (++count) + "个");
				count--;
			}
		}

	}
}
