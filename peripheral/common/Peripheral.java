package peripheral.common;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "Peripheral", name = "Peripheral", version = "1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class Peripheral
{
        public static Block peripheralBlock;

        
        @Mod.Instance("Peripheral")
        public static Object instance;
        @Init
        public void load(FMLInitializationEvent event)
        {
                GameRegistry.registerTileEntity(TileEntityPeripheral.class, "TileEntityPeripheral");
                peripheralBlock = new BlockPeripheralBlock(250, 0).setBlockName("peripheralBlock");
                GameRegistry.registerBlock(peripheralBlock);
                LanguageRegistry.addName(peripheralBlock, "Peripheral Block");
                ItemStack dBlock = new ItemStack(Block.blockDiamond);
                ItemStack sStone = new ItemStack(Block.stone);
                GameRegistry.addRecipe(new ItemStack(peripheralBlock), "xxx", "xyx", "xxx", 
                        'x', dBlock, 'y', sStone);
        }
}
