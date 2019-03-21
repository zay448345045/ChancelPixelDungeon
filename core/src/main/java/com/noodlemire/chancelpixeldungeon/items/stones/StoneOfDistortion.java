package com.noodlemire.chancelpixeldungeon.items.stones;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.items.Heap;
import com.noodlemire.chancelpixeldungeon.items.scrolls.EnvironmentScroll;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.BArray;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class StoneOfDistortion extends Runestone
{
	{
		image = ItemSpriteSheet.STONE_KAUNAN;
	}

	@Override
	protected void activate(int cell)
	{
		if(Dungeon.depth % 5 == 0)
		{
			GLog.i(Messages.get(ScrollOfTeleportation.class, "no_tele"));
			return;
		}

		boolean[] aoe = EnvironmentScroll.fovAt(cell, 3, false);
		boolean[] ignore = new boolean[aoe.length];
		BArray.setFalse(ignore);

		int entpos = -1;
		PathFinder.Path entpath;
		int extpos = -1;
		PathFinder.Path extpath;

		for(int i = 0; i < aoe.length; i++)
		{
			switch(Dungeon.level.map[i])
			{
				case Terrain.ENTRANCE:
					entpos = i;
					break;
				case Terrain.EXIT:
					extpos = i;
					break;
			}
		}

		if(entpos == -1 || extpos == -1)
			return;

		int start = Dungeon.hero.pos;
		entpath = PathFinder.find(start, entpos, Dungeon.level.passable);
		extpath = PathFinder.find(start, extpos, Dungeon.level.passable);

		if((entpos != start && entpath == null) ||
		   (extpos != start && extpath == null))
			return;

		if(entpos != start)
			for(int p : entpath)
				ignore[p] = true;

		if(extpos != start)
			for(int p : extpath)
				ignore[p] = true;

		ArrayList<Integer> affectedCells = new ArrayList<>();
		ArrayList<Integer> affectedTerrain = new ArrayList<>();

		for(int i = 1; i < aoe.length - 1; i++)
		{
			if(aoe[i] && !ignore[i] &&

			   i % Dungeon.level.width() > 0 &&
			   i % Dungeon.level.width() < Dungeon.level.width() - 1 &&
			   i / Dungeon.level.height() > 0 &&
			   i / Dungeon.level.width() < Dungeon.level.height() - 1 &&

			   Dungeon.level.map[i] != Terrain.ALCHEMY &&
			   Dungeon.level.map[i] != Terrain.ENTRANCE &&
			   Dungeon.level.map[i] != Terrain.EXIT &&
			   Dungeon.level.map[i] != Terrain.LOCKED_EXIT &&
			   Dungeon.level.map[i] != Terrain.UNLOCKED_EXIT &&
			   Dungeon.level.map[i] != Terrain.WELL &&

			   Dungeon.level.getCustomVisual(i) == null &&
			   Actor.findChar(i) == null &&
			   (Dungeon.level.heaps.get(i) == null ||
			    Dungeon.level.heaps.get(i).type == Heap.Type.HEAP))
			{
				affectedCells.add(i);
				affectedTerrain.add(Dungeon.level.map[i]);
			}
		}

		for(int c : affectedCells)
		{
			Integer terrain = Random.element(affectedTerrain);
			affectedTerrain.remove(terrain);
			Level.set(c, terrain);
			Dungeon.level.press(c, null);

			Heap h = Dungeon.level.heaps.get(c);
			if(h != null)
				ScrollOfTeleportation.randomTeleport(h);
		}

		for(int c = 0; c < aoe.length; c++)
			if(aoe[c])
				GameScene.updateMap(c);

		Dungeon.observe();
	}
}
