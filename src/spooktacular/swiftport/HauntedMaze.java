package spooktacular.swiftport;

import java.util.List;

/** Java expression of HauntedMaze.swift.
 *  Original: Sources/Ultimate/HauntedMaze.swift (31665 chars, 860 lines).
 *  Swift types: MazeHouse, MazePos, MazeDirection, MazeRNG, HauntedMaze, MazePhase, UltimateMazeTabView
 *  Port: spooktacular.engine.Engine (DFS maze) + spooktacular.game.MazePanel (raycast Swing view). */
public final class HauntedMaze {
    private HauntedMaze() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/HauntedMaze.swift";
    public static final int SWIFT_LINES = 860;
    public static final List<String> SWIFT_TYPES = List.of("MazeHouse", "MazePos", "MazeDirection", "MazeRNG", "HauntedMaze", "MazePhase", "UltimateMazeTabView");
    public static final String PORT = "spooktacular.engine.Engine (DFS maze) + spooktacular.game.MazePanel (raycast Swing view)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
