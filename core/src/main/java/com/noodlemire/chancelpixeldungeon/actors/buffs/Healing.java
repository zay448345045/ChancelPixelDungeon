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

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Regrowth;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;

public class Healing extends DurationBuff implements Expulsion
{
	private float percentHealPerTick;
	private int flatHealPerTick;

	{
		//unlike other buffs, this one acts after the hero and takes priority against other effects
		//healing is much more useful if you get some of it off before taking damage
		actPriority = HERO_PRIO - 1;
	}

	@Override
	public boolean act()
	{
		int healingThisTick = Math.round(left() * percentHealPerTick) + flatHealPerTick;

		healingThisTick = (int) GameMath.gate(1, healingThisTick, left());

		target.heal(healingThisTick);

		shorten(healingThisTick);
		spend(TICK);

		return true;
	}

	public void setHeal(int amount, float percentPerTick, int flatPerTick)
	{
		set(amount);
		percentHealPerTick = percentPerTick;
		flatHealPerTick = flatPerTick;
	}

	@Override
	public Class<? extends Blob> expulse()
	{
		return Regrowth.class;
	}

	@Override
	public void fx(boolean on)
	{
		if(on) target.sprite.add(CharSprite.State.HEALING);
		else target.sprite.remove(CharSprite.State.HEALING);
	}

	private static final String PERCENT = "percent";
	private static final String FLAT = "flat";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(PERCENT, percentHealPerTick);
		bundle.put(FLAT, flatHealPerTick);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		percentHealPerTick = bundle.getFloat(PERCENT);
		flatHealPerTick = bundle.getInt(FLAT);
	}
}
