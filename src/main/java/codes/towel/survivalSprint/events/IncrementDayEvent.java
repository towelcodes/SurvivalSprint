package codes.towel.survivalSprint.events;

import codes.towel.survivalSprint.Event;
import codes.towel.survivalSprint.ServerConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;

public class IncrementDayEvent extends Event {
    private boolean active = false;
    public boolean isActive() {
        return active;
    }

    // TODO implement
    public long getDuration() {
        return 0;
    }

    private final ServerConfiguration conf;
    IncrementDayEvent(ServerConfiguration conf) {
        this.conf = conf;
    }

    @Override
    public void start() {
        currentDay++;
        active = true;
    }

    @Override
    public void stop() {
        active = false;
    }


    /**
     * Get the short name of the event. May return differently when active and inactive.
     * @return the name
     */
    @Override
    public String getName() {
        if (isActive()) {
            return NamedTextColor.RED + "Border Shrinking";
        } else {
            return NamedTextColor.RED + "Border Shrink";
        }
    }

    @Override
    public String getDescription() {

    }
}
