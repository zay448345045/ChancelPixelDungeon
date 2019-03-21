package com.noodlemire.chancelpixeldungeon.levels.rooms.special;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.keys.IronKey;
import com.noodlemire.chancelpixeldungeon.items.stones.Runestone;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Random;

public class RunestoneRoom extends SpecialRoom
{
	@Override
	public int minWidth()
	{
		return 6;
	}

	@Override
	public int minHeight()
	{
		return 6;
	}

	@Override
	public void paint(Level level)
	{
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.CHASM);

		Painter.drawInside(level, this, entrance(), 2, Terrain.EMPTY_SP);
		Painter.fill(level, this, 2, Terrain.EMPTY);

		int n = Random.NormalIntRange(2, 3);
		int dropPos;
		for(int i = 0; i < n; i++)
		{
			do
			{
				dropPos = level.pointToCell(random());
			}
			while(level.map[dropPos] != Terrain.EMPTY);
			level.drop(prize(level), dropPos);
		}

		entrance().set(Door.Type.LOCKED);
		level.addItemToSpawn(new IronKey(Dungeon.depth));
	}

	private static Item prize(Level level)
	{
		Item prize = level.findPrizeItem(Runestone.class);
		if(prize == null)
			prize = Generator.random(Generator.Category.STONE);

		return prize;
	}
}
