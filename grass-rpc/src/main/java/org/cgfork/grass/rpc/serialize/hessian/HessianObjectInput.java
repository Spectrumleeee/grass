package org.cgfork.grass.rpc.serialize.hessian;

import com.caucho.hessian.io.Hessian2Input;
import org.cgfork.grass.rpc.serialize.ObjectInput;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class HessianObjectInput implements ObjectInput {

    private final Hessian2Input hessian;

    public HessianObjectInput(InputStream in) {
        hessian = new Hessian2Input(in);
    }

    @Override
    public Object readObject() throws IOException {
        return hessian.readObject();
    }

    @Override
    public Object readObject(int length) throws IOException {
        // ignore length
        return hessian.readObject();
    }

    @Override
    public boolean readBool() throws IOException {
        return hessian.readBoolean();
    }

    @Override
    public byte readByte() throws IOException {
        return (byte)hessian.readByte();
    }

    @Override
    public short readShort() throws IOException {
        return hessian.readShort();
    }

    @Override
    public int readInt() throws IOException {
        return hessian.readInt();
    }

    @Override
    public long readLong() throws IOException {
        return hessian.readLong();
    }

    @Override
    public float readFloat() throws IOException {
        return hessian.readFloat();
    }

    @Override
    public double readDouble() throws IOException {
        return hessian.readDouble();
    }

    @Override
    public String readUTF() throws IOException {
        return hessian.readString();
    }

    @Override
    public byte[] readBytes() throws IOException {
        return hessian.readBytes();
    }
}
