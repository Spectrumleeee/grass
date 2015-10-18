/**
 * Copyright (c) 2015, TP-Link Co.,Ltd.
 * Author:  chenbiren <chenbiren@tp-link.net>
 * Created: 2015-10-18
 */
package org.cgfork.grass;

import java.io.IOException;
import java.util.List;

import org.cgfork.grass.common.addon.Addon;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.ChannelBuffer;
import org.cgfork.grass.remote.Codec;

/**
 * 
 */
@Addon("tcodec")
public class TCodec implements Codec {

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
