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
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.RootCloud;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Barkskin;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.DurationBuff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Expulsion;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.particles.EarthParticle;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHealing;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfOvergrowth;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfShielding;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;

public class Earthroot extends Plant
{
	{
		image = 5;
	}

	@Override
	public void activate(Char ch, boolean doWardenBonus)
	{
		if(ch == Dungeon.hero)
			if(doWardenBonus)
				Buff.affect(ch, Barkskin.class).set((Dungeon.depth + 5) / 2f);
			else
				Buff.affect(ch, Armor.class).level(ch.HT());

		if(Dungeon.level.heroFOV[pos])
		{
			CellEmitter.bottom(pos).start(EarthParticle.FACTORY, 0.05f, 8);
			Camera.main.shake(1, 0.4f);
		}
	}

	public static class Seed extends Plant.Seed
	{
		{
			image = ItemSpriteSheet.SEED_EARTHROOT;

			plantClass = Earthroot.class;
			alchemyClass = PotionOfShielding.class;
			alchemyClassSecondary = PotionOfOvergrowth.class;
			alchemyClassFinal = PotionOfHealing.class;

			bones = true;
		}
	}

	public static class Armor extends DurationBuff implements Expulsion
	{
		private static final float STEP = 1f;

		private int pos;

		{
			type = buffType.POSITIVE;
		}

		@Override
		public boolean attachTo(Char target)
		{
			pos = target.pos;
			return super.attachTo(target);
		}

		@Override
		public boolean act()
		{
			if(target.pos != pos)
				detach();
			spend(STEP);
			return true;
		}

		private static int blocking()
		{
			return (Dungeon.depth + 5) / 2;
		}

		public int absorb(int damage)
		{
			int block = Math.min(damage, blocking());

			if(left() <= block)
				detach();
			else
			{
				shorten(block);
				BuffIndicator.refreshHero();
			}

			return damage - block;
		}

		public void level(int value)
		{
			set(value);
			BuffIndicator.refreshHero();

			pos = target.pos;
		}

		@Override
		public int icon()
		{
			return BuffIndicator.ARMOR;
		}

		@Override
		public String toString()
		{
			return Messages.get(this, "name");
		}

		@Override
		public String desc()
		{
			return Messages.get(this, "desc", blocking(), (int) left());
		}

		private static final String POS = "pos";

		@Override
		public Class<? extends Blob> expulse()
		{
			return RootCloud.class;
		}

		@Override
		public void storeInBundle(Bundle bundle)
		{
			super.storeInBundle(bundle);
			bundle.put(POS, pos);
		}

		@Override
		public void restoreFromBundle(Bundle bundle)
		{
			super.restoreFromBundle(bundle);
			pos = bundle.getInt(POS);
		}
	}
}
