package spooktacular.swiftport;

import java.util.List;

/** Java expression of MineContracts.swift.
 *  Original: Sources/Ultimate/MineContracts.swift (9112 chars, 222 lines).
 *  Swift types: MineContract, MineContractBoard, MineContractView
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class MineContracts {
    private MineContracts() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/MineContracts.swift";
    public static final int SWIFT_LINES = 222;
    public static final List<String> SWIFT_TYPES = List.of("MineContract", "MineContractBoard", "MineContractView");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
