package org.cgfork.grass.common.serialize.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cgfork.grass.common.UnsupportedException;
import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.common.serialize.DataInput;

import java.io.*;
import java.util.Map;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class JsonDataInput implements DataInput {

    private final InputStream in;

    private final ObjectMapper mapper;

    public JsonDataInput(InputStream in) {
        Checker.Arg.notNull(in);
        this.in = in;
        this.mapper = new ObjectMapper();
    }

    @Override
    public boolean readBool() throws IOException {
        return readValue(Boolean.class);
    }

    @Override
    public byte readByte() throws IOException {
        return readValue(Byte.class);
    }

    @Override
    public short readShort() throws IOException {
        return readValue(Short.class);
    }

    @Override
    public int readInt() throws IOException {
        return readValue(Integer.class);
    }

    @Override
    public long readLong() throws IOException {
        return readValue(Long.class);
    }

    @Override
    public float readFloat() throws IOException {
        return readValue(Float.class);
    }

    @Override
    public double readDouble() throws IOException {
        return readValue(Double.class);
    }

    @Override
    public String readUTF() throws IOException {
        return readValue(String.class);
    }

    @Override
    public byte[] readBytes() throws IOException {
        return readValue(byte[].class);
    }

    protected InputStream getInputStream() {
        return in;
    }

    protected <T> T readValue(Class<T> clazz) throws IOException {
        return mapper.readValue(in, clazz);
    }

    protected <T> T readValue(String json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }

    protected <T> T readValue(byte[] json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }
}
