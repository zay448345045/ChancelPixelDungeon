package com.noodlemire.chancelpixeldungeon.items.stones;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Hunger;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Regeneration;
import com.noodlemire.chancelpixeldungeon.items.artifacts.Artifact;
import com.noodlemire.chancelpixeldungeon.items.rings.Ring;
import com.noodlemire.chancelpixeldungeon.items.wands.Wand;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class StoneOfEquity extends Runestone
{
	{
		image = ItemSpriteSheet.STONE_SOWILO;
	}

	private static boolean isRemovable(Buff target)
	{
		return !(target instanceof Hunger ||
		         target instanceof Regeneration ||
		         target instanceof Ring.RingBuff ||
		         target instanceof Artifact.ArtifactBuff ||
		         target instanceof Wand.Charger);
	}

	@Override
	protected void activate(int cell)
	{
		Char ch = Actor.findChar(cell);

		if(ch != null)
		{
			ArrayList<Class<? extends Buff>> sharedBuffs = new ArrayList<>();

			for(Buff b : ch.buffs())
				if(isRemovable(b))
					if(Dungeon.hero.buff(b.getClass()) != null)
						sharedBuffs.add(b.getClass());
					else
						b.detach();

			for(Buff b : Dungeon.hero.buffs())
				if(isRemovable(b) && !sharedBuffs.contains(b.getClass()))
					b.detach();
		}
	}
}
