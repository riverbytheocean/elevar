package io.github.riverbytheocean.plugins.elevar;

import io.github.riverbytheocean.plugins.elevar.game.GamePeriod;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.List;

public class ElevarPlayerUtils {

    /**
     * A simple function to return the players still in the game.
     * @return a List of players still left in the game.
     */
    public static List<Player> playersAlive() {

        return List.copyOf(Bukkit.getOnlinePlayers().stream().filter(player -> !player.getGameMode().isInvulnerable()).toList());

    }

    /**
     * Handles cancelling the player's environmental damage according to configuration values for each period.
     * @param event The EntityDamageEvent event used for handling damage.
     * @param period The period to check for before executing.
     */
    public static void handlePlayerEnvironmentalDamage(EntityDamageEvent event, GamePeriod period) {
        if (!Elevar.getPeriod().equals(period)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (
                event.getCause().equals(EntityDamageEvent.DamageCause.WORLD_BORDER) || event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)
                        || event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)
        ) {
            return;
        }

        if (Elevar.config().getBoolean(period.name().toLowerCase() + "-period.damage.environmental")) {
            return;
        }

        event.setCancelled(true);
    }

    /**
     * Handles cancelling the damage other entities do to the player according to configuration values for each period.
     * @param event The EntityDamageByEntityEvent event used for handling damage.
     * @param period The period to check for before executing.
     */
    public static void handleEntityOnPlayerDamage(EntityDamageByEntityEvent event, GamePeriod period) {

        if (!Elevar.getPeriod().equals(period)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (event.getDamager() instanceof Player) {
            return;
        }

        if (Elevar.config().getBoolean(period.name().toLowerCase() + "-period.damage.mobs")) {
            return;
        }

        event.setCancelled(true);

    }

    /**
     * Handles cancelling the damage other players do to the player according to configuration values for each period.
     * @param event The EntityDamageByEntityEvent event used for handling damage.
     * @param period The period to check for before executing.
     */
    public static void handlePlayerOnPlayerDamage(EntityDamageByEntityEvent event, GamePeriod period) {

        if (!Elevar.getPeriod().equals(period)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        if (Elevar.config().getBoolean(period.name().toLowerCase() + "-period.damage.pvp")) {
            return;
        }

        event.setCancelled(true);

    }

    /**
     * Checks if there is only 1 player remaining and gives the win to that player.
     */
    public static void checkAndDeliverWin() {

        if (playersAlive().size() > 1) {
            return;
        }

        Elevar.setGameRunning(false);

    }

    public static void eliminatePlayer(Player player) {

        if (player.getGameMode().isInvulnerable()) {
            return;
        }

        if (Elevar.config().getBoolean("ghosts")) {
            player.setGameMode(GameMode.SPECTATOR);
        } else {
            Elevar.getBannedPlayers().add(player.getUniqueId());
            player.ban("You've been eliminated! Come back when the round has ended!", (Duration) null, null);
            player.kick(Component.text("You've been eliminated! Come back when the round has ended!").color(NamedTextColor.RED));
        }

        for (Player p : Bukkit.getOnlinePlayers()) {

            p.sendMessage(
                    Component.text(player.getName() + " has been eliminated! (" + ElevarPlayerUtils.playersAlive().size() + "/" + Bukkit.getOnlinePlayers().size() + ")")
                            .color(NamedTextColor.RED)
            );

        }

        ElevarPlayerUtils.checkAndDeliverWin();

    }

}
