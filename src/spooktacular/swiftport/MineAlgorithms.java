package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineAlgorithms.swift.
 *  Original: Sources/Ultimate/MineAlgorithms.swift (35375 chars, 940 lines).
 *  Swift types: SeededRNG, PerlinNoise, CarveCell, MazeCarveResult, MazeCarver, AStarPathfinder, Node, Heap, MineRaycaster, Hit, MineNoiseShowcase, MineCarverShowcase
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineAlgorithms {
    private MineAlgorithms() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineAlgorithms.swift";
    public static final int SWIFT_LINES = 940;
    public static final List<String> SWIFT_TYPES = List.of("SeededRNG", "PerlinNoise", "CarveCell", "MazeCarveResult", "MazeCarver", "AStarPathfinder", "Node", "Heap", "MineRaycaster", "Hit", "MineNoiseShowcase", "MineCarverShowcase", "MineCarveGrid", "MineAStarShowcase", "MineRaycastShowcase", "MineAlgorithmsShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
