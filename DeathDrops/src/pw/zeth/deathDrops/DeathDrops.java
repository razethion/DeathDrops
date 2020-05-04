package pw.zeth.deathDrops;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public class DeathDrops extends JavaPlugin implements Listener {
	Logger log = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	// Check if a player dies
	@EventHandler
	public void onEntityDeath(PlayerDeathEvent entity) {
		if (entity.getEntity() instanceof Player) {

			// Identify the player
			Player player = (Player) entity.getEntity();
			String playerName = player.getName();

			// Get the items the player dropped on death
			for (ItemStack i : entity.getDrops()) {

				ItemMeta im = i.getItemMeta();
				ArrayList<String> lore = new ArrayList<String>();

				// If the item already has lore, add it to the new lore first.
				if (i.getItemMeta().hasLore()) {
					lore.addAll(i.getItemMeta().getLore());

					// Check if item has "dropped on death by" lore already
					if (i.getItemMeta().getLore().toString().contains("Dropped on death by")) {

						// It does, so check if it was dropped by the same player
						if (i.getItemMeta().getLore().toString().contains("Dropped on death by " + playerName + ".")) {

							// Do nothing, the lore was dropped by the same player

						} else {

							// The lore has changed to a new player
							int lineNo = 0;

							// Remove the instances of the old player's name
							for (String line : im.getLore()) {
								lineNo = lineNo + 1;
								if (line.contains("Dropped on death by")) {
									lore.remove(lineNo - 1);
									i.getItemMeta().getLore().remove(lineNo - 1);
								} else {
									continue;
								}
							}

							// Add the new user's lore
							lore.add(ChatColor.BLUE + "Dropped on death by " + playerName + ".");

							// Set the lore to the item
							im.setLore(lore);
							i.setItemMeta(im);

						}

					} else {

						// There was no existing 'last dropped by' lore, we can remove it
						lore.add(ChatColor.BLUE + "Dropped on death by " + playerName + ".");

						// Set the lore to the item
						im.setLore(lore);
						i.setItemMeta(im);

					}

				} else {

					// There was no existing lore, so we can just add the lore.
					lore.add(ChatColor.BLUE + "Dropped on death by " + playerName + ".");

					// Set the lore to the item
					im.setLore(lore);
					i.setItemMeta(im);

				}

			}

		}

	}

	// Check if a player picks up an item
	@EventHandler
	public void onPlayerPickup(EntityPickupItemEvent entity) {

		if (entity.getEntity() instanceof Player) {

			// Identify the player
			Player player = (Player) entity.getEntity();
			String playerName = player.getName();

			// Get the item
			ItemStack i = entity.getItem().getItemStack();

			// Get the item meta
			ItemMeta im = i.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();

			// Check if the item has lore
			if (i.getItemMeta().hasLore()) {

				// It does, so store it
				lore.addAll(i.getItemMeta().getLore());

				// Check if item has "dropped on death by" lore already
				if (i.getItemMeta().getLore().toString().contains("Dropped on death by")) {

					// It does, so check if it was dropped by the same player
					if (i.getItemMeta().getLore().toString().contains("Dropped on death by " + playerName + ".")) {

						// It was, so find what line it's on and remove it.
						int lineNo = 0;

						// Remove the instances of the old player's name
						for (String line : im.getLore()) {
							lineNo = lineNo + 1;
							if (line.contains("Dropped on death by")) {
								lore.remove(lineNo - 1);
								i.getItemMeta().getLore().remove(lineNo - 1);
							} else {
								continue;
							}
						}

						// Set the lore to the item
						im.setLore(lore);
						i.setItemMeta(im);

					} // It wasn't, ignore it.

				} // It doesn't, ignore it.

			} // Do nothing

		} // entity is not a player

	}

	// Check if a player takes the item from a container
	@EventHandler
	public void onPlayerInventoryMove(InventoryClickEvent entity) {

		// Identify the player
		Player player = (Player) entity.getWhoClicked();
		String playerName = player.getName();

		// Get the item
		ItemStack i = entity.getCurrentItem();

		if (entity.getCurrentItem() != null && entity.getCurrentItem().getType() != Material.AIR ) {

			// Get the item meta
			ItemMeta im = i.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();

			// Check if the item has lore
			if (i.getItemMeta().hasLore()) {

				// It does, so store it
				lore.addAll(i.getItemMeta().getLore());

				// Check if item has "dropped on death by" lore already
				if (i.getItemMeta().getLore().toString().contains("Dropped on death by")) {

					// It does, so check if it was dropped by the same player
					if (i.getItemMeta().getLore().toString().contains("Dropped on death by " + playerName + ".")) {

						// It was, so find what line it's on and remove it.
						int lineNo = 0;

						// Remove the instances of the old player's name
						for (String line : im.getLore()) {
							lineNo = lineNo + 1;
							if (line.contains("Dropped on death by")) {
								lore.remove(lineNo - 1);
								i.getItemMeta().getLore().remove(lineNo - 1);
							} else {
								continue;
							}
						}

						// Set the lore to the item
						im.setLore(lore);
						i.setItemMeta(im);

					} // It wasn't, ignore it.

				} // It doesn't, ignore it.

			} // Do nothing

		} //Do nothing

	}

}