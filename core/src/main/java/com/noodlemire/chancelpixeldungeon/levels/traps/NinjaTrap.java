package com.noodlemire.chancelpixeldungeon.levels.traps;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Roots;
import com.noodlemire.chancelpixeldungeon.items.NinjaTrapItem;

public class NinjaTrap extends Trap
{
	private static final int DURATION = 20;

	{
		color = YELLOW;
		shape = LARGE_DOT;
	}

	@Override
	public void activate()
	{
		Char ch = Actor.findChar(pos);

		if(ch != null)
		{
			Buff.affect(ch, Roots.class, DURATION);
		}

		Dungeon.level.drop(new NinjaTrapItem(), pos);
	}
}
