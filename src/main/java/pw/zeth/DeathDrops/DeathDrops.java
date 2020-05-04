package pw.zeth.DeathDrops;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class DeathDrops extends JavaPlugin implements Listener {

    private static final String DEATH_DROP_IDENTIFIER = ChatColor.BLUE + "Dropped on death by";

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(this, this);

    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {

        final Player player = event.getEntity();
        if (event.getKeepInventory()) return;
        final List<ItemStack> drops = event.getDrops();
        if (drops.size() == 0) return;

        for (ItemStack item : drops) {
            final ItemMeta itemMeta = item.getItemMeta();
            final List<String> lore = new ArrayList<>();
            if (itemMeta.hasLore()) lore.addAll(itemMeta.getLore());
            lore.removeIf(line -> line.equals(DEATH_DROP_IDENTIFIER));
            lore.add(String.join(" ", DEATH_DROP_IDENTIFIER, player.getName()));
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
        }

    }

    @EventHandler
    public void onEntityPickupItem(final EntityPickupItemEvent event) {

        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            final ItemStack item = event.getItem().getItemStack();

            if (!item.hasItemMeta()) return;
            if (!item.getItemMeta().hasLore()) return;

            final List<String> lore = item.getItemMeta().getLore();
            if (lore.stream().anyMatch(line -> line.equals(DEATH_DROP_IDENTIFIER + " " + player.getName())))
                removeLoreEntry(item);
        }

    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {

        if (event.getCurrentItem() == null) return;
        final ItemStack item = event.getCurrentItem();

        if (!item.hasItemMeta()) return;
        if (!item.getItemMeta().hasLore()) return;

        final List<String> lore = item.getItemMeta().getLore();
        if (lore.stream().anyMatch(line -> line.startsWith(DEATH_DROP_IDENTIFIER)))
            removeLoreEntry(item);

    }

    private void removeLoreEntry(final ItemStack item) {

        if (item == null) return;
        if (!item.hasItemMeta()) return;
        if (!item.getItemMeta().hasLore()) return;

        final ItemMeta itemMeta = item.getItemMeta();
        final List<String> lore = itemMeta.getLore();
        lore.removeIf(line -> line.startsWith(DEATH_DROP_IDENTIFIER));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

    }

}
