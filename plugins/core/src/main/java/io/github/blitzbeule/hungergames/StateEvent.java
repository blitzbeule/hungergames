package io.github.blitzbeule.hungergames;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StateEvent extends Event implements Cancellable {

    public boolean isGamePhaseChanged() {
        return game_phase_changed;
    }

    private boolean game_phase_changed;
    private boolean cancelled;

    public State.GamePhase getOldGamePhase() {
        return old_game_phase;
    }

    private State.GamePhase old_game_phase;

    public State.GamePhase getNew_game_phase() {
        return new_game_phase;
    }

    public void setNew_game_phase(State.GamePhase new_game_phase) {
        this.new_game_phase = new_game_phase;
    }

    private State.GamePhase new_game_phase;

    public StateEvent(boolean game_phase_changed, State.GamePhase old_game_phase, State.GamePhase new_game_phase) {
        this.game_phase_changed = game_phase_changed;
        this.old_game_phase = old_game_phase;
        this.new_game_phase = new_game_phase;
        cancelled = false;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
