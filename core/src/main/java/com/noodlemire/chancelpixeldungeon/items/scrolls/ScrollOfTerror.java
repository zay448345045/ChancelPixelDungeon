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
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Paralysis;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Terror;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.effects.Flare;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfTerror extends EnvironmentScroll
{
	{
		initials = 10;
		if(isIdentified()) defaultAction = AC_SHOUT;
	}

	@Override
	public void doRead()
	{
		Sample.INSTANCE.play(Assets.SND_READ);
		readAnimation();

		terrorize(curUser.pos);
	}

	@Override
	protected void onSelect(int cell)
	{
		terrorize(cell);
	}

	private void terrorize(int cell)
	{
		new Flare(5, 32).color(0xFF0000, true).show(curUser.sprite, cell, 2f);

		boolean[] aoe = fovAt(cell, curUser.viewDistance);

		int count = 0;
		Mob affected = null;
		for(Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
		{
			if(mob.pos != cell && aoe[mob.pos])
			{
				Buff.affect(mob, Terror.class, Terror.DURATION).set(cell);

				if(mob.buff(Terror.class) != null)
				{
					count++;
					affected = mob;
				}
			}
		}

		Char centerMob = Actor.findChar(cell);
		if(centerMob instanceof Mob)
		{
			Buff.affect(centerMob, Paralysis.class, Terror.DURATION);

			if(centerMob.buff(Paralysis.class) != null)
			{
				count++;
				affected = (Mob) centerMob;
			}
		}

		switch(count)
		{
			case 0:
				GLog.i(Messages.get(this, "none"));
				break;
			case 1:
				GLog.i(Messages.get(this, "one", affected.name));
				break;
			default:
				GLog.i(Messages.get(this, "many"));
		}
	}

	@Override
	public void empoweredRead()
	{
		terrorize(curUser.pos);
		for(Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
		{
			if(Dungeon.level.heroFOV[mob.pos])
			{
				Terror t = mob.buff(Terror.class);
				if(t != null)
				{
					Buff.prolong(mob, Terror.class, Terror.DURATION * 1.5f);
					Buff.affect(mob, Paralysis.class, Terror.DURATION * .5f);
				}
			}
		}
	}

	@Override
	public int price()
	{
		return isKnown() ? 30 * quantity : super.price();
	}

	@Override
	public void setKnown()
	{
		super.setKnown();
		if(isIdentified()) defaultAction = AC_SHOUT;
	}
}
