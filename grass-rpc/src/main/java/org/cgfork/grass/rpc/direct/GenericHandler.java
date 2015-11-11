package org.cgfork.grass.rpc.direct;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface GenericHandler{

    void handleConnected() throws GenericException;

    void handleRequest() throws GenericException;

    void handleDisconnected() throws GenericException;

    void handleException() throws GenericException;
}
