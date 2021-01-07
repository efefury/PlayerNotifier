package net.playernotifier.listener;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        final Player pinger = event.getPlayer();
        final String msg = event.getMessage();

        if (pinger.hasPermission("playernotifier.everyone") && msg.contains("@everyone")) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                player.sendMessage("§6You got mentioned by §a" + pinger.getName());
                player.sendActionBar("§6New Ping!");
            }

            return;
        }

        handleMentions(pinger, event.getMessage(), event);
    }


    private void handleMentions(@NotNull final Player sender, @NotNull final String message, AsyncPlayerChatEvent event) {
        final String[] contents = message.split(" ");
        for (final String content : contents) {
            final Player mentionedPlayer = Bukkit.getPlayer(content.replace("@", ""));
            if (mentionedPlayer == null) return;
            if (mentionedPlayer == sender) {
                mentionedPlayer.sendMessage("§cYou can't ping yourself!");
                event.setCancelled(true);
                return;
            }
            mentionedPlayer.playSound(mentionedPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            mentionedPlayer.sendMessage("§6You got mentioned by §a" + sender.getName());
            mentionedPlayer.sendActionBar("§6New Ping!");
        }
    }
}
