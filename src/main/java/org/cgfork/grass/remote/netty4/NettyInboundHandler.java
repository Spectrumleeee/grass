/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-13
 */
package org.cgfork.grass.remote.netty4;

import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.RemoteLocator;

import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 */
public class NettyInboundHandler extends ChannelInboundHandlerAdapter  {

    public NettyInboundHandler(RemoteLocator locator, ChannelHandler handler) {
        
    }
}
