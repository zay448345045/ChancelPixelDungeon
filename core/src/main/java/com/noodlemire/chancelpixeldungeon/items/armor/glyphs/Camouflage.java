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

package com.noodlemire.chancelpixeldungeon.items.armor.glyphs;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Invisibility;
import com.noodlemire.chancelpixeldungeon.items.armor.Armor;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Camouflage extends Armor.Glyph
{

	private static final ItemSprite.Glowing GREEN = new ItemSprite.Glowing(0x448822);

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage)
	{
		//no proc effect, see HighGrass.trample
		return damage;
	}

	@Override
	public ItemSprite.Glowing glowing()
	{
		return GREEN;
	}

	public static class Camo extends Invisibility
	{
		private int pos;
		private int left;
		private int max;

		@Override
		public boolean act()
		{
			left--;
			if(left == 0 || target.pos != pos)
			{
				detach();
			}
			else
			{
				spend(TICK);
			}
			return true;
		}

		public void set(int time)
		{
			left = time;
			max = Math.max(max, left);
			pos = target.pos;
			Sample.INSTANCE.play(Assets.SND_MELD);
		}

		@Override
		public String toString()
		{
			return Messages.get(this, "name");
		}

		@Override
		public String desc()
		{
			return Messages.get(this, "desc", dispTurns(left));
		}

		private static final String POS = "pos";
		private static final String LEFT = "left";
		private static final String MAX = "max";

		@Override
		public void storeInBundle(Bundle bundle)
		{
			super.storeInBundle(bundle);
			bundle.put(POS, pos);
			bundle.put(LEFT, left);
			bundle.put(MAX, max);
		}

		@Override
		public void restoreFromBundle(Bundle bundle)
		{
			super.restoreFromBundle(bundle);
			pos = bundle.getInt(POS);
			left = bundle.getInt(LEFT);
			max = bundle.getInt(MAX);
		}

		@Override
		public float fadePercent()
		{
			return 1 - (float)left / max;
		}
	}
}

