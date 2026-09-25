package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineTransitions.swift.
 *  Original: Sources/Ultimate/MineTransitions.swift (13221 chars, 364 lines).
 *  Swift types: MineIrisWipe, MinePixelDissolve, MineCircleReveal, MineFlipCard, MineSlideBlurSwap, MineTransitionNavigator, MineFlipDemo, MineTransitionShowcaseView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineTransitions {
    private MineTransitions() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineTransitions.swift";
    public static final int SWIFT_LINES = 364;
    public static final List<String> SWIFT_TYPES = List.of("MineIrisWipe", "MinePixelDissolve", "MineCircleReveal", "MineFlipCard", "MineSlideBlurSwap", "MineTransitionNavigator", "MineFlipDemo", "MineTransitionShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
