package io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.world;

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

public class NetherToggleItem extends AbstractItem {
    @Override
    public ItemProvider getItemProvider() {

        Material material = Elevar.getInstance().getConfig().getBoolean("nether") ? Material.NETHERRACK : Material.OBSIDIAN;

        return new ItemBuilder(material)
                .setDisplayName(
                        new AdventureComponentWrapper(
                                Component.text("Nether")
                                        .decoration(TextDecoration.ITALIC, false)
                                        .decoration(TextDecoration.BOLD, true)
                        ))
                .addLoreLines(
                        new AdventureComponentWrapper(
                                Component.text("Sets whether the nether is enabled or not!")
                                        .decoration(TextDecoration.ITALIC, false)
                                        .color(TextColor.color(0xE9A1FF))
                        ),
                        new AdventureComponentWrapper(
                                Component.text("This is currently set to " + Elevar.config().getBoolean("nether") + "!")
                                        .decoration(TextDecoration.ITALIC, false)
                                        .color(TextColor.color(0xA3A1ff))
                        )
                );
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {

        Elevar.config().set("nether", !Elevar.getInstance().getConfig().getBoolean("nether"));
        Elevar.getInstance().saveConfig();
        Elevar.getInstance().reloadConfig();

        ElevarSettingsGUI.sendWorldSettingsGUI(player);

    }
}
