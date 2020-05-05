package pw.zeth.deathDrops;

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

	// Lore prefix that gets added to items before player's username
	private static final String DEATH_DROP_IDENTIFIER = ChatColor.BLUE + "Dropped on death by" + ChatColor.GOLD + ChatColor.BOLD;

	@Override
	public void onEnable() {
		// Register events baby!
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onPlayerDeath(final PlayerDeathEvent event) {

		// Get player's name
		final Player player = event.getEntity();

		// Ignore if keep inventory enabled
		if (event.getKeepInventory())
			return;

		// Store drops from player
		final List<ItemStack> drops = event.getDrops();
		if (drops.size() == 0)
			return;

		// Get every type of item in the drops
		for (ItemStack item : drops) {

			// Get meta
			final ItemMeta itemMeta = item.getItemMeta();
			final List<String> lore = new ArrayList<>();

			// Add existing lore if exists
			if (itemMeta.hasLore())
				lore.addAll(itemMeta.getLore());

			// Remove DEATH_DROP_IDENTIFIER lore if already exists to prevent duplicates
			lore.removeIf(line -> line.equals(DEATH_DROP_IDENTIFIER));

			// Add the new string
			lore.add(String.join(" ", DEATH_DROP_IDENTIFIER, player.getName()));

			// Store the modified lore to the item
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
		}

	}

	@EventHandler
	public void onEntityPickupItem(final EntityPickupItemEvent event) {

		// Check if player picked up an item, otherwise skip
		if (event.getEntity() instanceof Player) {

			// Get player username and the item they picked up
			final Player player = (Player) event.getEntity();
			final ItemStack item = event.getItem().getItemStack();

			// Skip check if item doesn't have meta or lore
			if (!item.hasItemMeta())
				return;
			if (!item.getItemMeta().hasLore())
				return;

			// Get the item's lore and remove it if it has DEATH_DROP_IDENTIFIER
			final List<String> lore = item.getItemMeta().getLore();
			if (lore.stream().anyMatch(line -> line.equals(DEATH_DROP_IDENTIFIER + " " + player.getName())))
				removeLoreEntry(item);
		}

	}

	@EventHandler
	public void onInventoryClick(final InventoryClickEvent event) {

		// Check if the item they clicked an item, otherwise skip
		if (event.getCurrentItem() == null)
			return;
		// Get the item clicked on
		final ItemStack item = event.getCurrentItem();

		// Skip if doesn't have meta or lore
		if (!item.hasItemMeta())
			return;
		if (!item.getItemMeta().hasLore())
			return;

		// Get the item's lore and remove it if it has DEATH_DROP_IDENTIFIER
		final List<String> lore = item.getItemMeta().getLore();
		if (lore.stream().anyMatch(line -> line.equals(DEATH_DROP_IDENTIFIER + " " + event.getWhoClicked())))
			removeLoreEntry(item);

	}

	private void removeLoreEntry(final ItemStack item) {

		// Confirm item isn't null, has meta, and has a lore.
		if (item == null)
			return;
		if (!item.hasItemMeta())
			return;
		if (!item.getItemMeta().hasLore())
			return;

		// Get the lore and store it
		final ItemMeta itemMeta = item.getItemMeta();
		final List<String> lore = itemMeta.getLore();

		// Remove DEATH_DROP_IDENTIFIER if it exists
		lore.removeIf(line -> line.startsWith(DEATH_DROP_IDENTIFIER));

		// Store the modified lore to the item
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);

	}

}
