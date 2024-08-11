package io.github.riverbytheocean.plugins.elevar.guis.settings;

import io.github.riverbytheocean.plugins.elevar.guis.settings.items.BackButtonItem;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.categories.periods.EliminationPeriodCategoryButton;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.categories.periods.SecondPeriodCategoryButton;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.categories.periods.StarterPeriodCategoryButton;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.categories.GamePeriodCategoryButton;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.categories.PlayerCategoryButton;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.categories.WorldCategoryButton;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.gameperiod.elimination.EliminationBorderWidthItem;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.gameperiod.elimination.EliminationTimeLengthItem;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.gameperiod.second.*;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.gameperiod.starter.*;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.player.GhostsSpeakToggleItem;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.player.GhostsToggleItem;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.player.SafeMarginItem;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.world.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ElevarSettingsGUI {

    public static List<UUID> interacted = new ArrayList<>();

    public static void sendSettingsGUI(Player player) {

        if (interacted.contains(player.getUniqueId())) {
            return;
        }

        Gui gui = Gui.normal()
                .setStructure(
                        ". . . . . . . . .",
                        ". o . . x . . c .",
                        ". . . . . . . . ."
                )
                .setBackground(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""))
                .addIngredient('x', new PlayerCategoryButton())
                .addIngredient('o', new WorldCategoryButton())
                .addIngredient('c', new GamePeriodCategoryButton())
                .build();

        Window.single()
                .setGui(gui)
                .setTitle(new AdventureComponentWrapper(Component.text("elevar settings!").color(TextColor.color(0xE9A1FF))))
                .open(player);

    }

    public static void sendWorldSettingsGUI(Player player) {

        Gui gui = Gui.normal()
                .setStructure(
                        ". . . . . . . . .",
                        ". o p . n . x c .",
                        ". . . . l . . . ."
                )
                .setBackground(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""))
                .addIngredient('n', new NetherToggleItem())
                .addIngredient('o', new RiseHeightLimitItem())
                .addIngredient('p', new BuildHeightLimitItem())
                .addIngredient('x', new SetBlockItem())
                .addIngredient('c', new DelayBetweenRisesItem())
                .addIngredient('l', new BackButtonItem() {
                    @Override
                    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                        sendSettingsGUI(player);
                    }
                })
                .build();

        Window.single()
                .setGui(gui)
                .setTitle(new AdventureComponentWrapper(Component.text("elevar settings: world").color(TextColor.color(0xE9A1FF))))
                .open(player);

    }

    public static void sendPlayerSettingsGUI(Player player) {

        Gui gui = Gui.normal()
                .setStructure(
                        ". . . . . . . . .",
                        ". . g . s . m . .",
                        ". . . . l . . . ."
                )
                .setBackground(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""))
                .addIngredient('g', new GhostsToggleItem())
                .addIngredient('s', new GhostsSpeakToggleItem())
                .addIngredient('m', new SafeMarginItem())
                .addIngredient('l', new BackButtonItem() {
                    @Override
                    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                        sendSettingsGUI(player);
                    }
                })
                .build();

        Window.single()
                .setGui(gui)
                .setTitle(new AdventureComponentWrapper(Component.text("elevar settings: players").color(TextColor.color(0xE9A1FF))))
                .open(player);

    }

    public static void sendGamePeriodSettingsGUI(Player player) {

        Gui gui = Gui.normal()
                .setStructure(
                        ". . . . . . . . .",
                        ". x . . c . . v .",
                        ". . . . l . . . ."
                )
                .setBackground(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""))
                .addIngredient('x', new StarterPeriodCategoryButton())
                .addIngredient('c', new SecondPeriodCategoryButton())
                .addIngredient('v', new EliminationPeriodCategoryButton())
                .addIngredient('l', new BackButtonItem() {
                    @Override
                    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                        sendSettingsGUI(player);
                    }
                })
                .build();

        Window.single()
                .setGui(gui)
                .setTitle(new AdventureComponentWrapper(Component.text("elevar settings: game periods").color(TextColor.color(0xE9A1FF))))
                .open(player);

    }

    public static void sendStarterPeriodSettingsGUI(Player player) {

        Gui gui = Gui.normal()
                .setStructure(
                        ". . . . . . . . .",
                        ". . c t p m e . .",
                        ". . . . l . . . ."
                )
                .setBackground(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""))
                .addIngredient('c', new StarterBorderWidthItem())
                .addIngredient('t', new StarterTimeLengthItem())
                .addIngredient('p', new StarterPVPDamageToggleItem())
                .addIngredient('m', new StarterMobDamageToggleItem())
                .addIngredient('e', new StarterEnvironmentDamageToggleItem())
                .addIngredient('l', new BackButtonItem() {
                    @Override
                    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                        sendGamePeriodSettingsGUI(player);
                    }
                })
                .build();

        Window.single()
                .setGui(gui)
                .setTitle(new AdventureComponentWrapper(Component.text("elevar settings: starter period").color(TextColor.color(0xE9A1FF))))
                .open(player);

    }

    public static void sendSecondPeriodSettingsGUI(Player player) {

        Gui gui = Gui.normal()
                .setStructure(
                        ". . . . . . . . .",
                        ". . c t p m e . .",
                        ". . . . l . . . ."
                )
                .setBackground(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""))
                .addIngredient('c', new SecondBorderWidthItem())
                .addIngredient('t', new SecondTimeLengthItem())
                .addIngredient('p', new SecondPVPDamageToggleItem())
                .addIngredient('m', new SecondMobDamageToggleItem())
                .addIngredient('e', new SecondEnvironmentDamageToggleItem())
                .addIngredient('l', new BackButtonItem() {
                    @Override
                    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                        sendGamePeriodSettingsGUI(player);
                    }
                })
                .build();

        Window.single()
                .setGui(gui)
                .setTitle(new AdventureComponentWrapper(Component.text("elevar settings: second period").color(TextColor.color(0xE9A1FF))))
                .open(player);

    }

    public static void sendEliminationPeriodSettingsGUI(Player player) {

        Gui gui = Gui.normal()
                .setStructure(
                        ". . . . . . . . .",
                        ". . . c . t . . .",
                        ". . . . l . . . ."
                )
                .setBackground(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""))
                .addIngredient('c', new EliminationBorderWidthItem())
                .addIngredient('t', new EliminationTimeLengthItem())
                .addIngredient('l', new BackButtonItem() {
                    @Override
                    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                        sendGamePeriodSettingsGUI(player);
                    }
                })
                .build();

        Window.single()
                .setGui(gui)
                .setTitle(new AdventureComponentWrapper(Component.text("elevar settings: elimination period").color(TextColor.color(0xE9A1FF))))
                .open(player);

    }

}
