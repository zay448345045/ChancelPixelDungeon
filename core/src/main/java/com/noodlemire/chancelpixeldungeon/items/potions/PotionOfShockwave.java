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

package com.noodlemire.chancelpixeldungeon.items.potions;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Paralysis;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class PotionOfShockwave extends Potion implements Hero.Doom
{

	{
		initials = 8;

		if(isIdentified()) defaultAction = AC_THROW;
	}

	@Override
	public void shatter(int cell)
	{
		splash(cell);
		Sample.INSTANCE.play(Assets.SND_SHATTER);

		for(int x = -4; x < 4; x++)
		{
			for(int y = -4 * Dungeon.level.width(); y < 4 * Dungeon.level.width(); y += Dungeon.level.width())
			{
				int falling_rock_cell = cell + x + y;

				if(falling_rock_cell >= 0 && falling_rock_cell < Dungeon.level.map.length)
				{
					CellEmitter.get(falling_rock_cell).start(Speck.factory(Speck.ROCK), 0.07f, 10);

					if(Dungeon.level.water[falling_rock_cell])
						GameScene.ripple(falling_rock_cell);

					Dungeon.level.press(falling_rock_cell, null, true);

					Char ch = Actor.findChar(falling_rock_cell);
					if(ch != null)
					{
						Buff.prolong(ch, Paralysis.class, 8);
						ch.damage(Random.Int(1, Dungeon.depth), this);
					}
				}
			}
		}

		Camera.main.shake(3, 0.7f);
		Sample.INSTANCE.play(Assets.SND_ROCKS);
	}

	@Override
	public void setKnown()
	{
		super.setKnown();
		if(isIdentified()) defaultAction = AC_THROW;
	}

	@Override
	public int price()
	{
		return isKnown() ? 40 * quantity : super.price();
	}

	@Override
	public void onDeath()
	{
		Dungeon.fail(getClass());
		GLog.n(Messages.get(this, "ondeath"));
	}
}
