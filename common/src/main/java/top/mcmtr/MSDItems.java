package top.mcmtr;

import mtr.RegistryObject;
import net.minecraft.world.item.Item;
import top.mcmtr.data.CatenaryType;
import top.mcmtr.items.ItemCatenaryModifier;
import top.mcmtr.items.ItemModelChangeStick;
import top.mcmtr.items.ItemRigidCatenaryModifier;

public interface MSDItems {
    RegistryObject<Item> CATENARY_CONNECTOR = new RegistryObject<>(() -> new ItemCatenaryModifier(true, CatenaryType.CATENARY));
    RegistryObject<Item> ELECTRIC_CONNECTOR = new RegistryObject<>(() -> new ItemCatenaryModifier(true, CatenaryType.ELECTRIC));
    RegistryObject<Item> CATENARY_REMOVER = new RegistryObject<>(ItemCatenaryModifier::new);
    RegistryObject<Item> RIGID_CATENARY_CONNECTOR = new RegistryObject<>(() -> new ItemRigidCatenaryModifier(true, CatenaryType.RIGID_CATENARY));
    RegistryObject<Item> RIGID_CATENARY_REMOVER = new RegistryObject<>(ItemRigidCatenaryModifier::new);
    RegistryObject<Item> RIGID_SOFT_CATENARY_CONNECTOR = new RegistryObject<>(() -> new ItemCatenaryModifier(true, CatenaryType.RIGID_SOFT_CATENARY));
    RegistryObject<Item> MODEL_CHANGE_STICK = new RegistryObject<>(ItemModelChangeStick::new);
}