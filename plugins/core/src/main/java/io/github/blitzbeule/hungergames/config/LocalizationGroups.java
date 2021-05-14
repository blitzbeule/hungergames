package io.github.blitzbeule.hungergames.config;

public enum LocalizationGroups {
    GLOBAL("global"),
    MESSAGES("messages");

    public final String file;

    LocalizationGroups(String file) {
        this.file = file;
    }
}