package de.xxschrandxx.awm.api.gamerulemanager;

public class v1_13_0 extends v1_12_0 {
  static Rule<Boolean> gameLoopFunction = null;
  static void setup() {
    v1_12_0.setup();
    gameLoopFunction = null;
  }
}
