package org.cgfork.grass.rpc.generic;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface GenericHandler{

    void handleConnected(GenericContext context) throws Exception;

    void handleRequest(GenericContext context, GenericRequest request) throws Exception;

    void handleDisconnected(GenericContext context) throws Exception;

    void handleException(GenericContext context, Throwable cause) throws Exception;
}
