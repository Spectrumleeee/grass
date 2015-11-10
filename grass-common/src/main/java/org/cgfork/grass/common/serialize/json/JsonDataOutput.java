package org.cgfork.grass.common.serialize.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cgfork.grass.common.serialize.DataOutput;

import java.io.*;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class JsonDataOutput implements DataOutput {

    private final Writer writer;

    private final ObjectMapper mapper;

    public JsonDataOutput(OutputStream out) {
        this(new OutputStreamWriter(out));
    }

    public JsonDataOutput(Writer writer) {
        this.writer = writer;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void writeBool(boolean v) throws IOException {
        writeValue(v);
    }

    @Override
    public void writeByte(byte v) throws IOException {
        writeValue(v);
    }

    @Override
    public void writeShort(short v) throws IOException {
        writeValue(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        writeValue(v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        writeValue(v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        writeValue(v);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        writeValue(v);
    }

    @Override
    public void writeUTF(String v) throws IOException {
        writeValue(v);
    }

    @Override
    public void writeBytes(byte[] v) throws IOException {
        writeValue(v);
    }

    @Override
    public void writeBytes(byte[] v, int off, int len) throws IOException {
        writeValue(v);
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    protected void writeValue(Object o) throws IOException{
        mapper.writeValue(writer, o);
    }
}
