package de.loewenjunges.hytale.backport.config;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.server.core.HytaleServer;
import de.loewenjunges.hytale.backport.BackPort;
import de.loewenjunges.hytale.backport.api.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BackPortConfig {
    private boolean Changed = false;

    public Map<String, Location> backs = new HashMap<>();
    public Map<String, Location> dBacks = new HashMap<>();

    public static final BuilderCodec<BackPortConfig> CONFIG =
            BuilderCodec.<BackPortConfig>builder(BackPortConfig.class, BackPortConfig::new)
                    .append(new KeyedCodec<Map<String, Location>>(
                                    "Back", new MapCodec<>(Location.CODEC, HashMap::new, false)
                            ),
                            (config, value) -> config.backs = (value == null ? new HashMap<>() : value),
                            config -> config.backs
                    ).add()
                    .append(new KeyedCodec<Map<String, Location>>(
                                    "DeathBack", new MapCodec<>(Location.CODEC, HashMap::new, false)
                            ),
                            (config, value) -> config.dBacks = (value == null ? new HashMap<>() : value),
                            config -> config.dBacks
                    ).add()
                    .build();


    public @NotNull BackPortConfig back(final @NotNull UUID playerUniqueId, final @NotNull Location location) {
        backs.put(playerUniqueId.toString(), location);
        setChanged(true);

        return this;
    }

    public @Nullable Location back(final @NotNull UUID playerUniqueId) {
        return backs.get(playerUniqueId.toString());
    }

    public boolean backContains(final @NotNull UUID playerUniqueId) {
        return backs.containsKey(playerUniqueId.toString());
    }

    public @NotNull BackPortConfig dBack(final @NotNull UUID playerUniqueId, final @NotNull Location location) {
        dBacks.put(playerUniqueId.toString(), location);
        setChanged(true);

        return this;
    }

    public @Nullable Location dBack(final @NotNull UUID playerUniqueId) {
        return dBacks.get(playerUniqueId.toString());
    }

    public boolean dBackContains(final @NotNull UUID playerUniqueId) {
        return dBacks.containsKey(playerUniqueId.toString());
    }

    public boolean isChanged() {
        return Changed;
    }

    private @NotNull BackPortConfig setChanged(boolean changed) {
        Changed = changed;
        return this;
    }

    public void saveTask () {
        HytaleServer.SCHEDULED_EXECUTOR.scheduleWithFixedDelay(() -> {
            if (!isChanged()) return;

            final long start = System.currentTimeMillis();
            try {
                BackPort.config().save();
                setChanged(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            BackPort.logger().atInfo().log("Postions saved in " + (System.currentTimeMillis() - start) + "ms!");
        }, 10, 10 , TimeUnit.SECONDS);
    }
}
