package org.cgfork.grass.rpc.direct;

import org.cgfork.grass.remote.RemoteException;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface GenericClient extends GenericChannel {
    void reconnect() throws RemoteException;
}
