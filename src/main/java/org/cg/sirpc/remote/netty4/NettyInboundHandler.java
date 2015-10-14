/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-13
 */
package org.cg.sirpc.remote.netty4;

import org.cg.sirpc.remote.ChannelHandler;
import org.cg.sirpc.remote.RemoteLocator;

import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 */
public class NettyInboundHandler extends ChannelInboundHandlerAdapter  {

    public NettyInboundHandler(RemoteLocator locator, ChannelHandler handler) {
        
    }
}
