package de.michiruf.serverportals.versioned;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

/**
 * @author Michael Ruf
 * @since 2023-12-13
 */
public class VersionedMessageSender {

    public static void send(CommandContext<ServerCommandSource> context, String text) {
        context.getSource().sendMessage(Text.literal(text));
    }
}
