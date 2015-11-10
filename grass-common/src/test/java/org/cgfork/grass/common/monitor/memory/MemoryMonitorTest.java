package org.cgfork.grass.common.monitor.memory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class MemoryMonitorTest {

    private MemoryMonitor monitor;

    private List<Object> lists;

    private volatile boolean goon = true;

    @Before
    public void setUp() throws Exception {
        monitor = new MemoryMonitor(0.9);
        lists = new LinkedList<>();
        goon = true;
    }

    @After
    public void tearDown() throws Exception {
        monitor = null;
        lists.clear();
    }

    @Test
    public void testMonitorTrigger() throws Exception {
        monitor.addListener(new MemoryListenerImpl());
        monitor.startMonitor();
//        monitor.stopMonitor();
        while (true) {
            boolean goon = this.goon;
            if (!goon) break;
            lists.add(new Object());
        }
        assertTrue(monitor.isMonitoring());
        assertTrue(monitor.isTriggered());
        monitor.stopMonitor();
        assertFalse(monitor.isMonitoring());
    }

    class MemoryListenerImpl implements MemoryListener {

        @Override
        public void onMonitorThresholdTrigger(MemoryMonitor monitor, MemoryState state) throws Exception {
            MemoryMonitorTest.this.goon = false;
            System.out.println("Triggered: " + state.toString());
        }

        @Override
        public void onMonitorStarted(MemoryMonitor monitor, MemoryState state) throws Exception {
            System.out.println("Started: " + state.toString());
        }

        @Override
        public void onMonitorStopped(MemoryMonitor monitor, MemoryState state) throws Exception {
            MemoryMonitorTest.this.goon = false;
            System.out.println("Stopped: " + state.toString());
        }

        @Override
        public void onMonitorCaught(MemoryMonitor monitor, Throwable cause) throws Exception {
            cause.printStackTrace();
        }

        @Override
        public void onMonitorRun(MemoryMonitor monitor, MemoryState state) throws Exception {
            System.out.println("Run: " + state.toString());
        }
    }
}