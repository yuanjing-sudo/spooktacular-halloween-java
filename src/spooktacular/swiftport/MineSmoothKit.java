package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineSmoothKit.swift.
 *  Original: Sources/Ultimate/MineSmoothKit.swift (43018 chars, 1107 lines).
 *  Swift types: MineQualityLevel, MineFrameMonitor, MineSpatialHash, MineNotificationQueue, Item, MineChunkStreamer, MineStatTracker, MineTutorialStep, MineTutorialState, MineFrameBadge, MinePerfPanel, MineTutorialView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineSmoothKit {
    private MineSmoothKit() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineSmoothKit.swift";
    public static final int SWIFT_LINES = 1107;
    public static final List<String> SWIFT_TYPES = List.of("MineQualityLevel", "MineFrameMonitor", "MineSpatialHash", "MineNotificationQueue", "Item", "MineChunkStreamer", "MineStatTracker", "MineTutorialStep", "MineTutorialState", "MineFrameBadge", "MinePerfPanel", "MineTutorialView", "MineSettingsView", "MineMapView", "SpookySectorMood", "MineEconomyForecast", "MineForecastView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
