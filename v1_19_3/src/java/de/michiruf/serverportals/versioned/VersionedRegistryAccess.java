package de.michiruf.serverportals.versioned;

import net.minecraft.block.Block;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;

/**
 * @author Michael Ruf
 * @since 2023-01-18
 */
public class VersionedRegistryAccess {

    public static DefaultedRegistry<Block> blocks() {
        return Registries.BLOCK;
    }
}
