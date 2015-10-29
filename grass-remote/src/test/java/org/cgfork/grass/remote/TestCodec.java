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
        byte[] body = message.toString().getBytes();
        out.writeShort((short)body.length);
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
        out.add(new String(array, offset, length - offset));
        return true;
    }
}
