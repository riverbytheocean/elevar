package io.github.riverbytheocean.plugins.elevar.game.listeners.general;

import io.github.riverbytheocean.plugins.elevar.Elevar;
import io.github.riverbytheocean.plugins.elevar.ElevarServerUtils;
import io.github.riverbytheocean.plugins.elevar.game.GamePeriod;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GeneralConnectionListener implements Listener {

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {

        if (!Elevar.getPeriod().equals(GamePeriod.LOBBY)) {
            return;
        }

        Location loc = ElevarServerUtils.createWorld("elevar_world").getHighestBlockAt(0, 0).getLocation().add(0, 1, 0);

        event.getPlayer().teleport(loc);
        event.getPlayer().setRespawnLocation(loc);

        event.getPlayer().setGameMode(GameMode.ADVENTURE);

    }

}
