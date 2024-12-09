package dev.sussolino.juicyregions.region.flags.api;

import dev.sussolino.juicyregions.Juicy;
import dev.sussolino.juicyregions.region.Region;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public abstract class Flag<T> {

    private final String name;
    @Getter(onMethod_ = {@NotNull})
    protected T defaultValue;

    public Flag(String name, @NotNull T defaultValue) {
        this.name = name.toLowerCase();
        this.defaultValue = defaultValue;
    }

    /**
     * Builds the path of the flag in the configuration.
     * @return The path of the flag as a string.
     */
    public String path() {
        return ".flags." + this.name;
    }

    /**
     * Retrieves the flag status for a region, returning the default value if not set.
     * @param region The region to get the flag from.
     * @return The flag value.
     */
    public T getFlagStatus(Region region) {
        String stringValue = Juicy.REGIONS.getConfig().getString(region.getName() + path());
        if (stringValue == null) return defaultValue;
        return getFromString(stringValue);
    }

    /**
     * Converts a string to a value of the generic type T.
     * @param string The flag value as a string.
     * @return The converted flag value.
     */
    public T getFromString(String string) {
        T value = unmarshal(string);
        if (value == null) {
            System.out.println("Invalid value, using default value");
            return defaultValue;
        }
        return value;
    }

    /**
     * Resets the flag status for the region to the default value.
     * @param region The region to reset the flag for.
     */
    public void resetFlagStatus(Region region) {
        setFlagStatus(region, defaultValue);
    }

    /**
     * Sets the flag from a string for the specified region.
     * @param region The region to set the flag for.
     * @param action The string representing the value to be set.
     */
    public void setFromString(Region region, String action) {
        setFlagStatus(region, getFromString(action));
    }

    /**
     * Sets the flag status for a region.
     * @param region The region to set the flag for.
     * @param value The flag value.
     */
    public void setFlagStatus(Region region, T value) {
        Object marshalledValue = marshal(value);
        Juicy.REGIONS.getConfig().set(region.getName() + path(),
                marshalledValue != null ? marshalledValue : marshal(defaultValue)
        );
        Juicy.REGIONS.reload();
    }

    /**
     * Abstract method to deserialize a value of type T from an object.
     * @param o The object to deserialize.
     * @return The deserialized value or null if deserialization fails.
     */
    @Nullable
    public abstract T unmarshal(Object o);

    /**
     * Abstract method to serialize a value of type T into an object.
     * @param t The value to serialize.
     * @return The serialized object.
     */
    public abstract Object marshal(T t);

    /**
     * Checks if a player is exempted from the application of a flag in a region.
     * @param region The region.
     * @param player The player.
     * @return True if the player is exempt, false otherwise.
     */
    public boolean isExempted(Region region, Player player) {
        boolean exempted = player.hasPermission("regions.%s.%s".formatted(region.getName(), name));
        return exempted || isFullExempted(player);
    }

    /**
     * Checks if a player is globally exempted for a specific flag.
     * @param player The player.
     * @return True if exempt, false otherwise.
     */
    public boolean isFullExempted(Player player) {
        return player.hasPermission("regions.*.%s".formatted(name));
    }
}
