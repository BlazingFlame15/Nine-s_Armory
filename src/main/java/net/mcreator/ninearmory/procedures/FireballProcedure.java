package net.mcreator.ninearmory.procedures;

import net.minecraft.entity.Entity;

import net.mcreator.ninearmory.NineArmoryModElements;

import java.util.Map;

@NineArmoryModElements.ModElement.Tag
public class FireballProcedure extends NineArmoryModElements.ModElement {
	public FireballProcedure(NineArmoryModElements instance) {
		super(instance, 4);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				System.err.println("Failed to load dependency entity for procedure Fireball!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		{
			Entity _ent = entity;
			if (!_ent.world.isRemote && _ent.world.getServer() != null) {
				_ent.world.getServer().getCommandManager().handleCommand(_ent.getCommandSource().withFeedbackDisabled().withPermissionLevel(4),
						"/summon Fireball ~ ~2 ~ {ExplosionPower:2,direction:[0.0,0.0,0.0]}");
			}
		}
	}
}
