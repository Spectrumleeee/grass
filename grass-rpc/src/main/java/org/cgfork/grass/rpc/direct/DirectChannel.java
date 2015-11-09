package org.cgfork.grass.rpc.direct;

import org.cgfork.grass.remote.Channel;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface DirectChannel extends Channel {

    ResponseFuture request(Object request) throws DirectException;

    ResponseFuture request(Object request, int timeout) throws DirectException;

    DirectHandler directHandler();

    void close(int timeout);
}
