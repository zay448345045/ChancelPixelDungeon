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

import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class ThrowingStone extends MissileWeapon
{
	{
		image = ItemSpriteSheet.THROWING_STONE;
		tier = 1;
		sticky = false;
		baseUses = 3;
		bones = false;
	}

	@Override
	public int min(int lvl)
	{
		return tier +                      //1 base, down from 2
		       (tier == 1 ? lvl : lvl / 2);  //scaling unchanged
	}

	@Override
	public int price()
	{
		return quantity;
	}
}
