package com.test.thread;

/**
 * RejectPolicy
 */
@FunctionalInterface
public interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}
