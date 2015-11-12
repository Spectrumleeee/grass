package org.cgfork.grass.rpc.generic;

import org.cgfork.grass.remote.Location;
import org.cgfork.grass.remote.RemoteException;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface GenericTransporter {
    GenericClient connect(Location location) throws RemoteException;

    GenericServer bind(Location location, GenericHandler handler) throws RemoteException;
}
