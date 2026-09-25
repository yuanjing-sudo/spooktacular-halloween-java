package spooktacular.swiftport;

import java.util.List;

/** Java expression of AvatarWorld.swift.
 *  Original: Sources/Ultimate/AvatarWorld.swift (137274 chars, 2926 lines).
 *  Swift types: AWSound, AWAnimState, AWPalette, AvatarWorldManager, AvatarWorldSceneView, AWNPC, AWGhost, AWBolt, AWPickup, AWPortal, AWBat, AWBomb
 *  Port: spooktacular.data.Entities (player/pets) + Tabs World tab. */
public final class AvatarWorld {
    private AvatarWorld() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/AvatarWorld.swift";
    public static final int SWIFT_LINES = 2926;
    public static final List<String> SWIFT_TYPES = List.of("AWSound", "AWAnimState", "AWPalette", "AvatarWorldManager", "AvatarWorldSceneView", "AWNPC", "AWGhost", "AWBolt", "AWPickup", "AWPortal", "AWBat", "AWBomb", "AWWolf", "Coordinator", "AvatarWorldView", "AWShopView", "AWShopRow");
    public static final String PORT = "spooktacular.data.Entities (player/pets) + Tabs World tab";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
