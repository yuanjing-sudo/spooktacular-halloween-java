package spooktacular.swiftport;

import java.util.List;

/** Java expression of MazeExpeditions.swift.
 *  Original: Sources/Ultimate/MazeExpeditions.swift (35501 chars, 863 lines).
 *  Swift types: MazeExpeditionEvent, MazeExpedition, MazeExpeditionKind, MazeExpeditionCatalog, MazeExpeditionBoard, MazeRegion, MazeRegionAtlas, MazeRegionDirector, MazeExpeditionView, MazeRegionBanner, MazeRegionAtlasView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MazeExpeditions {
    private MazeExpeditions() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MazeExpeditions.swift";
    public static final int SWIFT_LINES = 863;
    public static final List<String> SWIFT_TYPES = List.of("MazeExpeditionEvent", "MazeExpedition", "MazeExpeditionKind", "MazeExpeditionCatalog", "MazeExpeditionBoard", "MazeRegion", "MazeRegionAtlas", "MazeRegionDirector", "MazeExpeditionView", "MazeRegionBanner", "MazeRegionAtlasView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
