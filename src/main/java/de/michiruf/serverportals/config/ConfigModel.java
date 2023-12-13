package de.michiruf.serverportals.config;

import io.wispforest.owo.config.annotation.Config;

import java.util.List;

/**
 * @author Michael Ruf
 * @since 2022-12-02
 */
@Config(name = "server-portals", wrapperName = "Config")
@SuppressWarnings("unused")
public class ConfigModel {

    public int logLevel; // TODO Use this
    public List<PortalRegistrationData> portals;
}
