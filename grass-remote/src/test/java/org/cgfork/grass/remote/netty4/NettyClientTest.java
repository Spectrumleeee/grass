/**
 * Copyright (c) 2014, TP-Link Co,Ltd.
 * Author: C_G <cg.fork@gmail.com>
 * Updated: 2015/10/26
 */
package org.cgfork.grass.remote.netty4;

import org.cgfork.grass.remote.*;
import org.junit.Test;

public class NettyClientTest {

    @Test
    public void TestNettyClient() throws Exception {
        NettyClient client = new NettyClient(new Handler(), new RemoteLocator("grass://127.0.0" +
                ".1:9999/test?codec=testCodec"));

        TestCodec.Message message = new TestCodec.Message();
        message.setIndex(1);
        message.setBody("hello grass");
        client.write(message);
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
            System.out.println(((TestCodec.Message)message).getBody());
        }

        @Override
        public void onRead(ChannelContext ctx, Object message) throws RemoteException {
            System.out.println(((TestCodec.Message)message).getBody());
        }

        @Override
        public void onCaught(ChannelContext ctx, Throwable cause) throws RemoteException {

        }
    }
}