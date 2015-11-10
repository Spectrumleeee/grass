package org.cgfork.grass.rpc.serialize;

import org.cgfork.grass.common.serialize.DataInput;

import java.io.IOException;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public interface ObjectInput extends DataInput {
    Object readObject() throws IOException;

    Object readObject(int length) throws IOException;
}
