package de.michiruf.serverportals.config;

import blue.endless.jankson.Jankson;
import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.util.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Config extends ConfigWrapper<de.michiruf.serverportals.config.ConfigModel> {

    public final Keys keys = new Keys();

    private final Option<java.lang.Integer> logLevel = this.optionForKey(this.keys.logLevel);
    private final Option<java.util.List<de.michiruf.serverportals.config.PortalRegistrationData>> portals = this.optionForKey(this.keys.portals);

    private Config() {
        super(de.michiruf.serverportals.config.ConfigModel.class);
    }

    private Config(Consumer<Jankson.Builder> janksonBuilder) {
        super(de.michiruf.serverportals.config.ConfigModel.class, janksonBuilder);
    }

    public static Config createAndLoad() {
        var wrapper = new Config();
        wrapper.load();
        return wrapper;
    }

    public static Config createAndLoad(Consumer<Jankson.Builder> janksonBuilder) {
        var wrapper = new Config(janksonBuilder);
        wrapper.load();
        return wrapper;
    }

    public int logLevel() {
        return logLevel.value();
    }

    public void logLevel(int value) {
        logLevel.set(value);
    }

    public java.util.List<de.michiruf.serverportals.config.PortalRegistrationData> portals() {
        return portals.value();
    }

    public void portals(java.util.List<de.michiruf.serverportals.config.PortalRegistrationData> value) {
        portals.set(value);
    }


    public static class Keys {
        public final Option.Key logLevel = new Option.Key("logLevel");
        public final Option.Key portals = new Option.Key("portals");
    }
}

