
package net.mcreator.ninearmory.itemgroup;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;

import net.mcreator.ninearmory.item.BlazeItem;
import net.mcreator.ninearmory.NineArmoryModElements;

@NineArmoryModElements.ModElement.Tag
public class ArmoryItemGroup extends NineArmoryModElements.ModElement {
	public ArmoryItemGroup(NineArmoryModElements instance) {
		super(instance, 6);
	}

	@Override
	public void initElements() {
		tab = new ItemGroup("tabarmory") {
			@OnlyIn(Dist.CLIENT)
			@Override
			public ItemStack createIcon() {
				return new ItemStack(BlazeItem.block, (int) (1));
			}

			@OnlyIn(Dist.CLIENT)
			public boolean hasSearchBar() {
				return true;
			}
		}.setBackgroundImageName("item_search.png");
	}
	public static ItemGroup tab;
}
