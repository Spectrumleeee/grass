package org.cgfork.grass.common.addon.support;

import org.cgfork.grass.common.addon.AddonLoader;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class AddonLoadersTest {

    private AddonLoaders loaders = null;

    @Before
    public void setUp() {
        loaders = new AddonLoaders();
    }

    @Test
    public void testGetAddonLoader() throws Exception {
        AddonLoader<TestAddon> loader = loaders.getAddonLoader(TestAddon.class);
        assertNotNull(loader);

        TestAddon addon = loader.getAddon(TestAddonImpl.class);
        assertNotNull(addon);

        addon = loader.getAddon("testAddonImpl");
        assertNotNull(addon);
    }

    @Test
    public void testCreateAddonLoader() throws Exception {
        AddonLoader<TestAddon> loader = loaders.createAddonLoader(TestAddon.class);
        assertNotNull(loader);

        TestAddon addon = loader.getAddon(TestAddonImpl.class);
        assertNotNull(addon);

        addon = loader.getAddon("testAddonImpl");
        assertNotNull(addon);
    }
}