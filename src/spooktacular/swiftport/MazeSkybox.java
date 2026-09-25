package spooktacular.swiftport;

import java.util.List;

/** Java expression of MazeSkybox.swift.
 *  Original: Sources/Ultimate/MazeSkybox.swift (12751 chars, 331 lines).
 *  Swift types: MazeStars, MazeFogBand, MazeMoon, MazeRegionSky, MazeEmberRise, MazeLanternDots, MazeGoldMotes, MazeShootingStars, MazeSkyShowcaseView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MazeSkybox {
    private MazeSkybox() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MazeSkybox.swift";
    public static final int SWIFT_LINES = 331;
    public static final List<String> SWIFT_TYPES = List.of("MazeStars", "MazeFogBand", "MazeMoon", "MazeRegionSky", "MazeEmberRise", "MazeLanternDots", "MazeGoldMotes", "MazeShootingStars", "MazeSkyShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
