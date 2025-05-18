package org.kilka.waterFix;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WaterFix extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        if (isConcrete(event.getNewState().getType())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWaterFlow(BlockFromToEvent event) {
        Block toBlock = event.getToBlock();
        Material toType = toBlock.getType();

        if ((isConcretePowder(toType) || isTorch(toType)) && isWater(event.getBlock().getType())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock) {
            FallingBlock fallingBlock = (FallingBlock) event.getEntity();
            if (isConcretePowder(fallingBlock.getMaterial())) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isConcrete(Material mat) {
        return mat.name().endsWith("_CONCRETE");
    }

    private boolean isConcretePowder(Material mat) {
        return mat.name().endsWith("_CONCRETE_POWDER");
    }

    private boolean isTorch(Material mat) {
        return mat == Material.TORCH ||
                mat == Material.WALL_TORCH ||
                mat == Material.REDSTONE_TORCH ||
                mat == Material.REDSTONE_WALL_TORCH ||
                mat == Material.REDSTONE_WIRE ||
                mat == Material.LEVER ||
                mat == Material.TRIPWIRE ||
                mat == Material.TRIPWIRE_HOOK ||
                mat == Material.STONE_BUTTON ||
                mat == Material.OAK_BUTTON ||
                mat == Material.COMPARATOR ||
                mat == Material.REPEATER ||
                mat == Material.LEGACY_REDSTONE_WIRE;

    }

    private boolean isWater(Material mat) {
        return mat == Material.WATER || mat == Material.BUBBLE_COLUMN;
    }
}