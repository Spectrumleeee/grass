/**
 * Copyright (c) 2015, TP-Link Co.,Ltd.
 * Author:  chenbiren <chenbiren@tp-link.net>
 * Created: 2015-10-14
 */
package org.cgfork.grass.common.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
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