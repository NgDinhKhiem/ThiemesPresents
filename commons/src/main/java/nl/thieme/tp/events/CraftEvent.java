package nl.thieme.tp.events;

import nl.thieme.tp.ThiemesPresents;
import nl.thieme.tp.configs.MainConfig;
import nl.thieme.tp.configs.MessageConfig;
import nl.thieme.tp.utils.HeadUtil;
import nl.thieme.tp.utils.MsgUtil;
import nl.thieme.tp.utils.PresentUtil;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CraftEvent implements Listener {

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (!PresentUtil.isPresentItemStack(e.getCurrentItem())) return;
        Player p = (Player) e.getWhoClicked();
        List<String> disabledWorlds = MainConfig.ConfigKey.DISABLED_WORLDS.getStringList();
        if (disabledWorlds.contains(p.getWorld().getName())) {
            e.setCancelled(true);
            MsgUtil.sendMessage(p, MessageConfig.MessageKey.DISABLED_WORLD);
            return;
        }
        YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(new File(ThiemesPresents.INSTANCE.getDataFolder()+File.separator+"presents.yml"));
        } catch (IOException | InvalidConfigurationException ex) {
            throw new RuntimeException(ex);
        }
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = HeadUtil.setHeadUrl(PresentUtil.getPresentNBT(e.getCurrentItem()).openHead,item.getItemMeta());
        meta.setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName());
        item.setItemMeta(meta);
        item.setItemMeta(PresentUtil.setPresentMeta(item,PresentUtil.getPresentNBT(e.getCurrentItem())));
        e.setCurrentItem(item);
    }
}
