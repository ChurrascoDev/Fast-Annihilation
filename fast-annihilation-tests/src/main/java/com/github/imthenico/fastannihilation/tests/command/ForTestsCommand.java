package com.github.imthenico.fastannihilation.tests.command;

import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.annihilation.api.world.LocationReference;
import com.github.imthenico.fastannihilation.tests.NPCMemberPack;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

@Command(names = "annitest")
public class ForTestsCommand implements CommandClass {

    private final NPCMemberPack npcMemberPack;
    private final LocationReference hubReference;

    public ForTestsCommand(NPCMemberPack npcMemberPack, LocationReference hubReference) {
        this.npcMemberPack = npcMemberPack;
        this.hubReference = hubReference;
    }

    @Command(names = "join-npcs")
    public boolean joinNPCS(@Sender Player player, String roomName) {
        for (TeamColor value : TeamColor.values()) {
            consumeAll(value, (npcPlayer) -> {
                npcPlayer.performCommand("game join " + roomName);
                npcPlayer.performCommand("team " + value.name());
            });
        }

        return true;
    }

    private void consumeAll(TeamColor color, Consumer<Player> consumer) {
        npcMemberPack
                .getMembersOf(color)
                .forEach(npc -> {
                    if (!npc.isSpawned()) {
                        npc.spawn(hubReference.get());
                    }

                    Player player = (Player) npc.getEntity();
                    consumer.accept(player);
                });
    }
}