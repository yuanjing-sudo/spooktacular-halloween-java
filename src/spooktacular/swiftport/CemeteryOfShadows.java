package spooktacular.swiftport;

import java.util.List;

/** Java expression of CemeteryOfShadows.swift.
 *  Original: Sources/Ultimate/CemeteryOfShadows.swift (106021 chars, 2972 lines).
 *  Swift types: CemeteryGhostType, CemeteryGhostRarity, GhostEvidence, GhostState, CemeteryGhostMood, Player, CemeteryEquipment, CemeteryEquipmentType, CemeteryEquipmentUpgrade, InventoryItem, InventoryType, Objective
 *  Port: spooktacular.combat.Bestiary + spooktacular.combat.World. */
public final class CemeteryOfShadows {
    private CemeteryOfShadows() {}
    public static final String ORIGINAL_SWIFT = "Sources/Ultimate/CemeteryOfShadows.swift";
    public static final int SWIFT_LINES = 2972;
    public static final List<String> SWIFT_TYPES = List.of("CemeteryGhostType", "CemeteryGhostRarity", "GhostEvidence", "GhostState", "CemeteryGhostMood", "Player", "CemeteryEquipment", "CemeteryEquipmentType", "CemeteryEquipmentUpgrade", "InventoryItem", "InventoryType", "Objective", "ObjectiveType", "CemeteryLocation", "GhostEntity", "AtmosphericEffect", "AtmosphericType", "ParticleSystem", "ParticleType", "SoundEffect", "AmbientSound", "DiaryEntry", "Difficulty", "CemeteryGameManager", "CemeterySceneView");
    public static final String PORT = "spooktacular.combat.Bestiary + spooktacular.combat.World";
    public static String describe() {
        return ORIGINAL_SWIFT + " [" + SWIFT_LINES + " lines] -> " + PORT;
    }
}
