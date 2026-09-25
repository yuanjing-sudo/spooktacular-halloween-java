package spooktacular.swiftport;

import java.util.List;

/** Java expression of MazeTunnelThemes.swift.
 *  Original: Sources/Ultimate/MazeTunnelThemes.swift (4538 chars, 114 lines).
 *  Swift types: MazeTunnelTheme, MazeThemeGuide, MazeThemePickerView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MazeTunnelThemes {
    private MazeTunnelThemes() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MazeTunnelThemes.swift";
    public static final int SWIFT_LINES = 114;
    public static final List<String> SWIFT_TYPES = List.of("MazeTunnelTheme", "MazeThemeGuide", "MazeThemePickerView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
