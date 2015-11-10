/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
package org.cgfork.grass.common.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumberUtilsTest {

    @Test
    public void testLong() throws Exception {
        byte[] longArray = new byte[8];

        NumberUtils.putLong(longArray, 0, 0);
        assertEquals(0, NumberUtils.getLong(longArray, 0));

        NumberUtils.putLong(longArray, 0, -1);
        assertEquals(-1, NumberUtils.getLong(longArray, 0));

        NumberUtils.putLong(longArray, 0, 1);
        assertEquals(1, NumberUtils.getLong(longArray, 0));
    }

    @Test
    public void testShort() throws Exception {
        byte[] shortArray = new byte[8];

        NumberUtils.putShort(shortArray, 0, (short)0);
        assertEquals((short)0, NumberUtils.getShort(shortArray, 0));
        NumberUtils.putShort(shortArray, 0, (short)-1);
        assertEquals((short)-1, NumberUtils.getShort(shortArray, 0));
        NumberUtils.putShort(shortArray, 0, (short)1);
        assertEquals((short)1, NumberUtils.getShort(shortArray, 0));
    }
}