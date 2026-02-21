package de.loewenjunges.hytale.backport.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import de.loewenjunges.hytale.backport.BackPort;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Location {
    private String worldName;
    private double x, y, z;
    private float pitch, yaw, roll;

    protected Location(final @NotNull World world, final @NotNull Vector3d position, final @NotNull Vector3f rotation) {
        this.worldName = world.getName();

        this.x = position.x;
        this.y = position.y;
        this.z = position.z;

        this.pitch = rotation.getPitch();
        this.yaw = rotation.getYaw();
        this.roll = rotation.getRoll();
    }

    protected Location() {
    }

    public @Nullable World getWorld() {
        return Universe.get().getWorld(worldName);
    }

    public @NotNull String getWorldName() {
        return this.worldName;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public double getBlockX() {
        return (int) Math.floor(getX());
    }

    public double getBlockY() {
        return (int) Math.floor(getY());
    }

    public double getBlockZ() {
        return (int) Math.floor(getZ());
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getRoll() {
        return this.roll;
    }

    public Transform getTransform() {
        return new Transform(new Vector3d(getX(), getY(), getZ()), new Vector3f(getPitch(), getYaw(), getRoll()));
    }

    @Override
    public String toString() {
        return "{\"world\":\"" + this.worldName
                + "\", \"x\":" + getX() + ", \"y\":" + getY() + ", \"z\":" + getZ()
                + ", \"pitch\":" + getPitch() + ", \"yaw\": " + getYaw() + ", \"roll\":" + getRoll() + "}";
    }

    public static @Nullable Location fromString(final @NotNull String json) {
        try {
            final JsonNode node = BackPort.mapper().readTree(json);
            final World world = Universe.get().getWorld(node.get("world").textValue());
            if (world == null) return null;

            final Vector3d position = new Vector3d(node.get("x").doubleValue(),
                    node.get("y").doubleValue(),
                    node.get("z").doubleValue());

            final Vector3f rotation = new Vector3f(node.get("pitch").floatValue(),
                    node.get("yaw").floatValue(),
                    node.get("roll").floatValue());

            return new Location(world, position, rotation);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static final BuilderCodec<Location> CODEC =
            BuilderCodec.<Location>builder(Location.class, Location::new)
                    .append(new KeyedCodec<>("WorldName", Codec.STRING),
                            (location, value) -> location.worldName = value,
                            location -> location.worldName).add()
                    .append(new KeyedCodec<>("X", Codec.DOUBLE),
                            (location, value) -> location.x = value,
                            location -> location.x).add()
                    .append(new KeyedCodec<>("Y", Codec.DOUBLE),
                            (location, value) -> location.y = value,
                            location -> location.y).add()
                    .append(new KeyedCodec<>("Z", Codec.DOUBLE),
                            (location, value) -> location.z = value,
                            location -> location.z).add()
                    .append(new KeyedCodec<>("Pitch", Codec.FLOAT),
                            (location, value) -> location.pitch = value,
                            location -> location.pitch).add()
                    .append(new KeyedCodec<>("Yaw", Codec.FLOAT),
                            (location, value) -> location.yaw = value,
                            location -> location.yaw).add()
                    .append(new KeyedCodec<>("Roll", Codec.FLOAT),
                            (location, value) -> location.roll = value,
                            location -> location.roll).add()
                    .build();
}
