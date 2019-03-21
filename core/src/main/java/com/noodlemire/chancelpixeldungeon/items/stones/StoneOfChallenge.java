package com.noodlemire.chancelpixeldungeon.items.stones;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Challenged;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;

public class StoneOfChallenge extends Runestone
{
	{
		image = ItemSpriteSheet.STONE_LAGUZ;
	}

	@Override
	protected void activate(int cell)
	{
		for(int n : PathFinder.NEIGHBOURS9)
		{
			Char ch = Actor.findChar(cell + n);

			if(ch instanceof Mob && ch.alignment == Char.Alignment.ENEMY)
			{
				Buff.affect(ch, Challenged.class, Challenged.DURATION);
				((Mob)ch).beckon(Dungeon.hero.pos);
			}
		}
	}
}
