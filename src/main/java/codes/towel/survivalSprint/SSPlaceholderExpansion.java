package codes.towel.survivalSprint;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.xml.crypto.Data;
import java.util.List;

public class SSPlaceholderExpansion extends PlaceholderExpansion {
    @Override
    @NotNull
    public String getAuthor() {
        return "towelcodes";
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "ss";
    }

    @Override
    @NotNull
    public String getVersion() {
        return "1.0.0";
    }

    SSPlaceholderExpansion(List<Tuple<Event, Data>> eventQueue, List<Tuple<Event, Data>> activeEvents) {

    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        return "";
    }

}
