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
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.quest.CeremonialCandle;
import com.noodlemire.chancelpixeldungeon.items.quest.CorpseDust;
import com.noodlemire.chancelpixeldungeon.items.quest.Embers;
import com.noodlemire.chancelpixeldungeon.items.quest.RotberryCore;
import com.noodlemire.chancelpixeldungeon.items.wands.Wand;
import com.noodlemire.chancelpixeldungeon.journal.Notes;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.rooms.Room;
import com.noodlemire.chancelpixeldungeon.levels.rooms.special.MassGraveRoom;
import com.noodlemire.chancelpixeldungeon.levels.rooms.special.RotGardenRoom;
import com.noodlemire.chancelpixeldungeon.levels.rooms.standard.RitualSiteRoom;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.WandmakerSprite;
import com.noodlemire.chancelpixeldungeon.windows.WndQuest;
import com.noodlemire.chancelpixeldungeon.windows.WndWandmaker;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Wandmaker extends NPC
{

	{
		spriteClass = WandmakerSprite.class;

		properties.add(Property.IMMOVABLE);

		EXP = 15;
	}

	@Override
	protected boolean act()
	{
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

			Item item;
			switch(Quest.type)
			{
				case 1:
				default:
					item = Dungeon.hero.belongings.getItem(CorpseDust.class);
					break;
				case 2:
					item = Dungeon.hero.belongings.getItem(Embers.class);
					break;
				case 3:
					item = Dungeon.hero.belongings.getItem(RotberryCore.class);
					break;
			}

			if(item != null)
			{
				GameScene.show(new WndWandmaker(this, item));
			}
			else
			{
				String msg = "";
				switch(Quest.type)
				{
					case 1:
						msg = Messages.get(this, "reminder_dust", Dungeon.hero.givenName());
						break;
					case 2:
						msg = Messages.get(this, "reminder_ember", Dungeon.hero.givenName());
						break;
					case 3:
						msg = Messages.get(this, "reminder_berry", Dungeon.hero.givenName());
						break;
				}
				GameScene.show(new WndQuest(this, msg));
			}

		}
		else
		{

			String msg1 = "";
			String msg2 = "";
			switch(Dungeon.hero.heroClass)
			{
				case WARRIOR:
					msg1 += Messages.get(this, "intro_warrior");
					break;
				case ROGUE:
					msg1 += Messages.get(this, "intro_rogue");
					break;
				case MAGE:
					msg1 += Messages.get(this, "intro_mage", Dungeon.hero.givenName());
					break;
				case HUNTRESS:
					msg1 += Messages.get(this, "intro_huntress");
					break;
			}

			msg1 += Messages.get(this, "intro_1");

			switch(Quest.type)
			{
				case 1:
					msg2 += Messages.get(this, "intro_dust");
					break;
				case 2:
					msg2 += Messages.get(this, "intro_ember");
					break;
				case 3:
					msg2 += Messages.get(this, "intro_berry");
					break;
			}

			msg2 += Messages.get(this, "intro_2");
			final String msg2final = msg2;
			final NPC wandmaker = this;

			GameScene.show(new WndQuest(wandmaker, msg1)
			{
				@Override
				public void hide()
				{
					super.hide();
					GameScene.show(new WndQuest(wandmaker, msg2final));
				}
			});

			Notes.add(Notes.Landmark.WANDMAKER);
			Quest.given = true;
		}

		return false;
	}

	public static class Quest
	{

		private static int type;
		// 1 = corpse dust quest
		// 2 = elemental embers quest
		// 3 = rotberry quest

		private static boolean spawned;

		private static boolean given;

		public static Wand wand1;
		public static Wand wand2;

		public static void reset()
		{
			spawned = false;
			type = 0;

			wand1 = null;
			wand2 = null;
		}

		private static final String NODE = "wandmaker";

		private static final String SPAWNED = "spawned";
		private static final String TYPE = "type";
		private static final String GIVEN = "given";
		private static final String WAND1 = "wand1";
		private static final String WAND2 = "wand2";

		private static final String RITUALPOS = "ritualpos";

		public static void storeInBundle(Bundle bundle)
		{

			Bundle node = new Bundle();

			node.put(SPAWNED, spawned);

			if(spawned)
			{

				node.put(TYPE, type);

				node.put(GIVEN, given);

				node.put(WAND1, wand1);
				node.put(WAND2, wand2);

				if(type == 2)
				{
					node.put(RITUALPOS, CeremonialCandle.ritualPos);
				}

			}

			bundle.put(NODE, node);
		}

		public static void restoreFromBundle(Bundle bundle)
		{

			Bundle node = bundle.getBundle(NODE);

			if(!node.isNull() && (spawned = node.getBoolean(SPAWNED)))
			{

				type = node.getInt(TYPE);

				given = node.getBoolean(GIVEN);

				wand1 = (Wand) node.get(WAND1);
				wand2 = (Wand) node.get(WAND2);

				if(type == 2)
				{
					CeremonialCandle.ritualPos = node.getInt(RITUALPOS);
				}

			}
			else
			{
				reset();
			}
		}

		private static boolean questRoomSpawned;

		public static void spawnWandmaker(Level level, Room room)
		{
			if(questRoomSpawned)
			{
				questRoomSpawned = false;

				Wandmaker npc = new Wandmaker();
				do
				{
					npc.pos = level.pointToCell(room.random());
				}
				while(npc.pos == level.entrance);
				level.mobs.add(npc);

				spawned = true;

				given = false;
				wand1 = Generator.randomWand(false);
				wand1.cursed = false;
				wand1.level(2);

				do
				{
					wand2 = Generator.randomWand(false);
				}
				while(wand2.getClass().equals(wand1.getClass()));
				wand2.cursed = false;
				wand2.level(2);
			}
		}

		public static ArrayList<Room> spawnRoom(ArrayList<Room> rooms)
		{
			questRoomSpawned = false;
			if(!spawned && (type != 0 || (Dungeon.depth > 6 && Random.Int(10 - Dungeon.depth) == 0)))
			{

				// decide between 1,2, or 3 for quest type.
				if(type == 0) type = Random.Int(3) + 1;

				switch(type)
				{
					case 1:
					default:
						rooms.add(new MassGraveRoom());
						break;
					case 2:
						rooms.add(new RitualSiteRoom());
						break;
					case 3:
						rooms.add(new RotGardenRoom());
						break;
				}

				questRoomSpawned = true;

			}
			return rooms;
		}

		public static void complete()
		{
			wand1 = null;
			wand2 = null;

			Notes.remove(Notes.Landmark.WANDMAKER);
		}
	}
}
