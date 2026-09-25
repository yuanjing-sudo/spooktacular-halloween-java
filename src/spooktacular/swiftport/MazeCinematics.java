package spooktacular.swiftport;

import java.util.List;

/** Java expression of MazeCinematics.swift.
 *  Original: Sources/Ultimate/MazeCinematics.swift (24471 chars, 653 lines).
 *  Swift types: MazeCinematicKind, MazeCinematicDirector, MazeCinematicPhase, MazeCinematicShell, MazeExpeditionCinematic, MazeRegionCinematic, MazeBondCinematic, MazeComboCinematic, MazeShinyCinematic, MazeBossDownCinematic, MazeGrandTourCinematic, MazeCinematicHost
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MazeCinematics {
    private MazeCinematics() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MazeCinematics.swift";
    public static final int SWIFT_LINES = 653;
    public static final List<String> SWIFT_TYPES = List.of("MazeCinematicKind", "MazeCinematicDirector", "MazeCinematicPhase", "MazeCinematicShell", "MazeExpeditionCinematic", "MazeRegionCinematic", "MazeBondCinematic", "MazeComboCinematic", "MazeShinyCinematic", "MazeBossDownCinematic", "MazeGrandTourCinematic", "MazeCinematicHost", "MazeCinematicShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
