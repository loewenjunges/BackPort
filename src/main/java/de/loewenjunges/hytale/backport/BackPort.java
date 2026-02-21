package de.loewenjunges.hytale.backport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import de.loewenjunges.hytale.backport.command.CMD_Back;
import de.loewenjunges.hytale.backport.command.CMD_DeathBack;
import de.loewenjunges.hytale.backport.config.BackPortConfig;
import de.loewenjunges.hytale.backport.system.SYM_PlayerDeath;
import de.loewenjunges.hytale.backport.system.SYM_Teleport;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.jetbrains.annotations.NotNull;

public class BackPort extends JavaPlugin {
    private final static ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private static BackPort instance;
    private static Config<BackPortConfig> config;

    public BackPort(@NonNullDecl JavaPluginInit init) {
        super(init);
        this.config = this.withConfig("Config", BackPortConfig.CONFIG);
    }

    @Override
    protected void setup() {
        super.setup();
        instance = this;

        getEntityStoreRegistry().registerSystem(new SYM_PlayerDeath());
        logger().atInfo().log("register player death system");

        getEntityStoreRegistry().registerSystem(new SYM_Teleport());

        getCommandRegistry().registerCommand(new CMD_Back());
        getCommandRegistry().registerCommand(new CMD_DeathBack());

        config.save();
        config().get().saveTask();
    }

    public static @NotNull BackPort instance() {
        return instance;
    }

    public static @NotNull HytaleLogger logger() {
        return instance().getLogger();
    }

    public static @NotNull ObjectMapper mapper() {
        return MAPPER;
    }

    public static Config<BackPortConfig> config() {
        return config;
    }

    @Override
    protected void start() {
        super.start();
    }

    @Override
    protected void shutdown() {
        super.shutdown();
        config().save();
    }
}
