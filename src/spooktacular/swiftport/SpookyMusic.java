package spooktacular.swiftport;

import java.util.List;

/** Java expression of SpookyMusic.swift.
 *  Original: Sources/Ultimate/SpookyMusic.swift (12475 chars, 320 lines).
 *  Swift types: SpookyMusic, Bell, MusicToggleButton
 *  Port: spooktacular.app.SpookyMusic (single switchboard). */
public final class SpookyMusic {
    private SpookyMusic() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/SpookyMusic.swift";
    public static final int SWIFT_LINES = 320;
    public static final List<String> SWIFT_TYPES = List.of("SpookyMusic", "Bell", "MusicToggleButton");
    public static final String PORT = "spooktacular.app.SpookyMusic (single switchboard)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
