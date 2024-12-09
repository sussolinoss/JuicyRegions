package dev.sussolino.juicyregions.player.manager;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.sussolino.juicyregions.player.RegionPlayer;
import dev.sussolino.juicyregions.region.Region;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class RegionPlayerManager {

    private final Map<Integer, RegionPlayer> players = new ConcurrentHashMap<>();

    public RegionPlayer add(final User user, final Player player) {
       return this.players.putIfAbsent(user.getEntityId(), new RegionPlayer(user, player));
    }
    public void remove(final int entityId) {
        this.players.remove(entityId);
    }

    public List<RegionPlayer> getPlayers() {
        return new ArrayList<>(this.players.values());
    }
    public List<RegionPlayer> getPlayersFromRegion(final Region region) {
       return this.getPlayersFromRegion(region.getName());
    }
    public List<RegionPlayer> getPlayersFromRegion(final String regionName) {
        return this.getPlayers()
                .parallelStream()
                .filter(player -> player.getRegion().getName().equals(regionName))
                .toList();
    }

    public RegionPlayer get(final int entityId) {
        return this.players
                .values()
                .stream().filter(p -> p.getEntityId() == entityId)
                .findFirst().orElse(null);
    }
}
