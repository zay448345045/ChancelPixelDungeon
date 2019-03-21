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

package com.noodlemire.chancelpixeldungeon.actors.blobs;

import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.BlobEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class ToxicGas extends GasBlob implements Hero.Doom
{
	@Override
	void affect(Char ch, int cell)
	{
		int levelDamage = 5 + Dungeon.depth * 5;

		int damage = (ch.HT() + levelDamage) / 40;
		if(Random.Int(40) < (ch.HT() + levelDamage) % 40)
		{
			damage++;
		}

		ch.damage(damage, this);
	}

	@Override
	void affect(int cell)
	{
	}

	@Override
	public void use(BlobEmitter emitter)
	{
		super.use(emitter);

		emitter.pour(Speck.factory(Speck.TOXIC), 0.4f);
	}

	@Override
	public String tileDesc()
	{
		return Messages.get(this, "desc");
	}

	@Override
	public void onDeath()
	{
		Badges.validateDeathFromGas();

		Dungeon.fail(getClass());
		GLog.n(Messages.get(this, "ondeath"));
	}
}
