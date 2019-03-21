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
import com.noodlemire.chancelpixeldungeon.actors.buffs.Bleeding;
import com.noodlemire.chancelpixeldungeon.actors.buffs.BlobImmunity;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Cripple;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Drowsy;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MagicalSleep;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Poison;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Slow;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Vertigo;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Weakness;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfExpulsion;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHealing;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfPurity;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;

public class Dreamfoil extends Plant
{
	{
		image = 10;
	}

	@Override
	public void activate(Char ch, boolean doWardenBonus)
	{
		if(ch != null)
		{
			if(ch instanceof Mob)
				Buff.affect(ch, MagicalSleep.class);
			else if(ch instanceof Hero)
			{
				GLog.i(Messages.get(this, "refreshed"));
				Buff.detach(ch, Poison.class);
				Buff.detach(ch, Cripple.class);
				Buff.detach(ch, Weakness.class);
				Buff.detach(ch, Bleeding.class);
				Buff.detach(ch, Drowsy.class);
				Buff.detach(ch, Slow.class);
				Buff.detach(ch, Vertigo.class);

				if(doWardenBonus)
					Buff.affect(ch, BlobImmunity.class, 10f);
			}
		}
	}

	public static class Seed extends Plant.Seed
	{
		{
			image = ItemSpriteSheet.SEED_DREAMFOIL;

			plantClass = Dreamfoil.class;
			alchemyClass = PotionOfExpulsion.class;
			alchemyClassSecondary = PotionOfPurity.class;
			alchemyClassFinal = PotionOfHealing.class;
		}
	}
}