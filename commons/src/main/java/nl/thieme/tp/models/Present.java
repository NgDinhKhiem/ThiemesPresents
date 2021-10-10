package nl.thieme.tp.models;

import nl.thieme.tp.Main;
import nl.thieme.tp.utils.MsgUtil;
import nl.thieme.tp.utils.PresentUtil;
import nl.thieme.tp.utils.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static nl.thieme.tp.utils.HeadUtil.setHeadUrl;

public class Present extends ItemStack {

    private final String name;
    private final NamespacedKey namespacedKey;
    private final PresentNBT presentNBT;
    private ShapedRecipe recipe;

    public Present(String name, ItemStack stack) {
        super(stack);
        this.name = name.toLowerCase();
        namespacedKey = NamespacedKey.minecraft(this.name);
        presentNBT = new PresentNBT();
        updatePresentNBT();
    }

    public void setRecipe(String[] shape, HashMap<Character, String> ingredients) {
        recipe = new ShapedRecipe(namespacedKey, this);
        recipe.shape(shape);
        for (Character key : ingredients.keySet()) {
            String ing = ingredients.get(key);
            Material m = Material.getMaterial(ing);
            if (m == null) {
                Main.LOGGER.warning(ing + " IS NOT A VALID MATERIAL! RECIPE DISABLED");
                return;
            }
            recipe.setIngredient(key, m);
        }

        Bukkit.addRecipe(recipe);
    }

    public void removeRecipe() {
        Main.WRAPPER.removeRecipe(this, namespacedKey);
    }

    public void setClosedHeadUrl(String endpoint) {
        presentNBT.closed_head = endpoint;
        updatePresentNBT();
        setItemMeta(setHeadUrl(endpoint, getItemMeta()));
    }

    public void setDisplayName(String name) {
        ItemMeta im = getItemMeta();
        im.setDisplayName(name);
        setItemMeta(im);
    }

    public void setLore(List<String> list) {
        ItemMeta im = getItemMeta();
        List<String> result = new ArrayList<>();
        for (String s : list) {
            result.add(MsgUtil.replaceColors(s));
        }
        im.setLore(result);
        setItemMeta(im);
    }

    public String getName() {
        return name;
    }

    private void updatePresentNBT() {
        ItemMeta im = getItemMeta();
        NBTEditor.set(this,PresentUtil.presentNBTToString(presentNBT),  PresentUtil.presentNBTKey);
        setItemMeta(im);
    }

    public void setOpenHeadUrl(String endpoint) {
        presentNBT.open_head = endpoint; // open is optional
        updatePresentNBT();
        setItemMeta(setHeadUrl(endpoint, getItemMeta()));
    }


}