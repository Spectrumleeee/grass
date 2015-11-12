package org.cgfork.grass.rpc.generic.support;

import org.cgfork.grass.common.UnsupportedException;
import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.Location;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.remote.Transporter;
import org.cgfork.grass.remote.netty4.NettyTransporter;
import org.cgfork.grass.rpc.generic.GenericClient;
import org.cgfork.grass.rpc.generic.GenericHandler;
import org.cgfork.grass.rpc.generic.GenericServer;
import org.cgfork.grass.rpc.generic.GenericTransporter;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class DefaultGenericTransporter implements GenericTransporter {

    private DefaultGenericClientHandler clientHandler = new DefaultGenericClientHandler();

    @Override
    public GenericClient connect(Location location) throws RemoteException {
        Checker.Arg.notNull(location);
        Transporter transporter = getTransporter(location);
        return new DefaultGenericClient(transporter.connect(location, clientHandler));
    }

    @Override
    public GenericServer bind(Location location, GenericHandler handler) throws RemoteException {
        Checker.Arg.notNull(location);
        Transporter transporter = getTransporter(location);
        ChannelHandler channelHandler = new DefaultGenericServerHandler(handler);
        return new DefaultGenericServer(transporter.bind(location, channelHandler), handler);
    }

    private Transporter getTransporter(Location location) {
        String socket = location.getParameter("socket");
        if (socket == null || "netty".equals(socket) || "".equals(socket)) {
            return new NettyTransporter();
        }
        throw new UnsupportedException("Unsupported socket for " + socket);
    }
}
