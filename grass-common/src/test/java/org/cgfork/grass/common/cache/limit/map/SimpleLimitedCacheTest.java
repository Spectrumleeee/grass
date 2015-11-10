package org.cgfork.grass.common.cache.limit.map;

import org.cgfork.grass.common.cache.limit.RejectedException;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class SimpleLimitedCacheTest {

    @Test(expected = RejectedException.class)
    public void testCacheRejected() throws Exception {
        SimpleLimitedCache<Object, Object> cache = new SimpleLimitedCache<>(new HashMap<>(), 1);
        cache.put(new Object(), new Object());
        assertEquals(1, cache.size());
        assertEquals(1, cache.limitedSize());
        cache.put(new Object(), new Object());
    }
}