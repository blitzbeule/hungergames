package io.github.blitzbeule.hungergames.phases;

import io.github.blitzbeule.hungergames.Hungergames;

public abstract class Phase implements PhaseInterface {

    protected Hungergames hg;

    public Phase(Hungergames hg) {
        this.hg = hg;
    }

}
