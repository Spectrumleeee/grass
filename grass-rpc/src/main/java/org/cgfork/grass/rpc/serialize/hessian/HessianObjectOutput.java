package org.cgfork.grass.rpc.serialize.hessian;

import com.caucho.hessian.io.Hessian2Output;
import org.cgfork.grass.rpc.serialize.ObjectOutput;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class HessianObjectOutput implements ObjectOutput {

    private final Hessian2Output hessian;

    public HessianObjectOutput(OutputStream out) {
        hessian = new Hessian2Output(out);
    }

    @Override
    public void writeObject(Object v) throws IOException {
        hessian.writeObject(v);
    }

    @Override
    public void writeBool(boolean v) throws IOException {
        hessian.writeBoolean(v);
    }

    @Override
    public void writeByte(byte v) throws IOException {
        hessian.writeObject(v);
    }

    @Override
    public void writeShort(short v) throws IOException {
        hessian.writeObject(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        hessian.writeInt(v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        hessian.writeLong(v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        hessian.writeObject(v);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        hessian.writeDouble(v);
    }

    @Override
    public void writeUTF(String v) throws IOException {
        hessian.writeString(v);
    }

    @Override
    public void writeBytes(byte[] v) throws IOException {
        hessian.writeBytes(v);
    }

    @Override
    public void writeBytes(byte[] v, int off, int len) throws IOException {
        hessian.writeBytes(v, off, len);
    }

    @Override
    public void flush() throws IOException {
        hessian.flush();
    }
}
