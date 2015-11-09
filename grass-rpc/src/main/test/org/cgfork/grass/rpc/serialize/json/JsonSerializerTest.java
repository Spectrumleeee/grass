package org.cgfork.grass.rpc.serialize.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.cgfork.grass.rpc.serialize.Serializer;
import org.junit.Test;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import static org.junit.Assert.*;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class JsonSerializerTest {

    @Test
    public void testJsonSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        boolean v = mapper.readValue("1", Boolean.class);
        System.out.println(v);
    }

}