package org.cgfork.grass.rpc.direct.support;

import org.cgfork.grass.common.UnsupportedException;
import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.Location;
import org.cgfork.grass.remote.RemoteException;
import org.cgfork.grass.remote.Transporter;
import org.cgfork.grass.remote.netty4.NettyTransporter;
import org.cgfork.grass.rpc.direct.GenericClient;
import org.cgfork.grass.rpc.direct.GenericHandler;
import org.cgfork.grass.rpc.direct.GenericServer;
import org.cgfork.grass.rpc.direct.GenericTransporter;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class DirectGenericTransporter implements GenericTransporter {

    private DirectGenericClientHandler clientHandler = new DirectGenericClientHandler();

    @Override
    public GenericClient connect(Location location) throws RemoteException {
        Checker.Arg.notNull(location);
        Transporter transporter = getTransporter(location);
        return new DirectGenericClient(transporter.connect(location, clientHandler));
    }

    @Override
    public GenericServer bind(Location location, GenericHandler handler) throws RemoteException {
        Checker.Arg.notNull(location);
        Transporter transporter = getTransporter(location);
        ChannelHandler channelHandler = new DirectGenericServerHandler(handler);
        return new DirectGenericServer(transporter.bind(location, channelHandler), handler);
    }

    private Transporter getTransporter(Location location) {
        String socket = location.getParameter("socket");
        if (socket == null || "netty".equals(socket) || "".equals(socket)) {
            return new NettyTransporter();
        }
        throw new UnsupportedException("Unsupported socket for " + socket);
    }
}
