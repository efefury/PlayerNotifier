package net.playernotifier;

import net.playernotifier.commands.AnnouncementCommand;
import net.playernotifier.listener.PlayerChatListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerNotifier extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);
        getCommand("announcement").setExecutor(new AnnouncementCommand(this));
    }

    @Override
    public void onDisable() {
    }
}
