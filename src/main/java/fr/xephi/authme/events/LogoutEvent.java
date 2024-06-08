package fr.xephi.authme.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fictitious event since nLogin doesn't have a logout command
 * Just for be compatible with plugins that needs this class
 */
public class LogoutEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;

    public LogoutEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
