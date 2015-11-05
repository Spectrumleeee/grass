package org.cgfork.grass.rpc.protocol.grass;

import org.cgfork.grass.common.addon.Addon;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.ChannelBuffer;
import org.cgfork.grass.remote.Codec;

import java.io.IOException;
import java.util.List;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */

@Addon("grassCodec")
public class GrassCodec implements Codec {

    @Override
    public void encode(Channel channel, ChannelBuffer out, Object message) throws IOException {

    }

    @Override
    public boolean decode(Channel channel, ChannelBuffer in, List<Object> out) throws IOException {
        return false;
    }
}
