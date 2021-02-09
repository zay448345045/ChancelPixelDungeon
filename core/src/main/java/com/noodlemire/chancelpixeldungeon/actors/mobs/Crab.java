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
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.food.MysteryMeat;
import com.noodlemire.chancelpixeldungeon.sprites.CrabSprite;
import com.watabou.utils.Random;

public class Crab extends Mob
{
	{
		spriteClass = CrabSprite.class;

		EXP = Random.IntRange(4, 5);

		setHT(EXP * 4, true);
		baseSpeed = 2f;

		loot = new MysteryMeat();
		lootChance = 0.75f; //by default, see rollToDropLoot()
	}

	@Override
	public int damageRoll()
	{
		return Random.NormalIntRange(1, EXP * 2);
	}

	@Override
	public int attackSkill(Char target)
	{
		return EXP * 3;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(EXP - 1, EXP);
	}

	@Override
	public int defenseSkill()
	{
		return EXP * 2 - 3;
	}

	@Override
	public void rollToDropLoot()
	{
		lootChance *= ((5 - Dungeon.LimitedDrops.CRAB_MEAT.count) / 5f);
		super.rollToDropLoot();
	}

	@Override
	protected Item createLoot()
	{
		Dungeon.LimitedDrops.CRAB_MEAT.count++;
		return super.createLoot();
	}
}
