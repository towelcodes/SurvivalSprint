package codes.towel.survivalSprint;

public abstract class Event {
    @Override
    public String toString() {
        return getName()
                + " | " + getDuration()/1000 + "s"
                + " | " + (isActive() ? "Active" : "Inactive")
                + " (" + getClass().getSimpleName() + "@" + hashCode() + ")";
    }

    public abstract void start();
    public abstract void stop();

    /**
     * Get the short name of this event. May return differently when active and inactive.
     * It will already have colours applied to it.
     * @return The name
     */
    public abstract String getName();

    /**
     * Get a line of description about the event status. May change to reflect current information.
     * @return The description
     */
    public abstract String getDescription();
    public abstract long getDuration();
    public abstract boolean isActive();
}
