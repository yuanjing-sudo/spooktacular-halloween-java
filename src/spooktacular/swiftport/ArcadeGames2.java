package spooktacular.swiftport;

import java.util.List;

/** Java expression of ArcadeGames2.swift.
 *  Original: Sources/Ultimate/ArcadeGames2.swift (27959 chars, 746 lines).
 *  Swift types: GhostRaceGameView, TriviaQuestion, TriviaGameView, RhythmNote, RhythmGameView, MazeEscapeHostView
 *  Port: spooktacular.app.MiniGames + spooktacular.systems (FX/motion). */
public final class ArcadeGames2 {
    private ArcadeGames2() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/ArcadeGames2.swift";
    public static final int SWIFT_LINES = 746;
    public static final List<String> SWIFT_TYPES = List.of("GhostRaceGameView", "TriviaQuestion", "TriviaGameView", "RhythmNote", "RhythmGameView", "MazeEscapeHostView");
    public static final String PORT = "spooktacular.app.MiniGames + spooktacular.systems (FX/motion)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
