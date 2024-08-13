package io.github.riverbytheocean.plugins.elevar.game;

import io.github.riverbytheocean.plugins.elevar.Elevar;
import io.github.riverbytheocean.plugins.elevar.ElevarPlayerUtils;
import io.github.riverbytheocean.plugins.elevar.ElevarServerUtils;
import io.papermc.paper.ban.BanListType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GamePeriodStates {

    public static String millisecondsToReadableTime(long milliseconds) {

        final Duration duration = Duration.ofMillis(milliseconds);
        final long hours = duration.toHours();
        int minutes = duration.toMinutesPart();

        final double sec = duration.toSecondsPart();
        final double mil = duration.toMillisPart();

        long seconds = (long) Math.ceil(sec + mil / 1000);

        if (seconds >= 60) {
            minutes += 1;
            seconds -= 60;
        }

        final StringBuilder formattedDuration = new StringBuilder();

        if (hours > 0) {
            formattedDuration.append(hours + "h ");
        }

        if (minutes > 0 || hours > 0) {
            formattedDuration.append(minutes + "m ");
        }

        formattedDuration.append(seconds + "s");

        return formattedDuration.toString();

    }

    public static void starter() {

        Bukkit.getOnlinePlayers().forEach(player -> player.getInventory().clear());

        if (!ElevarServerUtils.hasGameStarted()) {
            announceWinner();
            return;
        }

        long endTime = (Elevar.config().getInt("starter-period.time-length") * 1000L) + System.currentTimeMillis();

        ElevarServerUtils.createWorld("elevar_world").getWorldBorder().setSize(Elevar.config().getDouble("starter-period.border-width"));

        for (Player player : Bukkit.getOnlinePlayers()) {

            Elevar.getCurrentRoundPlayers().add(player.getUniqueId());

            if (!player.isInvulnerable()) {
                player.setGameMode(GameMode.SURVIVAL);
            }

            player.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, 16));

            player.sendMessage(
                    Component.text("The game has begun! Gather all you can before the next period!")
                            .color(NamedTextColor.GREEN)
            );

        }

        Bukkit.getServerTickManager().setFrozen(false);

        Elevar.setPeriod(GamePeriod.STARTER);

        Elevar.setPeriodRunnable(new BukkitRunnable() {
            @Override
            public void run() {

                if (!ElevarServerUtils.hasGameStarted()) {
                    cancel();
                    return;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {

                    player.sendActionBar(Component.text(millisecondsToReadableTime(endTime - System.currentTimeMillis())).color(NamedTextColor.GREEN));

                    if (player.getLocation().getBlockY() < Elevar.getBlockHeight() - Elevar.config().getInt("safe-margin")) {

                        ElevarPlayerUtils.eliminatePlayer(player);

                    }

                }

                if (!(endTime > System.currentTimeMillis())) {
                    cancel();
                }

            }

            @Override
            public synchronized void cancel() throws IllegalStateException {
                super.cancel();

                second();
            }
        });

        Elevar.getPeriodRunnable().runTaskTimer(Elevar.getInstance(), 5, 5);

    }

    public static void second() {

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (!player.getWorld().equals(ElevarServerUtils.createWorld("elevar_nether"))) {
                continue;
            }

            if (Elevar.getNetherPortalLocations().containsKey(player.getUniqueId())) {

                player.teleport(Elevar.getNetherPortalLocations().get(player.getUniqueId()));

            } else {

                player.teleport(ElevarServerUtils.createWorld("elevar_world").getHighestBlockAt(0, 0).getLocation().add(0, 1, 0));

            }

        }

        if (!ElevarServerUtils.hasGameStarted()) {
            announceWinner();
            return;
        }

        long endTime = (Elevar.config().getInt("second-period.time-length") * 1000L) + System.currentTimeMillis();

        ElevarServerUtils.createWorld("elevar_world").getWorldBorder().setSize(
                Elevar.config().getDouble("second-period.border-width"),
                TimeUnit.SECONDS,
                Elevar.config().getInt("second-period.time-length")
        );

        for (Player player : Bukkit.getOnlinePlayers()) {

            player.sendMessage(
                    Component.text("It's the second period! The border will now begin to close in, don't get caught outside!")
                            .color(NamedTextColor.YELLOW)
            );

        }

        Elevar.setPeriod(GamePeriod.SECOND);

        Elevar.setPeriodRunnable(new BukkitRunnable() {
            @Override
            public void run() {

                if (!ElevarServerUtils.hasGameStarted()) {
                    cancel();
                    return;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {

                    player.sendActionBar(Component.text(millisecondsToReadableTime(endTime - System.currentTimeMillis())).color(NamedTextColor.YELLOW));

                    if (player.getLocation().getBlockY() < Elevar.getBlockHeight() - Elevar.config().getInt("safe-margin")) {

                        ElevarPlayerUtils.eliminatePlayer(player);

                    }

                }

                if (!(endTime > System.currentTimeMillis())) {
                    cancel();
                }

            }

            @Override
            public synchronized void cancel() throws IllegalStateException {
                super.cancel();

                elimination();
            }
        });

        Elevar.getPeriodRunnable().runTaskTimer(Elevar.getInstance(), 5, 5);

    }

    public static void elimination() {

        if (!ElevarServerUtils.hasGameStarted()) {
            announceWinner();
            return;
        }

        long endTime = (Elevar.config().getInt("elimination-period.time-length") * 1000L) + System.currentTimeMillis();

        ElevarServerUtils.createWorld("elevar_world").getWorldBorder().setSize(
                Elevar.config().getDouble("second-period.border-width")
        );
        ElevarServerUtils.createWorld("elevar_world").getWorldBorder().setSize(
                Elevar.config().getDouble("elimination-period.border-width"),
                TimeUnit.SECONDS,
                Elevar.config().getInt("elimination-period.time-length")
        );

        Elevar.setPeriod(GamePeriod.ELIMINATION);

        for (Player player : Bukkit.getOnlinePlayers()) {

            player.sendMessage(
                    Component.text("It's the elimination period! The border will close in even more, and you can actually be eliminated from this point forward! Good luck! :3")
                            .color(NamedTextColor.RED)
            );

        }

        Elevar.setPeriodRunnable(new BukkitRunnable() {
            @Override
            public void run() {

                if (!ElevarServerUtils.hasGameStarted()) {
                    cancel();
                    return;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {

                    player.sendActionBar(Component.text("Elimination! " + ElevarPlayerUtils.playersAlive().size() + " players remain!").color(NamedTextColor.RED));

                    if (player.getLocation().getBlockY() < Elevar.getBlockHeight() - Elevar.config().getInt("safe-margin")) {

                        ElevarPlayerUtils.eliminatePlayer(player);

                    }

                }

                if (!(endTime > System.currentTimeMillis())) {
                    cancel();
                }

            }

            @Override
            public synchronized void cancel() throws IllegalStateException {
                super.cancel();

                overtime();
            }
        });

        Elevar.getPeriodRunnable().runTaskTimer(Elevar.getInstance(), 5, 5);

        Elevar.setBlockRisingRunnable(new BukkitRunnable() {
            @Override
            public void run() {

                if (!ElevarServerUtils.hasGameStarted()) {
                    cancel();
                    return;
                }

                Material material = Material.matchMaterial(Elevar.config().getString("block"));

                World world = ElevarServerUtils.createWorld("elevar_world");

                int min_x = (int) Math.ceil(world.getWorldBorder().getCenter().getBlockX() - world.getWorldBorder().getSize() / 2);
                int max_x = (int) Math.ceil(world.getWorldBorder().getCenter().getBlockX() + world.getWorldBorder().getSize() / 2);

                int min_z = (int) Math.ceil(world.getWorldBorder().getCenter().getBlockZ() - world.getWorldBorder().getSize() / 2);
                int max_z = (int) Math.ceil(world.getWorldBorder().getCenter().getBlockZ() + world.getWorldBorder().getSize() / 2);

                for (int x = min_x; x < max_x; x++) {
                    for (int z = min_z; z < max_z; z++) {

                        world.getBlockAt(x, Elevar.getBlockHeight(), z).setType(material, false);

                    }
                }

                if (Elevar.getBlockHeight() < Elevar.config().getInt("rise-height-limit")) {
                    Elevar.setBlockHeight(Elevar.getBlockHeight() + 1);
                }

            }
        });

        Elevar.getBlockRisingRunnable().runTaskTimer(Elevar.getInstance(), 0, Elevar.config().getInt("delay-between-rises"));

    }

    public static void overtime() {

        if (!ElevarServerUtils.hasGameStarted()) {
            announceWinner();
            return;
        }

        ElevarServerUtils.createWorld("elevar_world").getWorldBorder().setSize(
                Elevar.config().getDouble("elimination-period.border-width")
        );

        Elevar.setPeriod(GamePeriod.OVERTIME);

        for (Player player : Bukkit.getOnlinePlayers()) {

            player.sendMessage(
                    Component.text("Overtime! The border has stopped moving, but only one can be left standing.")
                            .color(TextColor.color(0xE9A1FF))
            );

        }

        Elevar.setPeriodRunnable(new BukkitRunnable() {
            @Override
            public void run() {

                if (!ElevarServerUtils.hasGameStarted()) {
                    cancel();
                    return;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {

                    player.sendActionBar(Component.text("Overtime! " + ElevarPlayerUtils.playersAlive().size() + " players remain!").color(NamedTextColor.RED));

                    if (player.getLocation().getBlockY() < Elevar.getBlockHeight() - Elevar.config().getInt("safe-margin")) {

                        ElevarPlayerUtils.eliminatePlayer(player);

                    }

                }

                if (!Elevar.getPeriod().equals(GamePeriod.OVERTIME)) {
                    cancel();
                }

            }

            @Override
            public synchronized void cancel() throws IllegalStateException {
                Elevar.setPeriod(GamePeriod.LOBBY);
                announceWinner();
                Bukkit.getServerTickManager().setFrozen(true);
                Elevar.getBlockRisingRunnable().cancel();
                Elevar.setBlockHeight(ElevarServerUtils.createWorld("elevar_world").getMinHeight());
                Elevar.getCurrentRoundPlayers().clear();

                Bukkit.getOnlinePlayers().forEach(player -> player.getInventory().clear());

                for (UUID bannedPlayer : Elevar.getBannedPlayers()) {

                    Bukkit.getBanList(BanListType.PROFILE).pardon(Bukkit.getOfflinePlayer(bannedPlayer).getPlayerProfile());

                }

                Elevar.getBannedPlayers().clear();

                super.cancel();
            }
        });

        Elevar.getPeriodRunnable().runTaskTimer(Elevar.getInstance(), 5, 5);

    }

    public static void announceWinner() {

        for (Player player : Bukkit.getOnlinePlayers()) {

            player.showTitle(Title.title(
                    Component.text(ElevarPlayerUtils.playersAlive().getFirst().getName())
                            .color(TextColor.color(0xE9A1FF)),
                    Component.text("has won!")
                            .color(TextColor.color(0xE9A1FF))
            ));

        }

    }

}
