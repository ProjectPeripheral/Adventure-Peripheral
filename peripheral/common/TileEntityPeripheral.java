package peripheral.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.text.html.parser.Entity;

import com.jcraft.jorbis.Block;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityLargeFireball;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import dan200.CCTurtle;
import dan200.ComputerCraft;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import dan200.turtle.shared.BlockTurtle;
import dan200.turtle.shared.TileEntityTurtle;

public class TileEntityPeripheral extends TileEntity implements IPeripheral
{
        public IComputerAccess comp = null;
        public  String[] allMethods = { "getBlock", "setBlock", "findPeripheral", "findComputer","playerCoords", "getHealth", "attackPlayer","setPlayerPos", "givePlayer", "getPlayers", "say", "createFireball", "playerLooking", "createExplosion", "getDirection" };

        public String getType()
        {
                return "Peripheral";
        }
        
        
        public String[] getMethodNames()
        {
                return allMethods;
        }

        public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments)
        {
        	switch(method){ 
        	case 0: { //Returns the blockID and Metadata
        		if (arguments.length < 3) {
        			return new Object[] {false, "Requires 3 arguments"};
        		} else {
    			int x = ((Double) arguments[0]).intValue();
    			int y = ((Double) arguments[1]).intValue();
    			int z = ((Double) arguments[2]).intValue();
    			return new Object[] {this.worldObj.getBlockId(x, y, z), this.worldObj.getBlockMetadata(x,y,z)};
        		}
        	}
        	case 1: { //Sets blockID and (optionally) Metadata
        		if (arguments.length < 4) {
        			return new Object[] {false, "Requires at least 4 arguments"};
        		} else {
	        		World world = this.worldObj;
	            	int x = ((Double) arguments[0]).intValue();
	            	int y = ((Double) arguments[1]).intValue();
	    			int z = ((Double) arguments[2]).intValue();
	    			int id = ((Double) arguments[3]).intValue();
	    			int metadata = 0;
	    			if (arguments.length > 4) {
	    				metadata = ((Double) arguments[4]).intValue();
	    			}
	    			if (x==this.xCoord && y==this.yCoord && z==this.zCoord || isComputer(x,y,z)==true) {
	    				return new Object[] {"Cannot set computer or peripheral to block!"};
	    				
	    			} else if (metadata != 0) {
	    				world.setBlockAndMetadata(x, y, z, id, metadata);
	    				return new Object[] {"Set block with notify"};
	    			} else {
	    				world.setBlockWithNotify(x, y, z, id);
	    				return new Object[] {"Set block with notify"};
	    			}
        		}
        	}
        	case 2: { //Gets the peripheral x,y,z coords
        		return new Object[] {this.xCoord, this.yCoord, this.zCoord};
        	}
        	case 3: { //Gets the attached computer x,y,z coords
        		int[] coords = getComputer();
        		return new Object[] {coords[0],coords[1],coords[2]};
        	}
        	case 4: { //Gets the player position
        		if (arguments.length < 1) {
        			return new Object[] {false, "Requires a string"};
        		} else {
        		World world = this.worldObj;
        		EntityPlayer player = world.getPlayerEntityByName((String) arguments[0]);
        		if (player == null) {
        			return new Object[] {null};
        		} else {
        			return new Object[] {player.posX, player.posY, player.posZ};
        		}
        		}
        	}
        	case 5: { //Tests to see if the player is on the server and then returns that players health if they do exist
        		EntityPlayer player = this.worldObj.getPlayerEntityByName((String) arguments[0]);
        		if (player != null) {
        			return new Object[] {player.getHealth()};
        		} else {
        			return new Object[] {false, "Player does not exist!"};
        		}
        	}
        	case 6: { //Tests to see if the player is on the server and then damages the player by the specified amount if they do exist
        		EntityPlayer player = this.worldObj.getPlayerEntityByName((String) arguments[0]);
        		if (player != null) {
        			player.attackEntityFrom(DamageSource.onFire, ((Double)arguments[1]).intValue());
                	return new Object[] {true};
        		} else {
        			return new Object[] {"Player does not exist!"};
        		}
            	
        	}
        	case 7: { //Sets the specified player's position
        		if (arguments.length < 4) {
        			return new Object[] {"Requires 4 arguments"};
        		} else {
	        		double x = (Double) arguments[1];
	            	double y = (Double) arguments[2];
	    			double z = (Double) arguments[3];
	    			EntityPlayer player = this.worldObj.getPlayerEntityByName((String) arguments[0]);
	    			if (player != null) {
	    				player.setPositionAndUpdate(x,y,z);
	    				return new Object[] {true};
	    			} else {
	    				return new Object[] {false, "Player does not exist!"};
	    			}
        		}
        	}
        	case 8: { //Gives the given player the specified amount of the item
        		if (arguments.length < 3) {
        			return new Object[] {false, "Requires 3 arguments"};
        		} else {
	        		int id = ((Double) arguments[1]).intValue();
	        		int amount = ((Double) arguments[2]).intValue();
	        		return new Object[] {givePlayer((String) arguments[0],id, amount)};
        		}
        	}
        	case 9: { //Returns the username of the player at userlist[given integer]
        		if (arguments.length < 1) {
        			return new Object[] {"Requires an integer"};
        		} else {
	        		MinecraftServer server = MinecraftServer.getServer();
	        		String[] users = server.getAllUsernames();
	        		int thevalue = ((Double) arguments[0]).intValue() - 1;
	        		if (thevalue > users.length || thevalue < 0) {
	        			return new Object[] {"There are not that many players on the server"};
	        		} else {
	        			return new Object[] {users[thevalue]};
	        		}
        		}
        	}
        	case 10: { //Adds a chat message
        		if (arguments.length < 1) {
        			return new Object[] {false, "Requires a string value"};
        		} else {
	        		MinecraftServer server = MinecraftServer.getServer();
	        		String[] users = server.getAllUsernames();
	        		String user = users[0];
	        		String msg = (String) arguments[0];
	        		EntityPlayer player = worldObj.getPlayerEntityByName(user);
	        		if (player == null) {
	        			return new Object[] {false};
	        		} else {
	        			player.addChatMessage(msg);
	        			return new Object[] {true};
	        		}
        		}
        	}
        	case 11: { //Create a large fireball and spawn it in the world at thisX,thisY,thisZ with velocities of x,y,z
        		if (arguments.length < 6) {
        			return new Object[] {false, "Requires 6 arguments"};
        		} else {
	        		int thisX = ((Double) arguments[0]).intValue();
	        		int thisY = ((Double) arguments[1]).intValue();
	        		int thisZ = ((Double) arguments[2]).intValue();
	        		double x = (Double) arguments[3];
	        		double y = (Double) arguments[4];
	        		double z = (Double) arguments[5];
	        		EntityLargeFireball fireball = new EntityLargeFireball(this.worldObj, thisX, thisY, thisZ, x, y, z);
	        		this.worldObj.spawnEntityInWorld(fireball);
	        		return new Object[] {true};
        		}
        	}
        	case 12: { //Gets the requested player's look vectors
        		if (arguments.length < 1) {
        			return new Object[] {"Requires a player username"};
        		} else {
	        		EntityPlayer player = this.worldObj.getPlayerEntityByName((String) arguments[0]);
	        		Vec3 newvec = player.getLookVec();
	        		double x = newvec.xCoord;
	        		double y = newvec.yCoord;
	        		double z = newvec.zCoord;
	        		return new Object[] {x,y,z}; 
        		}
        	}
        	case 13: { //Creates an explosion at the given coordinates with the given damage value
        		if (arguments.length < 4) {
        			return new Object[] {false,"Not enough arguments"};
        		} else {
	        		MinecraftServer server = MinecraftServer.getServer();
	        		String[] list = server.getAllUsernames();
	        		EntityPlayer player = this.worldObj.getPlayerEntityByName(list[0]);
	        		double x = (Double) arguments[0];
	        		double y = (Double) arguments[1];
	        		double z = (Double) arguments[2];
	        		float f = ((Double) arguments[3]).floatValue();
	        		this.worldObj.createExplosion(player, x, y,z, f, true);
	        		return new Object[] {true};
        		}
        	}
        	case 14: { //Gets the direction the attached computer/turtle is facing
        		int[] coords = getComputer();
        		int x = coords[0];
        		int y = coords[1];
        		int z = coords[2];
        		if (this.worldObj.getBlockId(x, y, z) == ComputerCraft.computerBlockID)
        		{
        			int metadata = this.worldObj.getBlockMetadata(x, y, z) & 0x7;
        			String finalDir;
        			int iDir;
        			if (metadata == 3) {
        				finalDir="North";
        				iDir = 2;
        			} else if (metadata == 4) {
        				finalDir="East";
        				iDir = 3;
        			} else if (metadata == 2) {
        				finalDir="South";
        				iDir = 0;
        			} else if (metadata == 5) {
        				finalDir="West";
        				iDir = 1;
        			} else {
        				finalDir = "INVALID DIRECTION";
        				iDir = 5;
        			}
        			return new Object[] {finalDir,iDir};
        		} else {
        			TileEntity tile = this.worldObj.getBlockTileEntity(x, y, z);
        			TileEntityTurtle turtle = (TileEntityTurtle)tile;
        			int dir = turtle.getFacingDir();
        			String finalDir;
        			int iDir;
        			if (dir == 2) {
        				finalDir = "North";
        				iDir = 2;
        			} else if (dir == 5) {
        				finalDir = "East";
        				iDir = 3;
        			} else if (dir == 3) {
        				finalDir = "South";
        				iDir = 0;
        			} else if (dir == 4) {
        				finalDir = "West";
        				iDir = 1;
        			} else {
        				finalDir = "INVALID DIRECTION";
        				iDir = 5;
        			}
        			return new Object[] {finalDir,iDir};
        		}
        	}
        	}
            return null;
        }
        
        private int[] getComputer() { //A function to determine the computer's position in the world
        	int cX = this.xCoord;
        	int cY = this.yCoord;
        	int cZ = this.zCoord;
        	if (isComputer(cX,cY+1,cZ) == true ) {
        		int[] coords = {cX, cY+1, cZ};
        		return coords;
        	} else if (isComputer(cX,cY-1,cZ) == true) {
        		int[] coords = {cX, cY-1, cZ};
        		return coords;
        	} else if (isComputer(cX,cY,cZ+1) == true) {
        		int[] coords = {cX, cY, cZ+1};
        		return coords;
        	} else if (isComputer(cX,cY,cZ-1) == true) {
        		int[] coords = {cX, cY, cZ-1};
        		return coords;
        	} else if (isComputer(cX+1,cY,cZ) == true) {
        		int[] coords = {cX+1, cY, cZ};
        		return coords;
        	} else if (isComputer(cX-1,cY,cZ+1) == true) {
        		int[] coords = {cX-1, cY, cZ-1};
        		return coords;
        	} else {
        		int[] coords = {0,0,0,1};
        		return coords;
        	}
		}


		public boolean givePlayer(String username, int itemID, int itemAmount) { //A function to give the player items
        	ItemStack unknownItemStack = new ItemStack(itemID, itemAmount, 0);
        	EntityPlayer player = this.worldObj.getPlayerEntityByName(username);
        	if (player != null){
            	return player.inventory.addItemStackToInventory(unknownItemStack);
        	} else {
        		return false;
        	}
        }
        
        public boolean isComputer(int x, int y, int z) { //Checks to see if the specified x,y,z coordinates are inhabited by a computer or turtle
        	int id1 = ComputerCraft.computerBlockID;
        	int id2 = CCTurtle.turtleBlockID;
        	int id3 = CCTurtle.turtleUpgradedBlockID;
        	if (worldObj.getBlockId(x,y, z) == id1 || worldObj.getBlockId(x,y, z)==id2 || worldObj.getBlockId(x, y, z) == id3) {
        		return true;
        	} else {
        		return false;
        	}
        	
        	
        	
        }
        
  
        public boolean canAttachToSide(int side) //Auto-generated stub
        {
                return true;

        }
        
        public void attach(IComputerAccess computer, String computerSide) //Auto-generated stub
        {
        }
        
        public void detach(IComputerAccess computer) //Auto-generated stub
        {
        }
}
