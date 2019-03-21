package com.noodlemire.chancelpixeldungeon.items.stones;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Paralysis;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Lightning;
import com.noodlemire.chancelpixeldungeon.effects.particles.EnergyParticle;
import com.noodlemire.chancelpixeldungeon.effects.particles.SparkParticle;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.BArray;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class StoneOfShock extends Runestone
{
	{
		image = ItemSpriteSheet.STONE_EHWAZ;
	}

	@Override
	protected void activate(int cell)
	{
		Sample.INSTANCE.play(Assets.SND_LIGHTNING);

		ArrayList<Lightning.Arc> arcs = new ArrayList<>();
		int hits = 0;

		PathFinder.buildDistanceMap(cell, BArray.not(Dungeon.level.solid, null), 2);
		for(int i = 0; i < PathFinder.distance.length; i++)
		{
			if(PathFinder.distance[i] < Integer.MAX_VALUE)
			{
				Char n = Actor.findChar(i);
				if(n != null)
				{
					arcs.add(new Lightning.Arc(cell, n.sprite.center()));
					Buff.prolong(n, Paralysis.class, 1f);
					hits++;
				}
			}
		}

		CellEmitter.center(cell).burst(SparkParticle.FACTORY, 3);

		if(hits > 0)
		{
			curUser.sprite.parent.addToFront(new Lightning(arcs, null));
			curUser.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 10);
			curUser.belongings.charge(1f + hits);
		}
	}
}
