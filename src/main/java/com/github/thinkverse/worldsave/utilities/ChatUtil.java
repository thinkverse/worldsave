package com.github.thinkverse.worldsave.utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class ChatUtil {
  public static void message(@NotNull CommandSender sender, @NotNull String... message) {
    Arrays.stream(message).map(ChatUtil::translate).forEach(sender::sendMessage);
  }

  public static void notify(@NotNull String permission, @NotNull String... message) {
    Bukkit.getOnlinePlayers().forEach(player -> {
      if (player.hasPermission(permission)) ChatUtil.message(player, message);
    });
  }

  @NotNull
  public static String translate(String message) { return ChatColor.translateAlternateColorCodes((char) '&', message); }
}
