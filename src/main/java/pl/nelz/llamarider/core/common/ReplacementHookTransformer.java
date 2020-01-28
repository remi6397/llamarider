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
package pl.nelz.llamarider.core.common;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ReplacementHookTransformer implements TransformProvider {

	private String targetClass;
	private String targetName;
	private String targetDesc;
	private int retOpcode;

	public ReplacementHookTransformer(String targetClass, String targetName, String targetDesc, int retOpcode) {
		this.targetClass = targetClass;
		this.targetName = targetName;
		this.targetDesc = targetDesc;
		this.retOpcode = retOpcode;
	}

	@Override
	public void transform(MethodNode method) {
		InsnList newInsns = new InsnList();
		newInsns.add(new VarInsnNode(ALOAD, 0));
		newInsns.add(new MethodInsnNode(
				INVOKESTATIC,
				targetClass,
				targetName,
				targetDesc,
				false));
		newInsns.add(new InsnNode(retOpcode));
		method.instructions.clear();
		method.instructions.insert(newInsns);
	}

}
