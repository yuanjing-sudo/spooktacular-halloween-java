package spooktacular.swiftport;

import java.util.List;

/** Java expression of SpookyGameFeel.swift.
 *  Original: Sources/Ultimate/SpookyGameFeel.swift (25614 chars, 668 lines).
 *  Swift types: SpookyHaptics, SpookyNumbers, SpookyCurves, SpookyNames, AtmospherePreset, AtmosphereGuide, SpookyTips, SpookyToast, SpookyToastQueue, SpookyToastView, SpookyDifficulty
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class SpookyGameFeel {
    private SpookyGameFeel() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/SpookyGameFeel.swift";
    public static final int SWIFT_LINES = 668;
    public static final List<String> SWIFT_TYPES = List.of("SpookyHaptics", "SpookyNumbers", "SpookyCurves", "SpookyNames", "AtmospherePreset", "AtmosphereGuide", "SpookyTips", "SpookyToast", "SpookyToastQueue", "SpookyToastView", "SpookyDifficulty");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
