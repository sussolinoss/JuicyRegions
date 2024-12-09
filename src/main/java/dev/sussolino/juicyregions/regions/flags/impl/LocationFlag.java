package dev.sussolino.juicyregions.regions.flags.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.sussolino.juicyregions.regions.flags.api.Flag;
import dev.sussolino.juicyregions.system.utils.Serializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LocationFlag extends Flag<Location> {

    public LocationFlag(String name,@NotNull Location defaultValue) {
        super(name, defaultValue);
    }

    public LocationFlag(String name) {
        super(name, Bukkit.getWorlds().get(0).getSpawnLocation());
    }

    public Location unmarshal(Object o) {
        Map<?, ?> args;
        try {
            args = Serializer.deserialize(o.toString(), Map.class);
        } catch (JsonProcessingException e) {
            return null;
        }
        World world = null;
        if (args.containsKey("world")) {
            world = Bukkit.getWorld((String) args.get("world"));
            if (world == null) {
                throw new IllegalArgumentException("unknown world");
            }
        }
        return new Location(world, NumberConversions.toDouble(args.get("x")), NumberConversions.toDouble(args.get("y")), NumberConversions.toDouble(args.get("z")), NumberConversions.toFloat(args.get("yaw")), NumberConversions.toFloat(args.get("pitch")));
    }

    public Object marshal(Location t) {
        return t == null ? null : Serializer.serialize(t.serialize());
    }
}
