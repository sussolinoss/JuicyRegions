package dev.sussolino.juicyregions.config;

import dev.sussolino.juicyregions.system.utils.ColorUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Objects;

public enum ConfigUtils {

    LICENSE("license"),
    PREFIX("prefix"),
    ERROR("error.general"),
    PLAYER_OFFLINE("error.player-offline"),
    INVALID_VALUE("error.invalid-value"),

    REGION_ENABLED("region.enabled"),

    SPAWN_ENABLED("spawn.enabled"),
    SPAWN_JOIN_ENABLED("spawn.join.enabled"),

    JOIN_TITLE_ENABLED("join.send-title.enabled"),
    JOIN_SEND_TITLE_TITLE("join.send-title.title"),
    JOIN_SEND_TITLE_SUB("join.send-tile.sub-title"),

    JOIN_SEND_MESSAGE_ENABLED("join.send-message.enabled"),
    JOIN_SEND_MESSAGE_MESSAGE("join.send-message.message"),

    JOIN_SEND_GLOBAL_MESSAGE_ENABLED("join.send-global-message.enabled"),
    JOIN_SEND_GLOBAL_MESSAGE_MESSAGE("join.send-global-message.message"),

    /*
    SPAWN_LOCATION_X("spawn.location.x"),
    SPAWN_LOCATION_Y("spawn.location.y"),
    SPAWN_LOCATION_Z("spawn.location.z"),
    SPAWN_LOCATION_PITCH("spawn.location.pitch"),
    SPAWN_LOCATION_YAW("spawn.location.yaw"),
    SPAWN_LOCATION_WORLD("spawn.location.world"),
     */

    SPAWN_MESSAGES_ERROR("spawn.messages.error"),
    SPAWN_MESSAGES_SET("spawn.messages.set"),
    SPAWN_MESSAGES_TELEPORT("spawn.messages.teleport"),
    SPAWN_MESSAGES_TELEPORT_ALL("spawn.messages.teleport-all"),


    INVITES_DISCORD("invites.discord"),
    INVITES_TWITCH("invites.twitch"),
    INVITES_YOUTUBE("invites.youtube");




    /**
     *
     *   SETTERS
     *
     */

    public static void setString(String path, String newThing) {
        config.set(path, newThing);
        Config.save();
    }
    public static void setDouble(String path, double newThing) {
        config.set(path, newThing);
        Config.save();
    }

    /**
     *
     *   GETTERS
     *
     */

    public String getString() {
        String value = config.getString(path);
        if (value == null) {
            return "";
        }

        String prefix = config.getString(PREFIX.path);
        if (prefix == null) {
            prefix = "";
        }

        value = value.replace("{prefix}", prefix);

        return ColorUtils.color(value);
    }

    public String getErrorString() {
        String value = config.getString(path);

        if (value == null) {
            return "";
        }

        String prefix = config.getString(ERROR.path);
        if (prefix == null) {
            prefix = "";
        }

        value = value.replace("{prefix}", prefix);

        return ColorUtils.color(value);
    }


    public int getInt() {
        return config.getInt(path);
    }
    public double getDouble() {
        return config.getDouble(path);
    }
    public boolean getBoolean() {
        return config.getBoolean(path);
    }
    public short getShort() {
        return Short.parseShort(Objects.requireNonNull(config.getString(path)));
    }
    public List<String> getStringList() {
        return config.getStringList(path);
    }

    /**
     *
     *    CONSTRUCTOR
     *
     */

    public final String path;
    private static final FileConfiguration config = Config.getConfig();

    ConfigUtils(String path) {
        this.path = path;
    }
}
