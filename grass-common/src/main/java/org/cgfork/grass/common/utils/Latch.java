package org.cgfork.grass.common.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 * Updated: 2015/10/27
 */
public class Latch extends CountDownLatch {
    private final AtomicBoolean hasDone;

    public Latch() {
       super(1);
       hasDone = new AtomicBoolean(false);
    }

    public void countDown() {
        if (hasDone.compareAndSet(false, true)) {
            super.countDown();
        }
    }
    
    public boolean isDone() {
        return hasDone.get();
    }
 }