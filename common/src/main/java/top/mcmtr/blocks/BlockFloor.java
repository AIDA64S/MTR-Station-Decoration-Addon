package top.mcmtr.blocks;

import net.minecraft.world.level.material.Material;

public class BlockFloor extends BlockChangeModelBase{
    public BlockFloor() {
        super(1, Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(2.0F));
    }
}