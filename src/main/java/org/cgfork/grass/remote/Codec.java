/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-8
 */
package org.cgfork.grass.remote;

import java.util.List;
import java.io.IOException;

/**
 * 
 */
public interface Codec {
    void encode(Channel channel, ChannelBuffer out, Object message)
            throws IOException;
    
    boolean decode(Channel channel, ChannelBuffer in, List<Object> out)
            throws IOException;
}
