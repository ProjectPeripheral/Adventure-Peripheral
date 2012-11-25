package peripheral.common;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import dan200.computer.api.IComputerAccess;
import net.minecraft.src.World;

public class BlockPeripheralBlock extends BlockContainer
{
		public static EntityPlayer entityLiving;
	
        public BlockPeripheralBlock(int id, int texture)
        {
                super(id, texture, Material.cloth);
                this.setCreativeTab(CreativeTabs.tabBlock);
        }

		@Override
		public TileEntity createNewTileEntity(World var1) {
			return new TileEntityPeripheral();
		}
		
		public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
			entityLiving = player;
			return true;
		}
		
		
		public boolean hasTileEntity(int metadata) {
			return true;
		}

}