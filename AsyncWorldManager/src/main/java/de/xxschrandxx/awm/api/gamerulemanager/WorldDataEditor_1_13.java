package de.xxschrandxx.awm.api.gamerulemanager;

import java.util.Map;
import java.util.logging.Level;

import org.bukkit.GameRule;
import org.bukkit.World;

import de.xxschrandxx.awm.AsyncWorldManager;
import de.xxschrandxx.awm.api.config.Modifier;
import de.xxschrandxx.awm.api.config.WorldData;

public class WorldDataEditor_1_13 {

  public static WorldData setRule(WorldData worlddata, Rule<?> r, World world) {
    if (r == null)
      return worlddata;
    if (r.getName() == null)
      return worlddata;
    GameRule<?> gr = GameRule.getByName(r.getName());
    if (gr == null) {
      AsyncWorldManager.getLogHandler().log(true, Level.INFO, "Unknown GameRule: " + r.getName());
      return worlddata;
    }
    @SuppressWarnings("unchecked")
    Map<Rule<?>, Object> rules = (Map<Rule<?>, Object>) worlddata.getModifierValue(Modifier.gamerule);
    rules.put(r, world.getGameRuleValue(gr));
    return worlddata;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static boolean setGameRule(WorldData worlddata, Rule<?> r, World world) {
    if (r == null)
      return false;
    if (r.getType() == null && r.getName() == null)
      return false;
    GameRule rule = GameRule.getByName(r.getName());
    Map<Rule<?>, Object> rules = (Map<Rule<?>, Object>) worlddata.getModifierValue(Modifier.gamerule);
    Object value = rules.get(r);
    if (rule == null && value == null)
      return false;
    world.setGameRule(rule, value);
    return true;
  }

}
