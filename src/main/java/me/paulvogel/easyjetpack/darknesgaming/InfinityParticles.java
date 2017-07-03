package main.java.me.paulvogel.easyjetpack.darknesgaming;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * For use with DarknesGaming's jetpacks.
 */
public class InfinityParticles {
    public InfinityParticles() {
    }
    public static void jetpackEffect(Player player) {
        playEffect(Effect.FENCE_GATE_CLOSE, player.getLocation(), 256);
        playEffect(Effect.FIREWORK_SHOOT, player.getLocation(), 256);
        playEffect(Effect.MOBSPAWNER_FLAMES, player.getLocation(), 256);
        playEffect(Effect.SMOKE, player.getLocation(), 256);
        playEffect(Effect.GHAST_SHOOT, player.getLocation(), 1);
    }

    public static void playEffect(Effect e, Location l, int num) {
        for (final Player player : Bukkit.getOnlinePlayers())
            player.playEffect(l, e, num);
    }

}
