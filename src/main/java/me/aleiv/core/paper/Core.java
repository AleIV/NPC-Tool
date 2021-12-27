package me.aleiv.core.paper;

import com.comphenix.protocol.wrappers.WrappedGameProfile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;
import kr.entree.spigradle.annotations.SpigotPlugin;
import lombok.Getter;
import me.aleiv.core.paper.commands.GlobalCMD;
import me.aleiv.core.paper.listeners.GlobalListener;
import me.aleiv.core.paper.utilities.NegativeSpaces;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;
import net.kyori.adventure.text.minimessage.MiniMessage;
import uk.lewdev.entitylib.FakeEntityPlugin;
import uk.lewdev.entitylib.entity.FakePlayer;
import us.jcedeno.libs.rapidinv.RapidInvManager;

@SpigotPlugin
public class Core extends JavaPlugin {

    private static @Getter Core instance;
    private @Getter Game game;
    private @Getter PaperCommandManager commandManager;
    private @Getter static MiniMessage miniMessage = MiniMessage.get();
    private @Getter FakeEntityPlugin fakeEntityPlugin;

    @Override
    public void onEnable() {
        instance = this;

        RapidInvManager.register(this);
        BukkitTCT.registerPlugin(this);
        NegativeSpaces.registerCodes();

        this.fakeEntityPlugin = new FakeEntityPlugin();

        this.fakeEntityPlugin.onEnable();

        game = new Game(this);
        game.runTaskTimerAsynchronously(this, 0L, 20L);

        RapidInvManager.register(this);

        //LISTENERS

        Bukkit.getPluginManager().registerEvents(new GlobalListener(this), this);

        //COMMANDS
        
        commandManager = new PaperCommandManager(this);

        commandManager.registerCommand(new GlobalCMD(this));

    }

    @Override
    public void onDisable() {
        try {
            this.fakeEntityPlugin.onDisable();

        }catch (Exception e) {
            
        }
    }

    public void spawnClone(Player player) {
        var loc = player.getLocation();
        var property = WrappedGameProfile.fromPlayer(player).getProperties().entries().iterator().next().getValue();

        var playerSkinValue = property.getValue();
        var playerSkinSignature = property.getSignature();

        var npc = FakePlayer.of(player.getName(), loc, playerSkinValue, playerSkinSignature);

        var players = Bukkit.getOnlinePlayers();
        for (var p : players) {
            npc.show(p);
        }

    }

}