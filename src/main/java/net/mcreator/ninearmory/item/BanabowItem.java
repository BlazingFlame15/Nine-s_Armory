
package net.mcreator.ninearmory.item;

import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.World;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ActionResult;
import net.minecraft.network.IPacket;
import net.minecraft.item.UseAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.EntityRenderer;

import net.mcreator.ninearmory.itemgroup.ArmoryItemGroup;
import net.mcreator.ninearmory.NineArmoryModElements;

import java.util.Random;

import com.mojang.blaze3d.platform.GlStateManager;

import com.google.common.collect.Multimap;

@NineArmoryModElements.ModElement.Tag
public class BanabowItem extends NineArmoryModElements.ModElement {
	@ObjectHolder("nine_armory:banabow")
	public static final Item block = null;
	@ObjectHolder("nine_armory:entitybulletbanabow")
	public static final EntityType arrow = null;
	public BanabowItem(NineArmoryModElements instance) {
		super(instance, 4);
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemRanged());
		elements.entities.add(() -> (EntityType.Builder.<ArrowCustomEntity>create(ArrowCustomEntity::new, EntityClassification.MISC)
				.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(ArrowCustomEntity::new)
				.size(0.5f, 0.5f)).build("entitybulletbanabow").setRegistryName("entitybulletbanabow"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void init(FMLCommonSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(ArrowCustomEntity.class, renderManager -> new CustomRender(renderManager));
	}
	public static class ItemRanged extends Item {
		public ItemRanged() {
			super(new Item.Properties().group(ArmoryItemGroup.tab).maxDamage(100));
			setRegistryName("banabow");
		}

		@Override
		public UseAction getUseAction(ItemStack itemstack) {
			return UseAction.BOW;
		}

		@Override
		public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity entity, Hand hand) {
			entity.setActiveHand(hand);
			return new ActionResult(ActionResultType.SUCCESS, entity.getHeldItem(hand));
		}

		@Override
		public int getUseDuration(ItemStack itemstack) {
			return 72000;
		}

		@Override
		public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot) {
			Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot);
			if (slot == EquipmentSlotType.MAINHAND) {
				multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
						new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "ranged_item_damage", (double) 1, AttributeModifier.Operation.ADDITION));
				multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
						new AttributeModifier(ATTACK_SPEED_MODIFIER, "ranged_item_attack_speed", -2.4, AttributeModifier.Operation.ADDITION));
			}
			return multimap;
		}

		@Override
		public void onPlayerStoppedUsing(ItemStack itemstack, World world, LivingEntity entityLiving, int timeLeft) {
			if (!world.isRemote && entityLiving instanceof ServerPlayerEntity) {
				ServerPlayerEntity entity = (ServerPlayerEntity) entityLiving;
				double x = entity.posX;
				double y = entity.posY;
				double z = entity.posZ;
				if (true) {
					ArrowCustomEntity entityarrow = shoot(world, entity, random, 1f, 5, 5);
					itemstack.damageItem(1, entity, e -> e.sendBreakAnimation(entity.getActiveHand()));
					entityarrow.pickupStatus = AbstractArrowEntity.PickupStatus.DISALLOWED;
				}
			}
		}
	}

	@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
	public static class ArrowCustomEntity extends AbstractArrowEntity implements IRendersAsItem {
		public ArrowCustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			super(arrow, world);
		}

		public ArrowCustomEntity(EntityType<? extends ArrowCustomEntity> type, World world) {
			super(type, world);
		}

		public ArrowCustomEntity(EntityType<? extends ArrowCustomEntity> type, double x, double y, double z, World world) {
			super(type, x, y, z, world);
		}

		public ArrowCustomEntity(EntityType<? extends ArrowCustomEntity> type, LivingEntity entity, World world) {
			super(type, entity, world);
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack getItem() {
			return new ItemStack(BananaswordbananananananananaItem.block, (int) (1));
		}

		@Override
		protected ItemStack getArrowStack() {
			return null;
		}

		@Override
		protected void arrowHit(LivingEntity entity) {
			super.arrowHit(entity);
			entity.setArrowCountInEntity(entity.getArrowCountInEntity() - 1);
		}

		@Override
		public void tick() {
			super.tick();
			double x = this.posX;
			double y = this.posY;
			double z = this.posZ;
			World world = this.world;
			Entity entity = this.getShooter();
			if (this.inGround) {
				this.remove();
			}
		}
	}

	public static class CustomRender extends EntityRenderer<ArrowCustomEntity> {
		private static final ResourceLocation texture = new ResourceLocation("nine_armory:textures/banana.png");
		public CustomRender(EntityRendererManager renderManager) {
			super(renderManager);
		}

		@Override
		public void doRender(ArrowCustomEntity bullet, double d, double d1, double d2, float f, float f1) {
			this.bindEntityTexture(bullet);
			GlStateManager.pushMatrix();
			GlStateManager.translatef((float) d, (float) d1, (float) d2);
			GlStateManager.rotatef(f, 0, 1, 0);
			GlStateManager.rotatef(90f - bullet.prevRotationPitch - (bullet.rotationPitch - bullet.prevRotationPitch) * f1, 1, 0, 0);
			EntityModel model = new Modelbanana_arrow();
			model.render(bullet, 0, 0, 0, 0, 0, 0.0625f);
			GlStateManager.popMatrix();
		}

		@Override
		protected ResourceLocation getEntityTexture(ArrowCustomEntity entity) {
			return texture;
		}
	}

	// Made with Blockbench 3.7.4
	// Exported for Minecraft version 1.14
	// Paste this class into your mod and generate all required imports
	public static class Modelbanana_arrow extends EntityModel {
		private final RendererModel bb_main;
		public Modelbanana_arrow() {
			textureWidth = 16;
			textureHeight = 16;
			bb_main = new RendererModel(this);
			bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
			bb_main.cubeList.add(new ModelBox(bb_main, 4, 13, -2.0F, -1.0F, 0.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 6, 9, 0.0F, -1.0F, 0.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 5, 9, -3.0F, -1.0F, 0.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 6, 9, 1.0F, -1.0F, 0.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 6, 9, 1.0F, -2.0F, 0.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 7, 9, 2.0F, -2.0F, 0.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 12, 14, 2.0F, -3.0F, 0.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -1.0F, -1.0F, 0.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -4.0F, -1.0F, 0.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -4.0F, -2.0F, 0.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -5.0F, -2.0F, 0.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -6.0F, -2.0F, 0.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -6.0F, -3.0F, 0.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -1.0F, -1.0F, -1.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -2.0F, -1.0F, -1.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 0.0F, -1.0F, -1.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -3.0F, -1.0F, -1.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -4.0F, -1.0F, -1.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -4.0F, -2.0F, -1.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 1.0F, -1.0F, -1.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 1.0F, -2.0F, -1.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 2.0F, -2.0F, -1.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, 2.0F, -3.0F, -1.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -5.0F, -2.0F, -1.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -6.0F, -2.0F, -1.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -6.0F, -3.0F, -1.0F, 1, 1, 1, 0.0F, false));
			bb_main.cubeList.add(new ModelBox(bb_main, 12, 14, 3.0F, -3.0F, 0.0F, 1, 1, 1, 0.0F, true));
			bb_main.cubeList.add(new ModelBox(bb_main, 12, 14, 3.0F, -4.0F, 0.0F, 1, 1, 1, 0.0F, false));
		}

		@Override
		public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
			bb_main.render(f5);
		}

		public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x;
			modelRenderer.rotateAngleY = y;
			modelRenderer.rotateAngleZ = z;
		}

		public void setRotationAngles(Entity e, float f, float f1, float f2, float f3, float f4, float f5) {
			super.setRotationAngles(e, f, f1, f2, f3, f4, f5);
		}
	}
	public static ArrowCustomEntity shoot(World world, LivingEntity entity, Random random, float power, double damage, int knockback) {
		ArrowCustomEntity entityarrow = new ArrowCustomEntity(arrow, entity, world);
		entityarrow.shoot(entity.getLookVec().x, entity.getLookVec().y, entity.getLookVec().z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setIsCritical(false);
		entityarrow.setDamage(damage);
		entityarrow.setKnockbackStrength(knockback);
		world.addEntity(entityarrow);
		double x = entity.posX;
		double y = entity.posY;
		double z = entity.posZ;
		world.playSound((PlayerEntity) null, (double) x, (double) y, (double) z,
				(net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")),
				SoundCategory.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return entityarrow;
	}

	public static ArrowCustomEntity shoot(LivingEntity entity, LivingEntity target) {
		ArrowCustomEntity entityarrow = new ArrowCustomEntity(arrow, entity, entity.world);
		double d0 = target.posY + (double) target.getEyeHeight() - 1.1;
		double d1 = target.posX - entity.posX;
		double d3 = target.posZ - entity.posZ;
		entityarrow.shoot(d1, d0 - entityarrow.posY + (double) MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F, d3, 1f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setDamage(5);
		entityarrow.setKnockbackStrength(5);
		entityarrow.setIsCritical(false);
		entity.world.addEntity(entityarrow);
		double x = entity.posX;
		double y = entity.posY;
		double z = entity.posZ;
		entity.world.playSound((PlayerEntity) null, (double) x, (double) y, (double) z,
				(net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")),
				SoundCategory.PLAYERS, 1, 1f / (new Random().nextFloat() * 0.5f + 1));
		return entityarrow;
	}
}
