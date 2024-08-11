package io.github.riverbytheocean.plugins.elevar.game.listeners.starter;

import io.github.riverbytheocean.plugins.elevar.ElevarPlayerUtils;
import io.github.riverbytheocean.plugins.elevar.game.GamePeriod;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class StarterDamageListener implements Listener {

    @EventHandler
    public void handlePlayerOnPlayerDamage(EntityDamageByEntityEvent event) {

        ElevarPlayerUtils.handlePlayerOnPlayerDamage(event, GamePeriod.STARTER);

    }

    @EventHandler
    public void handleEntityOnPlayerDamage(EntityDamageByEntityEvent event) {

        ElevarPlayerUtils.handleEntityOnPlayerDamage(event, GamePeriod.STARTER);

    }

    @EventHandler
    public void handleEnvironmentalPlayerDamage(EntityDamageEvent event) {

        ElevarPlayerUtils.handlePlayerEnvironmentalDamage(event, GamePeriod.STARTER);

    }

}
