package codes.towel.survivalSprint

import codes.towel.survivalSprint.event.EventEnum
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.persistence.PersistentDataType
import org.slf4j.LoggerFactory
import java.io.Serial
import java.util.Date

abstract class EventManager<T: Event>(val plugin: SurvivalSprint) {
    protected val logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * List of upcoming events. Generally, the most immediate will be located at index 0,
     * and their full information will be displayed to the client.
     * However, this list cannot be expected to always be sorted as some events may be pushed to the front manually.
     */
    private val _upcomingEvents = mutableListOf<T>()
    val upcomingEvents: List<T> get() = _upcomingEvents.toList()

    private val _activeEvents = mutableListOf<T>()
    val activeEvents: List<T> get() = _activeEvents.toList()

    private fun sortedPush(event: T, list: MutableList<T >): Boolean {
        if (list.isEmpty()) {
            list.add(event)
            return true
        }

        // FIXME this should actually look for the first position that does have a start time first
        if (event.start == null) {
            list.add(event)
            return true
        }

        val iter = list.listIterator()
        while (iter.hasNext()) {
            val e = iter.next()
            if ((e.start?.compareTo(event.start) ?: 1) > 0) {
                iter.previous()
                iter.add(event)
                return true
            }
        }

        list.add(event)
        return true
    }

    /**
     * Add an upcoming event to the list.
     * Does nothing after (no scheduling)
     */
    fun addUpcoming(event: T): Boolean {
        if (_upcomingEvents.contains(event)) return false
        return sortedPush(event, _upcomingEvents)
    }

    /**
     * Adds an active event to the list
     * Does nothing after (no scheduling)
     */
    fun addActive(event: T): Boolean {
        if (_activeEvents.contains(event)) return false
        return sortedPush(event, _activeEvents)
    }

    /**
     * Promotes an event currently in the upcoming list to the active list
     * Returns false if the event was not in the active position
     */
    fun promoteToActive(event: T): Boolean {
        return _upcomingEvents.remove(event) && addActive(event)

    }

    /**
     * Remove an event. Does not cancel.
     */
    fun removeEvent(event: T): Boolean {
        return _upcomingEvents.remove(event) || _activeEvents.remove(event)
    }
}

class PlayerEventManager(plugin: SurvivalSprint, val player: Player) : EventManager<TargetedEvent>(plugin) {
    private fun serializeEvents(outerDelimiter: Char, innerDelimiter: Char): String {
        // TODO use iteration logic instead of for loop
        val events: MutableList<SerializableEvent> = mutableListOf()
        for (e in upcomingEvents) {
            events.add(
                SerializableEvent(
                    e.enum,
                    e.start?.time?.minus(Date().time),
                    e.duration
                )
            )
        }

        var out = ""
        var first = true
        for (e in events) {
            if (first) {
                out += e.toString(innerDelimiter)
                first = false
                continue
            }
            out += "${outerDelimiter}${e.toString(innerDelimiter)}"
        }

        logger.info("Serialized events for ${player.name} to: $out")
        return out
    }

    private fun deserializeEvents(input: String, outerDelimiter: Char, innerDelimiter: Char): List<SerializableEvent> {
        val events: MutableList<SerializableEvent> = mutableListOf()

        val parts = input.split(outerDelimiter)
        for (part in parts) {
            events.add(SerializableEvent.from(part, innerDelimiter))
        }

        logger.info("Deserialized events for ${player.name} from: $input to $events")
        return events.toList()
    }

    init {
        val pdc = player.persistentDataContainer;
        if (!pdc.has(NamespacedKey(plugin, "events"), PersistentDataType.STRING)) {
            logger.info("${player.name} has no stored events");
        } else {
            val input = pdc.get(NamespacedKey(plugin, "events"), PersistentDataType.STRING)
            if (input != null) {
                val events = deserializeEvents(input, '|', '_')
                for (event in events) {
                    // addUpcoming(event.enum.clazz.constructors)
                }
            } else {
                logger.warn("Failed to deserialize events for ${player.name}")
            }
        }
    }
}

class GlobalEventManager(plugin: SurvivalSprint) : EventManager<Event>(plugin), Listener {
    private val _playerEvents = mutableMapOf<Player, PlayerEventManager>()
    val playerEvents: Map<Player, PlayerEventManager> get() = _playerEvents.toMap()

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        _playerEvents[e.player] = PlayerEventManager(plugin, e.player)
        logger.info("${e.player.name} event manager started.")
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        val pe = _playerEvents.remove(e.player)
        logger.info("Saving events for ${e.player.name}")
    }
}

