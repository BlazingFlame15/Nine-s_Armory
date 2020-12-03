package net.mcreator.ninearmory.procedures;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;

import net.mcreator.ninearmory.NineArmoryModElements;

import java.util.Map;
import java.util.HashMap;

@NineArmoryModElements.ModElement.Tag
public class BananashootProcedure extends NineArmoryModElements.ModElement {
	public BananashootProcedure(NineArmoryModElements instance) {
		super(instance, 3);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				System.err.println("Failed to load dependency world for procedure Bananashoot!");
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		world.setBlockState(new BlockPos((int) (-166), (int) 69, (int) 310), Blocks.OAK_LOG.getDefaultState(), 3);
	}

	@SubscribeEvent
	public void onPickup(EntityItemPickupEvent event) {
		PlayerEntity entity = event.getEntityPlayer();
		ItemStack itemstack = event.getItem().getItem();
		double i = entity.posX;
		double j = entity.posY;
		double k = entity.posZ;
		World world = entity.world;
		Map<String, Object> dependencies = new HashMap<>();
		dependencies.put("x", i);
		dependencies.put("y", j);
		dependencies.put("z", k);
		dependencies.put("world", world);
		dependencies.put("entity", entity);
		dependencies.put("itemstack", itemstack);
		dependencies.put("event", event);
		this.executeProcedure(dependencies);
	}
}
