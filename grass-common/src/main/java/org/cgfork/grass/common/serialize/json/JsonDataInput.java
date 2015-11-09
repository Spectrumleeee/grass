package org.cgfork.grass.common.serialize.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cgfork.grass.common.UnsupportedException;
import org.cgfork.grass.common.serialize.DataInput;

import java.io.*;
import java.util.Map;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class JsonDataInput implements DataInput {

    private final BufferedReader reader;

    private final ObjectMapper mapper;

    public JsonDataInput(InputStream in) {
        this(new InputStreamReader(in));
    }

    public JsonDataInput(Reader reader) {
        this.reader = new BufferedReader(reader);
        this.mapper = new ObjectMapper();
    }

    @Override
    public boolean readBool() throws IOException {
        try {
            return mapper.readValue(readLine(), Boolean.class);
        } catch (EOFException e) {
            throw  new IOException(e);
        }
    }

    @Override
    public byte readByte() throws IOException {
        try {
            return mapper.readValue(readLine(), Byte.class);
        } catch (EOFException e) {
            throw  new IOException(e);
        }
    }

    @Override
    public short readShort() throws IOException {
        try {
            return mapper.readValue(readLine(), Short.class);
        } catch (EOFException e) {
            throw  new IOException(e);
        }
    }

    @Override
    public int readInt() throws IOException {
        try {
            return mapper.readValue(readLine(), Integer.class);
        } catch (EOFException e) {
            throw  new IOException(e);
        }
    }

    @Override
    public long readLong() throws IOException {
        try {
            return mapper.readValue(readLine(), Long.class);
        } catch (EOFException e) {
            throw  new IOException(e);
        }
    }

    @Override
    public float readFloat() throws IOException {
        try {
            return mapper.readValue(readLine(), Float.class);
        } catch (EOFException e) {
            throw  new IOException(e);
        }
    }

    @Override
    public double readDouble() throws IOException {
        try {
            return mapper.readValue(readLine(), Double.class);
        } catch (EOFException e) {
            throw  new IOException(e);
        }
    }

    @Override
    public String readUTF() throws IOException {
        try {
            return mapper.readValue(readLine(), String.class);
        } catch (EOFException e) {
            throw  new IOException(e);
        }
    }

    @Override
    public byte[] readBytes() throws IOException {
        try {
            return mapper.readValue(readLine(), byte[].class);
        } catch (EOFException e) {
            throw  new IOException(e);
        }
    }

    public Object readObject() throws IOException {
        try {
            String jsonLine = readLine();
            if (jsonLine.startsWith("{")) {
                return mapper.readValue(jsonLine, Map.class);
            } else {
                jsonLine = "{\"value\":" + jsonLine + "}";
                Map map = mapper.readValue(jsonLine, Map.class);
                return map.get("value");
            }
        } catch (JsonParseException e) {
            throw new IOException(e);
        }
    }

    private String readLine() throws EOFException, IOException {
        String jsonLine = reader.readLine();
        if (jsonLine == null || jsonLine.trim().length() == 0) {
            throw new EOFException();
        }
        return jsonLine;
    }
}
