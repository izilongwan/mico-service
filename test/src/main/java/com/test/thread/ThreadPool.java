package com.test.thread;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadPool {
    class Worker extends Thread {

        Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            while (task != null || (task = taskQueue.take(time, timeUnit)) != null) {
                try {
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }

            synchronized (workers) {
                workers.remove(this);
            }
        }

    }

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(1, 1, 2000, TimeUnit.MILLISECONDS);

        for (int i = 0; i < 3; i++) {
            int k = i;

            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000 * 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                log.debug("[{}] -> finish", k);
            });
        }

    }

    BlockingQueue<Runnable> taskQueue;

    int core;

    long time;

    TimeUnit timeUnit;

    RejectPolicy<Runnable> rejectPolicy;

    Set<Worker> workers = new HashSet<>(core);

    public ThreadPool(int core, int capacity, long time, TimeUnit timeUnit) {
        this.taskQueue = new BlockingQueue<>(capacity);
        this.core = core;
        this.time = time;
        this.timeUnit = timeUnit;
        this.rejectPolicy = (queue, task) -> queue.put(task, time, timeUnit);
    }

    public ThreadPool(int core, int capacity, long time, TimeUnit timeUnit, RejectPolicy<Runnable> rejectPolicy) {
        this.taskQueue = new BlockingQueue<>(capacity);
        this.core = core;
        this.time = time;
        this.timeUnit = timeUnit;
        this.rejectPolicy = rejectPolicy;
    }

    public void execute(Runnable task) {
        synchronized (workers) {
            if (workers.size() < core) {
                Worker worker = new Worker(task);
                log.debug("新增 worker -> {}", worker);

                workers.add(worker);
                worker.start();
                return;
            }

            // 死等
            // 超时等待
            // 调用者自定义 放弃;抛出异常;自己执行
            taskQueue.tryPut(rejectPolicy, task);
        }
    }
}
