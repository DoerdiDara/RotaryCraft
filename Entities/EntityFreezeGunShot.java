/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Entities;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.RotaryCraft.Base.EntityTurretShot;
import Reika.RotaryCraft.Registry.MachineRegistry;
import Reika.RotaryCraft.TileEntities.Weaponry.TileEntityFreezeGun;

public class EntityFreezeGunShot extends EntityTurretShot {

	public EntityFreezeGunShot(World world) {
		super(world);
	}

	public EntityFreezeGunShot(World world, double x, double y, double z, double vx, double vy, double vz) {
		super(world, x, y, z, 0, 0, 0);
		motionX = vx;
		motionY = vy;
		motionZ = vz;
		if (!world.isRemote)
			velocityChanged = true;
	}

	@Override
	public void onImpact(MovingObjectPosition mov) {
		if (mov != null && MachineRegistry.getMachine(worldObj, mov.blockX, mov.blockY, mov.blockZ) == MachineRegistry.FREEZEGUN) {
			this.setDead();
			return;
		}
		if (isDead)
			return;
		World world = worldObj;
		double x = posX;
		double y = posY;
		double z = posZ;
		int x0 = (int)x;
		int y0 = (int)y;
		int z0 = (int)z;
		EntityLivingBase el;
		Entity ent;
		//ReikaChatHelper.writeCoords(world, x, y, z);
		//ReikaChatHelper.writeBlockAtCoords(world, x0, y0, z0);

		AxisAlignedBB splash = AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(2, 2, 2);
		//world.createExplosion(this, x, y, z, 3F, false);
		List dmgd = world.getEntitiesWithinAABB(Entity.class, splash);
		for (int l = 0; l < dmgd.size(); l++) {
			ent = (Entity)dmgd.get(l);
			if (ent instanceof EntityLivingBase) {
				el = (EntityLivingBase)ent;
				//ReikaChatHelper.writeEntity(world, el);
				this.applyAttackEffectsToEntity(world, el);
				//if (el != null) {
				//	if (frozen != null)
				//		frozen.add(el);
				//	EntityIceBlock ice = new EntityIceBlock(world, el);
				//	world.spawnEntityInWorld(ice);
				//}
			}
		}

		this.setDead();
		//ent.attackEntityFrom(DamageSource.outOfWorld, el.getHealth()*(1+el.getTotalArmorValue()));
	}

	@Override
	protected void applyAttackEffectsToEntity(World world, EntityLivingBase el) {/*
		if (el instanceof EntityDragon) {
			el.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 60000, 27));
			el.addPotionEffect(new PotionEffect(Potion.jump.id, 60000, -29));
		}
		else if (el instanceof EntityWither) {
			el.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 60000, 27));
			el.addPotionEffect(new PotionEffect(Potion.jump.id, 60000, -29));
		}
		else {
			el.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 60000, 7));
			el.addPotionEffect(new PotionEffect(Potion.jump.id, 60000, -9));
		}*/
		el.addPotionEffect(TileEntityFreezeGun.getFreezeEffect(60000));
	}

	@Override
	public void onUpdate() {
		ticksExisted++;
		boolean hit = false;
		int id = worldObj.getBlockId((int)posX, (int)posY, (int)posZ);
		MachineRegistry m = MachineRegistry.getMachine(worldObj, posX, posY, posZ);
		List mobs = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getBoundingBox().expand(1, 1, 1));
		//ReikaJavaLibrary.pConsole("ID: "+id+" and "+mobs.size()+" mobs");
		hit = (mobs.size() > 0 || (m != MachineRegistry.FREEZEGUN && id != 0 && !ReikaWorldHelper.softBlocks(id)));
		//ReikaJavaLibrary.pConsole(hit+"   by "+id+"  or mobs "+mobs.size());
		if (hit) {
			//ReikaChatHelper.write("HIT  @  "+ticksExisted+"  by "+(mobs.size() > 0));
			this.onImpact(null);
			return;
		}
		//ReikaChatHelper.write(this.ticksExisted);
		//worldObj.spawnParticle("hugeexplosion", posX, posY, posZ, 0, 0, 0);
		if (!worldObj.isRemote && (shootingEntity != null && shootingEntity.isDead || !worldObj.blockExists((int)posX, (int)posY, (int)posZ)))
			this.setDead();
		else {
			if (ticksExisted > 80 && !worldObj.isRemote)
				this.onImpact(null);
			this.onEntityUpdate();
			Vec3 var15 = worldObj.getWorldVec3Pool().getVecFromPool(posX, posY, posZ);
			Vec3 var2 = worldObj.getWorldVec3Pool().getVecFromPool(posX + motionX, posY + motionY, posZ + motionZ);
			MovingObjectPosition var3 = worldObj.clip(var15, var2);
			var15 = worldObj.getWorldVec3Pool().getVecFromPool(posX, posY, posZ);
			var2 = worldObj.getWorldVec3Pool().getVecFromPool(posX + motionX, posY + motionY, posZ + motionZ);
			if (var3 != null)
				var2 = worldObj.getWorldVec3Pool().getVecFromPool(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
			Entity var4 = null;
			List var5 = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
			double var6 = 0.0D;
			for (int var8 = 0; var8 < var5.size(); ++var8) {
				Entity var9 = (Entity)var5.get(var8);
				if (var9.canBeCollidedWith() && (!var9.isEntityEqual(shootingEntity))) {
					float var10 = 0.3F;
					AxisAlignedBB var11 = var9.boundingBox.expand(var10, var10, var10);
					MovingObjectPosition var12 = var11.calculateIntercept(var15, var2);
					if (var12 != null) {
						double var13 = var15.distanceTo(var12.hitVec);
						if (var13 < var6 || var6 == 0.0D) {
							var4 = var9;
							var6 = var13;
						}
					}
				}
			}
			if (var4 != null)
				var3 = new MovingObjectPosition(var4);
			if (var3 != null)
				this.onImpact(var3);
			posX += motionX;
			posY += motionY;
			posZ += motionZ;
			if (this.isInWater()) {
				this.setDead();
			}
			this.setPosition(posX, posY, posZ);
		}
	}

	@Override
	public int getAttackDamage() {
		return 0;
	}
}
