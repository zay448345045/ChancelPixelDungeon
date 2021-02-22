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

package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Transparency;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Identification;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.effects.SpellSprite;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.noodlemire.chancelpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ScrollOfIdentify extends InventoryScroll
{
	{
		icon = ItemSpriteSheet.Icons.SCROLL_IDENTIFY;
		mode = WndBag.Mode.UNIDENTIFED;

		bones = true;
	}

	@Override
	public void empoweredRead()
	{
		ArrayList<Item> unIDed = new ArrayList<>();

		for(Item i : curUser.belongings)
		{
			if(!i.isIdentified())
			{
				unIDed.add(i);
			}
		}

		if(unIDed.size() > 1)
		{
			Random.element(unIDed).identify();
			Sample.INSTANCE.play(Assets.SND_TELEPORT);
		}

		doRead();
	}

	@Override
	protected void onItemSelected(Item item)
	{
		curUser.sprite.parent.add(new Identification(curUser.sprite.center().offset(0, -16)));

		item.identify();
		GLog.i(Messages.get(this, "it_is", item));

		Badges.validateItemLevelAquired(item);
	}

	@Override
	public void doShout()
	{
		Buff.affect(curUser, Transparency.class, Transparency.DURATION);
		Dungeon.observe();

		boolean noticed = false;

		for(int i = 0; i < Dungeon.level.length(); i++)
		{
			int terr = Dungeon.level.map[i];

			if(Dungeon.level.discoverable[i] && Dungeon.level.heroFOV[i])
			{
				Dungeon.level.visited[i] = true;
				if((Terrain.flags[terr] & Terrain.SECRET) != 0)
				{
					Dungeon.level.discover(i);

					GameScene.discoverTile(i, terr);
					discover(i);

					noticed = true;
				}
			}
		}

		GameScene.updateFog();

		GLog.i(Messages.get(this, "layout"));
		if(noticed)
			Sample.INSTANCE.play(Assets.SND_SECRET);

		SpellSprite.show(curUser, SpellSprite.MAP);
		Sample.INSTANCE.play(Assets.SND_CHALLENGE);

		readAnimation();
	}

	@Override
	public int price()
	{
		return isKnown() ? 30 * quantity : super.price();
	}

	public static void discover(int cell)
	{
		CellEmitter.get(cell).start(Speck.factory(Speck.DISCOVER), 0.1f, 4);
	}
}
