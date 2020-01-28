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
package pl.nelz.llamarider.common;

import static pl.nelz.llamarider.common.LlamaRider.*;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MODID, name = NAME, version = VERSION, acceptedMinecraftVersions = "[1.12.2]")
public class LlamaRider {

    public static final String MODID = "llamarider";
    public static final String NAME = "LlamaRider";
    public static final String VERSION = "1.0";
	public static final String CREDITS = "To the Great Architect of the Universe, for creation of marvelous fluffy creatures called Llamas.";
	public static final List<String> AUTHORS = Arrays.asList("remi6397");
	public static final String DESCRIPTION = "Let's ride Llamas in Minecraft!";
	public static final String[] SCREENSHOTS = new String[0];
	public static final String LOGO = "";

	private static Logger logger;

	@SidedProxy(serverSide = "pl.nelz.llamarider.common.ServerProxy", clientSide = "pl.nelz.llamarider.client.ClientProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		proxy.registerModels();
	}

	public static Logger getLogger() {
		return logger;
	}


}
