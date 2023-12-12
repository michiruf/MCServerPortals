package de.michiruf.serverportals.config;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import de.michiruf.serverportals.versioned.VersionedRegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Identifier;
import net.minecraft.world.level.block.Block;

/**
 * @author Michael Ruf
 * @since 2022-12-19
 */
public final class PortalRegistrationData {

    private String index;
    private String frameBlockId;
    private String lightWithItemId;
    private int color;
    private String command;

    @SuppressWarnings("unused")
    // Suppress, because this will be used for serialization
    private PortalRegistrationData() {
    }

    public PortalRegistrationData(
            String index,
            String frameBlockId,
            String lightWithItemId,
            int color,
            String command) {
        this.index = index;
        this.frameBlockId = frameBlockId;
        this.lightWithItemId = lightWithItemId;
        this.color = color;
        this.command = command;
    }

    public Block frameBlock() {
        return VersionedRegistryAccess.block().get(new ResourceLocation(frameBlockId));
        return VersionedRegistryAccess.block().get(new Identifier(frameBlockId));
    }

    public Item lightWithItem() {
        return VersionedRegistryAccess.item().get(new Identifier(lightWithItemId));
    }

    public String index() {
        return index;
    }

    public int color() {
        return color;
    }

    public String command() {
        return command;
    }

    @Override
    public String toString() {
        return "[" +
                "index=" + index + ", " +
                "frameBlockId=" + frameBlockId + ", " +
                "lightWithItemId=" + lightWithItemId + ", " +
                "color=" + color + ", " +
                "command=" + command + ']';
    }
}
