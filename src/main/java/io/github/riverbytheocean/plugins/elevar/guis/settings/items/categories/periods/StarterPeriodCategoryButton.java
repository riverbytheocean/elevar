package io.github.riverbytheocean.plugins.elevar.guis.settings.items.categories.periods;

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

public class StarterPeriodCategoryButton extends AbstractItem {

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.IRON_PICKAXE)
                .setDisplayName(
                        new AdventureComponentWrapper(
                                Component.text("Starter Period Settings")
                                        .decoration(TextDecoration.ITALIC, false)
                                        .decoration(TextDecoration.BOLD, true)
                        ))
                .addLoreLines(
                        new AdventureComponentWrapper(
                                Component.text("Settings related to the starter period!")
                                        .decoration(TextDecoration.ITALIC, false)
                                        .color(TextColor.color(0xE9A1FF))
                        )
                );
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {

        ElevarSettingsGUI.sendStarterPeriodSettingsGUI(player);

    }

}