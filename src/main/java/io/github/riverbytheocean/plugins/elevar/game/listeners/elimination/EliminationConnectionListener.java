package io.github.riverbytheocean.plugins.elevar.game.listeners.elimination;

import io.github.riverbytheocean.plugins.elevar.Elevar;
import io.github.riverbytheocean.plugins.elevar.ElevarPlayerUtils;
import io.github.riverbytheocean.plugins.elevar.game.GamePeriod;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EliminationConnectionListener implements Listener {

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {

        if (!(Elevar.getPeriod().equals(GamePeriod.ELIMINATION) || Elevar.getPeriod().equals(GamePeriod.OVERTIME))) {
            return;
        }

        Elevar.getCurrentRoundPlayers().remove(event.getPlayer().getUniqueId());
        ElevarPlayerUtils.eliminatePlayer(event.getPlayer());

    }

    @EventHandler
    public void handlePlayerJoin(PlayerQuitEvent event) {

        if (!(Elevar.getPeriod().equals(GamePeriod.ELIMINATION) || Elevar.getPeriod().equals(GamePeriod.OVERTIME))) {
            return;
        }

        Elevar.getCurrentRoundPlayers().remove(event.getPlayer().getUniqueId());
        ElevarPlayerUtils.eliminatePlayer(event.getPlayer());

    }

}
