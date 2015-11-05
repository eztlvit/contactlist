package com.zzt.springmvc.contactlist.thread;

/**
 * 
 * @author Eric
 *提醒一下，当多个线程共享一个资源的时候需要进行同步，但是过多的同步可能导致死锁。

此处列举经典的生产者和消费者问题。

【生产者和消费者问题】

先看一段有问题的代码。
 */
class Info {
    
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public int getAge() {
        return age;
    }
 
    public void setAge(int age) {
        this.age = age;
    }
 
    public synchronized void set(String name, int age){
        if(!flag){
            try{
                super.wait();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.name=name;
        this.age=age;
        flag=false;
        super.notify();
    }
     
    public synchronized void get(){
        if(flag){
            try{ 
                super.wait();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
         
        System.out.println(this.getName()+"<===>"+this.getAge());
        flag=true;
        super.notify();
    }
    private String name = "Rollen";
    private int age = 20;
    private boolean flag=false;
    
    public static void main(String[] args) {
        Info info = new Info();
        Producer pro = new Producer(info);
        Consumer con = new Consumer(info);
        new Thread(pro).start();
        new Thread(con).start();
    }
}
 
/**
 * 生产者
 * */
class Producer implements Runnable {
    private Info info = null;
 
    Producer(Info info) {
        this.info = info;
    }
 
    public void run() {
        boolean flag = false;
        for (int i = 0; i < 25; ++i) {
            if (flag) {
                 
                this.info.set("Rollen", 20);
                flag = false;
            } else {
                this.info.set("ChunGe", 100);
                flag = true;
            }
        }
    }
}
 
/**
 * 消费者类
 * */
class Consumer implements Runnable {
    private Info info = null;
 
    public Consumer(Info info) {
        this.info = info;
    }
 
    public void run() {
        for (int i = 0; i < 25; ++i) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.info.get();
        }
    }
}
 
/**
 * 测试类
 * */
class hello {
    
}
