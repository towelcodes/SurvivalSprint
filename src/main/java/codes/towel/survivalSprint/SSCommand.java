package codes.towel.survivalSprint;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SSCommand implements CommandExecutor {
    private static final TextComponent helpMsg =
            Component.text("SurvivalSprint Help\n", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD)
                    .append(Component.text("/ss set <key> <value>" +
                            "\n/ss get <key>" +
                            "\n/ss event <active|queue> <list|add|del> [event]" +
                            "\n/ss pvp <on|off>" +
                            "\n/ss halt <on|off>" +
                            "\n/ss reload" +
                            "\n/ss update border"));

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return true;
        }
        return true;
    }
}
