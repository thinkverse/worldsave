package com.github.thinkverse.worldsave.commands;

import com.github.thinkverse.worldsave.WorldSavePlugin;
import com.github.thinkverse.worldsave.utilities.ChatUtil;
import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

public class WorldSaveCommand implements CommandExecutor, TabExecutor {
  private WorldSavePlugin plugin = WorldSavePlugin.getPlugin(WorldSavePlugin.class);

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender.hasPermission("worldsave.admin")) {
      if ((args.length == 1)) {
        if (args[0].equalsIgnoreCase("reload")) {
          plugin.reloadConfig();
          plugin.saveConfig();

          ChatUtil.message(sender, plugin.getConfig().getString("prefix", "") + "&fConfiguration reloaded.");
        } else if (args[0].equalsIgnoreCase("save")) {
          plugin.saveWorlds();

          final String message = plugin.getConfig().getString("messages.admin.force").replace("{player}", sender.getName());
          ChatUtil.notify("worldsave.admin.notify", plugin.getConfig().getString("prefix", "") + message);
        }

        return true;
      }
    }

    return false;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    final List<String> completions = Lists.newArrayList("save", "reload");

    if (sender.hasPermission("worldsave.admin")) {
      if (args.length == 1) {
        return StringUtil.copyPartialMatches(args[0], completions, Lists.newArrayList());
      }
    }

    return Collections.emptyList();
  }

}
