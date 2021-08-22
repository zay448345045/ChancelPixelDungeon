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

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Challenges;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Pushing;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Gold;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfSupernova;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.MimicSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Mimic extends Mob
{
	public Mimic()
	{
		this(Dungeon.depth);
	}

	public Mimic(int level)
	{
		super();

		spriteClass = MimicSprite.class;

		EXP = level;

		TIME_TO_REST = 2;

		setHT((1 + EXP) * 7, true);

		properties.add(Property.DEMONIC);
	}

	public ArrayList<Item> items;

	private static final String LEVEL = "level";
	private static final String ITEMS = "items";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		if(items != null) bundle.put(ITEMS, items);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		if(bundle.contains(ITEMS))
		{
			items = new ArrayList<>((Collection<Item>) ((Collection<?>) bundle.getCollection(ITEMS)));
		}

		super.restoreFromBundle(bundle);
	}

	@Override
	public int damageRoll()
	{
		return HT() / 3;
	}

	@Override
	public int attackSkill(Char target)
	{
		return 9 + EXP;
	}

	@Override
	public int defenseSkill()
	{
		return attackSkill(null) / 2;
	}

	@Override
	public void rollToDropLoot()
	{
		if(items != null)
		{
			for(Item item : items)
			{
				Dungeon.level.drop(item, pos).sprite.drop();
			}
			items = null;
		}
		super.rollToDropLoot();
	}

	@Override
	public boolean reset()
	{
		state = WANDERING;
		return true;
	}

	public static Mimic spawnAt(int pos, List<Item> items)
	{
		return spawnAt(pos, items, Dungeon.depth);
	}

	public static Mimic spawnAt(int pos, List<Item> items, int level)
	{
		if(Dungeon.level.pit[pos]) return null;
		Char ch = Actor.findChar(pos);
		if(ch != null)
		{
			ArrayList<Integer> candidates = new ArrayList<>();
			for(int n : PathFinder.NEIGHBOURS8)
			{
				int cell = pos + n;
				if((Dungeon.level.passable[cell] || Dungeon.level.avoid[cell]) && Actor.findChar(cell) == null)
				{
					candidates.add(cell);
				}
			}
			if(candidates.size() > 0)
			{
				int newPos = Random.element(candidates);
				Actor.addDelayed(new Pushing(ch, ch.pos, newPos), -1);

				ch.pos = newPos;
				Dungeon.level.press(newPos, ch);

			}
			else
			{
				return null;
			}
		}

		Mimic m = new Mimic(level);
		m.items = new ArrayList<>(items);
		m.lookForEnemy(true);
		m.pos = pos;
		m.state = m.HUNTING;
		GameScene.add(m, 1);

		m.sprite.turnTo(pos, Dungeon.hero.pos);

		if(Dungeon.level.heroFOV[m.pos])
			CellEmitter.get(pos).burst(Speck.factory(Speck.STAR), 10);

		Dungeon.playAt(Assets.SND_MIMIC, pos);


		//generate an extra reward for killing the mimic
		Item reward = null;
		do
		{
			switch(Random.Int(5))
			{
				case 0:
					reward = new Gold().random();
					break;
				case 1:
					reward = Generator.randomMissile();
					break;
				case 2:
					reward = Generator.randomArmor();
					break;
				case 3:
					reward = Generator.randomWeapon();
					break;
				case 4:
					reward = Generator.random(Generator.Category.RING);
					break;
			}
		}
		while(reward == null || Challenges.isItemBlocked(reward));
		m.items.add(reward);

		return m;
	}

	{
		immunities.add(ScrollOfSupernova.class);
	}
}
