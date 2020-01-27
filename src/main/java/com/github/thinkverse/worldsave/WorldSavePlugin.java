package com.github.thinkverse.worldsave;

import com.github.thinkverse.worldsave.commands.WorldSaveCommand;
import com.github.thinkverse.worldsave.utilities.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class WorldSavePlugin extends JavaPlugin {
  final List<String> worlds = new ArrayList<>(Collections.singletonList("world"));

  @Override
  public void onEnable() {
    generateConfig();
    startWorldSaveRunnable();
    registerCommand("worldsave", new WorldSaveCommand(), true);
  }

  @Override
  public void onDisable() {
    getLogger().info("Plugin stopped, the configurable worlds will now be saved.");
    saveWorlds();
  }

  private void generateConfig() {
    saveDefaultConfig();
    getConfig().options().copyDefaults(true);
    saveConfig();
  }

  private void startWorldSaveRunnable() {
    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
      getLogger().info("Plugin initiated a world save.");
      sendNotificationType("start");
      saveWorlds();
      sendNotificationType("end");
    }, 10L, (1200 * getConfig().getInt("save.every", 25)));
  }

  public void saveWorlds() {
    for (Object world : getConfig().getList("save.worlds", worlds)) {
      Bukkit.getServer().getWorld((String) world).save();
    }
  }

  private void sendNotificationType(final String type) {
    if (getConfig().getBoolean("messages.notify")) {
      ChatUtil.notify("worldsave.notify", prefixMessage(type));
    }

    if (getConfig().getBoolean("messages.admin.notify")) {
      ChatUtil.notify("worldsave.admin.notify", prefixMessage(type));
    }
  }

  @NotNull
  public String prefixMessage(final String type) {
    return getConfig().getString("prefix", "") + getConfig().getString(String.format("messages.%s", type));
  }

  protected final void registerCommand(@NotNull final String command, @NotNull CommandExecutor executor, boolean tabCompleter) {
    Objects.requireNonNull(this.getCommand(command)).setExecutor(executor);
    if (tabCompleter) registerTabCompleter(command, (TabCompleter) executor);
  }

  protected final void registerTabCompleter(@NotNull final String command, @NotNull TabCompleter completer) {
    Objects.requireNonNull(this.getCommand(command)).setTabCompleter(completer);
  }

}
