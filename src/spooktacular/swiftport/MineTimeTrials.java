package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineTimeTrials.swift.
 *  Original: Sources/Ultimate/MineTimeTrials.swift (8292 chars, 198 lines).
 *  Swift types: MineTrialRule, MineTrialGuide, MineTimeTrial, MineTrialView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineTimeTrials {
    private MineTimeTrials() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineTimeTrials.swift";
    public static final int SWIFT_LINES = 198;
    public static final List<String> SWIFT_TYPES = List.of("MineTrialRule", "MineTrialGuide", "MineTimeTrial", "MineTrialView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
