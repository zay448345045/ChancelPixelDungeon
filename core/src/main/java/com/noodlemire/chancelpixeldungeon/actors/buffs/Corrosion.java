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
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.CorrosiveGas;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class Corrosion extends DurationBuff implements Hero.Doom, Expulsion
{
	private float damage = 1;

	private static final String DAMAGE = "damage";

	{
		type = buffType.NEGATIVE;
	}

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(DAMAGE, damage);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		damage = bundle.getFloat(DAMAGE);
	}

	public void set(float duration, int damage)
	{
		set(duration);
		if(this.damage < damage)
			this.damage = damage;
	}

	@Override
	public int icon()
	{
		return BuffIndicator.POISON;
	}

	@Override
	public void tintIcon(Image icon)
	{
		icon.hardlight(1f, 0.5f, 0f);
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
		return Messages.get(this, "desc", dispTurns(left()), (int) damage);
	}

	@Override
	public boolean act()
	{
		if(target.isAlive())
		{
			target.damage((int) damage, this);
			if(damage < (Dungeon.depth / 2) + 2)
				damage++;
			else
				damage += 0.5f;

			spend(TICK);
			shorten(TICK);
		}
		else
			detach();

		return true;
	}

	@Override
	public Class<? extends Blob> expulse()
	{
		return CorrosiveGas.class;
	}

	@Override
	public void onDeath()
	{
		Dungeon.fail(getClass());
		GLog.n(Messages.get(this, "ondeath"));
	}
}
