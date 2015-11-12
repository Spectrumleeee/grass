/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
package org.cgfork.grass.rpc.direct;

import org.cgfork.grass.remote.Location;
import org.cgfork.grass.rpc.direct.support.DirectGenericTransporter;
import org.junit.Before;
import org.junit.Test;

public class GenericTransporterTest {

    private GenericTransporter transporter;

    @Before
    public void setUp() throws Exception {
        transporter = new DirectGenericTransporter();
        transporter.bind(new Location("grass://127.0.0.1:9999/test?codec=directCodec&serializer=textSerializer"),
                new Handler());
    }

    @Test
    public void testGenericClient() throws Exception {
        GenericClient client = transporter.connect(new Location("grass://127.0.0" +
                ".1:9999/test?codec=directCodec&serializer=textSerializer"));

        GenericFuture future = client.invoke("hello grass");
        GenericResponse response = future.get();
        System.out.println(response.getData());
        Thread.sleep(1000);
    }

    class Handler implements GenericHandler {
        @Override
        public void handleConnected(GenericContext context) throws Exception {

        }

        @Override
        public void handleRequest(GenericContext context, GenericRequest request) throws Exception {
            System.out.println(request.getData());
            GenericResponse response = new GenericResponse();
            response.setId(request.getId());
            response.setData("yeah");
            context.write(response);
        }

        @Override
        public void handleDisconnected(GenericContext context) throws Exception {

        }

        @Override
        public void handleException(GenericContext context, Throwable cause) throws Exception {

        }
    }
}