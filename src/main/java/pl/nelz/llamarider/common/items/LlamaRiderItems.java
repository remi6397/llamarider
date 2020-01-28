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

public enum LlamaRiderItems {

	LLAMA_SADDLE(ItemLlamaSaddle.INSTANCE, "llama_saddle");

	private AbstractBasicItem item;
	private String registryName;

	private LlamaRiderItems(AbstractBasicItem item, String registryName) {
		this.item = item;
		this.registryName = registryName;

		this.item.setRegistryName(registryName);
	}

	public AbstractBasicItem getItem() {
		return item;
	}

	public String getRegistryName() {
		return registryName;
	}

}
