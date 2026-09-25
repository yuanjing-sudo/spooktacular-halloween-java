package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineBossCinema.swift.
 *  Original: Sources/Ultimate/MineBossCinema.swift (14589 chars, 371 lines).
 *  Swift types: MineBossData, MineBossGuide, MineBossIntro, MineBossBar, MineBossBarDemo, MineDefeatSlowMo, MineBountyCard, MineBossCinemaShowcaseView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineBossCinema {
    private MineBossCinema() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineBossCinema.swift";
    public static final int SWIFT_LINES = 371;
    public static final List<String> SWIFT_TYPES = List.of("MineBossData", "MineBossGuide", "MineBossIntro", "MineBossBar", "MineBossBarDemo", "MineDefeatSlowMo", "MineBountyCard", "MineBossCinemaShowcaseView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
