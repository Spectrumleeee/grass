/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-13
 */
package org.cgfork.grass;

import java.net.MalformedURLException;
import org.cgfork.grass.remote.ChannelBuffer;
import org.cgfork.grass.remote.ChannelContext;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.RemoteClient;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.remote.RemoteLocator;
import org.cgfork.grass.remote.Transporter;
import org.cgfork.grass.remote.netty4.NettyTransporter;

/**
 * 
 */
public class Main {

    static class Handler implements ChannelHandler {

        @Override
        public void onConnected(ChannelContext ctx) throws RemoteException {
            System.out.println("Connected: " + ctx);
        }

        @Override
        public void onDisconnected(ChannelContext ctx) throws RemoteException {
            System.out.println("Disconnected: " + ctx);
        }

        @Override
        public void onWritten(ChannelContext ctx, Object message)
                throws RemoteException {
            System.out.println("Written: " + ctx);
        }

        @Override
        public void onRead(ChannelContext ctx, Object message)
                throws RemoteException {
            System.out.println("Read: " + ctx);
            final ChannelBuffer msg = (ChannelBuffer) message;
            final byte[] array;
            final int offset;
            final int length = msg.readableBytes();
            if (msg.hasArray()) {
                array = msg.array();
                offset = msg.arrayOffset() + msg.readerIndex();
            } else {
                array = new byte[length];
                msg.getBytes(msg.readerIndex(), array, 0, length);
                offset = 0;
            }
            System.out.println("Message: " + new String(array, offset, length - offset));
        }

        @Override
        public void onCaught(ChannelContext ctx, Throwable cause)
                throws RemoteException {
        }
        
    }

    /**
     * @param args
     * @throws RemoteException 
     * @throws MalformedURLException 
     */
    public static void main(String[] args) throws Exception {
        Transporter transpoter = new NettyTransporter();
        
        RemoteClient client = transpoter.connect(new RemoteLocator("grass://127.0.0.1:9999/test?codecClass=org.cgfork.grass.TCodec"), new Handler());
        
        client.write("hello grass!".getBytes());
        //client.close();
    }

}
