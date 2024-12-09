package dev.sussolino.juicyregions.player.processor.model;

import dev.sussolino.juicyapi.packet.Packet;
import dev.sussolino.juicyregions.player.RegionPlayer;

public abstract class PlayerProcessor {

    protected final RegionPlayer player;

    public PlayerProcessor(RegionPlayer player) {
        this.player = player;
    }

    public void process(Packet packet) {

    }
}
