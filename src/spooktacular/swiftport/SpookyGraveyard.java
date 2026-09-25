package spooktacular.swiftport;

import java.util.List;

/** Java expression of SpookyGraveyard.swift.
 *  Original: Sources/Ultimate/SpookyGraveyard.swift (108487 chars, 2396 lines).
 *  Swift types: GYBlockType, GYBlock, GYGhost, GYGhostState, GYCrypt, GYToolType, GYEnchant, GYTool, GYInventoryItem, GYPlayer, GYParticle3D, GYEvent
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class SpookyGraveyard {
    private SpookyGraveyard() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/SpookyGraveyard.swift";
    public static final int SWIFT_LINES = 2396;
    public static final List<String> SWIFT_TYPES = List.of("GYBlockType", "GYBlock", "GYGhost", "GYGhostState", "GYCrypt", "GYToolType", "GYEnchant", "GYTool", "GYInventoryItem", "GYPlayer", "GYParticle3D", "GYEvent", "GraveyardManager", "GraveyardSceneView", "Coordinator", "SpookyGraveyardView", "GraveyardInventoryView", "GraveyardHostView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
