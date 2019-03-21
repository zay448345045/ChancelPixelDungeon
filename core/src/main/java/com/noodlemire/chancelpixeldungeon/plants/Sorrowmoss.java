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

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Poison;
import com.noodlemire.chancelpixeldungeon.actors.buffs.ToxicImbue;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.particles.PoisonParticle;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHydrocombustion;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfShockwave;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfToxicity;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class Sorrowmoss extends Plant
{
	{
		image = 2;
	}

	@Override
	public void activate(Char ch, boolean doWardenBonus)
	{
		if(ch != null)
			if(doWardenBonus)
				Buff.affect(ch, ToxicImbue.class).set(15f);
			else
				Buff.affect(ch, Poison.class).set(4 + Dungeon.depth / 2f);

		if(Dungeon.level.heroFOV[pos])
			CellEmitter.center(pos).burst(PoisonParticle.SPLASH, 3);
	}

	public static class Seed extends Plant.Seed
	{
		{
			image = ItemSpriteSheet.SEED_SORROWMOSS;

			plantClass = Sorrowmoss.class;
			alchemyClass = PotionOfToxicity.class;
			alchemyClassSecondary = PotionOfShockwave.class;
			alchemyClassFinal = PotionOfHydrocombustion.class;
		}
	}
}
