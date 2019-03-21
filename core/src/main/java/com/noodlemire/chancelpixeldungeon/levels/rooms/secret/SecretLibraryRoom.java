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

package com.noodlemire.chancelpixeldungeon.levels.rooms.secret;

import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.items.scrolls.Scroll;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfBalance;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfCharm;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfCleansing;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfDarkness;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfDecay;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfInsulation;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfMagma;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfNecromancy;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfRage;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfReflection;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfSupernova;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTaunt;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTerror;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Random;

import java.util.HashMap;

//TODO specific implementation
public class SecretLibraryRoom extends SecretRoom
{
	@Override
	public int minWidth()
	{
		return Math.max(7, super.minWidth());
	}

	@Override
	public int minHeight()
	{
		return Math.max(7, super.minHeight());
	}

	private static HashMap<Class<? extends Scroll>, Float> scrollChances = new HashMap<>();

	static
	{
		scrollChances.put(ScrollOfIdentify.class, 1f);
		scrollChances.put(ScrollOfTeleportation.class, 1f);
		scrollChances.put(ScrollOfCleansing.class, 3f);
		scrollChances.put(ScrollOfRecharging.class, 1f);
		scrollChances.put(ScrollOfRage.class, 1f);
		scrollChances.put(ScrollOfTerror.class, 2f);
		scrollChances.put(ScrollOfLullaby.class, 2f);
		scrollChances.put(ScrollOfSupernova.class, 5f);
		scrollChances.put(ScrollOfReflection.class, 1f);
		scrollChances.put(ScrollOfDecay.class, 4f);
		scrollChances.put(ScrollOfBalance.class, 2f);
		scrollChances.put(ScrollOfDarkness.class, 1f);
		scrollChances.put(ScrollOfNecromancy.class, 2f);
		scrollChances.put(ScrollOfTransmutation.class, 5f);
		scrollChances.put(ScrollOfCharm.class, 2f);
		scrollChances.put(ScrollOfInsulation.class, 2f);
		scrollChances.put(ScrollOfTaunt.class, 2f);
		scrollChances.put(ScrollOfMagma.class, 2f);
	}

	public void paint(Level level)
	{
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.BOOKSHELF);

		Painter.fillEllipse(level, this, 2, Terrain.EMPTY_SP);

		Door entrance = entrance();
		if(entrance.x == left || entrance.x == right)
			Painter.drawInside(level, this, entrance, (width() - 3) / 2, Terrain.EMPTY_SP);
		else
			Painter.drawInside(level, this, entrance, (height() - 3) / 2, Terrain.EMPTY_SP);
		entrance.set(Door.Type.HIDDEN);

		int n = Random.IntRange(2, 3);
		HashMap<Class<? extends Scroll>, Float> chances = new HashMap<>(scrollChances);
		for(int i = 0; i < n; i++)
		{
			int pos;
			do
			{
				pos = level.pointToCell(random());
			}
			while(level.map[pos] != Terrain.EMPTY_SP || level.heaps.get(pos) != null);

			try
			{
				Class<? extends Scroll> scrollCls = Random.chances(chances);
				chances.put(scrollCls, 0f);
				level.drop(scrollCls.newInstance(), pos);
			}
			catch(Exception e)
			{
				ChancelPixelDungeon.reportException(e);
			}
		}
	}
}
