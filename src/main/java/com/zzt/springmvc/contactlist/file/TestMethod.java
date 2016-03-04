package com.zzt.springmvc.contactlist.file;

public class TestMethod {
	public TestMethod() { // /xx/weblogic60b2_win.exe
		try {
			SiteInfoBean bean = new SiteInfoBean(
					"http://dlc2.pconline.com.cn/filedown3_61_17032501/bOGGlQdw/FlashFXP52_3918_Setup_5100000000617032501.exe", "D:/",
					"ok.exe", 5);
			// SiteInfoBean bean = new
			// SiteInfoBean("http://localhost:8080/down.zip","L:\\temp",
			SiteFileFetch fileFetch = new SiteFileFetch(bean);
			fileFetch.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new TestMethod();
	}
}
