package org.cgfork.grass.remote;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 * Updated: 2015/10/27
 */
public class RemoteException extends Exception{

    private static final long serialVersionUID = 1L;
    
    public RemoteException() {
        super();
    }
    
    public RemoteException(String message) {
        super(message);
    }
    
    public RemoteException(Throwable cause) {
        super(cause);
    }

    public RemoteException(String message, Throwable cause){
        super(message, cause);
    }
    
    public RemoteException(Channel channel) {
        this("Failed to send message from " + channel.getLocalAddress()
                + " to " + channel.getRemoteAddress());
    }
    
    public RemoteException(Channel channel, String cause) {
        this("Failed to send message from " + channel.getLocalAddress()
                + " to " + channel.getRemoteAddress() + ", cause by " + cause);
    }
    
    public RemoteException(Channel channel, Throwable cause) {
        this("Failed to send message from " + channel.getLocalAddress()
                + " to " + channel.getRemoteAddress(), cause);
    }
}
