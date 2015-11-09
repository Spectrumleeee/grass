package org.cgfork.grass.rpc.serialize;

import org.cgfork.grass.common.serialize.DataInput;
import org.cgfork.grass.common.serialize.DataOutput;

import java.io.IOException;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface ObjectOutput extends DataOutput {
    void writeObject(Object v) throws IOException;
}
