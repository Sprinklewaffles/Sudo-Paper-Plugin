package sudo;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SudoPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("SudoPlugin enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("SudoPlugin disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("sudo")) return false;

        if (!sender.hasPermission("sudo.use")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /sudo <player|console> <command>");
            return true;
        }

        String targetName = args[0];
        String commandToRun = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));

        if (targetName.equalsIgnoreCase("console")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToRun);
            sender.sendMessage("§aCommand run as console: §e" + commandToRun);
            return true;
        }

        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cPlayer not found: " + targetName);
            return true;
        }

        if (commandToRun.toLowerCase().startsWith("say ")) {
            String message = commandToRun.substring(4);
            String displayName = targetName.equalsIgnoreCase("console") ? "Server" :
                                (target != null ? target.getName() : targetName);
            Bukkit.broadcastMessage("§f<" + displayName + "> " + message);
            sender.sendMessage("§aForced " + displayName + " to say: §f" + message);
            return true;
        } else {
            target.performCommand(commandToRun);
            sender.sendMessage("§aForced " + target.getName() + " to run: §e/" + commandToRun);
        }

        sender.sendMessage("§aForced " + target.getName() + " to run: §e/" + commandToRun);
        return true;
    }
}
