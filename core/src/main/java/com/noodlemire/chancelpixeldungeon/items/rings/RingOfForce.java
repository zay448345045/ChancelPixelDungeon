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

import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.messages.Messages;

public class RingOfForce extends Ring
{
	@Override
	protected RingBuff buff()
	{
		return new Force();
	}

	public static int dynamicBonus(Hero target)
	{
		return Math.round(getBonus(target, Force.class) * target.dynamax() * 0.25f);
	}

	public static int dynamicExtension(Hero target)
	{
		int level = 0;

		for(Buff b : target.buffs())
			if(b instanceof Force)
				level += ((Force) b).level();

		if(level > 0)
			return Math.round((target.dynamax() + dynamicBonus(target)) * 0.3f);
		else if(level < 0)
			return -Math.round((target.dynamax() + dynamicBonus(target)) * 0.3f);
		else
			return 0;
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single)
	{
		if(super.doUnequip(hero, collect, single))
		{
			hero.dynamic(0);
			return true;
		}
		else
			return false;
	}

	@Override
	public String statsInfo()
	{
		if(isIdentified())
			return Messages.get(this, "stats", soloBonus() * 25);
		else
			return Messages.get(this, "avg_stats", 25);
	}

	public class Force extends RingBuff {}
}

