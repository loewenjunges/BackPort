package de.loewenjunges.hytale.backport.system;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefChangeSystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import de.loewenjunges.hytale.backport.BackPort;
import de.loewenjunges.hytale.backport.api.ApiPlayer;
import de.loewenjunges.hytale.backport.api.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SYM_Teleport extends RefChangeSystem<EntityStore, Teleport> {
    @Override
    public @NotNull ComponentType<EntityStore, Teleport> componentType() {
        return Teleport.getComponentType();
    }

    @Override
    public void onComponentAdded(@NotNull Ref<EntityStore> ref, @NotNull Teleport teleport, @NotNull Store<EntityStore> store, @NotNull CommandBuffer<EntityStore> commandBuffer) {
        final ApiPlayer player = ApiPlayer.of(store, ref);
        if (player == null) return;

        final Location location = player.getLocation();
        if (!player.getLocation().getWorldName().equalsIgnoreCase("default")) return;

        BackPort.config().get().back(player.getUniqueId(), location);
    }

    @Override
    public void onComponentSet(@NotNull Ref<EntityStore> ref, @Nullable Teleport teleport, @NotNull Teleport t1, @NotNull Store<EntityStore> store, @NotNull CommandBuffer<EntityStore> commandBuffer) {
    }

    @Override
    public void onComponentRemoved(@NotNull Ref<EntityStore> ref, @NotNull Teleport teleport, @NotNull Store<EntityStore> store, @NotNull CommandBuffer<EntityStore> commandBuffer) {
    }

    @Override
    public @Nullable Query<EntityStore> getQuery() {
        return Query.and(Player.getComponentType(), Teleport.getComponentType());
    }
}
