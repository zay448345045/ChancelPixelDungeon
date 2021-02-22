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

package com.noodlemire.chancelpixeldungeon.items.potions;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.buffs.BlobImmunity;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.geysers.Geyser;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.BArray;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class PotionOfPurity extends Potion
{
	private static final int DISTANCE = 3;

	private static ArrayList<Class> affectedBlobs;

	static
	{
		affectedBlobs = new ArrayList<>(new BlobImmunity().immunities());
	}

	{
		icon = ItemSpriteSheet.Icons.POTION_PURITY;
	}

	@Override
	public void shatter(int cell)
	{
		purifyBlobs(cell);

		if(Dungeon.level.heroFOV[cell])
		{
			splash(cell);
			GLog.i(Messages.get(this, "freshness"));
		}

		Dungeon.playAt(Assets.SND_SHATTER, cell);
	}

	public static void purifyBlobs(int at)
	{
		PathFinder.buildDistanceMap(at, BArray.not(Dungeon.level.solid, null), DISTANCE);

		ArrayList<Blob> blobs = new ArrayList<>();
		for(Class c : affectedBlobs)
		{
			Blob b = Dungeon.level.blobs.get(c);
			if(b != null && b.volume > 0)
				blobs.add(b);
		}

		for(int i = 0; i < Dungeon.level.length(); i++)
		{
			if(PathFinder.distance[i] < Integer.MAX_VALUE)
			{
				for(Blob blob : blobs)
				{
					int value = blob.cur[i];
					if(value > 0)
					{
						blob.clear(i);
						blob.cur[i] = 0;
						blob.volume -= value;
					}
				}

				Geyser geyser = Dungeon.level.findGeyser(i);
				if(geyser != null)
					Buff.prolong(geyser, Geyser.Disabled.class, Geyser.Disabled.DURATION);

				if(Dungeon.level.heroFOV[i])
					CellEmitter.get(i).burst(Speck.factory(Speck.DISCOVER), 2);
			}
		}
	}

	@Override
	public void apply(Hero hero)
	{
		GLog.w(Messages.get(this, "protected"));
		Buff.prolong(hero, BlobImmunity.class, BlobImmunity.DURATION);
	}

	@Override
	public int price()
	{
		return isKnown() ? 40 * quantity : super.price();
	}
}
