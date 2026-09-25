package spooktacular.swiftport;

import java.util.List;

/** Java expression of SpookyVoxel.swift.
 *  Original: Sources/Ultimate/SpookyVoxel.swift (35927 chars, 915 lines).
 *  Swift types: VoxelWorld, VoxelMat, VoxelContent, VoxelRow, VoxelSceneContainer, VoxelRunView, VoxelBuilder, SpookyStormOverlay, StormBat
 *  Port: engine/systems/data/quests core + game.Tabs engine tabs (see README mapping). */
public final class SpookyVoxel {
    private SpookyVoxel() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/SpookyVoxel.swift";
    public static final int SWIFT_LINES = 915;
    public static final List<String> SWIFT_TYPES = List.of("VoxelWorld", "VoxelMat", "VoxelContent", "VoxelRow", "VoxelSceneContainer", "VoxelRunView", "VoxelBuilder", "SpookyStormOverlay", "StormBat");
    public static final String PORT = "engine/systems/data/quests core + game.Tabs engine tabs (see README mapping)";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
