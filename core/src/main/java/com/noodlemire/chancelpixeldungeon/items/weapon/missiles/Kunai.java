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

package com.noodlemire.chancelpixeldungeon.items.weapon.missiles;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class Kunai extends MissileWeapon
{
	{
		image = ItemSpriteSheet.KUNAI;
		tier = 2;
		baseUses = 20;
	}

	@Override
	public int min(int lvl)
	{
		return Math.round(super.min(lvl) * 0.75f);
	}

	@Override
	public int max(int lvl)
	{
		return Math.round(super.max(lvl) * 0.75f);
	}

	@Override
	public int crit(Char attacker, Char defender, int damage)
	{
		//See rangedHit()
		return damage;
	}

	@Override
	public void rangedHit(Char enemy, int cell, boolean returnToHero)
	{
		//Skip over the part that lowers durability on crit
		if(curUser.critBoost(this))
			afterThrow(enemy, cell, returnToHero);
		else
			super.rangedHit(enemy, cell, returnToHero);
	}
}