/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
package org.cgfork.grass.rpc.generic.support;

import org.cgfork.grass.remote.ChannelContext;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.Location;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.remote.netty4.NettyClient;
import org.cgfork.grass.remote.netty4.NettyServer;
import org.cgfork.grass.rpc.generic.GenericRequest;
import org.cgfork.grass.rpc.generic.GenericResponse;
import org.cgfork.grass.rpc.generic.protocol.RemoteMethod;
import org.cgfork.grass.rpc.generic.protocol.RemoteParameter;
import org.cgfork.grass.rpc.generic.protocol.RemoteReturn;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectCodecTest {

    private NettyServer server = null;

    private Location location = null;

    @Before
    public void setup() throws Exception {
        location = new Location("grass://127.0.0.1:9090/text?codec=directCodec&serializer=jsonSerializer");
        server = new NettyServer(location, new Handler("server"));
    }

    @After
    public void tearDown() throws Exception {
        if (server != null)
            server.shutdown();
    }

    @Test
    public void testNettyClientWrite() throws Exception {
        NettyClient client = new NettyClient(location, new Handler("client"));
        GenericRequest request = new GenericRequest(1);
        RemoteMethod method = new RemoteMethod();
        method.setMethod("invoke");
        List<RemoteParameter> parameters = new ArrayList<>();
        parameters.add(new RemoteParameter("invoke"));
        method.setParameters(parameters);
        request.setData(method);
        System.out.println(client.codec());
        client.write(request);
        Thread.sleep(5000);
        client.close();
    }

    private class Handler implements ChannelHandler {

        String name;

        public Handler(String name) {
            this.name = name;
        }

        @Override
        public void onConnected(ChannelContext ctx) throws RemoteException {
            System.out.println(name + ": connected");
        }

        @Override
        public void onDisconnected(ChannelContext ctx) throws RemoteException {
            System.out.println(name + ": disconnected");
        }

        @Override
        public void onWritten(ChannelContext ctx, Object message) throws RemoteException {
            System.out.println(name + ": written: " + message);
        }

        @Override
        public void onRead(ChannelContext ctx, Object message) throws RemoteException {
            if (message instanceof GenericRequest) {
                GenericRequest request = (GenericRequest) message;
//                RemoteMethod method = (RemoteMethod) invoke.getData();
//                System.out.println(name + ": read invoke: " + method.getMethod());
                System.out.println(name + ": read invoke: " + request.getData());

                GenericResponse response = new GenericResponse();
                response.setId(request.getId());
                Map<String, Object> map =new HashMap<>();
                map.put("id", 0);
                map.put("error_code", 0);
                RemoteReturn rr = new RemoteReturn();
                rr.setValue(map);
                response.setData(rr);
                ctx.write(response);
            }

            if (message instanceof GenericResponse) {
                GenericResponse response = (GenericResponse) message;
                System.out.println(name + ": read response: " + response.getData());
            }
        }

        @Override
        public void onCaught(ChannelContext ctx, Throwable cause) throws RemoteException {
            System.out.println(name + ": caught");
            cause.printStackTrace();
        }
    }
}