package com.noodlemire.chancelpixeldungeon.items.stones;

import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Blindness;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Vertigo;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class StoneOfIllusion extends Runestone
{
	{
		image = ItemSpriteSheet.STONE_NAUDIZ;
	}

	@Override
	protected void activate(int cell)
	{
		Char ch = Actor.findChar(cell);

		if(ch != null)
		{
			Buff.affect(ch, Blindness.class, Vertigo.DURATION);
			Buff.affect(ch, Vertigo.class, Vertigo.DURATION);
		}
	}
}
