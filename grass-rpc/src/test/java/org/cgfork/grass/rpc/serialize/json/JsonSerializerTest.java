/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
package org.cgfork.grass.rpc.serialize.json;

import org.cgfork.grass.rpc.direct.Flag;
import org.cgfork.grass.rpc.direct.protocol.RemoteMethod;
import org.cgfork.grass.rpc.direct.protocol.RemoteParameter;
import org.cgfork.grass.rpc.serialize.ObjectInput;
import org.cgfork.grass.rpc.serialize.ObjectOutput;
import org.cgfork.grass.rpc.serialize.Serializer;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JsonSerializerTest {

    @Test
    public void testJsonSerializer1() throws IOException {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream();
        out.connect(in);
        RemoteMethod method = new RemoteMethod();
        method.setMethod("invoke");
        List<RemoteParameter> parameters = new ArrayList<>();
        parameters.add(new RemoteParameter("invoke"));
        method.setParameters(parameters);
        Flag flag = new Flag(Flag.FLAG_REQ);
        Serializer serializer = new JsonSerializer();

        ObjectInput input = serializer.deserialize(null, in, flag);
        ObjectOutput output = serializer.serialize(null, out, flag);

        output.writeObject(method);
        output.flush();
        RemoteMethod method1 = (RemoteMethod) input.readObject(in.available());
        assertEquals("invoke", method1.getMethod());
        assertEquals(1, method1.getParameters().size());
    }

    @Test
    public void testJsonSerializer() throws IOException {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream();
        out.connect(in);
        RemoteMethod method = new RemoteMethod();
        method.setMethod("invoke");
        List<RemoteParameter> parameters = new ArrayList<>();
        parameters.add(new RemoteParameter("invoke"));
        method.setParameters(parameters);
        Flag flag = new Flag(Flag.FLAG_REQ);
        Serializer serializer = new JsonSerializer();

        ObjectInput input = serializer.deserialize(null, in, flag);
        ObjectOutput output = serializer.serialize(null, out, flag);
        output.writeObject(method);
        output.flush();
        RemoteMethod method1 = (RemoteMethod) input.readObject();
        assertEquals("invoke", method1.getMethod());
        assertEquals(1, method1.getParameters().size());
    }
}