package org.cgfork.grass.common.check;

import org.cgfork.grass.common.utils.NetUtils;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public abstract class Checker {

    public final static Checker Arg = new ArgumentChecker();

    public final static Checker Access = new AccessChecker();

    public final static Checker State = new StateChecker();

    public final static Checker Assert = new AssertChecker();

    private final boolean doCheck;

    public Checker(boolean doCheck) {
        this.doCheck = doCheck;
    }

    protected abstract void onFailure(String errMsg);

    protected abstract void onFailure(String errMsg, Throwable cause);

    public Object notNull(Object value) {
        return notNull(value, "object is null");
    }

    public Object notNull(Object value, String warnMsg) {
        if (doCheck && value == null) {
            onFailure(warnMsg);
        }
        return value;
    }

    public boolean isTrue(boolean value) {
        return isTrue(value, "value is False");
    }

    public boolean isTrue(boolean value, String warnMsg) {
        if (doCheck && !value) {
            onFailure(warnMsg);
        }
        return value;
    }

    public boolean isFalse(boolean value) {
        return isTrue(value, "value is True");
    }

    public boolean isFalse(boolean value, String warnMsg) {
        if (doCheck && value) {
            onFailure(warnMsg);
        }
        return value;
    }

    public int zero(int value) {
        return zero(value, "value is not zero");
    }

    public int zero(int value, String warnMsg) {
        if (doCheck && value == 0) {
            onFailure(warnMsg);
        }
        return value;
    }

    public int notZero(int value) {
        return notZero(value, "value is zero");
    }

    public int notZero(int value, String warnMsg) {
        if (doCheck && value != 0) {
            onFailure(warnMsg);
        }
        return value;
    }

    public int in(int value, int low, int high) {
        if (low >= high) {
            int old = low;
            low = high;
            high = old;
        }
        return in(value, low, high, "value not in (" + low + "," + high + ")");
    }

    public int in(int value, int low, int high, String warnMsg) {
        if (low >= high) {
            int old = low;
            low = high;
            high = old;
        }

        if (doCheck && value < low || value > high) {
            onFailure(warnMsg);
        }
        return value;
    }

    public long in(long value, long low, long high) {
        if (low >= high) {
            long old = low;
            low = high;
            high = old;
        }
        return in(value, low, high, "value not in (" + low + "," + high + ")");
    }

    public long in(long value, long low, long high, String warnMsg) {
        if (low >= high) {
            long old = low;
            low = high;
            high = old;
        }

        if (doCheck && value < low || value > high) {
            onFailure(warnMsg);
        }
        return value;
    }


    public double in(double value, double low, double high) {
        if (low >= high) {
            double old = low;
            low = high;
            high = old;
        }
        return in(value, low, high, "value not in (" + low + "," + high + ")");
    }

    public double in(double value, double low, double high, String warnMsg) {
        if (low >= high) {
            double old = low;
            low = high;
            high = old;
        }

        if (doCheck && value < low || value > high) {
            onFailure(warnMsg);
        }
        return value;
    }



    public int validPort(int port) {
        return validPort(port, "port is invalid");
    }

    public int validPort(int port, String warnMsg) {
        if (doCheck && NetUtils.isInvalidPort(port)) {
            onFailure(warnMsg);
        }
        return port;
    }

    private static abstract class CheckerAdapter extends Checker {

        public CheckerAdapter() {this(true);}

        public CheckerAdapter(boolean doCheck) {
            super(doCheck);
        }

        protected void onFailure(String errMsg) {
            onFailure(errMsg, null);
        }
    }

    private static class ArgumentChecker extends CheckerAdapter {

        @Override
        protected void onFailure(String errMsg, Throwable cause) {
            throw new IllegalArgumentException(errMsg, cause);
        }
    }

    private static class AccessChecker extends CheckerAdapter {

        @Override
        protected void onFailure(String errMsg, Throwable cause) {
            IllegalAccessError iae = new IllegalAccessError(errMsg);
            iae.initCause(cause);
            throw iae;
        }
    }

    private static class StateChecker extends CheckerAdapter {

        @Override
        protected void onFailure(String errMsg, Throwable cause) {
        throw new IllegalStateException(errMsg, cause);
    }
}

    private static class AssertChecker extends CheckerAdapter {
        static final boolean assertEnabled = true;

        public AssertChecker() {
            super(assertEnabled);
        }

        @Override
        protected void onFailure(String errMsg, Throwable cause) {
            AssertionError ae = new AssertionError(errMsg);
            ae.initCause(cause);
            throw ae;
        }
    }
}
