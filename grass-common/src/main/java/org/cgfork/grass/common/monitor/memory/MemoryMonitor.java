package org.cgfork.grass.common.monitor.memory;

import org.cgfork.grass.common.check.Checker;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class MemoryMonitor {
    private static final boolean DEFAULT_RESTORE_JVM_ENABLED = false;

    private static final long DEFAULT_INTERVAL_MILLIS = 1000L;

    private static final double DEFAULT_THRESHOLD_TRIGGER_RATIO = 0.30;

    private static final AtomicLong MONITOR_INDEX = new AtomicLong(0);

    private final AtomicLong timerIndex = new AtomicLong(0);

    private boolean onceTriggeredEnabled;

    private volatile MemoryState state;

    private volatile boolean triggered = false;

    private Timer timer;

    private MonitorTask task;

    private String monitorName;

    private final long interval;

    private final double thresholdTriggerRatio;

    private final long monitorIndexNow = MONITOR_INDEX.incrementAndGet();

    private final Set<MemoryListener> listeners = new HashSet<>();

    public MemoryMonitor() {
        this(DEFAULT_INTERVAL_MILLIS, DEFAULT_THRESHOLD_TRIGGER_RATIO);
    }

    public MemoryMonitor(long interval) {
        this(interval, DEFAULT_THRESHOLD_TRIGGER_RATIO);
    }

    public MemoryMonitor(double thresholdTriggerRatio) {
        this(DEFAULT_INTERVAL_MILLIS, thresholdTriggerRatio);
    }

    public MemoryMonitor(long interval, double thresholdTriggerRatio) {
        this(interval, thresholdTriggerRatio, DEFAULT_RESTORE_JVM_ENABLED);
    }

    public MemoryMonitor(long interval, double thresholdTriggerRatio, boolean restoreJvmEnabled) {
        this(new MemoryMeasurer(restoreJvmEnabled), interval, thresholdTriggerRatio);
    }

    public MemoryMonitor(MemoryMeasurer measurer, long interval, double thresholdTriggerRatio) {
        Checker.Arg.in(thresholdTriggerRatio, 0, 1.0, "thresholdTriggerRatio is not in (0, 1)");
        Checker.Arg.in(interval, 0, Long.MAX_VALUE, "interval is not in (0, " + Long.MAX_VALUE + ")");
        Checker.Arg.notNull(measurer, "measurer is null");
        task = new MonitorTask(measurer, thresholdTriggerRatio);
        this.interval = interval;
        this.thresholdTriggerRatio = thresholdTriggerRatio;
    }

    public synchronized void startMonitor() {
        checkMonitoring(true);
        timer = new Timer(getTimerName(), true);
        timer.schedule(task, 0, interval);
        state = task.measurer.memoryState();
        fireMonitorStarted(state);
    }

    public synchronized void stopMonitor() {
        if (!isMonitoring()) return;
        timer.cancel();
        timer = null;
        if (state == null) {
            state = task.measurer.memoryState();
        }
        fireMonitorStopped(state);
    }

    public synchronized boolean isMonitoring() {
        return timer != null;
    }

    public synchronized boolean isTriggered() {
        return triggered;
    }

    public synchronized void triggered(boolean triggered) {
        this.triggered = triggered;
    }

    public long getInterval() {
        return interval;
    }

    public double getThresholdTriggerRatio() {
        return thresholdTriggerRatio;
    }

    public synchronized void openOnceTriggered() {
        onceTriggeredEnabled = true;
    }

    public synchronized void stopOnceTriggered() {
        onceTriggeredEnabled = false;
    }

    public boolean onceTriggeredEnabled() {
        return onceTriggeredEnabled;
    }

    public synchronized boolean addListener(MemoryListener listener) {
        Checker.Arg.notNull(listener, "listener is null");
        return listeners.add(listener);
    }

    public synchronized boolean removeListener(MemoryListener listener) {
        Checker.Arg.notNull(listener, "listener is null");
        return listeners.remove(listener);
    }

    public String toString() {
        return monitorName;
    }

    private void checkMonitoring(boolean monitoring) {
        if (monitoring && isMonitoring()) {
            throw new RuntimeException("monitor is started");
        }
        if (!monitoring && !isMonitoring()) {
            throw new RuntimeException("monitor is stopped");
        }
    }

    private String getTimerName() {
        monitorName = "MemoryMonitor#" + monitorIndexNow + "_timer#" + timerIndex.incrementAndGet();
        return monitorName;
    }

    private void fireMonitorThresholdTrigger(MemoryState state) {
        for (MemoryListener listener : listeners) {
            try {
                listener.onMonitorThresholdTrigger(this, state);
            } catch (Throwable cause) {
                try {
                    listener.onMonitorCaught(this, cause);
                } catch (Exception e) {
                    //TODO: logger
                }
            }
        }
    }

    private void fireMonitorStarted(MemoryState state) {
        for (MemoryListener listener : listeners) {
            try {
                listener.onMonitorStarted(this, state);
            } catch (Throwable cause) {
                try {
                    listener.onMonitorCaught(this, cause);
                } catch (Exception e) {
                    //TODO: logger
                }
            }
        }
    }

    private void fireMonitorStopped(MemoryState state) {
        for (MemoryListener listener : listeners) {
            try {
                listener.onMonitorStopped(this, state);
            } catch (Throwable cause) {
                try {
                    listener.onMonitorCaught(this, cause);
                } catch (Exception e) {
                    //TODO: logger
                }
            }
        }
    }

    private void fireMonitorRun(MemoryState state) {
        for (MemoryListener listener : listeners) {
            try {
                listener.onMonitorRun(this, state);
            } catch (Throwable cause) {
                try {
                    listener.onMonitorCaught(this, cause);
                } catch (Exception e) {
                    //TODO: logger
                }
            }
        }
    }

    private void fireMonitorCaught(Throwable cause) {
        for (MemoryListener listener : listeners) {
            try {
                listener.onMonitorCaught(this, cause);
            } catch (Exception e) {
                //TODO: logger
            }
        }
    }

    private synchronized MemoryState memoryState() {
        return state;
    }

    private synchronized void memoryState(MemoryState state) {
        this.state = state;
    }

    private class MonitorTask extends TimerTask {

        private final MemoryMeasurer measurer;

        private final double thresholdTriggerRatio;


        public MonitorTask(double thresholdTriggerRatio, boolean restoreJvmEnabled) {
            this(new MemoryMeasurer(restoreJvmEnabled), thresholdTriggerRatio);
        }

        public MonitorTask(MemoryMeasurer measurer, double thresholdTriggerRatio) {
            Checker.Arg.notNull(measurer, "measurer is null");
            Checker.Arg.in(thresholdTriggerRatio, 0, 1.0, "thresholdTriggerRatio is not in (0, 1)");
            this.measurer = measurer;
            this.thresholdTriggerRatio = thresholdTriggerRatio;
        }

        @Override
        public void run() {
            try {
                MemoryState state = measurer.memoryState();
                memoryState(state);
                fireMonitorRun(state);

                if (state.getAvailableRatio() < thresholdTriggerRatio) {
                    if (!onceTriggeredEnabled() || !isTriggered()) {
                        MemoryMonitor.this.triggered = true;
                        fireMonitorThresholdTrigger(state);
                    }
                }
            } catch (Throwable cause) {
                // TODO: logger or rethrow
                fireMonitorCaught(cause);
            }
        }
    }
}
