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

import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import pl.nelz.llamarider.common.LlamaRider;

public class LlamaRiderCore extends DummyModContainer {

	public LlamaRiderCore() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = LlamaRider.MODID+"_core";
		meta.name = LlamaRider.NAME+" Core Mod";
		meta.version = LlamaRider.VERSION;
		meta.credits = LlamaRider.CREDITS;
		meta.authorList = LlamaRider.AUTHORS;
		meta.description = LlamaRider.DESCRIPTION;
		meta.screenshots = LlamaRider.SCREENSHOTS;
		meta.logoFile = LlamaRider.LOGO;
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

}
