package net.playernotifier.commands;

import net.playernotifier.PlayerNotifier;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class AnnouncementCommand implements CommandExecutor {

    private final CooldownManager cooldown = new CooldownManager();
    private final PlayerNotifier plugin;

    public AnnouncementCommand(PlayerNotifier plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(" ");
            sb.append(args[i]);
        }
        int timeLeft = cooldown.getCooldown(player.getUniqueId());
        if (timeLeft == 0) {
            String message = sb.toString();
            Bukkit.broadcastMessage("§8[§eALERT§8]");
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
            Bukkit.broadcastMessage("§8[§eALERT§8]");
            System.out.println(timeLeft);
            cooldown.setCooldown(player.getUniqueId(), CooldownManager.DEFAULT_COOLDOWN);

            new BukkitRunnable() {
                @Override
                public void run() {
                    int timeLeft = cooldown.getCooldown(player.getUniqueId());
                    cooldown.setCooldown(player.getUniqueId(), --timeLeft);
                    if (timeLeft == 0) {
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 20, 20);
        } else {
                player.sendMessage("§cPlease wait §a" + cooldown.getCooldown(player.getUniqueId()) + " seconds §cbefore doing this again!");
        }
        return true;
    }
}
