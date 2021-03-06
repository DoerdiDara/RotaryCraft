/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Items.Tools.Charged;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import Reika.RotaryCraft.Base.ItemChargedArmor;
import Reika.RotaryCraft.Items.Tools.Bedrock.ItemBedrockArmor;
import Reika.RotaryCraft.Registry.ItemRegistry;

public class ItemSpringBoots extends ItemChargedArmor {

	public final int JUMP_LEVEL = 3;
	public final int SPEED_LEVEL = 2;

	public ItemSpringBoots(int ID, EnumArmorMaterial mat, int tex, int render) {
		super(ID, mat, render, 3, tex);
	}

	@Override
	public int getItemSpriteIndex(ItemStack item) {
		ItemRegistry ir = ItemRegistry.getEntry(item);
		return ir != null ? ir.getTextureIndex() : 0;
	}

	@Override
	public boolean providesProtection() {
		return itemID == ItemRegistry.BEDJUMP.getShiftedID();
	}

	@Override
	public boolean canBeDamaged() {
		return false;
	}

	@Override
	public double getDamageMultiplier() {
		return itemID == ItemRegistry.BEDJUMP.getShiftedID() ? 0.35 : 1;
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer ep, ItemStack is) {
		if (is.itemID == ItemRegistry.BEDJUMP.getShiftedID() || is.getItemDamage() > 0) {
			PotionEffect pot = ep.getActivePotionEffect(Potion.jump);
			if (pot == null || pot.getAmplifier() < JUMP_LEVEL) {
				ep.addPotionEffect(new PotionEffect(Potion.jump.id, 1, JUMP_LEVEL));
			}
			pot = ep.getActivePotionEffect(Potion.moveSpeed);
			if (pot == null || pot.getAmplifier() < SPEED_LEVEL) {
				ep.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 1, SPEED_LEVEL));
			}
			if (itemRand.nextInt(160) == 0) {
				if (is.itemID != ItemRegistry.BEDJUMP.getShiftedID()) {
					ep.setCurrentItemOrArmor(1, new ItemStack(is.itemID, is.stackSize, is.getItemDamage()-1));
					this.warnCharge(is);
				}
			}
		}
	}

	@Override
	public void getSubItems(int id, CreativeTabs cr, List li) //Adds the metadata blocks to the creative inventory
	{
		ItemStack is = new ItemStack(id, 1, 24000);
		if (itemID == ItemRegistry.BEDJUMP.getShiftedID()) {
			Enchantment ench = ((ItemBedrockArmor)ItemRegistry.BEDBOOTS.getItemInstance()).getDefaultEnchantment();
			if (ench != null)
				is.addEnchantment(ench, 4);
		}
		ItemRegistry ir = ItemRegistry.getEntry(is);
		if (ir.isAvailableInCreativeInventory())
			li.add(is);
	}
}
