package org.kilka.waterFix;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class WaterFix extends JavaPlugin implements Listener {

    private boolean isEnabled = true;
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        if (!isEnabled) return;
        if (isConcrete(event.getNewState().getType())) {
            event.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("waterfix")) {
            if (!sender.hasPermission("waterfix.toggle")) {
                sender.sendMessage("§cУ вас нет прав на использование этой команды!");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage("§6Текущий статус WaterFix: " + (isEnabled ? "§aВключен" : "§cВыключен"));
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "on":
                    isEnabled = true;
                    sender.sendMessage("§aWaterFix включен!");
                    break;
                case "off":
                    isEnabled = false;
                    sender.sendMessage("§cWaterFix выключен!");
                    break;
                default:
                    sender.sendMessage("§6Использование: /waterfix [on|off]");
                    break;
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onWaterFlow(BlockFromToEvent event) {
        if (!isEnabled) return;
        Block toBlock = event.getToBlock();
        Material toType = toBlock.getType();

        if ((isConcretePowder(toType) || isTorch(toType)) && isWater(event.getBlock().getType())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (!isEnabled) return;
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