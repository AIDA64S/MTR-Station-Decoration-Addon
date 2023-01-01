package top.mcmtr;

import mtr.RegistryObject;
import net.minecraft.world.item.Item;
import top.mcmtr.data.CatenaryType;
import top.mcmtr.items.ItemCatenaryModifier;

public interface MSDItems {
    RegistryObject<Item> CATENARY_CONNECTOR = new RegistryObject<>(() -> new ItemCatenaryModifier(true, CatenaryType.CATENARY));
    RegistryObject<Item> ELECTRIC_CONNECTOR = new RegistryObject<>(() -> new ItemCatenaryModifier(true, CatenaryType.ELECTRIC));
    RegistryObject<Item> CATENARY_REMOVER = new RegistryObject<>(ItemCatenaryModifier::new);
}