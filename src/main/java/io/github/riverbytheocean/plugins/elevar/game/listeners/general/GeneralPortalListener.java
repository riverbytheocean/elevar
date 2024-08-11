package io.github.riverbytheocean.plugins.elevar.game.listeners.general;

import io.github.riverbytheocean.plugins.elevar.Elevar;
import io.github.riverbytheocean.plugins.elevar.ElevarServerUtils;
import io.github.riverbytheocean.plugins.elevar.game.GamePeriod;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class GeneralPortalListener implements Listener {

    @EventHandler
    public void handlePortal(PlayerPortalEvent event) {

        if (!Elevar.config().getBoolean("nether")) {
            event.setCancelled(true);
            return;
        }

        if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
            return;
        }

        if (event.getFrom().getWorld().equals(ElevarServerUtils.createWorld("elevar_world"))) {

            if (Elevar.getPeriod().equals(GamePeriod.STARTER)) {

                Elevar.getNetherPortalLocations().put(event.getPlayer().getUniqueId(), event.getFrom());

                event.getTo().setWorld(ElevarServerUtils.createWorld("elevar_nether"));

                return;

            }

            event.setCancelled(true);

        } else if (event.getFrom().getWorld().equals(ElevarServerUtils.createWorld("elevar_nether"))) {

            Location finalLocation =
                    Elevar.getNetherPortalLocations().containsKey(event.getPlayer().getUniqueId())
                            ? Elevar.getNetherPortalLocations().get(event.getPlayer().getUniqueId())
                            : ElevarServerUtils.createWorld("elevar_world").getHighestBlockAt(0, 0).getLocation().add(0, 1, 0);

            event.getTo().setWorld(ElevarServerUtils.createWorld("elevar_world"));
            event.setTo(Elevar.getNetherPortalLocations().get(finalLocation));

            Elevar.getNetherPortalLocations().remove(event.getPlayer().getUniqueId());

        }

    }

}
