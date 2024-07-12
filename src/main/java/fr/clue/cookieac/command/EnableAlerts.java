package fr.clue.cookieac.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EnableAlerts implements TabExecutor {
    public static int MODE;
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length != 1) {
            commandSender.sendMessage("Usage: alerts <all|op|off>");
            return false;
        }

        switch (strings[0].toLowerCase()) {
            case "all":
                MODE = 0;
                commandSender.sendMessage("Alerts enabled for all.");
                break;
            case "op":
                MODE = 1;
                commandSender.sendMessage("Alerts enabled for operators only.");
                break;
            case "off":
                MODE = 2;
                commandSender.sendMessage("Alerts are now off.");
                break;
            default:
                commandSender.sendMessage("Usage: alerts <all|op|off>");
                return true;
        }
        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> list = Arrays.asList("all", "off", "op");
        String input = strings[0].toLowerCase();

        List<String> completions = null;
        for(String cur : list){
            if(cur.startsWith(input)){
                if(completions == null)
                    completions = new ArrayList<>();
                completions.add(cur);
            }
        }

        if (completions != null)
            Collections.sort(completions);
        return completions;
    }
}
