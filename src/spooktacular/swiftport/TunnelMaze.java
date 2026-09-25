package spooktacular.swiftport;

import java.util.List;

/** Java expression of TunnelMaze.swift.
 *  Original: Sources/Ultimate/TunnelMaze.swift (150919 chars, 3861 lines).
 *  Swift types: BlockType, Block, MonsterType, MonsterRarity, Monster, MonsterState, Tunnel, TunnelDirection, Room, RoomType, Treasure, TreasureType
 *  Port: spooktacular.engine.Engine + spooktacular.systems.Raycaster + MazePanel. */
public final class TunnelMaze {
    private TunnelMaze() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/TunnelMaze.swift";
    public static final int SWIFT_LINES = 3861;
    public static final List<String> SWIFT_TYPES = List.of("BlockType", "Block", "MonsterType", "MonsterRarity", "Monster", "MonsterState", "Tunnel", "TunnelDirection", "Room", "RoomType", "Treasure", "TreasureType", "CrystalShape", "CrystalCave", "ClosetKind", "ClosetCache", "ForkStyle", "MazePlayer", "Tool", "ToolType", "Weapon", "WeaponType", "Armor", "ArmorType", "Enchantment");
    public static final String PORT = "spooktacular.engine.Engine + spooktacular.systems.Raycaster + MazePanel";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
