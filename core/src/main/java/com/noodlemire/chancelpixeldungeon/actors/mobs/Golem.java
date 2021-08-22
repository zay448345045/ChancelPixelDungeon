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

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Amok;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Sleep;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Terror;
import com.noodlemire.chancelpixeldungeon.actors.mobs.npcs.Imp;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.sprites.GolemSprite;
import com.watabou.utils.Random;

public class Golem extends Mob
{
	{
		spriteClass = GolemSprite.class;

		EXP = Random.IntRange(19, 21);

		TIME_TO_REST = 4;

		setHT(EXP * 10 - 99, true);

		loot = Generator.Category.STONE;
		lootChance = 0.5f;

		properties.add(Property.INORGANIC);
	}

	@Override
	public int damageRoll()
	{
		return EXP * 4;
	}

	@Override
	public int attackSkill(Char target)
	{
		return EXP * 2;
	}

	@Override
	protected float attackDelay()
	{
		return 1.5f;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, EXP - 7);
	}

	@Override
	public int defenseSkill()
	{
		return EXP - 1;
	}

	@Override
	public void rollToDropLoot()
	{
		Imp.Quest.process(this);

		super.rollToDropLoot();
	}

	{
		immunities.add(Amok.class);
		immunities.add(Terror.class);
		immunities.add(Sleep.class);
	}
}
