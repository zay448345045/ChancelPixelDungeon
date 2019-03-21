/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.noodlemire.chancelpixeldungeon.items.wands;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ConfusionGas;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Fire;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ParalyticGas;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Regrowth;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ToxicGas;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Burning;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Frost;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Recharging;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mimic;
import com.noodlemire.chancelpixeldungeon.actors.mobs.npcs.Sheep;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Flare;
import com.noodlemire.chancelpixeldungeon.effects.MagicMissile;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.effects.SpellSprite;
import com.noodlemire.chancelpixeldungeon.effects.particles.ShadowParticle;
import com.noodlemire.chancelpixeldungeon.items.Bomb;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.levels.traps.CursingTrap;
import com.noodlemire.chancelpixeldungeon.levels.traps.ShockingTrap;
import com.noodlemire.chancelpixeldungeon.levels.traps.SummoningTrap;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.messages.Languages;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.plants.Plant;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.scenes.InterlevelScene;
import com.noodlemire.chancelpixeldungeon.ui.TargetHealthIndicator;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.noodlemire.chancelpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.io.IOException;
import java.util.ArrayList;

//helper class to contain all the cursed wand zapping logic, so the main wand class doesn't get huge.
public class CursedWand
{
	private static float COMMON_CHANCE = 0.6f;
	private static float UNCOMMON_CHANCE = 0.3f;
	private static float RARE_CHANCE = 0.09f;
	private static float VERY_RARE_CHANCE = 0.01f;

	public static void cursedZap(final Wand wand, final Hero user, final Ballistica bolt)
	{
		switch(Random.chances(new float[]{COMMON_CHANCE, UNCOMMON_CHANCE, RARE_CHANCE, VERY_RARE_CHANCE}))
		{
			case 0:
			default:
				commonEffect(wand, user, bolt);
				break;
			case 1:
				uncommonEffect(wand, user, bolt);
				break;
			case 2:
				rareEffect(wand, user, bolt);
				break;
			case 3:
				veryRareEffect(wand, user, bolt);
				break;
		}
	}

	private static void commonEffect(final Wand wand, final Hero user, final Ballistica bolt)
	{
		switch(Random.Int(4))
		{

			//anti-entropy
			case 0:
				cursedFX(user, bolt, new Callback()
				{
					public void call()
					{
						Char target = Actor.findChar(bolt.collisionPos);
						switch(Random.Int(2))
						{
							case 0:
								if(target != null)
									Buff.affect(target, Burning.class).reignite();
								Buff.affect(user, Frost.class, Frost.duration(user) * Random.Float(3f, 5f));
								break;
							case 1:
								Buff.affect(user, Burning.class).reignite();
								if(target != null)
									Buff.affect(target, Frost.class, Frost.duration(target) * Random.Float(3f, 5f));
								break;
						}
						wand.wandUsed();
					}
				});
				break;

			//spawns some regrowth
			case 1:
				cursedFX(user, bolt, new Callback()
				{
					public void call()
					{
						int c = Dungeon.level.map[bolt.collisionPos];
						if(c == Terrain.EMPTY ||
						   c == Terrain.EMBERS ||
						   c == Terrain.EMPTY_DECO ||
						   c == Terrain.GRASS ||
						   c == Terrain.HIGH_GRASS)
						{
							GameScene.add(Blob.seed(bolt.collisionPos, 30, Regrowth.class));
						}
						wand.wandUsed();
					}
				});
				break;

			//random teleportation
			case 2:
				switch(Random.Int(2))
				{
					case 0:
						ScrollOfTeleportation.randomTeleport(user);
						wand.wandUsed();
						break;
					case 1:
						cursedFX(user, bolt, new Callback()
						{
							public void call()
							{
								ScrollOfTeleportation.randomTeleport(Actor.findChar(bolt.collisionPos));
								wand.wandUsed();
							}
						});
						break;
				}
				break;

			//random gas at location
			case 3:
				cursedFX(user, bolt, new Callback()
				{
					public void call()
					{
						switch(Random.Int(3))
						{
							case 0:
								GameScene.add(Blob.seed(bolt.collisionPos, 800, ConfusionGas.class));
								break;
							case 1:
								GameScene.add(Blob.seed(bolt.collisionPos, 500, ToxicGas.class));
								break;
							case 2:
								GameScene.add(Blob.seed(bolt.collisionPos, 200, ParalyticGas.class));
								break;
						}
						wand.wandUsed();
					}
				});
				break;
		}

	}

	private static void uncommonEffect(final Wand wand, final Hero user, final Ballistica bolt)
	{
		switch(Random.Int(4))
		{

			//Random plant
			case 0:
				cursedFX(user, bolt, new Callback()
				{
					public void call()
					{
						int pos = bolt.collisionPos;
						//place the plant infront of an enemy so they walk into it.
						if(Actor.findChar(pos) != null && bolt.dist > 1)
						{
							pos = bolt.path.get(bolt.dist - 1);
						}

						if(pos == Terrain.EMPTY ||
						   pos == Terrain.EMBERS ||
						   pos == Terrain.EMPTY_DECO ||
						   pos == Terrain.GRASS ||
						   pos == Terrain.HIGH_GRASS)
						{
							Dungeon.level.plant((Plant.Seed) Generator.random(Generator.Category.SEED), pos);
						}
						wand.wandUsed();
					}
				});
				break;

			//Health transfer
			case 1:
				final Char target = Actor.findChar(bolt.collisionPos);
				if(target != null)
				{
					cursedFX(user, bolt, new Callback()
					{
						public void call()
						{
							int damage = user.lvl * 2;
							switch(Random.Int(2))
							{
								case 0:
									user.heal(damage);
									user.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);
									target.damage(damage, wand);
									target.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
									break;
								case 1:
									user.damage(damage, this);
									user.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
									target.heal(damage);
									target.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);
									Sample.INSTANCE.play(Assets.SND_CURSED);
									if(!user.isAlive())
									{
										Dungeon.fail(wand.getClass());
										GLog.n(Messages.get(CursedWand.class, "ondeath", wand.name()));
									}
									break;
							}
							wand.wandUsed();
						}
					});
				}
				else
				{
					GLog.i(Messages.get(CursedWand.class, "nothing"));
					wand.wandUsed();
				}
				break;

			//Bomb explosion
			case 2:
				cursedFX(user, bolt, new Callback()
				{
					public void call()
					{
						new Bomb().explode(bolt.collisionPos);
						wand.wandUsed();
					}
				});
				break;

			//shock and recharge
			case 3:
				new ShockingTrap().set(user.pos).activate();
				Buff.affect(user, Recharging.class).set(20f);
				ScrollOfRecharging.charge(user);
				SpellSprite.show(user, SpellSprite.CHARGE);
				wand.wandUsed();
				break;
		}

	}

	private static void rareEffect(final Wand wand, final Hero user, final Ballistica bolt)
	{
		switch(Random.Int(4))
		{

			//sheep transformation
			case 0:
				cursedFX(user, bolt, new Callback()
				{
					public void call()
					{
						Char ch = Actor.findChar(bolt.collisionPos);

						if(ch != null && ch != user
						   && !ch.properties().contains(Char.Property.BOSS)
						   && !ch.properties().contains(Char.Property.MINIBOSS))
						{
							Sheep sheep = new Sheep();
							sheep.lifespan = 10;
							sheep.pos = ch.pos;
							ch.destroy();
							ch.sprite.killAndErase();
							Dungeon.level.mobs.remove(ch);
							TargetHealthIndicator.instance.target(null);
							GameScene.add(sheep);
							CellEmitter.get(sheep.pos).burst(Speck.factory(Speck.WOOL), 4);
						}
						else
						{
							GLog.i(Messages.get(CursedWand.class, "nothing"));
						}
						wand.wandUsed();
					}
				});
				break;

			//curses!
			case 1:
				CursingTrap.curse(user);
				wand.wandUsed();
				break;

			//inter-level teleportation
			case 2:
				if(Dungeon.depth > 1 && !Dungeon.bossLevel())
				{
					//each depth has 1 more weight than the previous depth.
					float[] depths = new float[Dungeon.depth - 1];
					for(int i = 1; i < Dungeon.depth; i++) depths[i - 1] = i;
					int depth = 1 + Random.chances(depths);

					Hero.detachTimeFreeze();

					InterlevelScene.mode = InterlevelScene.Mode.RETURN;
					InterlevelScene.returnDepth = depth;
					InterlevelScene.returnPos = -1;
					Game.switchScene(InterlevelScene.class);
				}
				else
				{
					ScrollOfTeleportation.randomTeleport(user);
					wand.wandUsed();
				}
				break;

			//summon monsters
			case 3:
				new SummoningTrap().set(user.pos).activate();
				wand.wandUsed();
				break;
		}
	}

	private static void veryRareEffect(final Wand wand, final Hero user, final Ballistica bolt)
	{
		switch(Random.Int(4))
		{

			//great forest fire!
			case 0:
				for(int i = 0; i < Dungeon.level.length(); i++)
				{
					int c = Dungeon.level.map[i];
					if(c == Terrain.EMPTY ||
					   c == Terrain.EMBERS ||
					   c == Terrain.EMPTY_DECO ||
					   c == Terrain.GRASS ||
					   c == Terrain.HIGH_GRASS)
					{
						GameScene.add(Blob.seed(i, 15, Regrowth.class));
					}
				}
				do
				{
					GameScene.add(Blob.seed(Dungeon.level.randomDestination(), 10, Fire.class));
				}
				while(Random.Int(5) != 0);
				new Flare(8, 32).color(0xFFFF66, true).show(user.sprite, 2f);
				Sample.INSTANCE.play(Assets.SND_TELEPORT);
				GLog.p(Messages.get(CursedWand.class, "grass"));
				GLog.w(Messages.get(CursedWand.class, "fire"));
				wand.wandUsed();
				break;

			//superpowered mimic
			case 1:
				cursedFX(user, bolt, new Callback()
				{
					public void call()
					{
						Mimic mimic = Mimic.spawnAt(bolt.collisionPos, new ArrayList<Item>());
						if(mimic != null)
						{
							mimic.adjustStats(Dungeon.depth + 10);
							Item reward;
							do
							{
								reward = Generator.random(Random.oneOf(Generator.Category.WEAPON, Generator.Category.ARMOR,
										Generator.Category.RING, Generator.Category.WAND));
							}
							while(reward.level() < 1);
							Sample.INSTANCE.play(Assets.SND_MIMIC, 1, 1, 0.5f);
							mimic.items.clear();
							mimic.items.add(reward);
						}
						else
						{
							GLog.i(Messages.get(CursedWand.class, "nothing"));
						}

						wand.wandUsed();
					}
				});
				break;

			//crashes the game, yes, really.
			case 2:
				try
				{
					Dungeon.saveAll();
					if(Messages.lang() != Languages.ENGLISH)
					{
						//Don't bother doing this joke to none-english speakers, I doubt it would translate.
						GLog.i(Messages.get(CursedWand.class, "nothing"));
						wand.wandUsed();
					}
					else
					{
						GameScene.show(
								new WndOptions("CURSED WAND ERROR", "this application will now self-destruct", "abort", "retry", "fail")
								{

									@Override
									protected void onSelect(int index)
									{
										Game.instance.finish();
									}

									@Override
									public void onBackPressed()
									{
										//do nothing
									}
								}
						);
					}
				}
				catch(IOException e)
				{
					ChancelPixelDungeon.reportException(e);
					//oookay maybe don't kill the game if the save failed.
					GLog.i(Messages.get(CursedWand.class, "nothing"));
					wand.wandUsed();
				}
				break;

			//random transmogrification
			case 3:
				wand.wandUsed();
				wand.detach(user.belongings.backpack);
				Item result;
				do
				{
					result = Generator.random(Random.oneOf(Generator.Category.WEAPON, Generator.Category.ARMOR,
							Generator.Category.RING, Generator.Category.ARTIFACT));
				}
				while(result.cursed);
				if(result.isUpgradable()) result.upgrade();
				result.cursed = result.cursedKnown = true;
				GLog.w(Messages.get(CursedWand.class, "transmogrify"));
				Dungeon.level.drop(result, user.pos).sprite.drop();
				wand.wandUsed();
				break;
		}
	}

	private static void cursedFX(final Hero user, final Ballistica bolt, final Callback callback)
	{
		MagicMissile.boltFromChar(user.sprite.parent,
				MagicMissile.RAINBOW,
				user.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

}
