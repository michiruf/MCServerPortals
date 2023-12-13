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
        var source = context.getSource();
        var serverPlayerEntity = source.getPlayer();
        if (serverPlayerEntity != null) {
            serverPlayerEntity.sendMessage(Text.literal(text));
        } else {
            // MRU: I am not entirely sure, if sendFeedback or sendError should be the option to go here
            source.sendFeedback(Text.literal(text), false);
        }
    }
}
