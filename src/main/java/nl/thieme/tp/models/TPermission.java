package nl.thieme.tp.models;

import nl.thieme.tp.Main;
import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.utils.MsgUtil;
import org.bukkit.command.CommandSender;

public enum TPermission {
    NP_SIGN(MessageConfig.MessageKey.NP_SIGN, "use.sign"),
    NP_WRAP(MessageConfig.MessageKey.NP_WRAP, "use.wrap"),
    NP_OPEN(MessageConfig.MessageKey.NP_OPEN, "use.open"),
    NP_CMD_RELOAD(MessageConfig.MessageKey.NP_CMD, "admin.reload"),
    NP_CMD_INSIDE(MessageConfig.MessageKey.NP_CMD, "admin.lookinside");

    private MessageConfig.MessageKey key;
    private String node;

    TPermission(MessageConfig.MessageKey key, String node) {
        this.key = key;
        this.node = Main.pluginId + "." + node;
    }

    public String getPermission() {
        return node;
    }

    public MessageConfig.MessageKey getKey() {
        return key;
    }

    public static boolean hasPermission(CommandSender p, TPermission tp) {
        if(tp == null) return true;
        if(p.hasPermission(tp.getPermission())) return true;
        MsgUtil.sendMessage(p, tp.key);
        return false;
    }
}
