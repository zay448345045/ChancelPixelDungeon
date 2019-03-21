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

package com.noodlemire.chancelpixeldungeon.items.weapon.missiles.darts;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Invisibility;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Might;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MindVision;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class DemonDart extends TippedDart
{
	{
		image = ItemSpriteSheet.DEMON_DART;
	}

	@Override
	public int proc(Char attacker, Char defender, int damage)
	{
		int cost = defender.HP() / 2;

		if(cost > 0)
		{
			defender.damage(cost, this);

			Buff.prolong(defender, Might.class, cost).set(true);
			Buff.prolong(defender, MindVision.class, cost);
			Buff.prolong(defender, Invisibility.class, cost);
		}

		return super.proc(attacker, defender, damage);
	}
}
