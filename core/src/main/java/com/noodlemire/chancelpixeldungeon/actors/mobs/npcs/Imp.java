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

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Golem;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Monk;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.quest.DwarfToken;
import com.noodlemire.chancelpixeldungeon.items.rings.Ring;
import com.noodlemire.chancelpixeldungeon.journal.Notes;
import com.noodlemire.chancelpixeldungeon.levels.CityLevel;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ImpSprite;
import com.noodlemire.chancelpixeldungeon.windows.WndImp;
import com.noodlemire.chancelpixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Imp extends NPC
{
	{
		spriteClass = ImpSprite.class;

		properties.add(Property.IMMOVABLE);

		EXP = 30;
	}

	private boolean seenBefore = false;

	@Override
	protected boolean act()
	{

		if(!Quest.given && Dungeon.level.heroFOV[pos])
		{
			if(!seenBefore)
			{
				yell(Messages.get(this, "hey", Dungeon.hero.givenName()));
			}
			seenBefore = true;
		}
		else
		{
			seenBefore = false;
		}

		throwItem();

		return super.act();
	}

	@Override
	public int defenseSkill(Char enemy)
	{
		return 1000;
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

		sprite.turnTo(pos, Dungeon.hero.pos);
		if(Quest.given)
		{

			DwarfToken tokens = Dungeon.hero.belongings.getItem(DwarfToken.class);
			if(tokens != null && (tokens.quantity() >= 8 || (!Quest.alternative && tokens.quantity() >= 6)))
			{
				GameScene.show(new WndImp(this, tokens));
			}
			else
			{
				tell(Quest.alternative ?
						Messages.get(this, "monks_2", Dungeon.hero.givenName())
						: Messages.get(this, "golems_2", Dungeon.hero.givenName()));
			}

		}
		else
		{
			tell(Quest.alternative ? Messages.get(this, "monks_1") : Messages.get(this, "golems_1"));
			Quest.given = true;
			Quest.completed = false;

			Notes.add(Notes.Landmark.IMP);
		}

		return false;
	}

	private void tell(String text)
	{
		GameScene.show(
				new WndQuest(this, text));
	}

	public void flee()
	{

		yell(Messages.get(this, "cya", Dungeon.hero.givenName()));

		destroy();
		sprite.die();
	}

	public static class Quest
	{

		private static boolean alternative;

		private static boolean spawned;
		private static boolean given;
		private static boolean completed;

		public static Ring reward;

		public static void reset()
		{
			spawned = false;

			reward = null;
		}

		private static final String NODE = "demon";

		private static final String ALTERNATIVE = "alternative";
		private static final String SPAWNED = "spawned";
		private static final String GIVEN = "given";
		private static final String COMPLETED = "completed";
		private static final String REWARD = "reward";

		public static void storeInBundle(Bundle bundle)
		{

			Bundle node = new Bundle();

			node.put(SPAWNED, spawned);

			if(spawned)
			{
				node.put(ALTERNATIVE, alternative);

				node.put(GIVEN, given);
				node.put(COMPLETED, completed);
				node.put(REWARD, reward);
			}

			bundle.put(NODE, node);
		}

		public static void restoreFromBundle(Bundle bundle)
		{

			Bundle node = bundle.getBundle(NODE);

			if(!node.isNull() && (spawned = node.getBoolean(SPAWNED)))
			{
				alternative = node.getBoolean(ALTERNATIVE);

				given = node.getBoolean(GIVEN);
				completed = node.getBoolean(COMPLETED);
				reward = (Ring) node.get(REWARD);
			}
		}

		public static void spawn(CityLevel level)
		{
			if(!spawned && Dungeon.depth > 16 && Random.Int(20 - Dungeon.depth) == 0)
			{

				Imp npc = new Imp();
				do
				{
					npc.pos = level.randomRespawnCell();
				}
				while(
						npc.pos == -1 ||
						level.heaps.get(npc.pos) != null ||
						level.traps.get(npc.pos) != null ||
						level.findMob(npc.pos) != null ||
						//The imp doesn't move, so he cannot obstruct a passageway
						!(level.passable[npc.pos + PathFinder.CIRCLE4[0]] && level.passable[npc.pos + PathFinder.CIRCLE4[2]]) ||
						!(level.passable[npc.pos + PathFinder.CIRCLE4[1]] && level.passable[npc.pos + PathFinder.CIRCLE4[3]]));
				level.mobs.add(npc);

				spawned = true;
				alternative = Random.Int(2) == 0;

				given = false;

				reward = Generator.randomRing(false);
				reward.level(4);
				reward.cursed = true;
			}
		}

		public static void process(Mob mob)
		{
			if(spawned && given && !completed)
			{
				if((alternative && mob instanceof Monk) ||
				   (!alternative && mob instanceof Golem))
				{

					Dungeon.level.drop(new DwarfToken(), mob.pos).sprite.drop();
				}
			}
		}

		public static void complete()
		{
			reward = null;
			completed = true;

			Notes.remove(Notes.Landmark.IMP);
		}

		public static boolean isCompleted()
		{
			return completed;
		}
	}
}
