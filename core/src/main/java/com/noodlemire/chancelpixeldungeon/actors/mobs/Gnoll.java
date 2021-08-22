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
import com.noodlemire.chancelpixeldungeon.items.Gold;
import com.noodlemire.chancelpixeldungeon.sprites.GnollSprite;
import com.watabou.utils.Random;

public class Gnoll extends Mob
{
	{
		spriteClass = GnollSprite.class;

		EXP = Random.IntRange(2, 4);

		TIME_TO_REST = 2;

		setHT(11 + EXP, true);

		loot = Gold.class;
		lootChance = 0.5f;
	}

	@Override
	public int damageRoll()
	{
		return EXP * 2 + 2;
	}

	@Override
	public int attackSkill(Char target)
	{
		return 6 + EXP * 2;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, EXP);
	}

	@Override
	public int defenseSkill()
	{
		return 2 + EXP;
	}
}
