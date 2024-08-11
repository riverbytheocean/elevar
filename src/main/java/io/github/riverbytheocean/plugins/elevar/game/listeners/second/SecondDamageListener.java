package io.github.riverbytheocean.plugins.elevar.game.listeners.second;

import io.github.riverbytheocean.plugins.elevar.ElevarPlayerUtils;
import io.github.riverbytheocean.plugins.elevar.game.GamePeriod;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class SecondDamageListener implements Listener {

    @EventHandler
    public void handlePlayerOnPlayerDamage(EntityDamageByEntityEvent event) {

        ElevarPlayerUtils.handlePlayerOnPlayerDamage(event, GamePeriod.SECOND);

    }

    @EventHandler
    public void handleEntityOnPlayerDamage(EntityDamageByEntityEvent event) {

        ElevarPlayerUtils.handleEntityOnPlayerDamage(event, GamePeriod.SECOND);

    }

    @EventHandler
    public void handleEnvironmentalPlayerDamage(EntityDamageEvent event) {

        ElevarPlayerUtils.handlePlayerEnvironmentalDamage(event, GamePeriod.SECOND);

    }

}
