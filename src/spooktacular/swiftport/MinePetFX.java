package spooktacular.swiftport;

import java.util.List;

/** Java expression of MinePetFX.swift.
 *  Original: Sources/Ultimate/MinePetFX.swift (23897 chars, 627 lines).
 *  Swift types: MinePetSprite, MinePetBounce, MinePetMole, MinePetBat, MineBatWing, MinePetAxolotl, MinePetFox, MinePetWisp, MinePetDragon, MinePetEgg, MinePetHat, MineHatchCeremony
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MinePetFX {
    private MinePetFX() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MinePetFX.swift";
    public static final int SWIFT_LINES = 627;
    public static final List<String> SWIFT_TYPES = List.of("MinePetSprite", "MinePetBounce", "MinePetMole", "MinePetBat", "MineBatWing", "MinePetAxolotl", "MinePetFox", "MinePetWisp", "MinePetDragon", "MinePetEgg", "MinePetHat", "MineHatchCeremony", "MineEquipShine", "MinePetFXShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
