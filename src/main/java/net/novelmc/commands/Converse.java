package net.novelmc.commands;

import net.novelmc.commands.loader.CommandBase;
import net.novelmc.commands.loader.CommandParameters;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Show information about PramireProXConverseVersatilityFreedomModCraft69", usage = "/<command> [reload | debug]")
public class Converse extends CommandBase
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        sender.sendMessage(ChatColor.RED + "Want to suck my dick?");
        return true;
    }
}
