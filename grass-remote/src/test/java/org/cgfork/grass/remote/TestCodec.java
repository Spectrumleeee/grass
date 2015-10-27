package org.cgfork.grass.remote;

import org.cgfork.grass.common.addon.Addon;

import java.io.IOException;
import java.util.List;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
@Addon("testCodec")
public class TestCodec implements Codec {

    @Override
    public void encode(Channel channel, ChannelBuffer out, Object message) throws IOException {
        Message newMessage = (Message) message;
        byte[] body = newMessage.getBody().getBytes();
        out.writeShort((short)(body.length + 8));
        out.writeLong(newMessage.getIndex());
        out.writeBytes(body);
    }

    @Override
    public boolean decode(Channel channel, ChannelBuffer in, List<Object> out) throws IOException {
        in.markReaderIndex();
        if (!in.isReadable()) {
            in.resetReaderIndex();
            return false;
        }

        if (in.readableBytes() < 2) {
            in.resetReaderIndex();
            return false;
        }

        int length = (int) in.readShort();
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return false;
        }

        ChannelBuffer cb = in.readBytes(length);

        final byte[] array;
        final int offset;
        final int len = cb.readableBytes();
        if (cb.hasArray()) {
            array = cb.array();
            offset = cb.arrayOffset() + cb.readerIndex();
        } else {
            array = new byte[len];
            cb.getBytes(cb.readerIndex(), array, 0, len);
            offset = 0;
        }
        Message newMessage = new Message();
        newMessage.setIndex(getLong(array, offset));
        newMessage.setBody(new String(array, offset + 8, length - offset - 8));
        newMessage.setBody(new String(array, offset + 8, length - offset - 8));
        out.add(newMessage);
        return true;
    }

    public static class Message {
        private long index;

        private String body;

        public long getIndex() {
            return index;
        }

        public void setIndex(long index) {
            this.index = index;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }

    public static long getLong(byte[] b, int off) {
        return ((b[off + 7] & 0xFFL)) + ((b[off + 6] & 0xFFL) << 8)
                + ((b[off + 5] & 0xFFL) << 16) + ((b[off + 4] & 0xFFL) << 24)
                + ((b[off + 3] & 0xFFL) << 32) + ((b[off + 2] & 0xFFL) << 40)
                + ((b[off + 1] & 0xFFL) << 48) + (((long) b[off]) << 56);
    }
}
