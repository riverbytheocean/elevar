package io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.world;

import io.github.riverbytheocean.plugins.elevar.Elevar;
import io.github.riverbytheocean.plugins.elevar.guis.settings.ElevarSettingsGUI;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DelayBetweenRisesItem extends AbstractItem implements Listener {

    public static List<UUID> players = new ArrayList<>();

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.OMINOUS_BOTTLE)
                .setDisplayName(
                        new AdventureComponentWrapper(
                                Component.text("Delay Between Rises")
                                        .decoration(TextDecoration.ITALIC, false)
                                        .decoration(TextDecoration.BOLD, true)
                        ))
                .addLoreLines(
                        new AdventureComponentWrapper(
                                Component.text("Set the delay between block rises, in ticks!")
                                        .decoration(TextDecoration.ITALIC, false)
                                        .color(TextColor.color(0xE9A1FF))
                        ),
                        new AdventureComponentWrapper(
                                Component.text("This is currently set to " + Elevar.config().getInt("delay-between-rises") + " ticks!")
                                        .decoration(TextDecoration.ITALIC, false)
                                        .color(TextColor.color(0xA3A1ff))
                        )
                );
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {

        if (players.contains(player.getUniqueId())) {
            return;
        }

        ElevarSettingsGUI.interacted.add(player.getUniqueId());

        player.closeInventory();

        player.sendMessage(Component.text("Type an integer for this in chat. (Type cancel to cancel this!)").color(TextColor.color(0xE9A1FF)));
        players.add(player.getUniqueId());

    }

    @EventHandler
    public void handleChat(AsyncChatEvent event) {

        if (!DelayBetweenRisesItem.players.contains(event.getPlayer().getUniqueId())) {
            return;
        }

        PlainTextComponentSerializer plainSerializer = PlainTextComponentSerializer.plainText();
        String finalMessage = plainSerializer.serialize(event.message()).toUpperCase();

        if (finalMessage.equalsIgnoreCase("cancel")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Component.text("Cancelled!").color(TextColor.color(0xE9A1FF)));

            DelayBetweenRisesItem.players.remove(event.getPlayer().getUniqueId());
            ElevarSettingsGUI.interacted.remove(event.getPlayer().getUniqueId());
            return;
        }

        int value;

        // place code here
        try {
            value = Integer.parseInt(finalMessage);
        } catch (NumberFormatException e) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Component.text("...That isn't an integer.")
                    .color(NamedTextColor.RED));

            return;
        }

        event.setCancelled(true);
        event.getPlayer().sendMessage(Component.text("Set!").color(TextColor.color(0xE9A1FF)));

        DelayBetweenRisesItem.players.remove(event.getPlayer().getUniqueId());
        ElevarSettingsGUI.interacted.remove(event.getPlayer().getUniqueId());

        Elevar.config().set("delay-between-rises", value);
        Elevar.getInstance().saveConfig();
        Elevar.getInstance().reloadConfig();

        new BukkitRunnable() {
            @Override
            public void run() {
                ElevarSettingsGUI.sendWorldSettingsGUI(event.getPlayer());
            }
        }.runTask(Elevar.getInstance());

    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {

        DelayBetweenRisesItem.players.remove(event.getPlayer().getUniqueId());

    }

}
