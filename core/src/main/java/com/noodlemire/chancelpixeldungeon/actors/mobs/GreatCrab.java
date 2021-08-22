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

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.mobs.npcs.Ghost;
import com.noodlemire.chancelpixeldungeon.items.food.MysteryMeat;
import com.noodlemire.chancelpixeldungeon.items.wands.Wand;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.sprites.GreatCrabSprite;
import com.noodlemire.chancelpixeldungeon.utils.GLog;

public class GreatCrab extends Crab
{
	{
		spriteClass = GreatCrabSprite.class;

		setHT(26, true);
		baseSpeed = 1f;

		EXP = 6;

		state = WANDERING;

		properties.add(Property.MINIBOSS);
	}

	private int moving = 0;

	@Override
	protected boolean getCloser(int target)
	{
		//this is used so that the crab remains slower, but still detects the player at the expected rate.
		moving++;
		if(moving < 3)
		{
			return super.getCloser(target);
		}
		else
		{
			moving = 0;
			return true;
		}
	}

	@Override
	public void damage(int dmg, Object src)
	{
		//crab blocks all attacks originating from the hero or enemy characters or traps if it is alerted.
		//All direct damage from these sources is negated, no exceptions. blob/debuff effects go through as normal.
		if((enemyInView() && state != SLEEPING && paralysed == 0)
		   && (src instanceof Wand || src instanceof Char))
		{
			GLog.n(Messages.get(this, "noticed"));
			sprite.showStatus(CharSprite.NEUTRAL, Messages.get(this, "blocked"));
		}
		else
			super.damage(dmg, src);
	}

	@Override
	public void die(Object cause)
	{
		super.die(cause);

		Ghost.Quest.process();

		Dungeon.level.drop(new MysteryMeat(), pos);
		Dungeon.level.drop(new MysteryMeat(), pos).sprite.drop();
	}
}
