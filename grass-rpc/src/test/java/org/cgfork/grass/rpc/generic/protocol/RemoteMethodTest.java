/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
package org.cgfork.grass.rpc.generic.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RemoteMethodTest {
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJackson() throws Exception {
        RemoteMethod method = new RemoteMethod();
        method.setMethod("invoke");
        List<RemoteParameter> parameters = new ArrayList<>();
        parameters.add(new RemoteParameter("invoke"));
        method.setParameters(parameters);

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream();
        out.connect(in);

        mapper.writeValue(out, method);
        RemoteMethod method1 = mapper.readValue(in, RemoteMethod.class);
        assertEquals("invoke", method1.getMethod());
        assertEquals(1, method1.getParameters().size());
        assertEquals(String.class, method1.getParameters().get(0).getType());
    }
}