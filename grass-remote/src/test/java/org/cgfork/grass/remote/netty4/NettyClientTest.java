package org.cgfork.grass.remote.netty4;

import org.cgfork.grass.remote.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class NettyClientTest {

    private Handler serverHandler = null;

    private NettyServer server = null;

    private Object testMessage = "hello grass";

    private Location location = null;

    @Before
    public void setup() throws Exception {
        location = new Location("grass://127.0.0.1:9898/text?codec=testCodec");
        serverHandler = new Handler("server");
        server = new NettyServer(location, serverHandler);
    }

    @After
    public void tearDown() throws Exception {
        if (server != null)
            server.shutdown();
    }

    @Test
    public void testNettyClientWrite() throws Exception {
        Handler handler = new Handler("client");
        NettyClient client = new NettyClient(location, handler);
        client.write(testMessage);
        Thread.sleep(1000);
        client.close();
        assertEquals(0, handler.onCaughtCount.get());
        assertEquals(0, serverHandler.onCaughtCount.get());
    }

    @Test
    public void testNettyClientClose() throws Exception {
        List<Client> clients = new ArrayList<>(100);
        Handler handler = new Handler("client");
        for (int i = 0; i < 100; i++) {
            clients.add(new NettyClient(location, handler));
            Thread.sleep(10);
        }

        for (Client client : clients) {
            client.close();
        }

        Thread.sleep(1000);

        assertEquals(100, handler.onConnectedCount.get());
        assertEquals(100, serverHandler.onConnectedCount.get());
        assertEquals(100, handler.onDisconnectedCount.get());
        assertEquals(100, serverHandler.onDisconnectedCount.get());
        assertEquals(0, handler.onWrittenCount.get());
        assertEquals(0, serverHandler.onWrittenCount.get());
        assertEquals(0, handler.onReadCount.get());
        assertEquals(0, serverHandler.onReadCount.get());
        assertEquals(0, handler.onCaughtCount.get());
        assertEquals(0, serverHandler.onCaughtCount.get());
    }

    @Test
    public void testNettyServerClose() throws Exception {
        List<Client> clients = new ArrayList<>(100);
        Handler handler = new Handler("client");
        for (int i = 0; i < 100; i++) {
            clients.add(new NettyClient(location, handler));
            Thread.sleep(10);
        }

        server.shutdown();
        Thread.sleep(1000);
        for (Client client : clients) {
            assertTrue(!client.isConnected());
        }
        assertEquals(100, handler.onConnectedCount.get());
        assertEquals(100, serverHandler.onConnectedCount.get());
        assertEquals(100, handler.onDisconnectedCount.get());
        assertEquals(100, serverHandler.onDisconnectedCount.get());
        assertEquals(0, handler.onWrittenCount.get());
        assertEquals(0, serverHandler.onWrittenCount.get());
        assertEquals(0, handler.onReadCount.get());
        assertEquals(0, serverHandler.onReadCount.get());
        assertEquals(0, handler.onCaughtCount.get());
        assertEquals(0, serverHandler.onCaughtCount.get());
    }

    private class Handler implements ChannelHandler {

        String name;

        AtomicInteger onConnectedCount = new AtomicInteger(0);

        AtomicInteger onDisconnectedCount = new AtomicInteger(0);

        AtomicInteger onWrittenCount = new AtomicInteger(0);

        AtomicInteger onReadCount = new AtomicInteger(0);

        AtomicInteger onCaughtCount = new AtomicInteger(0);

        public Handler(String name) {
            this.name = name;
        }

        @Override
        public void onConnected(ChannelContext ctx) throws RemoteException {
            onConnectedCount.incrementAndGet();
            System.out.println(name + ": connected");
        }

        @Override
        public void onDisconnected(ChannelContext ctx) throws RemoteException {
            onDisconnectedCount.incrementAndGet();
            System.out.println(name + ": disconnected");
        }

        @Override
        public void onWritten(ChannelContext ctx, Object message) throws RemoteException {
            onWrittenCount.incrementAndGet();
            assertEquals(testMessage, message);
        }

        @Override
        public void onRead(ChannelContext ctx, Object message) throws RemoteException {
            onReadCount.incrementAndGet();
            System.out.println(message);
            if ("server".equals(name)) {
                ctx.channel().write(message);
            }
            assertEquals(testMessage, message);
        }

        @Override
        public void onCaught(ChannelContext ctx, Throwable cause) throws RemoteException {
            onCaughtCount.incrementAndGet();
            cause.printStackTrace();
        }
    }
}