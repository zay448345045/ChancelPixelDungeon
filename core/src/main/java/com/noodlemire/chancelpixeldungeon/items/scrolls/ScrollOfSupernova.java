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

package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Blindness;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Paralysis;
import com.noodlemire.chancelpixeldungeon.actors.geysers.Geyser;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ScrollOfSupernova extends Scroll
{
	{
		icon = ItemSpriteSheet.Icons.SCROLL_SUPERNOVA;

		bones = true;
	}

	@Override
	public void doRead()
	{
		//Normal read is the same as empowered read, but with self-damage
		empoweredRead();

		curUser.damage(Math.max(curUser.HT() / 5, curUser.HP() / 2), this);
		if(curUser.isAlive())
		{
			Buff.prolong(curUser, Paralysis.class, Random.Int(4, 6));
			Buff.prolong(curUser, Blindness.class, Random.Int(6, 9));
			Dungeon.observe();
		}

		if(!curUser.isAlive())
		{
			Dungeon.fail(getClass());
			GLog.n(Messages.get(this, "ondeath"));
		}
	}

	@Override
	public void empoweredRead()
	{
		GameScene.flash(0xFFFFFF);

		Sample.INSTANCE.play(Assets.SND_BLAST);

		for(Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
		{
			if(Dungeon.level.heroFOV[mob.pos])
			{
				Ballistica wallDetect = new Ballistica(curUser.pos, mob.pos, Ballistica.STOP_TARGET);
				int terrainPassed = 1;

				for(int p : wallDetect.subPath(1, wallDetect.dist))
					if(Dungeon.level.solid[p])
						terrainPassed++;

				mob.damage(mob.HP() / terrainPassed, this);
				if(mob.isAlive())
				{
					Buff.prolong(mob, Paralysis.class, Paralysis.DURATION * (float) Math.pow(0.9, terrainPassed));
					Buff.prolong(mob, Blindness.class, 2 * Paralysis.DURATION * (float) Math.pow(0.95, terrainPassed));
				}
			}
		}

		for(Geyser geyser : Dungeon.level.geysers.toArray(new Geyser[0]))
		{
			if(Dungeon.level.heroFOV[geyser.pos])
			{
				Ballistica wallDetect = new Ballistica(curUser.pos, geyser.pos, Ballistica.STOP_TARGET);
				int terrainPassed = 1;

				for(int p : wallDetect.subPath(1, wallDetect.dist))
					if(Dungeon.level.solid[p])
						terrainPassed++;

				geyser.damage(geyser.HP() / terrainPassed, this);
			}
		}

		readAnimation();
	}

	@Override
	public int price()
	{
		return isKnown() ? 50 * quantity : super.price();
	}
}
