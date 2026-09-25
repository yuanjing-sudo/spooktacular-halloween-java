package spooktacular.swiftport;

import java.util.List;

/** Java expression of AbandonedMine.swift.
 *  Original: Sources/Ultimate/AbandonedMine.swift (182620 chars, 4221 lines).
 *  Swift types: MNBlockType, MNPickTier, MNBlock, MNBatState, MNBat, MNMonsterKind, MNMonster, MNBoxyCritter, MNDepthLayer, MNPet, MNForkStyle, MNCrystalShape
 *  Port: spooktacular.game.MineSim (tycoon sim) + data/quests systems. */
public final class AbandonedMine {
    private AbandonedMine() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/AbandonedMine.swift";
    public static final int SWIFT_LINES = 4221;
    public static final List<String> SWIFT_TYPES = List.of("MNBlockType", "MNPickTier", "MNBlock", "MNBatState", "MNBat", "MNMonsterKind", "MNMonster", "MNBoxyCritter", "MNDepthLayer", "MNPet", "MNForkStyle", "MNCrystalShape", "MNCrystalCave", "MNClosetKind", "MNFrostPocket", "MNClosetCache", "MNPlayer", "MNEvent", "MNCell", "MineWorldData", "Block", "MineManager", "LiveBomb", "MineSceneView", "Coordinator");
    public static final String PORT = "spooktacular.game.MineSim (tycoon sim) + data/quests systems";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
