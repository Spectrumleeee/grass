package org.cgfork.grass.remote.netty4;

import org.cgfork.grass.remote.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class NettyClientTest {

    private static final Logger log = LoggerFactory.getLogger(NettyClientTest.class);

    @Test
    public void TestNettyClient() throws Exception {
        NettyClient client = new NettyClient(new Handler(), new RemoteLocator("grass://127.0.0" +
                ".1:9999/test?codec=testCodec"));

        client.write("hello grass");
    }

    private static class Handler implements ChannelHandler {

        @Override
        public void onConnected(ChannelContext ctx) throws RemoteException {

        }

        @Override
        public void onDisconnected(ChannelContext ctx) throws RemoteException {

        }

        @Override
        public void onWritten(ChannelContext ctx, Object message) throws RemoteException {
            log.info(message.toString());
        }

        @Override
        public void onRead(ChannelContext ctx, Object message) throws RemoteException {
            log.info(message.toString());
        }

        @Override
        public void onCaught(ChannelContext ctx, Throwable cause) throws RemoteException {

        }
    }
}