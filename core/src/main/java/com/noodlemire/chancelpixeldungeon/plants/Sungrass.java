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
import com.noodlemire.chancelpixeldungeon.actors.blobs.Regrowth;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.DurationBuff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Expulsion;
import com.noodlemire.chancelpixeldungeon.actors.buffs.FlavourBuff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Healing;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.effects.particles.ShaftParticle;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHealing;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class Sungrass extends Plant
{
	{
		image = 4;
	}

	@Override
	public void activate(Char ch, boolean doWardenBonus)
	{
		if(ch == Dungeon.hero)
			if(doWardenBonus)
				Buff.affect(ch, Healing.class).setHeal(ch.HT(), 0, 1);
			else
				Buff.affect(ch, Health.class).boost(ch.HT());

		if(Dungeon.level.heroFOV[pos])
			CellEmitter.get(pos).start(ShaftParticle.FACTORY, 0.2f, 3);
	}

	public static class Seed extends Plant.Seed
	{
		{
			image = ItemSpriteSheet.SEED_SUNGRASS;

			plantClass = Sungrass.class;
			alchemyClass = PotionOfHealing.class;

			bones = true;
		}
	}

	public static class Health extends DurationBuff implements Expulsion
	{
		private static final float STEP = 1f;

		private int pos;
		private float partialHeal;

		{
			type = buffType.POSITIVE;
		}

		@Override
		public boolean act()
		{
			if(target.pos != pos)
			{
				detach();
			}

			//for the hero, full heal takes ~50/93/111/120 turns at levels 1/10/20/30
			partialHeal += (40 + target.HT()) / 150f;

			if(partialHeal > 1)
			{
				target.heal((int) partialHeal);
				shorten((int) partialHeal);
				partialHeal -= (int) partialHeal;
				target.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);

				if(target instanceof Hero && target.HP() == target.HT())
					((Hero) target).resting = false;
			}

			BuffIndicator.refreshHero();
			spend(STEP);
			return true;
		}

		public void boost(int amount)
		{
			extend(amount);
			pos = target.pos;
		}

		@Override
		public int icon()
		{
			return BuffIndicator.HEALING;
		}

		@Override
		public void tintIcon(Image icon)
		{
			FlavourBuff.greyIcon(icon, target.HT() / 4f, left());
		}

		@Override
		public String toString()
		{
			return Messages.get(this, "name");
		}

		@Override
		public String desc()
		{
			return Messages.get(this, "desc", (int) left());
		}

		@Override
		public Class<? extends Blob> expulse()
		{
			return Regrowth.class;
		}

		private static final String POS = "pos";
		private static final String PARTIAL = "partial_heal";

		@Override
		public void storeInBundle(Bundle bundle)
		{
			super.storeInBundle(bundle);
			bundle.put(POS, pos);
			bundle.put(PARTIAL, partialHeal);
		}

		@Override
		public void restoreFromBundle(Bundle bundle)
		{
			super.restoreFromBundle(bundle);
			pos = bundle.getInt(POS);
			partialHeal = bundle.getFloat(PARTIAL);
		}
	}
}
