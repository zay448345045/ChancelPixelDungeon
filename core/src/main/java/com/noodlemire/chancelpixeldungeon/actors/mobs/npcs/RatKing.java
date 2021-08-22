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

package com.noodlemire.chancelpixeldungeon.actors.mobs.npcs;

import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.RatKingSprite;
import com.watabou.utils.Bundle;

public class RatKing extends NPC
{
	private boolean trapped = true;

	{
		spriteClass = RatKingSprite.class;

		SLEEPING = new RatKingSleeping();
		state = SLEEPING;

		EXP = 999;
	}

	@Override
	public int defenseSkill(Char enemy)
	{
		return 1000;
	}

	@Override
	public float speed()
	{
		return 2f;
	}

	@Override
	protected Char chooseEnemy()
	{
		return null;
	}

	@Override
	public void damage(int dmg, Object src)
	{
	}

	@Override
	public void add(Buff buff)
	{
	}

	@Override
	public boolean reset()
	{
		return true;
	}

	@Override
	public boolean interact()
	{
		if(properties.contains(Property.IMMOVABLE))
		{
			yell(Messages.get(this, "zzz"));
			return false;
		}

		sprite.turnTo(pos, Dungeon.hero.pos);
		if(state == SLEEPING)
		{
			notice();
			yell(Messages.get(this, "not_sleeping"));
			state = WANDERING;
		}
		else
			yell(Messages.get(this, "what_is_it"));

		return true;
	}

	@Override
	public String description()
	{
		String desc = ((RatKingSprite) sprite).festive ?
				Messages.get(this, "desc_festive")
				: super.description();

		if(properties.contains(Property.IMMOVABLE))
			desc += "\n\n" + Messages.get(this, "trapped");

		return desc;
	}

	private static final String TRAPPED = "trapped";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(TRAPPED, trapped);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		trapped = bundle.getBoolean(TRAPPED);

		if(trapped || !bundle.contains(TRAPPED))
		{
			properties.add(Property.IMMOVABLE);
			state = SLEEPING;
		}
	}

	private class RatKingSleeping extends Sleeping
	{
		//Do not check anything when sleeping, until Goo dies.
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted)
		{
			if(Badges.isUnlockedLocal(Badges.Badge.BOSS_SLAIN_1))
			{
				trapped = false;
				properties.remove(Property.IMMOVABLE);
				return super.act(enemyInFOV, justAlerted);
			}
			else
			{
				trapped = true;
				properties.add(Property.IMMOVABLE);

				spend(TICK);
				return true;
			}
		}
	}
}
