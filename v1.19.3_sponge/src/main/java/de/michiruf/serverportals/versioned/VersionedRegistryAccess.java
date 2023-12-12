package de.michiruf.serverportals.versioned;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * @author Michael Ruf
 * @since 2023-01-18
 */
public class VersionedRegistryAccess {

    public static Registry<Block> block() {
        return BuiltInRegistries.BLOCK;
    }

    public static Registry<Item> item() {
        return BuiltInRegistries.ITEM;
    }
}
