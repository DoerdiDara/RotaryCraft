/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.TileEntities.Transmission;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.RotaryCraft.API.ShaftPowerEmitter;
import Reika.RotaryCraft.Auxiliary.Interfaces.SimpleProvider;
import Reika.RotaryCraft.Base.TileEntity.TileEntity1DTransmitter;
import Reika.RotaryCraft.Registry.MachineRegistry;

public class TileEntityMonitor extends TileEntity1DTransmitter {

	@Override
	public void updateEntity(World world, int x, int y, int z, int meta) {
		super.updateTileEntity();
		power = (long)omega*(long)torque;
		this.getIOSides(world, x, y, z, meta, false);
		this.transferPower(world, x, y, z, meta);

		this.basicPowerReceiver();
	}

	@Override
	protected void transferPower(World world, int x, int y, int z, int meta) {
		omegain = torquein = 0;
		MachineRegistry m = this.getMachine(read);
		TileEntity te = this.getAdjacentTileEntity(read);
		if (this.isProvider(te)) {
			if (m == MachineRegistry.SHAFT) {
				TileEntityShaft devicein = (TileEntityShaft)te;
				if (devicein.isCross()) {
					this.readFromCross(devicein);
					return;
				}
				if (devicein.isWritingTo(this)) {
					torquein = devicein.torque;
					omegain = devicein.omega;
				}
			}
			if (m == MachineRegistry.POWERBUS) {
				TileEntityPowerBus pwr = (TileEntityPowerBus)te;
				ForgeDirection dir = this.getInputForgeDirection().getOpposite();
				omegain = pwr.getSpeedToSide(dir);
				torquein = pwr.getTorqueToSide(dir);
			}
			if (te instanceof SimpleProvider) {
				this.copyStandardPower(te);
			}
			if (te instanceof ShaftPowerEmitter) {
				ShaftPowerEmitter sp = (ShaftPowerEmitter)te;
				if (sp.isEmitting() && sp.canWriteToBlock(xCoord, yCoord, zCoord)) {
					torquein = sp.getTorque();
					omegain = sp.getOmega();
				}
			}
			if (m == MachineRegistry.SPLITTER) {
				TileEntitySplitter devicein = (TileEntitySplitter)te;
				if (devicein.isSplitting()) {
					this.readFromSplitter(devicein);
					return;
				}
				else if (devicein.isWritingTo(this)) {
					torquein = devicein.torque;
					omegain = devicein.omega;
				}
			}
			omega = omegain;
			torque = torquein;
		}
		else {
			omega = torque = 0;
		}
	}

	@Override
	public boolean hasModelTransparency() {
		return false;
	}

	@Override
	protected void animateWithTick(World world, int x, int y, int z) {
		if (!this.isInWorld()) {
			phi = 0;
			return;
		}
		phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega+1, 2), 1.25);
	}

	@Override
	public MachineRegistry getMachine() {
		return MachineRegistry.DYNAMOMETER;
	}

	@Override
	public int getRedstoneOverride() {
		return 0;
	}
}
