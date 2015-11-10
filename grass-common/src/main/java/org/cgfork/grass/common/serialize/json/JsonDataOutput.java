package org.cgfork.grass.common.serialize.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cgfork.grass.common.serialize.DataOutput;

import java.io.*;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class JsonDataOutput implements DataOutput {

    private final PrintWriter writer;

    private final ObjectMapper mapper;

    public JsonDataOutput(OutputStream out) {
        this(new OutputStreamWriter(out));
    }

    public JsonDataOutput(Writer writer) {
        this.writer = new PrintWriter(writer);
        this.mapper = new ObjectMapper();
    }

    @Override
    public void writeBool(boolean v) throws IOException {
        mapper.writeValue(writer, v);
    }

    @Override
    public void writeByte(byte v) throws IOException {
        mapper.writeValue(writer, v);
    }

    @Override
    public void writeShort(short v) throws IOException {
        mapper.writeValue(writer, v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        mapper.writeValue(writer, v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        mapper.writeValue(writer, v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        mapper.writeValue(writer, v);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        mapper.writeValue(writer, v);
    }

    @Override
    public void writeUTF(String v) throws IOException {
        mapper.writeValue(writer, v);
    }

    @Override
    public void writeBytes(byte[] v) throws IOException {
        mapper.writeValue(writer, v);
    }

    @Override
    public void writeBytes(byte[] v, int off, int len) throws IOException {
        mapper.writeValue(writer, v);
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    public void writeObject(Object o) throws IOException {
        mapper.writeValue(writer, o);
        writer.println();
        writer.flush();
    }
}
