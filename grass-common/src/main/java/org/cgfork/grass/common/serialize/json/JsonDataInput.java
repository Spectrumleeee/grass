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
        throw new UnsupportedException();
    }

    @Override
    public byte readByte() throws IOException {
       throw new UnsupportedException();
    }

    @Override
    public short readShort() throws IOException {
        throw new UnsupportedException();
    }

    @Override
    public int readInt() throws IOException {
        throw new UnsupportedException();
    }

    @Override
    public long readLong() throws IOException {
        throw new UnsupportedException();
    }

    @Override
    public float readFloat() throws IOException {
        throw new UnsupportedException();
    }

    @Override
    public double readDouble() throws IOException {
        throw new UnsupportedException();
    }

    @Override
    public String readUTF() throws IOException {
        throw new UnsupportedException();
    }

    @Override
    public byte[] readBytes() throws IOException {
        throw new UnsupportedException();
    }

    public Object readObject() throws IOException {
        try {
            String jsonLine = reader.readLine();
            if (jsonLine == null || jsonLine.trim().length() == 0) {
                throw new EOFException();
            }
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
}
