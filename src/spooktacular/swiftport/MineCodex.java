package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineCodex.swift.
 *  Original: Sources/Ultimate/MineCodex.swift (34043 chars, 688 lines).
 *  Swift types: MineOreEntry, MineOreGuide, MineCritterEntry, MineCritterGuide, MineLayerEntry, MineLayerGuide, MineCodexView, FirstsRow, PetGuideRow, MonsterGuideRow, SectorRow
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineCodex {
    private MineCodex() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineCodex.swift";
    public static final int SWIFT_LINES = 688;
    public static final List<String> SWIFT_TYPES = List.of("MineOreEntry", "MineOreGuide", "MineCritterEntry", "MineCritterGuide", "MineLayerEntry", "MineLayerGuide", "MineCodexView", "FirstsRow", "PetGuideRow", "MonsterGuideRow", "SectorRow");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
