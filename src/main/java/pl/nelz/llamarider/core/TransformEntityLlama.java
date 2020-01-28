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

import static org.objectweb.asm.Opcodes.*;

import java.util.Arrays;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.IClassTransformer;
import pl.nelz.llamarider.core.common.ReplacementHookTransformer;
import pl.nelz.llamarider.core.common.TransformProvider;

// TODO: This class should be genericized to account potential need for transforming other classes in future.
/**
 * Bytecode transformer that overrides vanilla behavior of EntityLlama
 */
public class TransformEntityLlama implements IClassTransformer {

	public static final String TRANSFORM_CLASS = "net.minecraft.entity.passive.EntityLlama";

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (transformedName.equals(TRANSFORM_CLASS)) {
			ClassNode clazz = new ClassNode();
			ClassReader reader = new ClassReader(basicClass);
			reader.accept(clazz, 0);

			transformEntityLlama(clazz, LlamaRiderLoadingPlugin.isObfuscated());

	        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
	        clazz.accept(classWriter);
	        return classWriter.toByteArray();
		}

		return basicClass;
	}

	// TODO: This function should be cleaned up
	public static void transformEntityLlama(ClassNode clazz, boolean isObfuscated) {
		final String canBeSteered =
				isObfuscated ? "func_82171_bF" : "canBeSteered";
		final String isArmor =
				isObfuscated ? "func_190682_f" : "isArmor";
		final String isHorseSaddled =
				isObfuscated ? "func_110257_ck" : "isHorseSaddled";
		final String mountTo =
				isObfuscated ? "func_110237_h" : "mountTo";
		final String setColorByItem =
				isObfuscated ? "func_190702_g" : "setColorByItem";
		final String entityInit =
				isObfuscated ? "func_70088_a" : "entityInit";

		// TODO: Use Type.getMethodDescriptor
		final String entityLlamaBoolHookDesc =
				"(Lnet/minecraft/entity/passive/EntityLlama;)Z";
		final String itemStackBoolHookDesc =
				"(Lnet/minecraft/item/ItemStack;)Z";
		final String entityLlamaVoidHookDesc =
				"(Lnet/minecraft/entity/passive/EntityLlama;)V";
		final String entityPlayerVoidDesc =
				"(Lnet/minecraft/entity/player/EntityPlayer;)V";
		final String entityLlamaItemStackVoidDesc =
				"(Lnet/minecraft/entity/passive/EntityLlama;Lnet/minecraft/item/ItemStack;)V";

		// [Transform] EntityLlama$canBeSteered
		MethodNode mnSteerable = clazz.methods.stream()
			.filter(m -> m.name.equals(canBeSteered) && m.desc.equals("()Z"))
			.findAny()
			.get();
		new ReplacementHookTransformer(Type.getInternalName(EntityLlamaHooks.class), "canBeSteered", entityLlamaBoolHookDesc, IRETURN)
			.transform(mnSteerable);

		// [Insert] EntityLlama$isHorseSaddled
		MethodNode mnSaddled = new MethodNode(ASM5, ACC_PUBLIC, isHorseSaddled, "()Z", null, null);
		new ReplacementHookTransformer(Type.getInternalName(EntityLlamaHooks.class), "isHorseSaddled", entityLlamaBoolHookDesc, IRETURN)
			.transform(mnSaddled);
		clazz.methods.add(mnSaddled);

		// [Inject] EntityLlama$isArmor
		MethodNode mnArmor = clazz.methods.stream()
			.filter(m -> m.name.equals(isArmor) && m.desc.equals(itemStackBoolHookDesc))
			.findAny()
			.get();
		new TransformProvider() {

			/**
			 * Inject a condition for premature return to "EntityLlama$isSaddle".
			 * This essentially boils down to returning from the method in case where the hook returns true.
			 *
			 * if (ELH.iS(anItemStack))
			 *   return true;
			 */
			@Override
			public void transform(MethodNode method) {
				InsnList insns = new InsnList();
				LabelNode invalidSaddleLabel = new LabelNode();
				insns.add(new VarInsnNode(ALOAD, 1));
				insns.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(EntityLlamaHooks.class), "isSaddle", itemStackBoolHookDesc, false));
				insns.add(new JumpInsnNode(IFEQ, invalidSaddleLabel)); // if (sc_isArmor returns false [IFEQ]) then JUMP to "invalidSaddleLabel"
				insns.add(new InsnNode(ICONST_1)); // else continue returning "true" [ICONST_1]
				insns.add(new InsnNode(IRETURN));
				insns.add(invalidSaddleLabel);
				insns.add(new FrameNode(F_SAME, 0, null, 0, null)); // Frames are PITA
				method.instructions.insert(insns);
			}

		}.transform(mnArmor);

		// [Inject] EntityLlama$setColorByItem
		MethodNode mnSetColorByItem = clazz.methods.stream()
				.filter(m -> m.name.equals(setColorByItem) && m.desc.equals(Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(ItemStack.class))))
				.findAny()
				.get();
		new TransformProvider() {

			@Override
			public void transform(MethodNode method) {
				InsnList insns = new InsnList();
				insns.add(new VarInsnNode(ALOAD, 0));
				insns.add(new VarInsnNode(ALOAD, 1));
				insns.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(EntityLlamaHooks.class), "setColorByItem", entityLlamaItemStackVoidDesc, false));
				method.instructions.insert(insns);
			}

		}.transform(mnSetColorByItem);

		// [Inject] EntityLlama$entityInit
		MethodNode mnEntityInit = clazz.methods.stream()
				.filter(m -> m.name.equals(entityInit) && m.desc.equals(Type.getMethodDescriptor(Type.VOID_TYPE)))
				.findAny()
				.get();
		new TransformProvider() {

			/**
			 * Insert a hook AFTER super call.
			 */
			@Override
			public void transform(MethodNode method) {
				InsnList insns = new InsnList();
				insns.add(new VarInsnNode(ALOAD, 0));
				insns.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(EntityLlamaHooks.class), "entityInit", entityLlamaVoidHookDesc, false));

				AbstractInsnNode inInvokeSpecial = Arrays.stream(mnEntityInit.instructions.toArray())
						.filter(in -> in.getOpcode() == INVOKESPECIAL)
						.findFirst()
						.get();
				method.instructions.insert(inInvokeSpecial, insns); // After INVOKESPECIAL (i.e. super call to a method)
			}

		}.transform(mnEntityInit);

		// [Insert] EntityLlama$mountTo
		MethodNode mnMount = new MethodNode(ASM5, ACC_PUBLIC, mountTo, entityPlayerVoidDesc, null, null);
		new TransformProvider() {

			/**
			 * Create a method that overrides AbstractHorse$mountTo and calls a hook.
			 */
			@Override
			public void transform(MethodNode method) {
				InsnList insns = new InsnList();
				insns.add(new VarInsnNode(ALOAD, 0));
				insns.add(new VarInsnNode(ALOAD, 1));
				insns.add(new MethodInsnNode(INVOKESPECIAL, Type.getType(clazz.superName).getInternalName(), mountTo, entityPlayerVoidDesc, false)); // super call
				insns.add(new VarInsnNode(ALOAD, 0));
				insns.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(EntityLlamaHooks.class), "mountTo", entityLlamaVoidHookDesc, false));
				insns.add(new InsnNode(RETURN));
				method.instructions.add(insns);
			}

		}.transform(mnMount);
		clazz.methods.add(mnMount);
	}

}
