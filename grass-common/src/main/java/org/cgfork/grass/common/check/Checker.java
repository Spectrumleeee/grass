package org.cgfork.grass.common.check;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public abstract class Checker {

    public final static Checker Arg = new ArgChecker();

    private final boolean doCheck;

    public Checker(boolean doCheck) {
        this.doCheck = doCheck;
    }

    protected abstract void onFailure(String errMsg);

    protected abstract void onFailure(String errMsg, Throwable cause);

    public Object notNull(Object o) {
        return notNull(o, "object is null");
    }

    public Object notNull(Object o, String warnMsg) {
        if (doCheck && o == null)
            onFailure(warnMsg);
        return o;
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

    private static class ArgChecker extends CheckerAdapter {

        @Override
        protected void onFailure(String errMsg, Throwable cause) {
            throw new IllegalArgumentException(errMsg, cause);
        }
    }

}
