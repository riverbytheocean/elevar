package io.github.riverbytheocean.plugins.elevar.commands;

import com.mojang.brigadier.Command;
import io.github.riverbytheocean.plugins.elevar.Elevar;
import io.github.riverbytheocean.plugins.elevar.ElevarServerUtils;
import io.github.riverbytheocean.plugins.elevar.game.GamePeriodStates;
import io.github.riverbytheocean.plugins.elevar.game.GamePeriod;
import io.github.riverbytheocean.plugins.elevar.guis.settings.ElevarSettingsGUI;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ElevarCommand {

    @SuppressWarnings("UnstableApiUsage")
    public ElevarCommand() {

        LifecycleEventManager<Plugin> manager = Elevar.getInstance().getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(
                    Commands.literal("elevar")
                            .executes(ctx -> {
                                ctx.getSource().getSender().sendMessage(
                                        Component.text("elevar! version " + Elevar.getInstance().getPluginMeta().getVersion() + "! a simple plugin for block rising events :3")
                                                .color(TextColor.color(0xE9A1FF))
                                );
                                return Command.SINGLE_SUCCESS;
                            })
                            .then(Commands.literal("start")
                                    .requires(ctx -> hasPermissionOrOp(ctx.getSender(), "elevar.admin.start"))
                                    .executes(ctx -> {
                                        if (ElevarServerUtils.hasGameStarted()) {
                                            ctx.getSource().getSender().sendMessage(
                                                    Component.text("There is a game already in progress!")
                                                            .color(NamedTextColor.RED)
                                            );
                                            return Command.SINGLE_SUCCESS;
                                        }

                                        GamePeriodStates.starter();
                                        return Command.SINGLE_SUCCESS;
                                    })
                            )
                            .then(Commands.literal("skipperiod")
                                    .requires(ctx -> hasPermissionOrOp(ctx.getSender(), "elevar.admin.skipperiod"))
                                    .executes(ctx -> {

                                        if (!ElevarServerUtils.hasGameStarted()) {
                                            ctx.getSource().getSender().sendMessage(
                                                    Component.text("Start the game instead!")
                                                            .color(NamedTextColor.RED)
                                            );
                                            return Command.SINGLE_SUCCESS;
                                        }

                                        ctx.getSource().getSender().sendMessage(
                                                Component.text("Skipped ahead a period!")
                                                        .color(TextColor.color(0xE9A1FF))
                                        );
                                        Elevar.getPeriodRunnable().cancel();
                                        return Command.SINGLE_SUCCESS;
                                    })
                            )
                            .then(Commands.literal("settings")
                                    .requires(ctx -> hasPermissionOrOp(ctx.getSender(), "elevar.admin.settings"))
                                    .executes(ctx -> {
                                        if (!(ctx.getSource().getExecutor() instanceof Player player)) {
                                            ctx.getSource().getSender().sendMessage(
                                                    Component.text("you'll need to be in-game to use this command!")
                                                            .color(NamedTextColor.RED)
                                            );
                                            return Command.SINGLE_SUCCESS;
                                        }

                                        ElevarSettingsGUI.sendSettingsGUI(player);

                                        return Command.SINGLE_SUCCESS;
                                    })
                            )
                            .then(Commands.literal("reset")
                                    .requires(ctx -> hasPermissionOrOp(ctx.getSender(), "elevar.admin.reset"))
                                    .executes(ctx -> {

                                        Elevar.setPeriod(GamePeriod.RESET);

                                        for (Player player : Bukkit.getOnlinePlayers()) {

                                            player.teleport(Objects.requireNonNull(Bukkit.getWorld("elevar_lobby")).getSpawnLocation());

                                        }

                                        ctx.getSource().getSender().sendMessage(
                                                Component.text("Deleting and recreating the world...")
                                                        .color(TextColor.color(0xE9A1FF))
                                        );

                                        ElevarServerUtils.deleteWorld("elevar_world");
                                        ElevarServerUtils.createWorld("elevar_world");

                                        ElevarServerUtils.deleteWorld("elevar_nether");
                                        ElevarServerUtils.createWorld("elevar_nether", new Random().nextLong(), World.Environment.NETHER, WorldType.NORMAL);

                                        ctx.getSource().getSender().sendMessage(
                                                Component.text("New world created! Players will be teleported shortly!")
                                                        .color(TextColor.color(0xE9A1FF))
                                        );

                                        Elevar.setPeriod(GamePeriod.LOBBY);

                                        return Command.SINGLE_SUCCESS;
                                    })
                            )
                            .build(),
                    "The main command for the elevar plugin!",
                    List.of("ele", "ev")
            );
        });

    }

    private boolean hasPermissionOrOp(@NotNull CommandSender sender, @NotNull String permission) {
        return sender.hasPermission(permission) || sender.isOp() || sender instanceof ConsoleCommandSender;
    }

}
