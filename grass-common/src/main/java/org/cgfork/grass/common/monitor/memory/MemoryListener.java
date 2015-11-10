package org.cgfork.grass.common.monitor.memory;

import java.io.Closeable;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface MemoryListener {

    void onMonitorThresholdTrigger(MemoryMonitor monitor, MemoryState state) throws Exception;

    void onMonitorStarted(MemoryMonitor monitor, MemoryState state) throws Exception;

    void onMonitorStopped(MemoryMonitor monitor, MemoryState state) throws Exception;

    void onMonitorCaught(MemoryMonitor monitor,  Throwable cause) throws Exception;

    void onMonitorRun(MemoryMonitor monitor, MemoryState state) throws Exception;
}
