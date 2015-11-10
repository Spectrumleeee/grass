package org.cgfork.grass.rpc.serialize.test;

import org.cgfork.grass.rpc.serialize.ObjectOutput;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class TextObjectOutput implements ObjectOutput {
    private final OutputStream out;

    private final boolean writeLine;

    private final String lineSeparator;

    public TextObjectOutput(OutputStream out) {
        this(out, false);
    }

    public TextObjectOutput(OutputStream out, boolean writeLine) {
        this.out = out;
        this.writeLine = writeLine;
        lineSeparator = java.security.AccessController.doPrivileged(
                new sun.security.action.GetPropertyAction("line.separator"));
    }

    @Override
    public void writeObject(Object v) throws IOException {
        writeUTF(String.valueOf(v));
    }

    @Override
    public void writeBool(boolean v) throws IOException {
        writeUTF(String.valueOf(v));
    }

    @Override
    public void writeByte(byte v) throws IOException {
        writeUTF(String.valueOf(v));
    }

    @Override
    public void writeShort(short v) throws IOException {
        writeUTF(String.valueOf(v));
    }

    @Override
    public void writeInt(int v) throws IOException {
        writeUTF(String.valueOf(v));
    }

    @Override
    public void writeLong(long v) throws IOException {
        writeUTF(String.valueOf(v));
    }

    @Override
    public void writeFloat(float v) throws IOException {
        writeUTF(String.valueOf(v));
    }

    @Override
    public void writeDouble(double v) throws IOException {
        writeUTF(String.valueOf(v));
    }

    @Override
    public void writeUTF(String v) throws IOException {
        out.write(v.getBytes());
    }

    @Override
    public void writeBytes(byte[] v) throws IOException {
        out.write(v);
    }

    @Override
    public void writeBytes(byte[] v, int off, int len) throws IOException {
        out.write(v, off, len);
    }

    @Override
    public void flush() throws IOException {
        if (writeLine) {
            writeUTF(lineSeparator);
        }
        out.flush();
    }
}
