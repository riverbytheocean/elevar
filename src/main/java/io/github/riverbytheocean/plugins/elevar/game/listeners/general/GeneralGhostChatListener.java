package io.github.riverbytheocean.plugins.elevar.game.listeners.general;

import io.github.riverbytheocean.plugins.elevar.Elevar;
import io.github.riverbytheocean.plugins.elevar.ElevarServerUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralGhostChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void handleGhostChat(AsyncChatEvent event) {

        if (!event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
            return;
        }

        if (!ElevarServerUtils.hasGameStarted()) {
            return;
        }

        if (!Elevar.config().getBoolean("ghosts-can-speak")) {
            return;
        }

        Player p = event.getPlayer();

        event.setCancelled(true);

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (!player.getGameMode().isInvulnerable()) {
                continue;
            }

            player.sendMessage(Component.text("[SPECTATOR] <" + p.getName() + "> ").append(event.message()));

        }

    }

    @EventHandler(priority = EventPriority.LOW)
    public void handleGhostMessaging(PlayerCommandPreprocessEvent event) {

        if (!event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
            return;
        }

        if (!Elevar.isGameRunning()) {
            return;
        }

        if (!Elevar.config().getBoolean("ghosts-can-speak")) {
            return;
        }

        Pattern pattern = Pattern.compile("(^/msg.*)|(^/message.*)|(^/w.*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(event.getMessage());
        boolean matchFound = matcher.find();

        if (!matchFound) {
            return;
        }

        event.getPlayer().sendMessage(
                Component.text("Spectators cannot send message commands at the moment, sorry! :3").color(NamedTextColor.RED)
        );
        event.setCancelled(true);

    }

}
