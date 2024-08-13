package io.github.riverbytheocean.plugins.elevar.game.listeners.general;

import io.github.riverbytheocean.plugins.elevar.Elevar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class GeneralHeightLimitListener implements Listener {

    @EventHandler
    public void handleBlockPlacement(BlockPlaceEvent event) {

        if (event.getPlayer().isInvulnerable()) {
            return;
        }

        if (!(event.getBlock().getLocation().getBlockY() > Elevar.config().getInt("build-height-limit"))) {
            return;
        }

        event.setCancelled(true);

    }

}
