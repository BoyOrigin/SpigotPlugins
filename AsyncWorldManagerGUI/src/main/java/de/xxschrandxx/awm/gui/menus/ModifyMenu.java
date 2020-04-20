package de.xxschrandxx.awm.gui.menus;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import de.xxschrandxx.awm.api.config.Modifier;
import de.xxschrandxx.awm.api.config.WorldConfigManager;
import de.xxschrandxx.awm.api.config.WorldData;
import de.xxschrandxx.awm.gui.AsyncWorldManagerGUI;
import de.xxschrandxx.awm.gui.Storage;
import de.xxschrandxx.awm.gui.menus.MenuManager.MenuForm;
import net.md_5.bungee.api.chat.HoverEvent;

public final class ModifyMenu extends Menu {

  public ModifyMenu(WorldData WorldData) {
    super(Storage.messages.get().getString("menu.modify.name").replace("%world%", WorldData.getWorldName()), 9*6);
    worlddata = WorldData;
  }

  public ModifyMenu(WorldData WorldData, int Page, int Maxpage, ConcurrentHashMap<String, ItemStack> Modifier) {
    super(Storage.messages.get().getString("menu.modify.name").replace("%world%", WorldData.getWorldName()), 9*6);
    worlddata = WorldData;
    page = Page;
    maxpage = Maxpage;
    imodifier = Modifier;
  }

  protected int page, maxpage = 0;
  protected ItemStack previous, next, save, cancel;
  protected ConcurrentHashMap<String, ItemStack> imodifier = null;
  protected WorldData worlddata, oldworlddata;

  @Override
  public MenuForm getForm() {
    return MenuForm.ModifyMenu;
  }


  @Override
  public void initializeItems() {

    previous = MenuManager.createGuiItem(Material.ARROW, Storage.messages.get().getString("menu.modify.previous.itemname"), Storage.messages.get().getStringList("menu.modify.previous.itemlore"));
    next = MenuManager.createGuiItem(Material.ARROW, Storage.messages.get().getString("menu.modify.next.itemname"), Storage.messages.get().getStringList("menu.modify.next.itemlore"));

    save = MenuManager.createGuiItem(Material.GREEN_WOOL, Storage.messages.get().getString("menu.modify.save.itemname"), Storage.messages.get().getStringList("menu.modify.save.itemlore"));
    cancel = MenuManager.createGuiItem(Material.RED_WOOL, Storage.messages.get().getString("menu.modify.cancel.itemname"), Storage.messages.get().getStringList("menu.modify.cancel.itemlore"));

    if (imodifier == null) {
      imodifier = new ConcurrentHashMap<String, ItemStack>();
      for (Modifier modifier : Modifier.values()) {
        if (modifier != null) {
          AsyncWorldManagerGUI.getLogHandler().log(true, Level.INFO, "ModifyMenu | Setting " + modifier.name);
          ItemStack istack = MenuManager.createModifyItem(modifier, worlddata);
          imodifier.put(modifier.name, istack);
        }
      }
    }

    List<String> keys = imodifier.keySet().stream().collect(Collectors.toList());
    Collections.sort(keys);

    if (imodifier.size() < 9*5) {
      for (int i = 0; i < keys.size(); i++) {
        getInventory().setItem(i, imodifier.get(keys.get(i)));
      }
      getInventory().setItem(48, save);
      getInventory().setItem(50, cancel);
    }
    else {
      maxpage = (int) Math.ceil(imodifier.size()/44);
      AsyncWorldManagerGUI.getLogHandler().log(true, Level.INFO, "ListMenu | MaxPage is " + maxpage);
      int inv = 0;
      for (int i = page*44; i < keys.size(); i++) {
        if (inv > 44) {
          break;
        }
        getInventory().setItem(inv, imodifier.get(keys.get(i)));
        inv++;
      }
    }
    AsyncWorldManagerGUI.getLogHandler().log(true, Level.INFO, "ListMenu | Page is " + page);
    if (page > 0) {
      getInventory().setItem(45, previous);
    }
    if (page < maxpage) {
      getInventory().setItem(53, next);
    }
    getInventory().setItem(48, save);
    getInventory().setItem(50, cancel);
  }

  @EventHandler
  public void onClick(InventoryClickEvent e) {

    if (e.getInventory() != getInventory()) {
      return;
    }

    e.setCancelled(true);

    if (e.getWhoClicked() instanceof Player) {

      Player p = (Player) e.getWhoClicked();

      if (e.getCurrentItem() == null) {
        return;
      }

      if (e.getCurrentItem().isSimilar(previous)) {
        if (AsyncWorldManagerGUI.getPermissionHandler().hasPermission(p, Storage.config.get().getString("permission.openmenu.modify").replace("%world%", worlddata.getWorldName()))) {
          MenuManager.removeMenu(p);
          MenuManager.addMenu(p, new ModifyMenu(worlddata, page-1, maxpage, imodifier));
        }
        else {
          AsyncWorldManagerGUI.getCommandSenderHandler().sendMessage(p, Storage.messages.get().getString("nopermission"), HoverEvent.Action.SHOW_TEXT, "(Required: &e%perm%&7)".replace("%perm%", Storage.config.get().getString("permission.openmenu.modify")));
          MenuManager.removeMenu(p);
        }
      }

      if (e.getCurrentItem().isSimilar(next)) {
        if (AsyncWorldManagerGUI.getPermissionHandler().hasPermission(p, Storage.config.get().getString("permission.openmenu.modify").replace("%world%", worlddata.getWorldName()))) {
          MenuManager.removeMenu(p);
          MenuManager.addMenu(p, new ModifyMenu(worlddata, page+1, maxpage, imodifier));
        }
        else {
          AsyncWorldManagerGUI.getCommandSenderHandler().sendMessage(p, Storage.messages.get().getString("nopermission"), HoverEvent.Action.SHOW_TEXT, "(Required: &e%perm%&7)".replace("%perm%", Storage.config.get().getString("permission.openmenu.modify")));
          MenuManager.removeMenu(p);
        }
      }

      if (e.getCurrentItem().isSimilar(save)) {
        if (AsyncWorldManagerGUI.getPermissionHandler().hasPermission(p, Storage.config.get().getString("permission.openmenu.modify").replace("%world%", worlddata.getWorldName()))) {
          WorldConfigManager.setWorldData(worlddata);
          World world;
          if ((world = Bukkit.getWorld(worlddata.getWorldName())) != null) {
            WorldConfigManager.setWorldsData(world, worlddata);
          }
          AsyncWorldManagerGUI.getCommandSenderHandler().sendMessage(p, Storage.messages.get().getString("menu.modify.save.success").replace("%world%", worlddata.getWorldName()));
          MenuManager.removeMenu(p);
        }
        else {
          AsyncWorldManagerGUI.getCommandSenderHandler().sendMessage(p, Storage.messages.get().getString("nopermission"), HoverEvent.Action.SHOW_TEXT, "(Required: &e%perm%&7)".replace("%perm%", Storage.config.get().getString("permission.openmenu.modify")));
          MenuManager.removeMenu(p);
        }
      }

      if (e.getCurrentItem().isSimilar(cancel)) {
        if (AsyncWorldManagerGUI.getPermissionHandler().hasPermission(p, Storage.config.get().getString("permission.openmenu.modify").replace("%world%", worlddata.getWorldName()))) {
          AsyncWorldManagerGUI.getCommandSenderHandler().sendMessage(p, Storage.messages.get().getString("menu.modify.cancel.success"));
          MenuManager.removeMenu(p);
        }
        else {
          AsyncWorldManagerGUI.getCommandSenderHandler().sendMessage(p, Storage.messages.get().getString("nopermission"), HoverEvent.Action.SHOW_TEXT, "(Required: &e%perm%&7)".replace("%perm%", Storage.config.get().getString("permission.openmenu.modify")));
          MenuManager.removeMenu(p);
        }
      }

      for (Entry<String, ItemStack> entry : imodifier.entrySet()) {
        if (e.getCurrentItem().isSimilar(entry.getValue())) {
          if (AsyncWorldManagerGUI.getPermissionHandler().hasPermission(p, Storage.config.get().getString("permission.openmenu.modify").replace("%world%", worlddata.getWorldName()))) {
            Modifier modifier = Modifier.getModifier(entry.getKey());
            if (modifier != null) {
              MenuManager.changeMenu(p, new ModifierMenu(worlddata, page, maxpage, imodifier, modifier));
            }
            else {
              AsyncWorldManagerGUI.getCommandSenderHandler().sendMessage(p, Storage.messages.get().getString("menu.modify.change.error"));
              MenuManager.removeMenu(p);
            }
          }
          else {
            AsyncWorldManagerGUI.getCommandSenderHandler().sendMessage(p, Storage.messages.get().getString("nopermission"), HoverEvent.Action.SHOW_TEXT, "(Required: &e%perm%&7)".replace("%perm%", Storage.config.get().getString("permission.openmenu.modify")));
            MenuManager.removeMenu(p);
          }
        }
      }

    }

  }

}
