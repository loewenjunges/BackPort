package de.loewenjunges.hytale.backport.api;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.UUID;

public class ApiPlayer {
    private final Store<EntityStore> store;
    private final Ref<EntityStore> ref;
    private final Player player;
    private final UUID uniqueId;
    private final Location location;

    private ApiPlayer(final @NotNull Store<EntityStore> store, final @NotNull Ref<EntityStore> ref,
                      final @NotNull Player player, final @NotNull UUID uniqueId, final @NotNull Location location) {
        this.store = store;
        this.ref = ref;
        this.player = player;
        this.uniqueId = uniqueId;
        this.location = location;
    }

    public static @Nullable ApiPlayer of(final @NotNull Store<EntityStore> store, final @NotNull Ref<EntityStore> ref) {
        final Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) return null;

        final PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null) return null;

        final UUIDComponent uuidComponent = store.getComponent(ref, UUIDComponent.getComponentType());
        if (uuidComponent == null) return null;

        final TransformComponent transformComponent = store.getComponent(ref, TransformComponent.getComponentType());
        if (transformComponent == null) return null;
        if (player.getWorld() == null) return null;

        final Location location = new Location(player.getWorld(), transformComponent.getTransform().getPosition(), playerRef.getHeadRotation());
        return new ApiPlayer(store, ref, player, uuidComponent.getUuid(), location);
    }

    public @NotNull Location getLocation() {
        return location;
    }

    public @NotNull UUID getUniqueId() {
        return this.uniqueId;
    }

    public @NotNull ApiPlayer sendMessage(final String message) {
        this.player.sendMessage(Message.raw(message));
        return this;
    }

    public @NotNull ApiPlayer sendMessageError(final String message) {
        this.player.sendMessage(Message.raw(message).color(Color.ORANGE));
        return this;
    }

    public @NotNull ApiPlayer teleport(final @NotNull Location location) {
        Teleport teleport = Teleport.createForPlayer(location.getWorld(), location.getTransform());
        store.addComponent(ref, Teleport.getComponentType(), teleport);
        return this;
    }
}
