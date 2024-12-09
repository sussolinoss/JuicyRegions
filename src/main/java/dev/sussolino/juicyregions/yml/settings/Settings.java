package dev.sussolino.juicyregions.yml.settings;

import dev.sussolino.juicyapi.color.ColorUtils;
import dev.sussolino.juicyregions.Juicy;
import org.bukkit.configuration.file.FileConfiguration;

public enum Settings {

    PREFIX,

    INVALID__NUMBER,
    NOT__EXIST,
    NO__POSITIONS,

    ALREADY__EXIST,

    COMMAND_CREATE,
    COMMAND_DELETE,
    COMMAND_REDEFINE,

    COMMAND_FLAG_SET,
    COMMAND_FLAG_NULL,
    COMMAND_EXEMPT,
    COMMAND_POSITION;

    private final String path;
    private final FileConfiguration config;

    Settings() {
        this.path = name().toLowerCase()
                .replace("__", "-")
                .replace('_', '.');
        this.config = Juicy.SETTINGS.getConfig();
    }

    public String getAsString() {
        final String prefix = this.config.getString("prefix");
        final String text = this.config.getString(this.path);

        assert prefix != null;
        assert text != null;

        return ColorUtils.color(text.replace("%prefix%", prefix));
    }

    public String getAsString(String replace, String replacer) {
        return ColorUtils.color(getAsString().replace(replace, replacer));
    }
}
