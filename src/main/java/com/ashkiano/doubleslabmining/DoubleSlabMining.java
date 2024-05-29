package com.ashkiano.doubleslabmining;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class DoubleSlabMining extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Metrics metrics = new Metrics(this, 21921);
        this.getLogger().info("Thank you for using the DoubleSlabMining plugin! If you enjoy using this plugin, please consider making a donation to support the development. You can donate at: https://donate.ashkiano.com");
    }

    @EventHandler
    public void on(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        if (!event.getPlayer().hasPermission("slabbreak.use")) return;
        if (isDoubleSlab(event.getBlock())) {
            event.setCancelled(true);

            Vector direction = event.getPlayer().getLocation().getDirection();
            Location location = event.getPlayer().getEyeLocation();
            Block block = event.getBlock();

            for (double d = 1; d < 16; d += 0.06) {
                Location checkLocation = location.add(direction.clone().multiply(d));
                if (checkLocation.getBlock().equals(block)) {
                    double blockY = checkLocation.getY() - Math.floor(checkLocation.getY());
                    Slab.Type type = blockY > 0.5 ? Slab.Type.BOTTOM : Slab.Type.TOP;

                    Slab slab = (Slab) block.getBlockData();
                    slab.setType(type);
                    block.setBlockData(slab, true);

                    if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(block.getType(), 1));
                    }
                    return;
                }
            }
        }
    }

    boolean isDoubleSlab(Block block) {
        if (block == null) return false;
        BlockData data = block.getBlockData();
        if (data instanceof Slab) {
            Slab slab = (Slab) data;
            return slab.getType() == Slab.Type.DOUBLE;
        }
        return false;
    }
}
