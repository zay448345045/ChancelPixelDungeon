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
import com.noodlemire.chancelpixeldungeon.effects.Splash;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Bleeding extends DurationBuff
{
	{
		type = buffType.NEGATIVE;
	}

	@Override
	public int icon()
	{
		return BuffIndicator.BLEEDING;
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	@Override
	public boolean act()
	{
		if(target.isAlive())
		{
			shorten(Random.NormalIntRange(0, (int) Math.ceil(left() / 2)));

			if(left() > 0)
			{
				target.damage((int) Math.ceil(left()), this);
				if(target.sprite.visible)
				{
					Splash.at(target.sprite.center(), -PointF.PI / 2, PointF.PI / 6,
							target.sprite.blood(), Math.min(10 * Math.round(left()) / target.HT(), 10));
				}

				if(target == Dungeon.hero && !target.isAlive())
				{
					Dungeon.fail(getClass());
					GLog.n(Messages.get(this, "ondeath"));
				}

				spend(TICK);
			}
		}
		else
			detach();

		return true;
	}

	@Override
	public String heroMessage()
	{
		return Messages.get(this, "heromsg");
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc", (int) Math.ceil(left()));
	}
}
