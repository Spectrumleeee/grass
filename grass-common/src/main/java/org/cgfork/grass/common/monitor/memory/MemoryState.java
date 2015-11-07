package org.cgfork.grass.common.monitor.memory;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class MemoryState {

    private static final String defaultSep = ", ";

    private long used;

    private long free;

    private long available;

    private long total;

    private long max;

    public MemoryState() {}  
    public MemoryState(long free, long total, long max) {
        this.free = free;
        this.total = total;
        this.max = max;
        this.used = total - free;
        this.available = max - used;
    }

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public long getFree() {
        return free;
    }

    public void setFree(long free) {
        this.free = free;
    }

    public long getAvailable() {
        return available;
    }

    public void setAvailable(long available) {
        this.available = available;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public double getAvailableRatio() {
        return ((double) available) / ((double) max);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof MemoryState) {
            MemoryState state = (MemoryState) o;
            return (this.free == state.free) &&
                    (this.total == state.total) &&
                    (this.max == state.max);
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Memory[");
        builder.append("used=").append(getUsed()).append(", ");
        builder.append("free=").append(getFree()).append(", ");
        builder.append("total=").append(getTotal()).append(", ");
        builder.append("max=").append(getMax()).append(", ");
        builder.append("available=").append(getAvailable()).append(", ");
        builder.append("availableRatio=").append(getAvailableRatio()).append("]");
        return builder.toString();
    }
}
