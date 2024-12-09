package dev.sussolino.juicyregions.system;

import com.github.retrooper.packetevents.util.Vector3d;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;

@Getter
@Setter
@AllArgsConstructor
public class JuicyLocation {

    private World world;
    private Vector3d position;
    private float yaw, pitch;
    private boolean  onGround;

    public Location toLocation() {
        return new Location(world, position.x, position.y, position.z, yaw, pitch);
    }
}
