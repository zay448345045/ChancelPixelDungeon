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

package com.noodlemire.chancelpixeldungeon.actors.mobs;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.StenchGas;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Ooze;
import com.noodlemire.chancelpixeldungeon.actors.mobs.npcs.Ghost;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.FetidRatSprite;
import com.watabou.utils.Random;

public class FetidRat extends Rat
{
	{
		spriteClass = FetidRatSprite.class;

		setHT(21, true);

		EXP = 4;

		state = WANDERING;

		properties.add(Property.MINIBOSS);
		properties.add(Property.DEMONIC);
	}

	@Override
	public int attackProc(Char enemy, int damage)
	{
		damage = super.attackProc(enemy, damage);
		if(Random.Int(3) == 0)
		{
			Buff.affect(enemy, Ooze.class);
		}

		return damage;
	}

	@Override
	public int defenseProc(Char enemy, int damage)
	{
		GameScene.add(Blob.seed(pos, 20, StenchGas.class));

		return super.defenseProc(enemy, damage);
	}

	@Override
	public void die(Object cause)
	{
		super.die(cause);

		Ghost.Quest.process();
	}

	{
		immunities.add(StenchGas.class);
	}
}