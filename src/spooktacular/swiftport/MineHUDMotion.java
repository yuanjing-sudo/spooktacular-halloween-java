package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineHUDMotion.swift.
 *  Original: Sources/Ultimate/MineHUDMotion.swift (20160 chars, 521 lines).
 *  Swift types: MineSpringButtonStyle, MineShimmerBar, MineShimmerText, MineRollingModifier, MineRollingNumber, MineBackpackWave, MineSellButton, MinePopBadge, MineBounceOnChange, MineCountdownBadge, MineTabSwitcher, MineStreakFlame
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineHUDMotion {
    private MineHUDMotion() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineHUDMotion.swift";
    public static final int SWIFT_LINES = 521;
    public static final List<String> SWIFT_TYPES = List.of("MineSpringButtonStyle", "MineShimmerBar", "MineShimmerText", "MineRollingModifier", "MineRollingNumber", "MineBackpackWave", "MineSellButton", "MinePopBadge", "MineBounceOnChange", "MineCountdownBadge", "MineTabSwitcher", "MineStreakFlame", "MineHUDMotionShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
