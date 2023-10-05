package de.michiruf.serverportals;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.util.SHOULDTP;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.util.math.random.RandomSeed.getSeed;

/**
 * @author Michael Ruf
 * @since 2022-11-29
 */
public class ServerPortalsMod implements DedicatedServerModInitializer, ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("ServerPortals");
    public static final de.michiruf.serverportals.config.Config CONFIG = de.michiruf.serverportals.config.Config.createAndLoad();

    @Override
    public void onInitializeServer() {
        registerPluginHooks();
    }

    @Override
    public void onInitializeClient() {
        // Another variant:
        //switch (FabricLauncherBase.getLauncher().getEnvironmentType()) {

        // Register everything delayed until a world got loaded
        //ServerWorldEvents.LOAD.register((server, world) -> registerPluginHooks());
    }

    private void registerPluginHooks() {
        CommandRegistrationCallback.EVENT.register(Command::registerCommands);
        if (CONFIG.portals() != null)
            registerPortals();
        LOGGER.info("ServerPortals registered {} portals", CONFIG.portals() == null ? 0 : CONFIG.portals().size());
    }

    private void registerPortals() {
        for (var portal : CONFIG.portals()) {
            CustomPortalBuilder.beginPortal()
                    .frameBlock(portal.frameBlock())
                    .lightWithItem(portal.lightWithItem())
                    .destDimID(new Identifier("the_nether"))
                    .tintColor(portal.color())
                    .registerBeforeTPEvent(entity -> {
                        executeCommand(entity, parseCommand(portal.command(), entity));
                        return SHOULDTP.CANCEL_TP;
                    })
                    .registerPortal();
        }
    }

    private String parseCommand(String command, Entity entity) {
        command = command.replace("%PLAYERNAME%", entity.getName().getString());
        command = command.replace("%PLAYERUUID%", entity.getUuid().toString());
        command = command.replace("%PLAYERPOS%", entity.getPos().toString());
        command = command.replace("%PLAYERSERVER%", "Server IP: " + entity.getServer().getServerIp().toString() + ", Server MOTD: " + entity.getServer().getServerMotd() + ", Server Version: " + entity.getServer().getVersion() + ", Player Count: " + entity.getServer().getCurrentPlayerCount() + "/" + entity.getServer().getMaxPlayerCount());
        return command;
    }

    private void executeCommand(Entity entity, String command) {
        var server = entity.getServer();
        if (server == null)
            throw new RuntimeException("Server may not be null");

        var commandManager = server.getCommandManager();
        commandManager.executeWithPrefix(entity.getCommandSource(), command);
    }
}
