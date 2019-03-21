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

package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.effects.Splash;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfExpulsion;
import com.noodlemire.chancelpixeldungeon.items.scrolls.EnvironmentScroll;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Ooze extends Buff implements Expulsion
{
	{
		type = buffType.NEGATIVE;
	}

	@Override
	public int icon()
	{
		return BuffIndicator.OOZE;
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	@Override
	public String heroMessage()
	{
		return Messages.get(this, "heromsg");
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc");
	}

	@Override
	public boolean act()
	{
		if(target.isAlive())
		{
			if(Dungeon.depth > 4)
				target.damage(Dungeon.depth / 5, this);
			else if(Random.Int(2) == 0)
				target.damage(1, this);
			if(!target.isAlive() && target == Dungeon.hero)
			{
				Dungeon.fail(getClass());
				GLog.n(Messages.get(this, "ondeath"));
			}
			spend(TICK);
		}
		if(Dungeon.level.water[target.pos])
		{
			detach();
		}
		return true;
	}

	@Override
	public Class<? extends Blob> expulse()
	{
		boolean[] aoe = EnvironmentScroll.fovAt(target.pos, PotionOfExpulsion.MAX_RANGE, false);

		for(int i = 0; i < aoe.length; i++)
			if(aoe[i] && i != target.pos)
			{
				Splash.at(i, 0xFF000000, 5);
				Char ch = Actor.findChar(i);

				if(ch != null)
					Buff.affect(ch, Ooze.class);
			}

		return null;
	}
}
