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

import com.watabou.utils.Bundle;

//buff whose only internal logic is to wait and detach after a time.
public class FlavourBuff extends Buff implements FadePercent
{
	private static final String MAX = "max";

	private float max;

	@Override
	public boolean act()
	{
		detach();
		return true;
	}

	//flavour buffs can all just rely on cooldown()
	protected String dispTurns()
	{
		//add one turn as buffs act last, we want them to end at 1 visually, even if they end at 0 internally.
		return dispTurns(cooldown() + 1f);
	}

	@Override
	protected void spend(float time)
	{
		super.spend(time);

		max = Math.max(max, cooldown() + 1);
	}

	@Override
	protected void postpone(float time)
	{
		super.postpone(time);

		max = Math.max(max, time);
	}

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(MAX, max);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		max = bundle.getFloat(MAX);
	}

	@Override
	public float fadePercent()
	{
		return 1 - (cooldown() + 1) / max;
	}
}
