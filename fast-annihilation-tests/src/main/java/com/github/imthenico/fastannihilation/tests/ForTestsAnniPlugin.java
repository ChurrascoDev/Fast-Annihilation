package com.github.imthenico.fastannihilation.tests;

import com.github.imthenico.annihilation.api.game.GameRoom;
import com.github.imthenico.annihilation.api.model.lobby.GameLobbyData;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.fastannihilation.tests.command.ForTestsCommand;
import com.github.imthenico.fastannihilation.FastAnnihilationPlugin;
import com.github.imthenico.fastannihilation.command.AnniCommandModule;
import com.github.imthenico.gmlib.MapModel;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.command.Command;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class ForTestsAnniPlugin extends FastAnnihilationPlugin {

    private PartInjector partInjector;
    private NPCRegistry npcRegistry;

    @Override
    public void onEnable() {
        super.onEnable();

        npcRegistry = CitizensAPI.createNamedNPCRegistry("anni-npc-registry", new MemoryNPCDataStore());
        NPCMemberPack npcMemberPack = new NPCMemberPack(npcRegistry);

        for (List<NPC> value : npcMemberPack.getNpcMembers().values()) {
            for (NPC npc : value) {
                checkNPCSpawnStatus(npc);

                npc.addRunnable(() -> checkNPCSpawnStatus(npc));

                getAPI()
                        .playerCache()
                        .registerPlayer(new AnniPlayer(() -> (Player) npc.getEntity()));
            }
        }

        createPartInjector();
        CommandManager commandManager = new BukkitCommandManager("anni-plugin-test");
        AnnotatedCommandTreeBuilder commandTreeBuilder = new AnnotatedCommandTreeBuilderImpl(partInjector);

        List<Command> commands = commandTreeBuilder
                .fromClass(new ForTestsCommand(npcMemberPack, getGameService().getLobbySpawnReference()));
        commandManager.registerCommands(commands);

        registerTestRoom();
    }

    private void registerTestRoom() {
        MapModel<? extends GameLobbyData> lobbyModel =
                getModelService()
                        .getModelStorage()
                        .getCachedModels().getModel("anni_lobby");

        if (lobbyModel == null)
            return;

        GameRoom gameRoom = getGameService()
                .factory()
                .newGame("test_room", "default", lobbyModel);

        gameRoom.getRules()
                .setMinPlayersPerTeam(1);

        gameRoom.setEnabled(true);
        getGameService().gameManager().registerGame(gameRoom);
        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "Test room registered");
    }

    private void checkNPCSpawnStatus(NPC npc) {
        if (!npc.isSpawned()) {
            npc.spawn(getGameService().getLobbySpawnReference().get());
        }
    }

    private void createPartInjector() {
        partInjector = PartInjector.create();

        partInjector.install(new DefaultsModule());
        partInjector.install(new AnniCommandModule(getAPI().playerCache()));
    }
}