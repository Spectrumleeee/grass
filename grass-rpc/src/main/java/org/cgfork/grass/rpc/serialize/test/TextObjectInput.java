package org.cgfork.grass.rpc.serialize.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cgfork.grass.rpc.serialize.ObjectInput;

import java.io.*;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class TextObjectInput implements ObjectInput {
    private final BufferedReader reader;

    private final InputStream in;

    public TextObjectInput(InputStream in) {
        this.in = in;
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    @Override
    public Object readObject() throws IOException {
        return readUTF();
    }

    @Override
    public Object readObject(int length) throws IOException {
        if (length == 0) {
            return "";
        }
        byte[] bytes = new byte[length];
        if (in.read(bytes) < length) {
            throw new IOException("readable bytes is not so long");
        }
        return new String(bytes);
    }

    @Override
    public boolean readBool() throws IOException {
        return Boolean.valueOf(readUTF());
    }

    @Override
    public byte readByte() throws IOException {
        return Byte.valueOf(readUTF());
    }

    @Override
    public short readShort() throws IOException {
        return Short.valueOf(readUTF());
    }

    @Override
    public int readInt() throws IOException {
        return Integer.valueOf(readUTF());
    }

    @Override
    public long readLong() throws IOException {
        return Long.valueOf(readUTF());
    }

    @Override
    public float readFloat() throws IOException {
        return Float.valueOf(readUTF());
    }

    @Override
    public double readDouble() throws IOException {
        return Double.valueOf(readUTF());
    }

    @Override
    public String readUTF() throws IOException {
        return reader.readLine();
    }

    @Override
    public byte[] readBytes() throws IOException {
        return readUTF().getBytes();
    }
}
