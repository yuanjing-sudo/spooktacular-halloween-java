package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineGameFX.swift.
 *  Original: Sources/Ultimate/MineGameFX.swift (17224 chars, 491 lines).
 *  Swift types: MineGameTileFX, MineFXMemory, MineFXPumpkin, MineFXGhostRace, MineFXCandy, MineFXDuel, MineFXMaze, MineFXTrivia, MineFXRhythm, MineFXVoxel, MineFXGraveyard, MineFXMineTile
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineGameFX {
    private MineGameFX() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineGameFX.swift";
    public static final int SWIFT_LINES = 491;
    public static final List<String> SWIFT_TYPES = List.of("MineGameTileFX", "MineFXMemory", "MineFXPumpkin", "MineFXGhostRace", "MineFXCandy", "MineFXDuel", "MineFXMaze", "MineFXTrivia", "MineFXRhythm", "MineFXVoxel", "MineFXGraveyard", "MineFXMineTile", "MineGameDetailArt", "MineGameFXShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
