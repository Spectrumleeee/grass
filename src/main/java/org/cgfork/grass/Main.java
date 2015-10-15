/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-13
 */
package org.cgfork.grass;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.ChannelBuffer;
import org.cgfork.grass.remote.ChannelContext;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.Codec;
import org.cgfork.grass.remote.RemoteClient;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.remote.RemoteLocator;
import org.cgfork.grass.remote.Transporter;
import org.cgfork.grass.remote.netty4.NettyTransporter;

/**
 * 
 */
public class Main {
    
    public static class TestCodec implements Codec {

        @Override
        public void encode(Channel channel, ChannelBuffer out, Object msg)
                throws IOException {
            int bodyLen = ((byte[])msg).length;
            out.ensuredWritableBytes(bodyLen);
            out.writeByte((byte)(bodyLen >> 8));
            out.writeByte((byte)(bodyLen));
            out.writeBytes((byte[])msg);
        }

        @Override
        public boolean decode(Channel channel, ChannelBuffer in, List<Object> out)
                throws IOException {
            if (!in.isReadable()) {
                return false;
            }
            
            if (in.readableBytes() < 2) {
                return false;
            }
            
            int length = (int) in.readShort();
            if (in.readableBytes() < length) {
                return false;
            }
            out.add(in.readBytes(length));
            return true;
        }

    }

    
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
        
        RemoteClient client = transpoter.connect(new RemoteLocator("grass://127.0.0.1:9999/test"), new Handler());
        
        client.write("hello grass!".getBytes());
        //client.close();
    }

}
