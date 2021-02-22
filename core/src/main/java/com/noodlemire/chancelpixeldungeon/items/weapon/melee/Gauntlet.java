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

package com.noodlemire.chancelpixeldungeon.items.weapon.melee;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class Gauntlet extends MeleeWeapon
{
	{
		image = ItemSpriteSheet.GAUNTLETS;
		tier = 5;
		DLY = 0.5f; //2x speed
	}

	@Override
	public int max(int lvl)
	{
		return Math.round(super.max(lvl) * 0.5f); //15 base, down from 30, and +3 per level, down from +6
	}

	@Override
	public int defenseFactor(Char owner)
	{
		return 5;    //5 extra defence
	}

	@Override
	public int crit(Char attacker, Char defender, int damage)
	{
		//No on-hit bonus, see speedfactor
		return damage;
	}

	@Override
	public float speedFactor(Char owner)
	{
		if(owner instanceof Hero && ((Hero) owner).critBoost(this))
			return 0;
		else
			return super.speedFactor(owner);
	}
}
