package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineWaterFX.swift.
 *  Original: Sources/Ultimate/MineWaterFX.swift (12243 chars, 315 lines).
 *  Swift types: MineDripCurtain, MineRippleField, MineBubbleColumn, MineWaterfallSheet, MineFloodShimmer, MineReflectionPool, MineWaterShowcaseView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineWaterFX {
    private MineWaterFX() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineWaterFX.swift";
    public static final int SWIFT_LINES = 315;
    public static final List<String> SWIFT_TYPES = List.of("MineDripCurtain", "MineRippleField", "MineBubbleColumn", "MineWaterfallSheet", "MineFloodShimmer", "MineReflectionPool", "MineWaterShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
