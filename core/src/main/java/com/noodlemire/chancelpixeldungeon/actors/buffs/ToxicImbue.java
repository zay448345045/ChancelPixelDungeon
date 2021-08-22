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
import com.noodlemire.chancelpixeldungeon.actors.blobs.ToxicGas;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;

public class ToxicImbue extends DurationBuff implements Expulsion
{
	public static final float DURATION = 30f;

	@Override
	public boolean act()
	{
		GameScene.add(Blob.seed(target.pos, 50, ToxicGas.class));

		spend(TICK);
		shorten(TICK);

		if(left() < 5)
			BuffIndicator.refreshHero();

		return true;
	}

	@Override
	public int icon()
	{
		return BuffIndicator.IMMUNITY;
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc", dispTurns(left()));
	}

	@Override
	public Class<? extends Blob> expulse()
	{
		return ToxicGas.class;
	}

	{
		immunities.add(ToxicGas.class);
		immunities.add(Poison.class);
	}
}
