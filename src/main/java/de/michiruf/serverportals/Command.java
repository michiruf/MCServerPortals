package de.michiruf.serverportals;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.michiruf.serverportals.config.PortalRegistrationData;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * TODO Enhance this class with positions and dimensions?
 * <p>
 * Commands to test with:
 * /serverportals list
 * /serverportals register 1 minecraft:bone_block minecraft:ender_eye black "velocity version"
 * /serverportals unregister 1
 *
 * @author Michael Ruf
 * @since 2022-12-03
 */
public class Command {

    private static final Map<String, Supplier<?>> commands = new HashMap<>();

    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher,
                                        CommandRegistryAccess registry,
                                        CommandManager.RegistrationEnvironment environment) {
        LiteralCommandNode<ServerCommandSource> rootNode = CommandManager
                .literal("serverportals")
                .requires(cmd -> cmd.hasPermissionLevel(4))
                .executes(context -> {
                    context.getSource().sendMessage(Text.literal("Usage: /serverportals list"));
                    context.getSource().sendMessage(Text.literal("Usage: /serverportals register name frameBlock lightWith color command"));
                    context.getSource().sendMessage(Text.literal("Usage: /serverportals unregister name"));
                    return 1;
                })
                .build();
        registerListCommand(rootNode);
        registerRegisterCommand(rootNode, registry);
        registerUnregisterCommand(rootNode, registry);
        dispatcher.getRoot().addChild(rootNode);
    }

    private static void registerListCommand(LiteralCommandNode<ServerCommandSource> node) {
        node.addChild(CommandManager
                .literal("list")
                .executes(Command::executeListCommand)
                .build());
    }

    private static int executeListCommand(CommandContext<ServerCommandSource> context) {
        var listString = ServerPortalsMod.CONFIG.portals() != null
                ? ServerPortalsMod.CONFIG.portals().stream()
                .map(PortalRegistrationData::toString)
                .collect(Collectors.joining("\n"))
                : "";
        if (listString.length() == 0)
            listString = "None";
        context.getSource().sendMessage(Text.literal(listString));
        return 0;
    }

    private static void registerRegisterCommand(LiteralCommandNode<ServerCommandSource> node, CommandRegistryAccess registry) {
        node.addChild(CommandManager
                .literal("register")
                .executes(context -> {
                    context.getSource().sendMessage(Text.literal("Invalid usage. See /serverportals"));
                    return 1;
                })
                .then(CommandManager.argument("index", StringArgumentType.word())
                        .then(CommandManager.argument("frameBlock", BlockStateArgumentType.blockState(registry))
                                .then(CommandManager.argument("lightWith", ItemStackArgumentType.itemStack(registry))
                                        .then(CommandManager.argument("color", ColorArgumentType.color())
                                                .then(CommandManager.argument("command", StringArgumentType.string())
                                                        .executes(Command::executeRegisterCommand)
                                                )
                                        )
                                )
                        )
                )
                .build());
    }

    private static int executeRegisterCommand(CommandContext<ServerCommandSource> context) {
        var index = StringArgumentType.getString(context, "index");
        var frameBlock = BlockStateArgumentType.getBlockState(context, "frameBlock");
        var lightWith = ItemStackArgumentType.getItemStackArgument(context, "lightWith");
        var color = ColorArgumentType.getColor(context, "color");
        var command = StringArgumentType.getString(context, "command");

        // Create the list if null
        if (ServerPortalsMod.CONFIG.portals() == null) {
            ServerPortalsMod.LOGGER.error("IS NULL");
            ServerPortalsMod.CONFIG.portals(new ArrayList<>());
        }

        // Check if the list contains this index and abort if so
        var registeredIndexes = ServerPortalsMod.CONFIG.portals().stream()
                .map(PortalRegistrationData::index)
                .toList();
        if (registeredIndexes.contains(index)) {
            ServerPortalsMod.LOGGER.error("Portal with index {} is already registered. Unregister it first", index);
            context.getSource().sendMessage(Text.literal("Portal with index " + index + " is already registered. Unregister it first"));
            return 2;
        }

        var portal = new PortalRegistrationData(
                index,
                Registries.BLOCK.getId(frameBlock.getBlockState().getBlock()).toString(),
                Registries.ITEM.getId(lightWith.getItem()).toString(),
                color.getColorValue() != null ? color.getColorValue() : 0,
                command);
        // Save must be trigger manually here, because it is a list and cannot observe changes (even when calling
        // getter and setter manually
        ServerPortalsMod.CONFIG.portals().add(portal);
        ServerPortalsMod.CONFIG.save();

        ServerPortalsMod.LOGGER.error("Registered portal {}", portal);
        context.getSource().sendMessage(Text.literal("Registered portal " + portal));

        printRestartInfo(context);
        return 0;
    }

    private static void registerUnregisterCommand(LiteralCommandNode<ServerCommandSource> node, CommandRegistryAccess registry) {
        node.addChild(CommandManager
                .literal("unregister")
                .executes(context -> {
                    context.getSource().sendMessage(Text.literal("Invalid usage. See /serverportals"));
                    return 1;
                })
                .then(CommandManager.argument("index", StringArgumentType.word())
                        .executes(Command::executeUnregisterCommand)
                )
                .build());
    }

    private static int executeUnregisterCommand(CommandContext<ServerCommandSource> context) {
        var index = StringArgumentType.getString(context, "index");

        // Cancel if not list exists
        if (ServerPortalsMod.CONFIG.portals() == null) {
            ServerPortalsMod.LOGGER.error("Portal list does not exist");
            context.getSource().sendMessage(Text.literal("Portal list does not exist"));
            return 2;
        }

        // Remove the entry (iterative, if some duplicate entries exist because errors occurred at some point)
        boolean contained;
        do {
            contained = false;
            for (int i = 0; i < ServerPortalsMod.CONFIG.portals().size(); i++) {
                var portal = ServerPortalsMod.CONFIG.portals().get(i);
                if (index.equals(portal.index())) {
                    contained = ServerPortalsMod.CONFIG.portals().remove(portal);
                    ServerPortalsMod.LOGGER.error("Unregistered portal {}", portal);
                    context.getSource().sendMessage(Text.literal("Unregistered portal " + portal));
                }
            }
        } while (contained);

        // Save must be trigger manually here, because it is a list and cannot observe changes (even when calling
        // getter and setter manually
        ServerPortalsMod.CONFIG.save();

        printRestartInfo(context);
        return 0;
    }

    private static void printRestartInfo(CommandContext<ServerCommandSource> context) {
        ServerPortalsMod.LOGGER.info("For this configuration to take effect, restart the server");
        context.getSource().sendMessage(Text.literal("For this configuration to take effect, restart the server"));
    }
}
