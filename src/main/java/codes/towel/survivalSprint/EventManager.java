package codes.towel.survivalSprint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class EventManager {
    private static Logger logger = LoggerFactory.getLogger(EventManager.class);

    /**
     * Ordered ArrayList of events
     * The next event is always at index 0
     * The Date specifies when the event will start
     */
    private ArrayList<Tuple<Event, Date>> eventQueue = new ArrayList<>();
    public final List<Tuple<Event, Date>> getEventQueue() {
        return Collections.unmodifiableList(eventQueue);
    }

    /**
     * Ordered ArrayList of active events
     * The next event to terminate is always at index 0
     * The Date specifies when the event will end
     */
    private ArrayList<Tuple<Event, Date>> activeEvents = new ArrayList<>();
    public final List<Tuple<Event, Date>> getActiveEvents() {
        return Collections.unmodifiableList(activeEvents);
    }

    public void doEvents() {
        if (eventQueue.isEmpty()) { return; }
        if (eventQueue.getFirst().y.compareTo(new Date()) >= 0) {
            eventQueue.getFirst().x.start();
            Tuple<Event, Date> e = eventQueue.removeFirst();
            logger.info("Event started: {}", e.toString());
            addActiveEvent(e.x, new Date(e.y.getTime()+e.x.getDuration()));
            doEvents();
        }
    }

    public void stopEvents() {
        if (activeEvents.isEmpty()) { return; }
        if (activeEvents.getFirst().y.compareTo(new Date()) >= 0) {
            activeEvents.getFirst().x.stop();
            logger.info("Event ended: {}", activeEvents.removeFirst().toString());
            stopEvents();
        }
    }

    // TODO implement
    public void closeAllEvents() {
    }

    private void sortedPush(Tuple<Event, Date> obj, List<Tuple<Event, Date>> list) {
        ListIterator<Tuple<Event, Date>> iter = list.listIterator();
        while (iter.hasNext()) {
            if (iter.next().y.compareTo(obj.y) > 0) {
                list.add(iter.previousIndex(), obj);
                return;
            }
        }
        list.add(obj);
    }

    private void addActiveEvent(Event event, Date complete) {
        sortedPush(new Tuple<>(event, complete), activeEvents);
        logger.info("Event became active: {}", event.toString());
    }

    public void queueEvent(Event event, Date start) {
        sortedPush(new Tuple<>(event, start), eventQueue);
        logger.info("Event queued: {}", event.toString());
    }

}
