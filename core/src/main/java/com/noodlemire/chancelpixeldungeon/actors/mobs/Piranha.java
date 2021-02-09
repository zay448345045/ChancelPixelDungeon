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

import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.Statistics;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Burning;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Challenged;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Vertigo;
import com.noodlemire.chancelpixeldungeon.items.food.MysteryMeat;
import com.noodlemire.chancelpixeldungeon.levels.RegularLevel;
import com.noodlemire.chancelpixeldungeon.levels.rooms.Room;
import com.noodlemire.chancelpixeldungeon.levels.rooms.special.PoolRoom;
import com.noodlemire.chancelpixeldungeon.sprites.PiranhaSprite;
import com.watabou.utils.Random;

public class Piranha extends Mob
{
	{
		spriteClass = PiranhaSprite.class;

		baseSpeed = 2f;

		loot = MysteryMeat.class;
		lootChance = 1f;

		HUNTING = new Hunting();

		properties.add(Property.BLOB_IMMUNE);
	}

	public Piranha()
	{
		super();

		EXP = Dungeon.depth;
		setHT(15 + Dungeon.depth * 5, true);
	}

	@Override
	protected boolean act()
	{
		if(!Dungeon.level.water[pos])
		{
			die(null);
			sprite.killAndErase();
			return true;
		}
		else
			return super.act();
	}

	@Override
	public int damageRoll()
	{
		return Random.NormalIntRange(Dungeon.depth, 4 + Dungeon.depth * 2);
	}

	@Override
	public int attackSkill(Char target)
	{
		return 20 + Dungeon.depth * 2;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, Dungeon.depth);
	}

	@Override
	public int defenseSkill()
	{
		return 10 + Dungeon.depth * 2;
	}

	@Override
	public void die(Object cause)
	{
		super.die(cause);

		Statistics.piranhasKilled++;
		Badges.validatePiranhasKilled();
	}

	@Override
	public boolean reset()
	{
		return true;
	}

	@Override
	protected boolean getCloser(int target)
	{
		if(buff(Challenged.class) != null)
			return super.getCloser(target);

		if(rooted)
			return false;

		int step = Dungeon.findStep(this, pos, target,
				Dungeon.level.water,
				fieldOfView);
		if(step != -1)
		{
			move(step);
			return true;
		}
		else
			return false;
	}

	@Override
	protected boolean getFurther(int target)
	{
		int step = Dungeon.flee(this, pos, target,
				Dungeon.level.water,
				fieldOfView);
		if(step != -1)
		{
			move(step);
			return true;
		}
		else
			return false;
	}

	{
		immunities.add(Burning.class);
		immunities.add(Vertigo.class);
	}

	private class Hunting extends Mob.Hunting
	{
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted)
		{
			boolean result = super.act(enemyInFOV, justAlerted);
			//this causes piranha to move away when a door is closed on them in a pool room.
			if(state == WANDERING && Dungeon.level instanceof RegularLevel)
			{
				Room curRoom = ((RegularLevel) Dungeon.level).room(pos);
				if(curRoom instanceof PoolRoom)
				{
					target = Dungeon.level.pointToCell(curRoom.random(1));
				}
			}
			return result;
		}
	}
}
