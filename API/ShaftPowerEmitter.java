/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.API;

public interface ShaftPowerEmitter extends ShaftMachine, PowerGenerator {

	/** x,y,z to write to */
	public boolean canWriteToBlock(int x, int y, int z);

	/** Whether your machine is emitting power right now */
	public boolean isEmitting();

}
