package io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.player;

import io.github.riverbytheocean.plugins.elevar.Elevar;
import io.github.riverbytheocean.plugins.elevar.guis.settings.ElevarSettingsGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class GhostsToggleItem extends AbstractItem {

    @Override
    public ItemProvider getItemProvider() {

        Material material = Elevar.getInstance().getConfig().getBoolean("ghosts") ? Material.WHITE_CONCRETE : Material.BLACK_CONCRETE;

        return new ItemBuilder(material)
                .setDisplayName(
                        new AdventureComponentWrapper(
                                Component.text("Ghosts")
                                        .decoration(TextDecoration.ITALIC, false)
                                        .decoration(TextDecoration.BOLD, true)
                        ))
                .addLoreLines(
                        new AdventureComponentWrapper(
                                Component.text("Sets whether players are able to spectate after the game ends!")
                                        .decoration(TextDecoration.ITALIC, false)
                                        .color(TextColor.color(0xE9A1FF))
                        ),
                        new AdventureComponentWrapper(
                                Component.text("(Players are temporarily banned until the game ends if otherwise.)")
                                        .decoration(TextDecoration.ITALIC, false)
                                        .color(TextColor.color(0xE9A1FF))
                        ),
                        new AdventureComponentWrapper(
                                Component.text("This is currently set to " + Elevar.config().getBoolean("ghosts") + "!")
                                        .decoration(TextDecoration.ITALIC, false)
                                        .color(TextColor.color(0xA3A1ff))
                        )
                );
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {

        Elevar.config().set("ghosts", !Elevar.getInstance().getConfig().getBoolean("ghosts"));
        Elevar.getInstance().saveConfig();
        Elevar.getInstance().reloadConfig();

        ElevarSettingsGUI.sendPlayerSettingsGUI(player);

    }

}