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
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Drowsy;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfLullaby extends Scroll
{
	{
		initials = 1;
	}

	@Override
	public void doRead()
	{
		curUser.sprite.centerEmitter().start(Speck.factory(Speck.NOTE), 0.3f, 5);
		Sample.INSTANCE.play(Assets.SND_LULLABY);

		singasong(curUser.viewDistance, true);

		GLog.i(Messages.get(this, "sooth"));

		readAnimation();
	}

	@Override
	public void doShout()
	{
		curUser.sprite.centerEmitter().start(Speck.factory(Speck.NOTE), 0.3f, 5);
		Sample.INSTANCE.play(Assets.SND_CHALLENGE);
		GLog.i(Messages.get(this, "too_loud"));
		readAnimation();
	}

	public static void singasong(int range, boolean affectSelf)
	{
		boolean[] effectiveFOV =
				range == curUser.viewDistance ?
						Dungeon.level.heroFOV :
						EnvironmentScroll.fovAt(curUser.pos, range, false);

		for(Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
		{
			if(effectiveFOV[mob.pos])
			{
				Buff.affect(mob, Drowsy.class);
				mob.sprite.centerEmitter().start(Speck.factory(Speck.NOTE), 0.3f, 5);
			}
		}

		if(affectSelf) Buff.affect(curUser, Drowsy.class);
	}

	@Override
	public void empoweredRead()
	{
		doRead();
		for(Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
		{
			if(Dungeon.level.heroFOV[mob.pos])
			{
				Buff drowsy = mob.buff(Drowsy.class);
				if(drowsy != null) drowsy.act();
			}
		}
	}

	@Override
	public int price()
	{
		return isKnown() ? 40 * quantity : super.price();
	}
}
