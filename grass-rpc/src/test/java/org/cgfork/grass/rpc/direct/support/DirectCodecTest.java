/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
package org.cgfork.grass.rpc.direct.support;

import org.cgfork.grass.remote.ChannelContext;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.Location;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.remote.netty4.NettyClient;
import org.cgfork.grass.remote.netty4.NettyServer;
import org.cgfork.grass.rpc.direct.Request;
import org.cgfork.grass.rpc.direct.Response;
import org.cgfork.grass.rpc.direct.protocol.RemoteMethod;
import org.cgfork.grass.rpc.direct.protocol.RemoteParameter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DirectCodecTest {

    private NettyServer server = null;

    private Location location = null;

    @Before
    public void setup() throws Exception {
        location = new Location("grass://127.0.0.1:9999/text?codec=directCodec&serializer=jsonSerializer");
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
        Request request = new Request(1);
        RemoteMethod method = new RemoteMethod();
        method.setMethod("invoke");
        List<RemoteParameter> parameters = new ArrayList<>();
        parameters.add(new RemoteParameter("request"));
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
            if (message instanceof Request) {
                Request request = (Request) message;
//                RemoteMethod method = (RemoteMethod) request.getData();
//                System.out.println(name + ": read request: " + method.getMethod());
                System.out.println(name + ": read request: " + request.getData());

                Response response = new Response();
                response.setId(request.getId());
                response.setData(request.getData());
                ctx.write(response);
            }

            if (message instanceof Response) {
                Response response = (Response) message;
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