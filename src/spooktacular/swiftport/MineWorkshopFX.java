package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineWorkshopFX.swift.
 *  Original: Sources/Ultimate/MineWorkshopFX.swift (15458 chars, 398 lines).
 *  Swift types: MineAnvilStrike, MineMoltenPour, MineUpgradeBeamUp, MineEggIncubator, MineToolRack, MineWorkshopShowcaseView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineWorkshopFX {
    private MineWorkshopFX() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineWorkshopFX.swift";
    public static final int SWIFT_LINES = 398;
    public static final List<String> SWIFT_TYPES = List.of("MineAnvilStrike", "MineMoltenPour", "MineUpgradeBeamUp", "MineEggIncubator", "MineToolRack", "MineWorkshopShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
