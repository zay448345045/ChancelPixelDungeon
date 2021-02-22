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
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Paralysis;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class ThrowingHammer extends MissileWeapon
{
	{
		image = ItemSpriteSheet.THROWING_HAMMER;
		tier = 5;
		baseUses = 15;
		sticky = false;
	}

	@Override
	public int min(int lvl)
	{
		return Math.round(1.6f * tier) +   //8 base, down from 10
		       (tier == 1 ? lvl : lvl / 2);  //scaling unchanged
	}

	@Override
	public int max(int lvl)
	{
		return 4 * tier +                  //20 base, down from 25
		       (tier) * lvl;               //scaling unchanged
	}

	@Override
	public int crit(Char attacker, Char defender, int damage)
	{
		Buff.prolong(defender, Paralysis.class, 2);

		return damage;
	}
}
