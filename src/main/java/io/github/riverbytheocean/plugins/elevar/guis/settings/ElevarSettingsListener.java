package io.github.riverbytheocean.plugins.elevar.guis.settings;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ElevarSettingsListener implements Listener {

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {

        ElevarSettingsGUI.interacted.remove(event.getPlayer().getUniqueId());

    }

}
