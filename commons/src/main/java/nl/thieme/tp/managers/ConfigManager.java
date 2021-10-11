package nl.thieme.tp.managers;

import nl.thieme.tp.ThiemesPresents;
import nl.thieme.tp.configs.MainConfig;
import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.configs.PresentConfig;

public class ConfigManager {

    private static PresentConfig presentConfig;
    private static MessageConfig messageConfig;
    private static MainConfig mainConfig;

    public ConfigManager() {
        mainConfig = new MainConfig("config");
        presentConfig = new PresentConfig("presents");
        messageConfig = new MessageConfig("messages");
    }

    public PresentConfig getPresentConfig() {
        return presentConfig;
    }

    public MessageConfig getMsgConfig() {
        return messageConfig;
    }

    public MainConfig getConfig() {
        return mainConfig;
    }

    public void reloadAll() {
        ThiemesPresents.INSTANCE.onDisable();
        ThiemesPresents.INSTANCE.onEnable();
    }


}
