
package net.mcreator.ninearmory.item;

import net.minecraftforge.registries.ObjectHolder;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.SwordItem;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.IItemTier;

import net.mcreator.ninearmory.itemgroup.ArmoryItemGroup;
import net.mcreator.ninearmory.NineArmoryModElements;

@NineArmoryModElements.ModElement.Tag
public class BlazeItem extends NineArmoryModElements.ModElement {
	@ObjectHolder("nine_armory:blaze")
	public static final Item block = null;
	public BlazeItem(NineArmoryModElements instance) {
		super(instance, 1);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new SwordItem(new IItemTier() {
			public int getMaxUses() {
				return 100;
			}

			public float getEfficiency() {
				return 4f;
			}

			public float getAttackDamage() {
				return 2f;
			}

			public int getHarvestLevel() {
				return 1;
			}

			public int getEnchantability() {
				return 2;
			}

			public Ingredient getRepairMaterial() {
				return Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT, (int) (1)));
			}
		}, 3, -3f, new Item.Properties().group(ArmoryItemGroup.tab)) {
		}.setRegistryName("blaze"));
	}
}
