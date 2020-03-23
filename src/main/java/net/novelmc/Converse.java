package net.novelmc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import me.lucko.luckperms.api.LuckPermsApi;
import net.novelmc.bans.Ban;
import net.novelmc.bridge.LuckPermsBridge;
import net.novelmc.commands.*;
import net.novelmc.config.BanConfig;
import net.novelmc.config.MainConfig;
import net.novelmc.config.PermbanConfig;
import net.novelmc.config.PlayerData;
import net.novelmc.listeners.BanListener;
import net.novelmc.listeners.ChatListener;
import net.novelmc.listeners.ModeListener;
import net.novelmc.listeners.MuteListener;
import net.novelmc.listeners.PlayerDataListener;
import net.novelmc.listeners.TabListener;
import net.novelmc.listeners.WorldListener;
import net.novelmc.permban.Permban;
import net.novelmc.shop.*;
import net.novelmc.util.PlayerOrganizer;
import net.novelmc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Converse extends JavaPlugin
{
    public static Converse plugin;
    public static final BuildProperties build = new BuildProperties();
    public static Server server;
    // Configs
    public BanConfig banConfig;
    public MainConfig config;
    public PermbanConfig permbanConfig;
    public PlayerData pd;
    public PlayerOrganizer po;
    // Banning
    public Ban ban;
    public Permban permban;
    // LuckPerms
    public LuckPermsBridge lp;
    // Shop
    public MainMenu shop;
    public TrailsMenu trails;
    public PlayersMenu players;
    public ShopListener shl;
    // Listeners
    public BanListener bl;
    public ChatListener cl;
    public ModeListener ml;
    public MuteListener mul;
    public PlayerDataListener pdl;
    public TabListener sl;
    public WorldListener wl;

    @Override
    public void onLoad()
    {
        plugin = this;
        server = plugin.getServer();
        banConfig = new BanConfig(this);
        config = new MainConfig(this);
        permbanConfig = new PermbanConfig(this);
        pd = new PlayerData(this);
        loadShops();
    }

    @Override
    public void onEnable()
    {
        // Config
        registerConfigs();
        // BuildProperties
        build.load(this);
        // LuckPerms
        getLuckPermsAPI();
        lp = new LuckPermsBridge(this);
        // Commands
        registerCommands();
        // Listener
        registerListeners();
        // Banning
        ban = new Ban(this);
        permban = new Permban(this);
        //Scoreboard for Tablist
        po = new PlayerOrganizer();
    }

    @Override
    public void onDisable()
    {
        // Unregister configs
        unregisterConfigs();
    }
    
    private void loadShops() {
        shop = new MainMenu();
        trails = new TrailsMenu();
        players = new PlayersMenu();
    }

    public static LuckPermsApi getLuckPermsAPI()
    {
        RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
        if (provider != null)
        {
            return provider.getProvider();
        }
        return null;
    }

    @SuppressWarnings("null")
    private void registerCommands()
    {
        getCommand("adminchat").setExecutor(new AdminchatCommand());
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("banlookup").setExecutor(new BanLookupCommand());
        getCommand("blackchat").setExecutor(new BlackChatCommand());
        getCommand("converse").setExecutor(new ConverseCommand());
        getCommand("dban").setExecutor(new DbanCommand());
        getCommand("flatworld").setExecutor(new FlatworldCommand());
        getCommand("manageshop").setExecutor(new ShopAdminCommand());
        getCommand("mode").setExecutor(new ModeCommand());
        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("permban").setExecutor(new PermbanCommand());
        getCommand("plotworld").setExecutor(new PlotworldCommand());
        getCommand("shop").setExecutor(new ShopCommand());
        getCommand("staff").setExecutor(new StaffCommand());
        getCommand("staffworld").setExecutor(new StaffworldCommand());
        getCommand("unban").setExecutor(new UnbanCommand());
        getCommand("unloadchunks").setExecutor(new UnloadChunksCommand());
        
    }

    private void registerListeners()
    {
        bl = new BanListener(this);
        cl = new ChatListener(this);
        ml = new ModeListener(this);
        mul = new MuteListener(this);
        pdl = new PlayerDataListener(this);
        sl = new TabListener(this);
        wl = new WorldListener(this);
        shl = new ShopListener(this);
        
    }

    public void registerConfigs()
    {
        banConfig.load();
        config.load();
        permbanConfig.load();
    }

    private void unregisterConfigs()
    {
        banConfig.save();
        config.save();
        permbanConfig.save();
    }

    private void reregisterConfigs()
    {
        unregisterConfigs();
        registerConfigs();
    }

    public static class BuildProperties
    {
        public String author;
        public String version;
        public String number;
        public String date;
        public String head;

        void load(Converse plugin)
        {
            try
            {
                final Properties props;

                try (InputStream in = plugin.getResource("build.properties"))
                {
                    props = new Properties();
                    props.load(in);
                }

                author = props.getProperty("buildAuthor", "unknown");
                version = props.getProperty("buildVersion", plugin.getDescription().getVersion());
                number = props.getProperty("buildNumber", "1");
                date = props.getProperty("buildDate", "unknown");
                head = props.getProperty("buildHead", "unknown").replace("${git.commit.id.abbrev}", "unknown");
            }
            catch (IOException ex)
            {
                server.getLogger().severe("Could not load build properties! Did you compile with NetBeans/Maven?");
                server.getLogger().severe(ex.toString());
            }
        }
    }
}
