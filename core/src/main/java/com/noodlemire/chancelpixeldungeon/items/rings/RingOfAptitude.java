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

import java.text.DecimalFormat;

public class RingOfAptitude extends Ring
{
	{
		icon = ItemSpriteSheet.Icons.RING_APTITUDE;
	}

	@Override
	protected RingBuff buff()
	{
		return new Aptitude();
	}

	public static float experienceMultiplier(Char target)
	{
		return (float) Math.pow(1.15f, getBonus(target, Aptitude.class));
	}

	@Override
	public String statsInfo()
	{
		if(isIdentified())
			return Messages.get(this, "stats", new DecimalFormat("#.##").format(100f * (Math.pow(1.15f, soloBonus()) - 1f)));
		else
			return Messages.get(this, "typical_stats", new DecimalFormat("#.##").format(15f));
	}

	public class Aptitude extends RingBuff {}
}

