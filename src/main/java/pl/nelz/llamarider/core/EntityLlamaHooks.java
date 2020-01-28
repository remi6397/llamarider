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
package pl.nelz.llamarider.core;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import pl.nelz.llamarider.common.items.ItemLlamaSaddle;

public final class EntityLlamaHooks {

	/**
	 * Parameter that maintains synchronization of Llama's decoration item stack (actually it's the second (subscript [1]) stack in horseChest).
	 */
	private static final DataParameter<ItemStack> LLAMA_DECO_STACK = EntityDataManager
			.<ItemStack>createKey(EntityLlama.class, DataSerializers.ITEM_STACK);

	private static List<Item> ACCEPTABLE_SADDLE_ITEMS = Arrays.asList(ItemLlamaSaddle.INSTANCE);

	public static boolean canBeSteered(EntityLlama llamaIn) {
		return llamaIn.getControllingPassenger() instanceof EntityLivingBase && isHorseSaddled(llamaIn);
	}

	public static boolean isHorseSaddled(EntityLlama llamaIn) {
		return isSaddle(llamaIn.getDataManager().get(LLAMA_DECO_STACK));
	}

	public static void setColorByItem(EntityLlama llamaIn, ItemStack stack) {
		llamaIn.getDataManager().set(LLAMA_DECO_STACK, stack);
	}

	public static boolean isSaddle(ItemStack stack) {
		return ACCEPTABLE_SADDLE_ITEMS.contains(stack.getItem());
	}

	public static void entityInit(EntityLlama llamaIn) {
		llamaIn.getDataManager().register(LLAMA_DECO_STACK, ItemStack.EMPTY);
	}

	public static void mountTo(EntityLlama llamaIn) {
		llamaIn.leaveCaravan();
	}

}
