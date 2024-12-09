package dev.sussolino.juicyregions.api;

import dev.sussolino.juicyregions.Juicy;
import dev.sussolino.juicyregions.player.RegionPlayer;

public class RegionAPI {

    public static RegionPlayer getPlayer(final int entityId) {
        return Juicy.PLAYER_MANAGER.get(entityId);
    }
}
