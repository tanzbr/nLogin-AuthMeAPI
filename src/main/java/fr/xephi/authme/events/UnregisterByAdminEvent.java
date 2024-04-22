package fr.xephi.authme.events;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event fired after a player has been unregistered from an external source (by an admin or via the API).
 * <p>
 * Note that only the {@code playerName} is guaranteed to be not {@code null} in any case.
 * <p>
 * The {@code player} may be null if a name is supplied which has never been online on the server &ndash;
 * due to migrations, data removal, etc. it is possible that a user exists in the database for which the
 * server knows no {@link Player} object.
 * <p>
 * If a player is unregistered via an API call, the {@code initiator} is null as the action has not been
 * started by any {@link CommandSender}. Otherwise, the {@code initiator} is the user who performed the
 * command to unregister the player name.
 */
public class UnregisterByAdminEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final String playerName;

    /**
     * Constructor.
     *
     * @param playerName the name of the player that was unregistered
     */
    public UnregisterByAdminEvent(String playerName, boolean async) {
        super(async);
        this.playerName = playerName;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerName);
    }

    /**
     * @return the name of the player that was unregistered
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Return the list of handlers, equivalent to {@link #getHandlers()} and required by {@link org.bukkit.event.Event}.
     *
     * @return The list of handlers
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}