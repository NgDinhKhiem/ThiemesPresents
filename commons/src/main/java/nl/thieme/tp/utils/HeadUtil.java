package nl.thieme.tp.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import nl.thieme.tp.ThiemesPresents;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HeadUtil {

    private static final String minecraft_head_url_base = "https://textures.minecraft.net/texture/";

    public static SkullMeta setHeadUrl(String endpoint, ItemMeta im) {
        ThiemesPresents.consoleFilter.toggle(true);
        String url = minecraft_head_url_base + endpoint;
        if (!(im instanceof SkullMeta)) return null;
        SkullMeta headMeta = (SkullMeta) im;

        GameProfile profile = new GameProfile(UUID.randomUUID(), endpoint);
        char[] encodedData = Base64Coder.encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
        } catch (Exception ignored) {
        }
        profileField.setAccessible(true);
        try {
            profileField.set(headMeta, profile);
        } catch (Exception ignored) {
        }
        return headMeta;
    }

    public static String itemStackToBase64(ItemStack items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(items);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static ItemStack itemStackFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack item = (ItemStack) dataInput.readObject();
            dataInput.close();
            return item;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public static ItemMeta addLore(ItemMeta im, String line) {
        List<String> lore = im.getLore();
        if(lore == null) lore = new ArrayList<>();
        lore.add(MsgUtil.replaceColors(line));
        im.setLore(lore);
        return im;
    }

    public static ItemMeta setLore(ItemMeta im, String line) {
        String[] lines = line.split("\\r?\\n");
        List<String> lore = new ArrayList<>();
        for (String s : lines) {
            lore.add(MsgUtil.replaceColors(s));
        }
        im.setLore(lore);
        return im;
    }
}
