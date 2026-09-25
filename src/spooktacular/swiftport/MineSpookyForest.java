package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineSpookyForest.swift.
 *  Original: Sources/Ultimate/MineSpookyForest.swift (51048 chars, 1312 lines).
 *  Swift types: MineForestSky, MineMoonPhase, MineForestSkyDirector, MineSunsetSky, MineMidnightSky, MinePhaseMoon, MineLightningFlash, MineBoltShape, MineTallPine, MineTaperedTrunk, MineDeadOak, MineBoxyPine
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineSpookyForest {
    private MineSpookyForest() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineSpookyForest.swift";
    public static final int SWIFT_LINES = 1312;
    public static final List<String> SWIFT_TYPES = List.of("MineForestSky", "MineMoonPhase", "MineForestSkyDirector", "MineSunsetSky", "MineMidnightSky", "MinePhaseMoon", "MineLightningFlash", "MineBoltShape", "MineTallPine", "MineTaperedTrunk", "MineDeadOak", "MineBoxyPine", "MineForestFog", "MineMushrooms", "MineToadstool", "MineBoxyProps", "MineFloorAir", "MineGhostHD", "MineForestAvatar", "MineForestTree", "MineSpookyForestView", "MineForestShootingStars", "MineOwlFlyby", "MineWindGust", "MineForestShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
