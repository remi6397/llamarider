/* LlamaRider -- Let's ride Llamas in Minecraft!
 * Copyright (C) 2020  remi6397
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package pl.nelz.llamarider.common.items;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import pl.nelz.llamarider.common.LlamaRider;

@Mod.EventBusSubscriber(modid = LlamaRider.MODID)
public class ItemLlamaSaddle extends AbstractBasicItem {

    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.<EnumDyeColor>create("color", EnumDyeColor.class);

	public static final ItemLlamaSaddle INSTANCE = new ItemLlamaSaddle();

	public ItemLlamaSaddle() {
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.TRANSPORTATION);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int i = stack.getMetadata();
		return "item.llama_saddle." + EnumDyeColor.byMetadata(i).getUnlocalizedName();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			for (EnumDyeColor dye : EnumDyeColor.values()) {
				items.add(new ItemStack(this, 1, dye.getMetadata()));
			}
		}
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		for (EnumDyeColor dye : EnumDyeColor.values()) {
			ShapelessOreRecipe sor = new ShapelessOreRecipe(
					new ResourceLocation(LlamaRider.MODID, "llama_saddle_dyed"),
					new ItemStack(INSTANCE, 1, dye.getMetadata()),
					new ItemStack(Items.SADDLE, 1, 0), new ItemStack(Blocks.CARPET, 1, dye.getMetadata()));
			sor.setRegistryName(new ResourceLocation(LlamaRider.MODID, "llama_saddle." + dye.getUnlocalizedName()));
			event.getRegistry().register(sor);
		}
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(INSTANCE);
	}

	@Override
	public void updateModel() {
		for (EnumDyeColor dye : EnumDyeColor.values()) {
			ModelLoader
					.setCustomModelResourceLocation(INSTANCE, dye.getMetadata(),
							new ModelResourceLocation(
									new ResourceLocation(LlamaRider.MODID,
											"llama_saddle_" + dye.getUnlocalizedName()),
									"inventory"));
		}
	}

}
