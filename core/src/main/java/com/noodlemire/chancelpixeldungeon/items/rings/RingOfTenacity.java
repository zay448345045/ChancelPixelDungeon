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

package com.noodlemire.chancelpixeldungeon.items.rings;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.ui.QuickSlotButton;

import java.text.DecimalFormat;

public class RingOfTenacity extends Ring
{
	{
		icon = ItemSpriteSheet.Icons.RING_TENACITY;
	}

	@Override
	protected RingBuff buff()
	{
		return new Tenacity();
	}

	public static int levelBonus(Char t)
	{
		int bonus = getBonus(t, Tenacity.class);
		if(bonus == 0)
			return 0;

		bonus += Math.abs(bonus) / bonus;

		return (int)(bonus * ((1f - (float)t.HP() / t.HT())));
	}

	public String statsInfo()
	{
		if(isIdentified())
			return Messages.get(this, "stats", soloBonus(), new DecimalFormat("#.##").format(100f * (1 / (Math.abs(soloBonus()) + 1f))));
		else
			return Messages.get(this, "typical_stats", 1, new DecimalFormat("#.##").format(50f));
	}

	private class Tenacity extends RingBuff
	{
		@Override
		public boolean act()
		{
			QuickSlotButton.refresh();

			return super.act();
		}
	}
}

