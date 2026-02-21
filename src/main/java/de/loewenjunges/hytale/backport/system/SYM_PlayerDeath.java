package de.loewenjunges.hytale.backport.system;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import de.loewenjunges.hytale.backport.BackPort;
import de.loewenjunges.hytale.backport.api.ApiPlayer;
import de.loewenjunges.hytale.backport.api.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SYM_PlayerDeath extends DeathSystems.OnDeathSystem {
    @Override
    public void onComponentAdded(@NotNull Ref<EntityStore> ref, @NotNull DeathComponent deathComponent, @NotNull Store<EntityStore> store, @NotNull CommandBuffer<EntityStore> commandBuffer) {
        final ApiPlayer player = ApiPlayer.of(store, ref);
        if (player == null) return;

        final Location location = player.getLocation();
        if (!player.getLocation().getWorldName().equalsIgnoreCase("default")) return;

        BackPort.config().get().dBack(player.getUniqueId(), location);
        player.sendMessage("You died in the world " + location.getWorldName() + " by " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + ".");
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(Player.getComponentType());
    }
}
