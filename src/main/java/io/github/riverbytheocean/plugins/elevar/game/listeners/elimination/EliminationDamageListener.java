package io.github.riverbytheocean.plugins.elevar.game.listeners.elimination;

import io.github.riverbytheocean.plugins.elevar.Elevar;
import io.github.riverbytheocean.plugins.elevar.ElevarPlayerUtils;
import io.github.riverbytheocean.plugins.elevar.ElevarServerUtils;
import io.github.riverbytheocean.plugins.elevar.game.GamePeriod;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EliminationDamageListener implements Listener {

    @EventHandler
    public void handleDeath(PlayerDeathEvent event) {

        if (!(Elevar.getPeriod().equals(GamePeriod.ELIMINATION) || Elevar.getPeriod().equals(GamePeriod.OVERTIME))) {
            return;
        }

        Player player = event.getPlayer();

        event.setKeepInventory(true);
        event.setKeepLevel(true);
        event.deathMessage(Component.empty());

        ElevarPlayerUtils.eliminatePlayer(player);

    }

    @EventHandler
    public void handleRespawn(PlayerRespawnEvent event) {

        event.setRespawnLocation(ElevarServerUtils.createWorld("elevar_world").getHighestBlockAt(0, 0).getLocation().add(0, 1, 0));

    }

}
