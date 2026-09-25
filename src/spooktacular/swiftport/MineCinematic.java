package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineCinematic.swift.
 *  Original: Sources/Ultimate/MineCinematic.swift (27056 chars, 707 lines).
 *  Swift types: MineCinematicKind, MineCinematicDirector, MineCinematicPhase, MineCinematicShell, MineRebirthCinematic, MineDiscoveryCinematic, MineLevelUpCinematic, MineQuestDoneCinematic, MineLegendaryCinematic, MineWispCinematic, MineGlowRiseCinematic, MineCinematicHost
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineCinematic {
    private MineCinematic() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineCinematic.swift";
    public static final int SWIFT_LINES = 707;
    public static final List<String> SWIFT_TYPES = List.of("MineCinematicKind", "MineCinematicDirector", "MineCinematicPhase", "MineCinematicShell", "MineRebirthCinematic", "MineDiscoveryCinematic", "MineLevelUpCinematic", "MineQuestDoneCinematic", "MineLegendaryCinematic", "MineWispCinematic", "MineGlowRiseCinematic", "MineCinematicHost", "MineCinematicShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
