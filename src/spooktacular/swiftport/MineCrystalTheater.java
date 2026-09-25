package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineCrystalTheater.swift.
 *  Original: Sources/Ultimate/MineCrystalTheater.swift (50365 chars, 1259 lines).
 *  Swift types: MineCrystalAnimPhase, MineDoorStyle, MineUnlockPhase, MineHarvestPhase, MineAnimatedCube, MineCubeCluster, MineAnimatedSpike, Triangle, MineSpikePair, MineAnimatedOrb, MineOrbConstellation, MineCaveShimmer
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineCrystalTheater {
    private MineCrystalTheater() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineCrystalTheater.swift";
    public static final int SWIFT_LINES = 1259;
    public static final List<String> SWIFT_TYPES = List.of("MineCrystalAnimPhase", "MineDoorStyle", "MineUnlockPhase", "MineHarvestPhase", "MineAnimatedCube", "MineCubeCluster", "MineAnimatedSpike", "Triangle", "MineSpikePair", "MineAnimatedOrb", "MineOrbConstellation", "MineCaveShimmer", "MineLightShafts", "MineLockedDoor", "MineUnlockCeremony", "CeremonyPhase", "MineHarvestBurst", "MineLockedCavePanel", "CostLine", "MinePrismPalace", "MineCrystalCredits", "MineCrystalShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
