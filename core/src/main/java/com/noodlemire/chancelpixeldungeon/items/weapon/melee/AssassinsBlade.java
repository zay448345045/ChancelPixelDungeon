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
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class AssassinsBlade extends MeleeWeapon
{
	//25% extra damage on surprise attack
	float SURPRISE_BONUS_RATE = 1.25f;

	{
		image = ItemSpriteSheet.ASSASSINS_BLADE;
		tier = 4;
	}

	@Override
	public int max(int lvl)
	{
		//20 base, down from 25; scaling unchanged
		return super.max(lvl) - (tier + 1);
	}

	@Override
	public int damageRoll(Char owner)
	{
		if(owner instanceof Hero)
		{
			Hero hero = (Hero) owner;
			Char enemy = hero.enemy();
			if(enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero))
			{
				return Math.round(super.damageRoll(owner) * SURPRISE_BONUS_RATE);
			}
		}
		return super.damageRoll(owner);
	}
}
