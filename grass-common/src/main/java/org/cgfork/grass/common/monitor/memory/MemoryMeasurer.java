package org.cgfork.grass.common.monitor.memory;

import java.lang.management.ManagementFactory;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class MemoryMeasurer {

    private static final int MAX_RESTORE_JVM_LOOPS = 100;

    private final boolean restoreJvmEnabled;

    public MemoryMeasurer() {
        this(false);
    }

    public MemoryMeasurer(boolean restoreJvmEnabled) {
        this.restoreJvmEnabled = restoreJvmEnabled;
    }

    public MemoryState memoryState() {
        if (restoreJvmEnabled) {
            restoreJvm();
        }

        Runtime rt = Runtime.getRuntime();
        return new MemoryState( rt.freeMemory(), rt.totalMemory(), rt.maxMemory() );
    }

    /**
     * @return how much memory on the heap is currently being used.
     */
    public static long usedMemory() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }

    /**
     * @return how much memory on the heap is currently being free.
     */
    public static long freeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * @return the total amount of memory in the Java virtual machine.
     */
    public static long totalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    /**
     * @return the maximum amount of memory that the Java virtual machine will
     * attempt to use.
     */
    public static long maxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * Tries to restore the JVM to as clean as possible.
     */
    public static void restoreJvm() {
        long usedMemPrev = usedMemory();
        for (int i = 0; i < MAX_RESTORE_JVM_LOOPS; i++) {
            // free all unused memory
            System.runFinalization();
            System.gc();

            // check memory
            long usedMemNow = usedMemory();
            if (ManagementFactory.getMemoryMXBean().getObjectPendingFinalizationCount() == 0 &&
                    usedMemNow >= usedMemPrev) {
                break;
            } else  {
                usedMemPrev = usedMemNow;
            }
        }
    }

    public static MemoryState perform() {
        return perform(false);
    }

    public static MemoryState perform(boolean restoreJvmEnabled) {
        return new MemoryMeasurer(restoreJvmEnabled).memoryState();
    }
}
