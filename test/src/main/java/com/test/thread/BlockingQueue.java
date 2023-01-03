package com.test.thread;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "cn")
public class BlockingQueue<T> {
    // 队列
    Deque<T> queue = new ArrayDeque<>();

    // 队列容量
    int capacity;

    // 锁
    final ReentrantLock lock = new ReentrantLock();

    // 生产者条件
    final Condition productWaitSet = lock.newCondition();

    // 消费者条件
    final Condition consumerWaitSet = lock.newCondition();

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public <T> T trySecurity(Supplier<T> callback) {
        lock.lock();

        try {
            return callback.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }

    public T take(long time, TimeUnit timeUnit) {
        return trySecurity(() -> {
            long nanos = timeUnit.toNanos(time);

            while (queue.isEmpty()) {
                try {
                    if (nanos <= 0) {
                        return null;
                    }
                    log.debug("消费者 -> 等待中");
                    nanos = consumerWaitSet.awaitNanos(nanos);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            T remove = queue.remove();
            log.debug("消费者消费完成 -> {}", remove);
            productWaitSet.signal();

            return remove;
        });
    }

    public T take() {
        return trySecurity(() -> {

            while (queue.isEmpty()) {
                try {
                    log.debug("消费者 -> 等待中");
                    consumerWaitSet.wait();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            T remove = queue.remove();
            log.debug("消费者消费完成 -> {}", remove);
            productWaitSet.signal();

            return remove;
        });
    }

    public boolean put(T e, long time, TimeUnit timeUnit) {
        return trySecurity(() -> {
            long nanos = timeUnit.toNanos(time);

            while (queue.size() >= capacity) {
                try {

                    if (nanos <= 0) {
                        return false;
                    }

                    log.debug("生产者 -> 等待中");
                    nanos = productWaitSet.awaitNanos(nanos);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            log.debug("添加到任务队列 -> {}", e);
            queue.addLast(e);
            consumerWaitSet.signal();

            return true;
        });
    }

    public void put(T e) {
        trySecurity(() -> {
            while (queue.size() >= capacity) {
                try {
                    log.debug("生产者 -> 等待中");
                    productWaitSet.await();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            log.debug("添加到任务队列 -> {}", e);
            queue.addLast(e);
            consumerWaitSet.signal();

            return null;
        });
    }

    public Integer size() {
        return trySecurity(() -> queue.size());
    }

    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        trySecurity(() -> {
            if (queue.size() >= capacity) {
                rejectPolicy.reject(this, task);
                return null;
            }

            queue.addLast(task);
            consumerWaitSet.signal();
            return null;
        });
    }
}
