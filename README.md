LlamaRider
==========

> Let's make Llamas rideable!

Llamas are actually a subtype of horses under the hood in our favorite brick game, though you can't ride them in vanilla game.

This mod fixes that for you by adding "Llama Saddles" and restoring the abilities inhertited from their horsey ancestors.

(Side note: I know that Llamas are not closely related with horses IRL, you don't have to tell me that, thank you \^\^)

Unlike some other attempts, this one modifies the vanilla logic by *bytecode manipulation* instead of a common naïve _"killing-all-entities-and-replacing-them-with-custom-subclass"_ method.

Frankly speaking, this mod is a side effect of my study of JVM's inner workings (okay, okay, and I also like their cheerful fluffy muzzles :3 )

(Side note \#2: You can use this command to summon yourself a very nice llama: ``/summon minecraft:llama ~0 ~1 ~0 {Attributes:[{Name:generic.maxHealth,Base:40.0},{Name:generic.movementSpeed,Base:0.3375},{Name:horse.jumpStrength,Base:1.0}],Tame:1}``)
