/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.API.Event;

import net.minecraft.world.World;
import net.minecraftforge.event.Event;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;

public class RailgunImpactEvent extends Event {

	public final int blockX;
	public final int blockY;
	public final int blockZ;

	public final World world;

	public final int projectileMass;

	public RailgunImpactEvent(World world, int x, int y, int z, int tier) {
		this.world = world;
		blockX = x;
		blockY = y;
		blockZ = z;

		projectileMass = ReikaMathLibrary.intpow2(2, tier);
	}

}
