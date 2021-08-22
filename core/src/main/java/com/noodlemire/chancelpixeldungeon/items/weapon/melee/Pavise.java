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
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MagicShield;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class Pavise extends MeleeWeapon
{
	{
		image = ItemSpriteSheet.PAVISE;
		tier = 5;
	}

	@Override
	public int max(int lvl)
	{
		return Math.round(super.max(lvl) * 0.5f); //15 base, down from 30, and +3 per level, down from +6
	}

	@Override
	public int defenseFactor(Char owner)
	{
		return 10 + 3 * level(); //10 extra defence, plus 3 per level;
	}

	@Override
	public int crit(Char attacker, Char defender, int damage)
	{
		Buff.affect(attacker, MagicShield.class).set(Math.max(attacker.SHLD() - damage, 0));

		return damage;
	}
}
