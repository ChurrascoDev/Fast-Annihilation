package com.github.imthenico.fastannihilation.tests;

import com.github.imthenico.annihilation.api.team.TeamColor;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPCMemberPack {

    private final Map<TeamColor, List<NPC>> npcMembers = new HashMap<>();

    public NPCMemberPack(NPCRegistry npcRegistry) {
        for (TeamColor value : TeamColor.values()) {
            List<NPC> npcs = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                NPC npc = npcRegistry.createNPC(EntityType.PLAYER, value.getColoredName() + "_player_" + i);
                npcs.add(npc);
            }

            npcMembers.put(value, npcs);
        }
    }

    public List<NPC> getMembersOf(TeamColor teamColor) {
        return npcMembers.get(teamColor);
    }

    public Map<TeamColor, List<NPC>> getNpcMembers() {
        return new HashMap<>(npcMembers);
    }
}