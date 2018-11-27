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

package com.noodlemire.chancelpixeldungeon.plants;

import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Invisibility;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Might;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MindVision;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfInvisibility;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfMight;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfTelepathy;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class Deadnettle extends Plant {

	{
		image = 11;
	}

	@Override
	public void activate() {
		Char ch = Actor.findChar(pos);

		if (ch != null)
		{
			int damage = ch.HP / 2;

			if(damage > 0)
			{
				ch.damage(damage, this);

				Buff.prolong(ch, Might.class, damage).set(true);
				Buff.prolong(ch, MindVision.class, damage);
				Buff.prolong(ch, Invisibility.class, damage);
			}
		}
	}

	public static class Seed extends Plant.Seed{

		{
			image = ItemSpriteSheet.SEED_DEADNETTLE;

			plantClass = Deadnettle.class;
			alchemyClass = PotionOfMight.class;
			alchemyClassSecondary = PotionOfTelepathy.class;
			alchemyClassFinal = PotionOfInvisibility.class;
		}
	}
}
