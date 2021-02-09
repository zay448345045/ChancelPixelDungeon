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

import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Bestiary
{
	private static final String TOTAL_EXP = "totalExp";
	private static final int MAX_DEPTH = 25;

	private static final int[] totalEXP = new int[MAX_DEPTH];

	private static int maxEXP(int depth)
	{
		return 4 + 16 * depth;
	}

	public static Mob spawnMob(int depth, boolean ignoreEXP)
	{
		if(depth < 1 || depth > MAX_DEPTH || (!ignoreEXP && totalEXP[depth-1] >= maxEXP(depth)))
			return null;

		try
		{
			Mob mob = getRandomStandardMob(depth).newInstance();

			if(!ignoreEXP)
				totalEXP[depth-1] += mob.EXP;

			return mob;
		}
		catch (Exception e)
		{
			ChancelPixelDungeon.reportException(e);
			return null;
		}
	}

	private static Class<? extends Mob> getRandomStandardMob(int depth)
	{
		Class<? extends Mob> mob;

		switch(depth)
		{
			// Sewers
			case 1:
			default:
				//100% rat
				return Rat.class;
			case 2:
				//50% rat, 50% gnoll
				return Random.oneOf(Rat.class, Gnoll.class);
			case 3:
				//25% rat, 50% gnoll, 12.5% crab, 12.5% swarm
				return Random.oneOf(Rat.class, Rat.class,
						Gnoll.class, Gnoll.class, Gnoll.class, Gnoll.class,
						Crab.class, Swarm.class);
			case 4:
				mob = getRareMob(depth);
				if(mob != null) return mob;

				//14% rat, 28% gnoll, 42% crab, 14% swarm
				return Random.oneOf(Rat.class,
						Gnoll.class, Gnoll.class,
						Crab.class, Crab.class, Crab.class,
						Swarm.class);

			// Prison
			case 6:
				//60% skeleton, 20% thief, 20% swarm
				return Random.oneOf(Skeleton.class, Skeleton.class, Skeleton.class,
						Thief.class,
						Swarm.class);
			case 7:
				//50% skeleton, 17% thief, 17% shaman, 17% guard
				return Random.oneOf(Skeleton.class, Skeleton.class, Skeleton.class,
						Thief.class,
						Shaman.class,
						Guard.class);
			case 8:
				//37.5% skeleton, 12.5% thief, 25% shaman, 25% guard
				return Random.oneOf(Skeleton.class, Skeleton.class, Skeleton.class,
						Thief.class,
						Shaman.class, Shaman.class,
						Guard.class, Guard.class);
			case 9:
				mob = getRareMob(depth);
				if(mob != null) return mob;

				//33% skeleton, 11% thief, 22% shaman, 33% guard
				return Random.oneOf(Skeleton.class, Skeleton.class, Skeleton.class,
						Thief.class,
						Shaman.class, Shaman.class,
						Guard.class, Guard.class, Guard.class);

			// Caves
			case 11:
				//83% bat, 17% brute
				return Random.oneOf(
						Bat.class, Bat.class, Bat.class, Bat.class, Bat.class,
						Brute.class);
			case 12:
				//45% bat, 45% brute, 10% spinner
				return Random.oneOf(
						Bat.class, Bat.class, Bat.class, Bat.class, Bat.class,
						Brute.class, Brute.class, Brute.class, Brute.class, Brute.class,
						Spinner.class);
			case 13:
				//17% bat, 50% brute, 17% shaman, 17% spinner
				return Random.oneOf(
						Bat.class,
						Brute.class, Brute.class, Brute.class,
						Shaman.class,
						Spinner.class);
			case 14:
				mob = getRareMob(depth);
				if(mob != null) return mob;

				//11% bat, 33% brute, 11% shaman, 44% spinner
				return Random.oneOf(
						Bat.class,
						Brute.class, Brute.class, Brute.class,
						Shaman.class,
						Spinner.class, Spinner.class, Spinner.class, Spinner.class);

			// City
			case 16:
				//45% elemental, 45% warlock, 10% monk
				return Random.oneOf(
						Elemental.class, Elemental.class, Elemental.class, Elemental.class, Elemental.class,
						Warlock.class, Warlock.class, Warlock.class, Warlock.class, Warlock.class,
						Monk.class);
			case 17:
				//33% elemental, 33% warlock, 33% monk
				return Random.oneOf(Elemental.class, Warlock.class, Monk.class);
			case 18:
				//20% elemental, 20% warlock, 40% monk, 20% golem
				return Random.oneOf(
						Elemental.class,
						Warlock.class,
						Monk.class, Monk.class,
						Golem.class);
			case 19:
				mob = getRareMob(depth);
				if(mob != null) return mob;

				//14% elemental, 14% warlock, 28% monk, 42% golem
				return Random.oneOf(
						Elemental.class,
						Warlock.class,
						Monk.class, Monk.class,
						Golem.class, Golem.class, Golem.class);

			// Halls
			case 22:
				//50% succubus, 50% evil eye
				return Random.oneOf(Succubus.class, Eye.class);
			case 23:
				//25% succubus, 50% evil eye, 25% scorpio
				return Random.oneOf(
						Succubus.class,
						Eye.class, Eye.class,
						Scorpio.class);
			case 24:
				mob = getRareMob(depth);
				if(mob != null) return mob;

				//17% succubus, 33% evil eye, 50% scorpio
				return Random.oneOf(
						Succubus.class,
						Eye.class, Eye.class,
						Scorpio.class, Scorpio.class, Scorpio.class);
		}
	}

	private static Class<? extends Mob> getRareMob(int depth)
	{
		Class<? extends Mob> mob = null;

		if(depth / 5 + 1 > Dungeon.LimitedDrops.RARE_MOB.count)
		{
			switch(depth)
			{
				// Sewers
				case 4:
					mob = Random.oneOf(Skeleton.class, Thief.class, Albino.class);
					break;

				// Prison
				case 9:
					mob = Random.oneOf(Bat.class, Brute.class, Bandit.class);
					break;

				// Caves
				case 14:
					mob = Random.oneOf(Elemental.class, Monk.class, Shielded.class);
					break;

				// City
				case 19:
					mob = Random.oneOf(Succubus.class, Senior.class);
					break;

				case 24:
					mob = Acidic.class;
					break;
			}

			Dungeon.LimitedDrops.RARE_MOB.count++;
		}

		return mob;
	}

	public static void store(Bundle bundle)
	{
		for(int i  = 0; i < MAX_DEPTH; i++)
		{
			bundle.put(TOTAL_EXP + i, totalEXP[i]);
		}
	}

	public static void restore(Bundle bundle)
	{
		for(int i  = 0; i < MAX_DEPTH; i++)
		{
			if(bundle.contains(TOTAL_EXP + i))
				totalEXP[i] = bundle.getInt(TOTAL_EXP + i);
		}
	}

	public static void reset()
	{
		for(int i = 0; i < MAX_DEPTH; i++)
			totalEXP[i] = 0;
	}
}
