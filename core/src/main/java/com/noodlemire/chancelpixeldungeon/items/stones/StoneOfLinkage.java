package com.noodlemire.chancelpixeldungeon.items.stones;

import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Linkage;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class StoneOfLinkage extends Runestone
{
	{
		image = ItemSpriteSheet.STONE_SOWILO;
	}

	@Override
	protected void activate(int cell)
	{
		Char ch = Actor.findChar(cell);

		if(ch instanceof Mob)
		{
			Buff.affect(curUser, Linkage.class, Linkage.DURATION).object = ch.id();
			Buff.affect(ch, Linkage.class, Linkage.DURATION).object = curUser.id();
		}
	}
}
