package com.asle.quiz.config;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class LockFreeMPMCRingBuffer<T> {

    private final AtomicReferenceArray<T> buffer;
    private final int capacity;
    private final AtomicLong head = new AtomicLong(0); // consumer index
    private final AtomicLong tail = new AtomicLong(0); // producer index

    public LockFreeMPMCRingBuffer(int capacity) {
        if (Integer.bitCount(capacity) != 1) {
            throw new IllegalArgumentException("Capacity must be a power of 2");
        }
        this.capacity = capacity;
        this.buffer = new AtomicReferenceArray<>(capacity);
    }

    public boolean offer(T item) {

        while (true) {
            long currentTail = tail.get();
            long wrapPoint = currentTail - capacity;
            if (head.get() <= wrapPoint) {
                return false; // buffer is full
            }
            if (tail.compareAndSet(currentTail, currentTail + 1)) {
                int index = (int)(currentTail & (capacity - 1));
                buffer.set(index, item);
                return true;
            }
        }
    }

    public T poll() {

        while (true) {
            long currentHead = head.get();
            if (currentHead >= tail.get()) {
                return null; // buffer is empty
            }
            if (head.compareAndSet(currentHead, currentHead + 1)) {
                int index = (int)(currentHead & (capacity - 1));
                T item = buffer.get(index);
                buffer.set(index, null); // help GC
                return item;
            }
        }
    }

    public int size() {
        return (int)(tail.get() - head.get());
    }

    public boolean isEmpty() {
        return tail.get() == head.get();
    }

    public boolean isFull() {
        return size() == capacity;
    }
}
