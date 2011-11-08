package us.azkedar;

import org.blockface.virtualshop.events.ServerEvents;
import org.blockface.virtualshop.managers.EconomyManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import java.util.ArrayList;

import java.util.logging.Logger;

public class ItemExchange extends JavaPlugin {
    private static Configuration config;
    private static String itemName;
    private static Logger logger;
    
    public void onDisable() {
        ItemExchangeDB.Close();
    }

    public void onEnable() {
        logger = Logger.getLogger("minecraft");
        logger.info("ItemExchange is loading");

        // Load configuration settings
	config = getConfiguration();
        ItemExchangeDB.Initialize(this);
        itemName = config.getString("item-name","Gold");
        config.save();
        RegisterEvents();
    }

    private void RegisterEvents()
    {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, new ServerEvents(), Event.Priority.Normal, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(label.equalsIgnoreCase("exch")) {
            if( args.length > 0) {
                if (args[0].equalsIgnoreCase("history")) {
                    if (args.length == 2) {
                        ItemExchangeDB.history(sender,Integer.parseInt(args[1]));
                        return true;
                    }
                    else {
                        ItemExchangeDB.history(sender,1);
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("buy")) {
                    if (args.length == 2) {
                        ItemExchangeDB.buy(sender,Integer.parseInt(args[1]));
                        return true;
                    }
                    else {
                        sender.sendMessage("Usage: /exch buy <quantity>");
                        return true;
                    }  
                }
                if (args[0].equalsIgnoreCase("sell")) {
                    if (args.length == 2) {
                        ItemExchangeDB.sell(sender,Integer.parseInt(args[1]));
                        return true;
                    }
                    else {
                        sender.sendMessage("Usage: /exch sell <quantity>");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("rate")) {
                    showRate(sender);
                    return true;
                }
            }
            helpCommand(sender);
        }
        return true;
    }
    
    private void showRate(CommandSender sender) {
        double price = ItemExchangeDB.current_rate();
        sender.sendMessage(ChatColor.BLUE + " [" + ChatColor.WHITE + "Current exchange rate: " + ChatColor.YELLOW +  EconomyManager.getMethod().format(price) + ChatColor.GRAY + " per " + ChatColor.LIGHT_PURPLE + itemName + ChatColor.BLUE + "]" );
    }
    
    private void helpCommand(CommandSender sender) {
        sender.sendMessage(ChatColor.BLUE + "|------------- " + ChatColor.LIGHT_PURPLE + itemName + " Exchange" + ChatColor.BLUE + " -------------|");
        sender.sendMessage(ChatColor.GREEN + "/exch" + ChatColor.WHITE + " - Show this message and current rate");
        sender.sendMessage(ChatColor.GREEN + "/exch buy " + ChatColor.GOLD + "<amount> " + ChatColor.WHITE + " - buy this many " + itemName);
        sender.sendMessage(ChatColor.GREEN + "/exch sell " + ChatColor.GOLD + "<amount> " + ChatColor.WHITE + " - sell this many " + itemName); 
        sender.sendMessage(ChatColor.GREEN + "/exch history " + ChatColor.AQUA + "[page]" + ChatColor.WHITE + " - show exchange history");
        sender.sendMessage(ChatColor.GREEN + "/exch rate " + ChatColor.WHITE + " - show current exchange rate");
        showRate(sender);
    }
    
    // Database stuffs
    
}
