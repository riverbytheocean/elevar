package io.github.riverbytheocean.plugins.elevar.game.listeners.starter;

import io.github.riverbytheocean.plugins.elevar.Elevar;
import io.github.riverbytheocean.plugins.elevar.ElevarServerUtils;
import io.github.riverbytheocean.plugins.elevar.game.GamePeriod;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class StarterConnectionListener implements Listener {

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {

        if (!Elevar.getPeriod().equals(GamePeriod.STARTER)) {
            return;
        }

        if (Elevar.getCurrentRoundPlayers().contains(event.getPlayer().getUniqueId())) {
            return;
        }

        Location loc = ElevarServerUtils.createWorld("elevar_world").getHighestBlockAt(0, 0).getLocation().add(0, 1, 0);

        event.getPlayer().teleport(loc);
        event.getPlayer().setRespawnLocation(loc);

        Elevar.getCurrentRoundPlayers().add(event.getPlayer().getUniqueId());

    }

    @EventHandler
    public void handlePlayerLeave(PlayerJoinEvent event) {

        if (!Elevar.getPeriod().equals(GamePeriod.STARTER)) {
            return;
        }

        if (Elevar.getCurrentRoundPlayers().contains(event.getPlayer().getUniqueId())) {
            return;
        }

        Elevar.getCurrentRoundPlayers().add(event.getPlayer().getUniqueId());

    }

}
