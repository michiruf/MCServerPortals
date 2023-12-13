package de.michiruf.serverportals.versioned;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

/**
 * @author Michael Ruf
 * @since 2023-01-18
 */
public class VersionedRegistry {

    public static Registry<Block> block() {
        return Registry.BLOCK;
    }

    public static Registry<Item> item() {
        return Registry.ITEM;
    }
}