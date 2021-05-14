package io.github.blitzbeule.hungergames.config;

public interface LocalizationProvider {

    String getVariable(String name, String ... arg);

}
