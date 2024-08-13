package io.github.riverbytheocean.plugins.elevar;

import io.github.riverbytheocean.plugins.elevar.commands.ElevarCommand;
import io.github.riverbytheocean.plugins.elevar.game.GamePeriod;
import io.github.riverbytheocean.plugins.elevar.game.listeners.elimination.EliminationConnectionListener;
import io.github.riverbytheocean.plugins.elevar.game.listeners.elimination.EliminationDamageListener;
import io.github.riverbytheocean.plugins.elevar.game.listeners.general.GeneralConnectionListener;
import io.github.riverbytheocean.plugins.elevar.game.listeners.general.GeneralGhostChatListener;
import io.github.riverbytheocean.plugins.elevar.game.listeners.general.GeneralHeightLimitListener;
import io.github.riverbytheocean.plugins.elevar.game.listeners.general.GeneralPortalListener;
import io.github.riverbytheocean.plugins.elevar.game.listeners.lobby.LobbyDamageListener;
import io.github.riverbytheocean.plugins.elevar.game.listeners.second.SecondConnectionListener;
import io.github.riverbytheocean.plugins.elevar.game.listeners.second.SecondDamageListener;
import io.github.riverbytheocean.plugins.elevar.game.listeners.starter.StarterConnectionListener;
import io.github.riverbytheocean.plugins.elevar.game.listeners.starter.StarterDamageListener;
import io.github.riverbytheocean.plugins.elevar.guis.settings.ElevarSettingsListener;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.gameperiod.elimination.EliminationBorderWidthItem;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.gameperiod.elimination.EliminationTimeLengthItem;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.gameperiod.second.SecondBorderWidthItem;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.gameperiod.second.SecondTimeLengthItem;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.gameperiod.starter.StarterBorderWidthItem;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.gameperiod.starter.StarterTimeLengthItem;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.player.SafeMarginItem;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.world.BuildHeightLimitItem;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.world.DelayBetweenRisesItem;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.world.RiseHeightLimitItem;
import io.github.riverbytheocean.plugins.elevar.guis.settings.items.options.world.SetBlockItem;
import io.github.riverbytheocean.plugins.elevar.listeners.WorldCreationListener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.invui.InvUI;

import java.io.IOException;
import java.util.*;

/*

TODO:

GUIs
- Sound cues for the GUIs

Work After I'm Finished With Pretty Much Everything Else
- Team System (i'll try both a draft system and admins picking teams)

 */

public final class Elevar extends JavaPlugin {

    @Getter
    @Setter
    private static GamePeriod period;

    @Getter
    private static Elevar instance;

    @Getter
    @Setter
    private static int blockHeight;

    @Getter
    @Setter
    private static BukkitRunnable periodRunnable;

    @Getter
    @Setter
    private static BukkitRunnable blockRisingRunnable;

    @Getter
    private static List<UUID> bannedPlayers;

    @Getter
    private static Map<UUID, Location> netherPortalLocations;

    @Getter
    private static List<UUID> currentRoundPlayers;

    @Override
    public void onLoad() {

        try {

            ElevarServerUtils.checkForServerProperties(true, true);
            getLogger().info("disabling spawn protection and flight kicks, if they aren't already disabled");

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

    }

    @Override
    public void onEnable() {

        saveDefaultConfig();
        getLogger().info("config loaded!");

        instance = this;

        // inv ui
        InvUI.getInstance().setPlugin(instance);
        getLogger().info("invui initialized!");

        // register events

        // gui related events
        getServer().getPluginManager().registerEvents(new ElevarSettingsListener(), instance);

        getServer().getPluginManager().registerEvents(new SetBlockItem(), instance);
        getServer().getPluginManager().registerEvents(new DelayBetweenRisesItem(), instance);
        getServer().getPluginManager().registerEvents(new RiseHeightLimitItem(), instance);
        getServer().getPluginManager().registerEvents(new BuildHeightLimitItem(), instance);

        getServer().getPluginManager().registerEvents(new SafeMarginItem(), instance);

        getServer().getPluginManager().registerEvents(new StarterBorderWidthItem(), instance);
        getServer().getPluginManager().registerEvents(new StarterTimeLengthItem(), instance);

        getServer().getPluginManager().registerEvents(new SecondBorderWidthItem(), instance);
        getServer().getPluginManager().registerEvents(new SecondTimeLengthItem(), instance);

        getServer().getPluginManager().registerEvents(new EliminationBorderWidthItem(), instance);
        getServer().getPluginManager().registerEvents(new EliminationTimeLengthItem(), instance);

        // minigame related events
        getServer().getPluginManager().registerEvents(new WorldCreationListener(), instance);

        getServer().getPluginManager().registerEvents(new GeneralHeightLimitListener(), instance);
        getServer().getPluginManager().registerEvents(new GeneralGhostChatListener(), instance);
        getServer().getPluginManager().registerEvents(new GeneralPortalListener(), instance);
        getServer().getPluginManager().registerEvents(new GeneralConnectionListener(), instance);

        getServer().getPluginManager().registerEvents(new LobbyDamageListener(), instance);

        getServer().getPluginManager().registerEvents(new StarterDamageListener(), instance);
        getServer().getPluginManager().registerEvents(new StarterConnectionListener(), instance);

        getServer().getPluginManager().registerEvents(new SecondDamageListener(), instance);
        getServer().getPluginManager().registerEvents(new SecondConnectionListener(), instance);

        getServer().getPluginManager().registerEvents(new EliminationDamageListener(), instance);
        getServer().getPluginManager().registerEvents(new EliminationConnectionListener(), instance);
        getLogger().info("events listened!");

        // register commands
        new ElevarCommand();
        getLogger().info("commands registered!");

        // create and load worlds if not already existing
        ElevarServerUtils.createLobbyWorld();
        ElevarServerUtils.createWorld("elevar_world");
        ElevarServerUtils.createWorld("elevar_nether", new Random().nextLong(), World.Environment.NETHER, WorldType.NORMAL);
        getLogger().info("lobby and game worlds loaded!");

        // set game period to lobby
        period = GamePeriod.LOBBY;
        getLogger().info("game period set to the default!");

        // initializing the banned players list
        bannedPlayers = new ArrayList<>();
        getLogger().info("getting ready to ban players! :3");

        // initializing the portal locations
        netherPortalLocations = new HashMap<>();
        getLogger().info("getting nether stuff ready!");

        // adding a check for players in the current round in case they log out mid round
        currentRoundPlayers = new ArrayList<>();
        getLogger().info("adding checks for current players!");

        getLogger().info("plugin is loaded! hope you enjoy :3");

    }

    @Override
    public void onDisable() {

        getLogger().info("goodbye forever 3:");

    }

    public static FileConfiguration config() {
        return getInstance().getConfig();
    }

}
