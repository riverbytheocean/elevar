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

public class BuildHeightLimitItem extends AbstractItem implements Listener {

    public static List<UUID> players = new ArrayList<>();

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.TUFF)
                .setDisplayName(
                        new AdventureComponentWrapper(
                                Component.text("Build Height Limit")
                                        .decoration(TextDecoration.ITALIC, false)
                                        .decoration(TextDecoration.BOLD, true)
                        ))
                .addLoreLines(
                        new AdventureComponentWrapper(
                                Component.text("The limit on how high blocks can be placed!")
                                        .decoration(TextDecoration.ITALIC, false)
                                        .color(TextColor.color(0xE9A1FF))
                        ),
                        new AdventureComponentWrapper(
                                Component.text("This is currently set to " + Elevar.config().getInt("build-height-limit") + " blocks!")
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

        if (!BuildHeightLimitItem.players.contains(event.getPlayer().getUniqueId())) {
            return;
        }

        PlainTextComponentSerializer plainSerializer = PlainTextComponentSerializer.plainText();
        String finalMessage = plainSerializer.serialize(event.message()).toUpperCase();

        if (finalMessage.equalsIgnoreCase("cancel")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Component.text("Cancelled!").color(TextColor.color(0xE9A1FF)));

            BuildHeightLimitItem.players.remove(event.getPlayer().getUniqueId());
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

        if (value > event.getPlayer().getWorld().getMaxHeight() - 1 || value < event.getPlayer().getWorld().getMinHeight()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Component.text("That value is out of this world!")
                    .color(NamedTextColor.RED));

            return;
        }

        event.setCancelled(true);
        event.getPlayer().sendMessage(Component.text("Set!").color(TextColor.color(0xE9A1FF)));

        BuildHeightLimitItem.players.remove(event.getPlayer().getUniqueId());
        ElevarSettingsGUI.interacted.remove(event.getPlayer().getUniqueId());

        Elevar.config().set("build-height-limit", value);
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

        BuildHeightLimitItem.players.remove(event.getPlayer().getUniqueId());

    }

}