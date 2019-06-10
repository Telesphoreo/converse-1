package net.novelmc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnbanCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (!sender.hasPermission("converse.unban"))
        {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length != 1)
        {
            return false;
        }
        return true;
    }
}
