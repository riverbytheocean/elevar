package io.github.riverbytheocean.plugins.elevar.listeners;

import io.github.riverbytheocean.plugins.elevar.Elevar;
import io.github.riverbytheocean.plugins.elevar.ElevarServerUtils;
import io.github.riverbytheocean.plugins.elevar.game.GamePeriod;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldCreationListener implements Listener {

    @EventHandler
    public void handleWorldCreation(WorldLoadEvent event) {

        if (!event.getWorld().getName().equals("elevar_world")) {
            return;
        }

        Elevar.setBlockHeight(event.getWorld().getMinHeight());

        event.getWorld().getWorldBorder().setSize(10);
        Bukkit.getServerTickManager().setFrozen(true);

        Block highestBlock = event.getWorld().getHighestBlockAt(0,0);

        for (int x = -5; x < 5; x++) {
            for (int z = -5; z < 5; z++) {

                highestBlock.getRelative(x, 0, z).setBlockData(Material.BEDROCK.createBlockData());

            }
        }

        for (int x = -5; x < 5; x++) {
            for (int z = -5; z < 5; z++) {
                for (int y = 1; y < 5; y++) {

                    highestBlock.getRelative(x, y, z).setBlockData(Material.AIR.createBlockData());

                }
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {

            player.teleport(highestBlock.getLocation().add(0, 1, 0));

        }

    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {

        if (Elevar.getPeriod().equals(GamePeriod.RESET)) {

            event.getPlayer().teleport(ElevarServerUtils.createWorld("elevar_lobby").getHighestBlockAt(0, 0).getLocation().add(0, 1, 0));

        }

    }

    // prevent damage in the lobby
    @EventHandler
    public void handlePlayerDamage(EntityDamageEvent event) {

        if (!Elevar.getPeriod().equals(GamePeriod.RESET)) {
            return;
        }

        event.setCancelled(true);

    }

}
