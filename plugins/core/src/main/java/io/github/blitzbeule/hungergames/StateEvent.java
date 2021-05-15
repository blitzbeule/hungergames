package io.github.blitzbeule.hungergames;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class StateEvent extends Event {

    public boolean isGamePhaseChanged() {
        return game_phase_changed;
    }

    private boolean game_phase_changed;

    public State.GamePhase getOldGamePhase() {
        return old_game_phase;
    }

    private State.GamePhase old_game_phase;

    public StateEvent(boolean game_phase_changed, State.GamePhase old_game_phase) {
        this.game_phase_changed = game_phase_changed;
        this.old_game_phase = old_game_phase;
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
