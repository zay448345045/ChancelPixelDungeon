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
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.Heap;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.CellSelector;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.HeroSprite;
import com.noodlemire.chancelpixeldungeon.utils.BArray;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class ScrollOfTeleportation extends EnvironmentScroll
{
	private static ArrayList<String> names;

	{
		initials = 9;
	}

	@Override
	public void doRead()
	{
		Sample.INSTANCE.play(Assets.SND_READ);
		readAnimation();

		onSelect(curUser.pos);
	}

	@Override
	protected void onSelect(int cell)
	{
		randomTeleport(cell);
	}

	@Override
	public void empoweredRead()
	{
		if(Dungeon.bossLevel())
		{
			GLog.w(Messages.get(this, "no_tele"));
			return;
		}

		GameScene.selectCell(new CellSelector.Listener()
		{
			@Override
			public void onSelect(Integer target)
			{
				if(target != null)
				{
					//time isn't spent
					((HeroSprite) curUser.sprite).read();
					teleportToLocation(curUser, target);
				}
			}

			@Override
			public String prompt()
			{
				return Messages.get(ScrollOfTeleportation.class, "prompt");
			}
		});
	}

	private static void teleportToLocation(Hero hero, int pos)
	{
		PathFinder.buildDistanceMap(pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
		if(PathFinder.distance[hero.pos] == Integer.MAX_VALUE
		   || (!Dungeon.level.passable[pos] && !Dungeon.level.avoid[pos])
		   || Actor.findChar(pos) != null)
		{
			GLog.w(Messages.get(ScrollOfTeleportation.class, "cant_reach"));
			return;
		}

		appear(hero, pos);
		Dungeon.level.press(pos, hero);
		Dungeon.observe();
		GameScene.updateFog();

		GLog.i(Messages.get(ScrollOfTeleportation.class, "tele"));
	}

	//By default, simply randomly teleporting at a given position will teleport anything that is movable.
	public static void randomTeleport(int pos)
	{
		names = new ArrayList<>();

		if(Dungeon.bossLevel())
		{
			GLog.w(Messages.get(ScrollOfTeleportation.class, "no_tele"));
			return;
		}

		Char ch = Actor.findChar(pos);
		if(ch != null && !ch.properties().contains(Char.Property.IMMOVABLE))
			randomTeleport(ch, true);

		Heap hp = Dungeon.level.heaps.get(pos);
		if(hp != null && hp.type == Heap.Type.HEAP)
			randomTeleport(hp, true);

		if(names.isEmpty())
			GLog.i(Messages.get(ScrollOfTeleportation.class, "tele_nothing"));
		else if(names.get(0).equals("you"))
			GLog.i(Messages.get(ScrollOfTeleportation.class, "tele"));
		else if(names.size() == 1)
			GLog.i(Messages.get(ScrollOfTeleportation.class, "tele_other", names.get(0)));
		else
			GLog.i(Messages.get(ScrollOfTeleportation.class, "tele_others", names.get(0)));
	}

	//If instead a specific creature is given, only that creature will be teleported
	public static void randomTeleport(Char ch)
	{
		if(ch instanceof Hero || (ch instanceof Mob && !ch.properties().contains(Char.Property.IMMOVABLE)))
		{
			if(!Dungeon.bossLevel())
			{
				if(ch instanceof Hero)
				{
					((Hero) ch).curAction = null;
					GLog.i(Messages.get(ScrollOfTeleportation.class, "tele"));
				}
				else
				{
					if(((Mob) ch).state == ((Mob) ch).HUNTING)
						((Mob) ch).state = ((Mob) ch).WANDERING;
					GLog.i(Messages.get(ScrollOfTeleportation.class, "tele_other", ch.name));
				}

				ScrollOfTeleportation.randomTeleport(ch, false);
			}
			else
				GLog.i(Messages.get(ScrollOfTeleportation.class, "no_tele"));
		}
		else
			GLog.i(Messages.get(ScrollOfTeleportation.class, "tele_nothing"));
	}

	//This private version of creature teleportation is separated so that glog will wait until other methods have completed
	private static void randomTeleport(Char ch, boolean doGLog)
	{
		int count = 10;
		int pos;
		do
		{
			pos = Dungeon.level.randomRespawnCell();
		}
		while(pos == -1 && count-- > 0);

		if(pos != -1)
		{
			appear(ch, pos);
			Dungeon.level.press(pos, ch);
			Dungeon.observe();
			GameScene.updateFog();

			if(doGLog) names.add(ch.name);
		}
	}

	//This specific method only teleports items on the ground.
	public static void randomTeleport(Heap hp)
	{
		randomTeleport(hp, false);
	}

	//This private version of heap teleportation is separated so that glog will wait until other methods have completed
	private static void randomTeleport(Heap hp, boolean doGLog)
	{
		int initialSize = hp.size();
		for(int i = 0; i < initialSize; i++)
		{
			Item item = hp.pickUp();
			if(doGLog)
				names.add(item.name());
			int cell = Dungeon.level.randomRespawnCell();
			if(cell != -1)
				Dungeon.level.drop(item, cell);
		}
	}

	public static void appear(Char ch, int pos)
	{
		ch.sprite.interruptMotion();

		ch.move(pos);
		ch.sprite.place(pos);

		if(ch.invisible == 0)
		{
			ch.sprite.alpha(0);
			ch.sprite.parent.add(new AlphaTweener(ch.sprite, 1, 0.4f));
		}

		if(Dungeon.level.heroFOV[pos])
			ch.sprite.emitter().start(Speck.factory(Speck.LIGHT), 0.2f, 3);
		Sample.INSTANCE.play(Assets.SND_TELEPORT);
	}

	@Override
	public int price()
	{
		return isKnown() ? 30 * quantity : super.price();
	}
}
