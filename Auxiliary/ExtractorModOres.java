/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2013
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Auxiliary;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import Reika.DragonAPI.Auxiliary.ModOreList;
import Reika.DragonAPI.Libraries.ReikaItemHelper;
import Reika.DragonAPI.Libraries.ReikaJavaLibrary;
import Reika.RotaryCraft.RotaryCraft;

public class ExtractorModOres {

	public static void registerRCIngots() {
		for (int i = 0; i < ModOreList.oreList.length; i++) {
			String[] ingots = ModOreList.oreList[i].getOreDictIngots();
			for (int j = 0; j < ingots.length; j++) {
				OreDictionary.registerOre(ingots[j], new ItemStack(RotaryCraft.modingots.itemID, 1, i));
			}
		}
		OreDictionary.registerOre("ingotLead", getSmeltedIngot(ModOreList.GALENA));
		OreDictionary.registerOre("ingotSilver", getSmeltedIngot(ModOreList.GALENA));
	}

	public static void addSmelting() {
		for (int i = 0; i < ModOreList.oreList.length; i++) {
			ReikaJavaLibrary.pConsole("Adding smelting for "+new ItemStack(RotaryCraft.modextracts.itemID, 1, getFlakesIndex(ModOreList.oreList[i]))+" to "+ReikaItemHelper.getSizedItemStack(getSmeltedIngot(ModOreList.oreList[i]), ModOreList.oreList[i].getDropCount()));
			FurnaceRecipes.smelting().addSmelting(RotaryCraft.modextracts.itemID, getFlakesIndex(ModOreList.oreList[i]), ReikaItemHelper.getSizedItemStack(getSmeltedIngot(ModOreList.oreList[i]), ModOreList.oreList[i].getDropCount()), 0.7F);
		}
	}

	public static boolean isModOreIngredient(ItemStack is) {
		if (is == null)
			return false;
		if (is.itemID != RotaryCraft.modextracts.itemID)
			return false;
		return ModOreList.getEntryFromDamage(is.getItemDamage()/4) != null;
	}

	public static int getStageFromMetadata(ItemStack is) {
		if (ModOreList.isModOre(is))
			return -1;
		return is.getItemDamage()%4;
	}

	public static int getDustIndex(ModOreList ore) {
		return ore.ordinal()*4;
	}

	public static int getSlurryIndex(ModOreList ore) {
		return getDustIndex(ore)+1;
	}

	public static int getSolutionIndex(ModOreList ore) {
		return getDustIndex(ore)+2;
	}

	public static int getFlakesIndex(ModOreList ore) {
		return getDustIndex(ore)+3;
	}

	public static boolean isDust(ModOreList ore, int index) {
		return index == getDustIndex(ore);
	}

	public static boolean isSlurry(ModOreList ore, int index) {
		return index == getSlurryIndex(ore);
	}

	public static boolean isSolution(ModOreList ore, int index) {
		return index == getSolutionIndex(ore);
	}

	public static boolean isFlakes(ModOreList ore, int index) {
		return index == getFlakesIndex(ore);
	}

	public static ItemStack getDustProduct(ModOreList ore) {
		return new ItemStack(RotaryCraft.modextracts.itemID, 1, getDustIndex(ore));
	}

	public static ItemStack getSlurryProduct(ModOreList ore) {
		return new ItemStack(RotaryCraft.modextracts.itemID, 1, getSlurryIndex(ore));
	}

	public static ItemStack getSolutionProduct(ModOreList ore) {
		return new ItemStack(RotaryCraft.modextracts.itemID, 1, getSolutionIndex(ore));
	}

	public static ItemStack getFlakeProduct(ModOreList ore) {
		return new ItemStack(RotaryCraft.modextracts.itemID, 1, getFlakesIndex(ore));
	}

	public static ItemStack getSmeltedIngot(ModOreList ore) {
		return new ItemStack(RotaryCraft.modingots.itemID, 1, ore.ordinal());
	}
}