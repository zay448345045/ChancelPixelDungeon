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
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Amok;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mimic;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.Heap;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRage extends Scroll
{
	{
		icon = ItemSpriteSheet.Icons.SCROLL_RAGE;
	}

	@Override
	public void doRead()
	{
		for(Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
		{
			mob.beckon(curUser.pos);
			if(Dungeon.level.heroFOV[mob.pos])
				Buff.prolong(mob, Amok.class, 5f);
		}

		for(Heap heap : Dungeon.level.heaps.values())
		{
			if(heap.type == Heap.Type.MIMIC)
			{
				Mimic m = Mimic.spawnAt(heap.pos, heap.items);
				if(m != null)
				{
					m.beckon(curUser.pos);
					heap.destroy();
				}
			}
		}

		GLog.w(Messages.get(this, "roar"));

		curUser.sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.3f, 3);
		Sample.INSTANCE.play(Assets.SND_CHALLENGE);

		readAnimation();
	}

	@Override
	public void empoweredRead()
	{
		for(Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
			if(Dungeon.level.heroFOV[mob.pos])
				Buff.prolong(mob, Amok.class, 5f);

		curUser.sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.3f, 3);
		Sample.INSTANCE.play(Assets.SND_READ);

		readAnimation();
	}

	@Override
	public int price()
	{
		return isKnown() ? 30 * quantity : super.price();
	}
}
