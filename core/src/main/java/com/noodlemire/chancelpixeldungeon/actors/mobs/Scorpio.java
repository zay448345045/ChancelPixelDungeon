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
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Cripple;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Light;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.food.MysteryMeat;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHealing;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.sprites.ScorpioSprite;
import com.watabou.utils.Random;

public class Scorpio extends Mob
{
	{
		spriteClass = ScorpioSprite.class;

		EXP = Random.IntRange(23, 26);

		TIME_TO_REST = 2;
		setAttacksBeforeRest(2);

		setHT(EXP * 5, true);
		viewDistance = Light.DISTANCE;

		loot = null; //See createLoot()
		lootChance = 1f;

		properties.add(Property.DEMONIC);
	}

	@Override
	public int damageRoll()
	{
		return EXP * 2 - 10 + 8 * restTimeNeeded(false);
	}

	@Override
	public int attackSkill(Char target)
	{
		return EXP * 2;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, EXP / 2 + 5);
	}

	@Override
	public int defenseSkill()
	{
		return EXP * 3 - 20;
	}

	@Override
	protected boolean canAttack(Char enemy)
	{
		Ballistica attack = new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE);
		return !Dungeon.level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos;
	}

	@Override
	public int attackProc(Char enemy, int damage)
	{
		damage = super.attackProc(enemy, damage);
		if(Random.Int(2) == 0)
		{
			Buff.prolong(enemy, Cripple.class, Cripple.DURATION);
		}

		return damage;
	}

	@Override
	protected boolean getCloser(int target)
	{
		if(state == HUNTING)
		{
			return enemySeen() && getFurther(target);
		}
		else
		{
			return super.getCloser(target);
		}
	}

	@Override
	protected Item createLoot()
	{
		//25% * (3-count) / 3 chance of getting healing, otherwise 25% * (3-count) / 3 chance of mystery meat
		//In both cases, the base chance with no previous drops from this mob of 25%
		if(Random.Float() < 0.25 * ((3 - Dungeon.LimitedDrops.SCORPIO_HP.count) / 3f))
		{
			Dungeon.LimitedDrops.SCORPIO_HP.count++;
			return new PotionOfHealing();
		}
		else if(Random.Float() < 0.25 * ((3 - Dungeon.LimitedDrops.SCORPIO_MEAT.count) / 3f))
		{
			Dungeon.LimitedDrops.SCORPIO_MEAT.count++;
			return new MysteryMeat();
		}

		return null;
	}
}
