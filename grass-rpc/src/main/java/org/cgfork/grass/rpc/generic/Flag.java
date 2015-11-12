package org.cgfork.grass.rpc.generic;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class Flag {

    public static final byte FLAG_EVENT = (byte)0x08;

    public static final byte FLAG_BUSY = (byte)0x04;

    public static final byte FLAG_NOT_OK = (byte)0x02;

    public static final byte FLAG_REQ = (byte)0x01;

    public static final byte FLAG_RSP = (byte)0x00;

    private static final int NOT_OK = FLAG_NOT_OK;

    private static final int BUSY = FLAG_BUSY;

    private static final int REQ = FLAG_REQ;

    private static final int RSP = FLAG_RSP;

    private static final int EVENT = FLAG_EVENT;

    private int flag;

    public Flag(int flag) {
        this.flag = 0x00ff & flag;
    }

    public Flag(byte flag) {
        this.flag = 0x00ff & flag;
    }

    public byte getFlag() {
        return (byte)flag;
    }

    public void setBusyFlag() {
        flag |= FLAG_BUSY;
    }

    public void setReqFlag() {
        flag |= FLAG_REQ;
    }

    public void setRspFlag() {
        flag |= FLAG_RSP;
    }

    public void setHbFlag() {
        flag |= FLAG_EVENT;
    }

    public void setNotOkFlag() {
        flag |= FLAG_NOT_OK;
    }

    public boolean isNotOk() {
        return (flag & NOT_OK) == NOT_OK;
    }

    public boolean isOk() {
        return !isNotOk();
    }

    public boolean isEvent() {
        return (flag & EVENT) == EVENT;
    }

    public boolean isReq() {
        return (flag & REQ) == REQ;
    }

    public boolean isRsp() {
        return !isReq();
    }

    public boolean isBusy() {
        return (flag & BUSY) == BUSY;
    }

    public static boolean isNotOk(byte flag) {
        return (FLAG_NOT_OK & flag) == NOT_OK;
    }

    public static boolean isOk(byte flag) {
        return !isNotOk(flag);
    }

    public static boolean isEvent(byte flag) {
        return (FLAG_EVENT & flag) == EVENT;
    }

    public static boolean isReq(byte flag) {
        return (FLAG_REQ & flag) == REQ;
    }

    public static boolean isRsp(byte flag) {
        return !isReq(flag);
    }

    public static boolean isBusy(byte flag) {
        return (FLAG_BUSY & flag) == BUSY;
    }
}
