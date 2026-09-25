package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineFishing.swift.
 *  Original: Sources/Ultimate/MineFishing.swift (18393 chars, 435 lines).
 *  Swift types: MNFish, MNFishGuide, MNRodTier, MNFishingState, MineFishingGame, MineFishingView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineFishing {
    private MineFishing() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineFishing.swift";
    public static final int SWIFT_LINES = 435;
    public static final List<String> SWIFT_TYPES = List.of("MNFish", "MNFishGuide", "MNRodTier", "MNFishingState", "MineFishingGame", "MineFishingView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
