/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-13
 */
package org.cgfork.grass.remote.netty4;

import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.RemoteLocator;

import io.netty.channel.ChannelOutboundHandlerAdapter;

/**
 * 
 */
public class NettyOutboundHandler extends ChannelOutboundHandlerAdapter {
    
    public NettyOutboundHandler(RemoteLocator locator, ChannelHandler handler) {
        
    }
}
