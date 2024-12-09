package dev.sussolino.juicyregions;

import dev.sussolino.juicyapi.packet.Packet;

public interface IHandler {

    void packet(final Packet packet);
    void register();
}
