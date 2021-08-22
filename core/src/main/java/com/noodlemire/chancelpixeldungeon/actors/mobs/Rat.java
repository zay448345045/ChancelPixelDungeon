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
import com.noodlemire.chancelpixeldungeon.sprites.RatSprite;
import com.watabou.utils.Random;

public class Rat extends Mob
{
	{
		spriteClass = RatSprite.class;

		EXP = Random.IntRange(1, 3);

		setHT(8 + EXP, true);
	}

	@Override
	public int damageRoll()
	{
		return EXP + 3;
	}

	@Override
	public int attackSkill(Char target)
	{
		return 7 + EXP;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, 1 + EXP / 2);
	}

	@Override
	public int defenseSkill()
	{
		return 2 + EXP / 2;
	}
}
