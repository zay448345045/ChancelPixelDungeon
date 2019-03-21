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

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Invisibility;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHaste;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfInvisibility;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfRepulsion;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class Fadeleaf extends Plant
{
	{
		image = 6;
	}

	@Override
	public void activate(Char ch, boolean doWardenBonus)
	{
		if(doWardenBonus)
			Buff.affect(ch, Invisibility.class, 5f);
		ScrollOfTeleportation.randomTeleport(ch);
	}

	public static class Seed extends Plant.Seed
	{
		{
			image = ItemSpriteSheet.SEED_FADELEAF;

			plantClass = Fadeleaf.class;
			alchemyClass = PotionOfRepulsion.class;
			alchemyClassSecondary = PotionOfHaste.class;
			alchemyClassFinal = PotionOfInvisibility.class;
		}
	}
}
