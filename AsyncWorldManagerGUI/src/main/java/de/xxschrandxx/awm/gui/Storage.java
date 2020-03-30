package de.xxschrandxx.awm.gui;

import java.util.ArrayList;
import java.util.List;

import de.xxschrandxx.api.minecraft.Config;

public class Storage {

  public static Config config, messages;

  public static void start() {

    config = new Config(AsyncWorldManagerGUI.getInstance(), "config.yml");
    config.get().options().copyDefaults(true);
    List<String> logs = new ArrayList<String>();
    logs.add("INFO");
    logs.add("WARNING");
    config.get().addDefault("logging.show", logs);
    config.get().addDefault("logging.debug", false);

    config.get().addDefault("permission.command", "awmgui.command");
    config.get().addDefault("permission.openmenu.create", "awmgui.menu.create");
    config.get().addDefault("permission.openmenu.gamerule", "awmgui.menu.gamerule.%world%");
    config.get().addDefault("permission.openmenu.import", "awmgui.menu.import");
    config.get().addDefault("permission.openmenu.list", "awmgui.menu.list");
    config.get().addDefault("permission.openmenu.modify", "awmgui.menu.modify.%world%");
    config.get().addDefault("permission.openmenu.overview", "awmgui.menu.overview");
    config.get().addDefault("permission.openmenu.search", "awmgui.menu.search");
    config.get().addDefault("permission.openmenu.world", "awmgui.menu.world.%world%");

    config.save();

    messages = new Config(AsyncWorldManagerGUI.getInstance(), "messages.yml");
    messages.get().options().copyDefaults(true);
    messages.get().addDefault("prefix", "&8[&6AWM&7-&cGUI&8] &7");
    messages.get().addDefault("header", "&8&m[]&6&m------------------------WM------------------------&8&m[]");
    messages.get().addDefault("footer", "&8&m[]&6&m--------------------------------------------------&8&m[]");

    messages.get().addDefault("nopermission", "You don't have permission to use that.");
    messages.get().addDefault("command.console", "&cOnly Players can execute this command.");
    messages.get().addDefault("command.open", "Opening &e%menu%&7... &8[&cThis can take a while&8]");

    //Menus
    //Overview Menu
    messages.get().addDefault("menu.overview.name", "Overview");
    //Item Create
    messages.get().addDefault("menu.overview.create.itemname", "Create World");
    List<String> createlore = new ArrayList<String>();
    createlore.add("&7Open the Worldcreation Menu.");
    messages.get().addDefault("menu.overview.create.itemlore", createlore);
    //Item List
    messages.get().addDefault("menu.overview.list.itemname", "List Worlds");
    List<String> listlore = new ArrayList<String>();
    listlore.add("&7Open the Worldlist Menu.");
    messages.get().addDefault("menu.overview.list.itemlore", listlore);

    //List Menu
    messages.get().addDefault("menu.list.name", "World List");
    //Item Previous
    messages.get().addDefault("menu.list.previous.itemname", "Previous Page");
    List<String> previouslore = new ArrayList<String>();
    previouslore.add("&7Open previous List page.");
    messages.get().addDefault("menu.list.previous.itemlore", previouslore);
    //Item Next
    messages.get().addDefault("menu.list.next.itemname", "Next Page");
    List<String> nextlore = new ArrayList<String>();
    nextlore.add("&7Open next List page.");
    messages.get().addDefault("menu.list.next.itemlore", nextlore);
    //Item World
    messages.get().addDefault("menu.list.world.itemname.unknown", "&7%world%");
    messages.get().addDefault("menu.list.world.itemname.loaded", "&a%world%");
    messages.get().addDefault("menu.list.world.itemname.unloaded", "&c%world%");
    messages.get().addDefault("menu.list.world.itemname.bukkit", "&e%world%");
    List<String> worldlore = new ArrayList<String>();
    worldlore.add("&7Open the menu for this world.");
    messages.get().addDefault("menu.list.world.itemlore", worldlore);

    //List World
    messages.get().addDefault("menu.world.name", "%world%");
    //Item Import
    messages.get().addDefault("menu.world.import.itemname", "Import %world%");
    List<String> importlore = new ArrayList<String>();
    importlore.add("&7Open the Menu to import this World.");
    messages.get().addDefault("menu.world.import.itemlore", importlore);
    //Item Modify
    messages.get().addDefault("menu.world.modify.itemname", "Modify %world%");
    List<String> modifylore = new ArrayList<String>();
    modifylore.add("&7Open the Menu to modify this World.");
    messages.get().addDefault("menu.world.modify.itemlore", modifylore);
    messages.get().addDefault("menu.world.modify.error", "&cERROR&7, WorldData not found...");
    //Item Teleport
    messages.get().addDefault("menu.world.teleport.itemname", "Teleport into %world%");
    List<String> teleportlore = new ArrayList<String>();
    teleportlore.add("&7Teleport into this World.");
    messages.get().addDefault("menu.world.teleport.itemlore", teleportlore);

    messages.save();

  }

  public static void stop() {}

}
