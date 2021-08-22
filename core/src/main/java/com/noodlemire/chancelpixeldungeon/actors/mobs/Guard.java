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

package com.noodlemire.chancelpixeldungeon.actors.mobs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Cripple;
import com.noodlemire.chancelpixeldungeon.effects.Chains;
import com.noodlemire.chancelpixeldungeon.effects.Pushing;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.armor.Armor;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHealing;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.GuardSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Guard extends Mob
{
	//they can only use their chains once
	private boolean chainsUsed = false;

	{
		spriteClass = GuardSprite.class;

		EXP = Random.IntRange(9, 11);

		TIME_TO_REST = 4;

		setHT(EXP * 5, true);

		loot = null;    //see createloot.
		lootChance = 1f;

		properties.add(Property.UNDEAD);
		properties.add(Property.METALLIC);

		HUNTING = new Hunting();
	}

	@Override
	public int damageRoll()
	{
		return EXP * 3 - 6;
	}

	private boolean chain(int target)
	{
		if(chainsUsed || enemy.properties().contains(Property.IMMOVABLE))
			return false;

		Ballistica chain = new Ballistica(pos, target, Ballistica.PROJECTILE);

		if(chain.collisionPos != enemy.pos
		   || chain.path.size() < 2
		   || Dungeon.level.pit[chain.path.get(1)])
			return false;
		else
		{
			int newPos = -1;
			for(int i : chain.subPath(1, chain.dist))
			{
				if(!Dungeon.level.solid[i] && Actor.findChar(i) == null)
				{
					newPos = i;
					break;
				}
			}

			if(newPos == -1)
			{
				return false;
			}
			else
			{
				final int newPosFinal = newPos;
				this.target = newPos;
				yell(Messages.get(this, "scorpion"));
				sprite.parent.add(new Chains(sprite.center(), enemy.sprite.center(), new Callback()
				{
					public void call()
					{
						Actor.addDelayed(new Pushing(enemy, enemy.pos, newPosFinal, new Callback()
						{
							public void call()
							{
								enemy.pos = newPosFinal;
								Dungeon.level.press(newPosFinal, enemy, true);
								Cripple.prolong(enemy, Cripple.class, 4f);
								if(enemy == Dungeon.hero)
								{
									Dungeon.hero.interrupt();
									Dungeon.observe();
									GameScene.updateFog();
								}
							}
						}), -1);
						next();
					}
				}));
			}
		}
		chainsUsed = true;
		return true;
	}

	@Override
	public int attackSkill(Char target)
	{
		return EXP * 2 - 4;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(1, EXP);
	}

	@Override
	public int defenseSkill()
	{
		return EXP + 1;
	}

	@Override
	protected Item createLoot()
	{
		//first see if we drop armor, overall chance is 1/8
		if(Random.Int(8) == 0)
		{
			Armor loot;
			do
			{
				loot = Generator.randomArmor();
				//50% chance of re-rolling tier 4 or 5 items
			}
			while(loot.tier >= 4 && Random.Int(2) == 0);
			loot.level(0);
			return loot;
			//otherwise, we may drop a health potion. overall chance is 5/8 * (5-potions dropped)/5
			//with 0 potions dropped that simplifies to 5/8
		}
		else if(Random.Float() < 0.625 * ((5f - Dungeon.LimitedDrops.GUARD_HP.count) / 5f))
		{
			Dungeon.LimitedDrops.GUARD_HP.count++;
			return new PotionOfHealing();
		}

		return null;
	}

	private final String CHAINSUSED = "chainsused";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(CHAINSUSED, chainsUsed);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		chainsUsed = bundle.getBoolean(CHAINSUSED);
	}

	private class Hunting extends Mob.Hunting
	{
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted)
		{
			lookForEnemy(enemyInFOV);

			if(!chainsUsed
			   && enemyInFOV
			   && !isCharmedBy(enemy)
			   && !canAttack(enemy)
			   && Dungeon.level.distance(pos, enemy.pos) < 5
			   && Random.Int(3) == 0

			   && chain(enemy.pos))
			{
				return false;
			}
			else
			{
				return super.act(enemyInFOV, justAlerted);
			}
		}
	}
}
