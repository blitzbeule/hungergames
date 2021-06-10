package io.github.blitzbeule.hungergames.phases;

import io.github.blitzbeule.hungergames.Hungergames;
import org.bukkit.event.Listener;

public abstract class Phase implements PhaseInterface, Listener {

    protected Hungergames hg;

    public Phase(Hungergames hg) {
        this.hg = hg;
    }

}
