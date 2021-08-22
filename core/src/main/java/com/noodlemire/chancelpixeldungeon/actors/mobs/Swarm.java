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
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Burning;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Corruption;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Poison;
import com.noodlemire.chancelpixeldungeon.effects.Pushing;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHealing;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.levels.features.Door;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.SwarmSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Swarm extends Mob
{
	{
		spriteClass = SwarmSprite.class;

		EXP = Random.IntRange(3, 5);

		setHT(20 + EXP * 10, true);

		flying = true;

		loot = new PotionOfHealing();
		lootChance = 0.75f; //by default, see rollToDropLoot()
	}

	private static final float SPLIT_DELAY = 1f;

	private int generation = 1;

	private static final String GENERATION = "generation";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(GENERATION, generation);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		generation = bundle.getInt(GENERATION);
		if(generation > 1) EXP = 0;
	}

	@Override
	public int damageRoll()
	{
		return EXP + 1;
	}

	@Override
	public int defenseProc(Char enemy, int damage)
	{
		if(HP() >= damage + 2)
		{
			ArrayList<Integer> candidates = new ArrayList<>();
			boolean[] solid = Dungeon.level.solid;

			int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
			for(int n : neighbours)
				if(!solid[n] && Actor.findChar(n) == null)
					candidates.add(n);

			if(candidates.size() > 0)
			{
				Swarm clone = split();
				clone.setHP((HP() - damage) / 2);
				clone.pos = Random.element(candidates);
				clone.state = clone.HUNTING;

				if(Dungeon.level.map[clone.pos] == Terrain.DOOR)
					Door.enter(clone.pos);

				GameScene.add(clone, SPLIT_DELAY);
				Actor.addDelayed(new Pushing(clone, pos, clone.pos), -1);

				setHP(HP() - clone.HP());
			}
		}

		return super.defenseProc(enemy, damage);
	}

	@Override
	public int attackSkill(Char target)
	{
		return EXP * 3;
	}

	@Override
	public int defenseSkill()
	{
		return 2 + EXP;
	}

	private Swarm split()
	{
		Swarm clone = new Swarm();
		clone.generation = generation + 1;
		if(buff(Burning.class) != null)
		{
			Buff.affect(clone, Burning.class).reignite();
		}
		if(buff(Poison.class) != null)
		{
			Buff.affect(clone, Poison.class).set(2);
		}
		if(buff(Corruption.class) != null)
		{
			Buff.affect(clone, Corruption.class);
		}
		return clone;
	}

	@Override
	public void die(Object cause)
	{
		if(generation > 1)
			EXP = 0;

		super.die(cause);
	}

	@Override
	public void rollToDropLoot()
	{
		lootChance *= (1f / generation) * ((5 - Dungeon.LimitedDrops.SWARM_HP.count) / 5f);
		super.rollToDropLoot();
	}

	@Override
	protected Item createLoot()
	{
		Dungeon.LimitedDrops.SWARM_HP.count++;
		return super.createLoot();
	}
}
