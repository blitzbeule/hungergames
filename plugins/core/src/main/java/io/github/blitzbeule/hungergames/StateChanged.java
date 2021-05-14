package io.github.blitzbeule.hungergames;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StateChanged extends Event {

    boolean phase_changed;

    public StateChanged(boolean phase_changed) {
        this.phase_changed = phase_changed;
    }

    public boolean getPhaseChanged() {
        return phase_changed;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
