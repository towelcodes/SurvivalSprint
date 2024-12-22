package codes.towel.survivalSprint;

public record ServerConfiguration(
    int initialBorder,
    int borderShrink,
    int borderShrinkSpeed,
    int dayChangeoverHour,
    int currentDay

) {}
