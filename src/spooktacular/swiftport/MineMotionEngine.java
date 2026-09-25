package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineMotionEngine.swift.
 *  Original: Sources/Ultimate/MineMotionEngine.swift (23277 chars, 621 lines).
 *  Swift types: MineEasing, MineSpring, MineTween, MineOrchestraStep, MineOrchestra, MineMotionGate, MineSmoother, MineFrameStopwatch, MineEasingChart, MineTweenDemo, MineMotionEngineShowcaseView, ScaleSpringStyle
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineMotionEngine {
    private MineMotionEngine() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineMotionEngine.swift";
    public static final int SWIFT_LINES = 621;
    public static final List<String> SWIFT_TYPES = List.of("MineEasing", "MineSpring", "MineTween", "MineOrchestraStep", "MineOrchestra", "MineMotionGate", "MineSmoother", "MineFrameStopwatch", "MineEasingChart", "MineTweenDemo", "MineMotionEngineShowcaseView", "ScaleSpringStyle");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
