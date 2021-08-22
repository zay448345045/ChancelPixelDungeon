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
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Fire;
import com.noodlemire.chancelpixeldungeon.effects.particles.FlameParticle;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Random;

public class FireImbue extends DurationBuff implements Expulsion, MeleeProc
{
	public static final float DURATION = 30f;

	@Override
	public boolean act()
	{
		if(Dungeon.level.map[target.pos] == Terrain.GRASS)
		{
			Level.set(target.pos, Terrain.EMBERS);
			GameScene.updateMap(target.pos);
		}

		spend(TICK);
		shorten(TICK);

		if(left() < 5)
			BuffIndicator.refreshHero();

		return true;
	}

	@Override
	public void proc(Char enemy)
	{
		if(Random.Int(2) == 0)
			Buff.affect(enemy, Burning.class).reignite();

		enemy.sprite.emitter().burst(FlameParticle.FACTORY, 2);
	}

	@Override
	public int icon()
	{
		return BuffIndicator.FIRE;
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
		return Fire.class;
	}

	{
		immunities.add(Burning.class);
	}
}
