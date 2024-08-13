package io.github.riverbytheocean.plugins.elevar.game.listeners.lobby;

import io.github.riverbytheocean.plugins.elevar.ElevarServerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class LobbyDamageListener implements Listener {

    @EventHandler
    public void handleDamage(EntityDamageEvent event) {

        if (ElevarServerUtils.hasGameStarted()) {
            return;
        }

        event.setCancelled(true);

    }

}
