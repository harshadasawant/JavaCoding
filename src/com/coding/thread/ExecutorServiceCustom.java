package com.coding.thread;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Harshada Sawant
 */


interface MyExecutorService {
    void submit(Runnable r);
}

class MyThreadPool implements MyExecutorService {
    static int capacity;
    static int currentCapacity;
    static LinkedBlockingQueue<Runnable> linkedTaskBlockingQueue;
    Execution e;

    public MyThreadPool(int capacity) {
        this.capacity = capacity;
        currentCapacity = 0;
        linkedTaskBlockingQueue = new LinkedBlockingQueue<Runnable>();
        e = new Execution();
    }

    @Override
    public void submit(Runnable r) {
        linkedTaskBlockingQueue.add(r);
        e.executeMyMethod();
    }
}

class Execution implements Runnable {
    void executeMyMethod() {
        if (MyThreadPool.currentCapacity < MyThreadPool.capacity) {
            MyThreadPool.currentCapacity++;
            Thread t = new Thread(new Execution());
            t.start();
        }
    }

    @Override
    public void run() {
        while (true) {
            if (MyThreadPool.linkedTaskBlockingQueue.size() != 0) {
                MyThreadPool.linkedTaskBlockingQueue.poll().run();
            }
        }
    }
}

class MyExecutors {
    int capacity;

    static MyExecutorService myNewFixedThreadPool(int capacity) {
        return new MyThreadPool(capacity);
    }
}

class Mytask implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("print :-> " + Thread.currentThread().getName());
    }
}

public class ExecutorServiceCustom {
    public static void main(String[] args) {
        MyExecutorService service = MyExecutors.myNewFixedThreadPool(3);
        for (int i = 0; i < 20; i++) {
            service.submit(new Mytask());
        }
    }
}
