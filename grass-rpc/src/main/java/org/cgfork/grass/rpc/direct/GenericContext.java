package org.cgfork.grass.rpc.direct;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface GenericContext extends GenericChannel {

    GenericChannel channel();

    void handleConnected() throws Exception;

    void handleRequest(GenericRequest request) throws Exception;

    void handleDisconnected() throws Exception;

    void handleException(Throwable cause) throws Exception;
}
