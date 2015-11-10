package org.cgfork.grass.common.cache.single;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class SingletonCacheTest {

    private SingletonCache cache;

    @Before
    public void setUp() throws Exception {
        cache = new SingletonCache();
    }

    @After
    public void tearDown() throws Exception {
        cache.clear();
    }

    @Test
    public void testGet() throws Exception {
        Case ca = new Case();
        cache.register(ca);

        assertEquals(ca, cache.get(ca.getClass()));
        assertEquals(ca, cache.get("case"));
    }

    @Test
    public void testRemove() throws Exception {
        Case ca = new Case();
        cache.register(ca);
        cache.remove(ca);
        assertEquals(null, cache.get("case"));
        assertEquals(null, cache.get(ca.getClass()));
    }

    @Test
    public void testRemoveByClass() throws Exception {
        Case ca = new Case();
        cache.register(ca);
        cache.remove(ca.getClass());
        assertEquals(null, cache.get("case"));
        assertEquals(null, cache.get(ca.getClass()));
    }

    @Test
    public void testRemoveByName() throws Exception {
        Case ca = new Case();
        cache.register(ca);
        cache.remove("case");
        assertEquals(null, cache.get("case"));
        assertEquals(null, cache.get(ca.getClass()));
    }

    @Singleton("case")
    static class Case {}
}