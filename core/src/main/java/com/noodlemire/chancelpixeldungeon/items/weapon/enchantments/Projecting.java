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

package com.noodlemire.chancelpixeldungeon.items.weapon.enchantments;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;

public class Projecting extends Weapon.Enchantment
{
	private static final ItemSprite.Glowing GREY = new ItemSprite.Glowing(0x888888);

	@Override
	public boolean procChance(int level, Char attacker, Char defender, int damage)
	{
		return false;
	}

	@Override
	public int proc(Weapon weapon, Char attacker, Char defender, int damage)
	{
		//Does nothing as a proc, instead increases weapon range.
		//See weapon.reachFactor, and MissileWeapon.throwPos;
		return damage;
	}

	@Override
	public ItemSprite.Glowing glowing()
	{
		return GREY;
	}
}
