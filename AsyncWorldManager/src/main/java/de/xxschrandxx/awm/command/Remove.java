package de.xxschrandxx.awm.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.xxschrandxx.awm.Main;
import de.xxschrandxx.awm.api.config.*;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class Remove {
  public static boolean removecmd (CommandSender sender, String[] args) {
    if (WorldManager.hasPermission(sender, "command.permissions.worldmanager.remove")) {
      if (args.length != 1) {
        if (!args[1].isEmpty()) {
          WorldData worlddata = Storage.getWorlddataFromAlias(args[1]);
          Config config = Storage.getWorldConfig(worlddata.getWorldName());
          if ((worlddata != null) && (config != null)) {
            if (Storage.getAllLoadedWorlds().contains(worlddata.getWorldName())) {
              for (Player p : Bukkit.getWorld(worlddata.getWorldName()).getPlayers()) {
                p.sendMessage(Main.Loop(Main.messages.get().getString("prefix") + Main.messages.get().getString("command.remove.teleport")));
                p.teleport(Bukkit.getWorld(Main.config.get().getString("MainWorld")).getSpawnLocation());
              }
              WorldConfigManager.remove(Bukkit.getWorld(worlddata.getWorldName()), config);
              sender.sendMessage(Main.Loop(Main.messages.get().getString("prefix") + Main.messages.get().getString("command.remove.success").replace("%world%", worlddata.getWorldName())));
              return true;
            }
            else {
              sender.spigot().sendMessage(new ComponentBuilder(Main.Loop(Main.messages.get().getString("prefix") + Main.messages.get().getString("command.remove.failed.chat").replace("%world%", worlddata.getWorldName()))).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Main.Loop(Main.messages.get().getString("command.remove.failed.hover"))).create())).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wm list")).create());
              return true;
            }
          }
          else {
            sender.spigot().sendMessage(new ComponentBuilder(Main.Loop(Main.messages.get().getString("prefix") + Main.messages.get().getString("command.remove.failed.chat").replace("%world%", args[1]))).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Main.Loop(Main.messages.get().getString("command.remove.failed.hover"))).create())).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wm list")).create());
            return true;
          }
        }
        else {
          return false;
        }
      }
      else {
        return false;
      }
    }
    else {
      sender.spigot().sendMessage(new ComponentBuilder(Main.Loop(Main.messages.get().getString("prefix") + Main.messages.get().getString("nopermission"))).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Main.Loop("(Required: &e%perm%&7)".replace("%perm%", Main.config.get().getString("command.permissions.worldmanager.remove")))).create())).create());
      return true;
    }
  }
  public static List<String> removelist(String[] args, CommandSender sender) {
    List<String> list = new ArrayList<String>();
    if (WorldManager.hasPermission(sender, "command.permissions.worldmanager.remove")) {
      if (args.length == 1) {
        list.add("remove");
      }
      else if ((args.length == 2) && args[1].equalsIgnoreCase("remove")) {
        list.addAll(Storage.getAllLoadedWorlds());
      }
    }
    return list;
  }
}