package net.ragnaroknetwork.ragnarokfalldamage;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class RagnarokFallDamage extends JavaPlugin implements Listener {

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {
        this.getLogger().info("[RagnarokBowBoosting] Disabling plugin.");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        EntityPlayer entityPlayer = (EntityPlayer) event.getEntity();
        double fallDistance = player.getFallDistance();
        double damage = fallDistance/2;
        double health = player.getHealth();
        double absorption = entityPlayer.getAbsorptionHearts();
        double level = 0;

        ItemStack boots = player.getInventory().getBoots();
        if (boots != null) {
            level = player.getInventory().getBoots().getEnchantmentLevel(Enchantment.PROTECTION_FALL);
            if (level > 0) damage = damage/(1 + level * 0.2);
        }

        double leftOver = damage - absorption;

        event.setDamage(0);

        if (absorption > 0) {
            if (leftOver > 0) entityPlayer.setAbsorptionHearts(0);
            else entityPlayer.setAbsorptionHearts((float) absorption - (float) damage);
        }

        if (leftOver > 0) player.setHealth(health - leftOver);
    }
}

