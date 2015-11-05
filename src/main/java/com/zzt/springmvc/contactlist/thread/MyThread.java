package com.zzt.springmvc.contactlist.thread;

/**
 * 【同步方法】

也可以采用同步方法。

语法格式为synchronized 方法返回类型方法名（参数列表）{

    // 其他代码

}

现在，我们采用同步方法解决上面的问题。
 * @author Eric
 *
 */
public class MyThread implements Runnable {

	public void run() {
		for (int i = 0; i < 10; ++i) {
			sale();
		}
	}

	private synchronized void sale() {
		if (count > 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(count--);
		}
	}

	public static void main(String[] args) {
		MyThread he = new MyThread();
		Thread h1 = new Thread(he);
		Thread h2 = new Thread(he);
		Thread h3 = new Thread(he);
		h1.start();
		h2.start();
		h3.start();
	}

	private int count = 5;
}
