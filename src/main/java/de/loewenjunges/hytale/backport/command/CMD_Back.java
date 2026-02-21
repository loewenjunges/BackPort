package de.loewenjunges.hytale.backport.command;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import de.loewenjunges.hytale.backport.BackPort;
import de.loewenjunges.hytale.backport.api.ApiPlayer;
import de.loewenjunges.hytale.backport.api.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CMD_Back extends AbstractPlayerCommand {

    public CMD_Back() {
        super("back", "Teleports you to your last position", false);
    }

    @Override
    protected void execute(@NotNull CommandContext commandContext, @NotNull Store<EntityStore> store, @NotNull Ref<EntityStore> ref, @NotNull PlayerRef playerRef, @NotNull World world) {
        final ApiPlayer player = ApiPlayer.of(store, ref);
        if (player == null) return;
        if (!BackPort.config().get().backContains(player.getUniqueId())) {
            player.sendMessageError("You do not yet have a last position to which you can teleport back!");
            return;
        }

        if (!player.getLocation().getWorldName().equalsIgnoreCase("default")) {
            player.sendMessageError("You can only do this on the default world!");
            return;
        }

        final Location location = BackPort.config().get().back(player.getUniqueId());
        if (location == null) return;

        player.teleport(location);
        player.sendMessage("You are now at your last position.");
    }

    @Override
    public @Nullable String getPermission() {
        return "backPort.back";
    }
}
