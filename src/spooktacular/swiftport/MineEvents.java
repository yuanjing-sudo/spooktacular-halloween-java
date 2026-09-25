package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineEvents.swift.
 *  Original: Sources/Ultimate/MineEvents.swift (15443 chars, 316 lines).
 *  Swift types: MineEvent, MineEventCatalog, MineMerchantOffer, MineMerchant, MineEventDirector, MineMerchantView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineEvents {
    private MineEvents() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineEvents.swift";
    public static final int SWIFT_LINES = 316;
    public static final List<String> SWIFT_TYPES = List.of("MineEvent", "MineEventCatalog", "MineMerchantOffer", "MineMerchant", "MineEventDirector", "MineMerchantView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
