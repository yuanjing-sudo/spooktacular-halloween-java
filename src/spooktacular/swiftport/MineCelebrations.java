package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineCelebrations.swift.
 *  Original: Sources/Ultimate/MineCelebrations.swift (14168 chars, 364 lines).
 *  Swift types: MineSpinPrize, MineSpinTable, MineSpinWheel, MineDailyWheelView, MineSellParade, MineMilestoneToast, MineCelebrationShowcaseView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineCelebrations {
    private MineCelebrations() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineCelebrations.swift";
    public static final int SWIFT_LINES = 364;
    public static final List<String> SWIFT_TYPES = List.of("MineSpinPrize", "MineSpinTable", "MineSpinWheel", "MineDailyWheelView", "MineSellParade", "MineMilestoneToast", "MineCelebrationShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
