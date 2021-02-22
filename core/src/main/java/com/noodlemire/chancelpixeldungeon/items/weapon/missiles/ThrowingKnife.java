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

import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Blindness;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class ThrowingKnife extends MissileWeapon
{
	private Char enemy;

	{
		image = ItemSpriteSheet.THROWING_KNIFE;
		tier = 1;
		baseUses = 5;
		bones = false;
	}

	@Override
	public int max(int lvl)
	{
		return 6 * tier +                      //6 base, up from 5
		       (tier == 1 ? 2 * lvl : tier * lvl); //scaling unchanged
	}

	@Override
	protected void onThrow(int cell)
	{
		enemy = Actor.findChar(cell);
		super.onThrow(cell);
	}

	@Override
	public int damageRoll(Char owner)
	{
		if(owner instanceof Hero)
		{
			Hero hero = (Hero) owner;
			if(enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero))
			{
				return Math.round(super.damageRoll(owner) * 1.5f);
			}
		}
		return super.damageRoll(owner);
	}

	@Override
	public int crit(Char attacker, Char defender, int damage)
	{
		Buff.affect(defender, Blindness.class, 2);
		return damage;
	}
}
