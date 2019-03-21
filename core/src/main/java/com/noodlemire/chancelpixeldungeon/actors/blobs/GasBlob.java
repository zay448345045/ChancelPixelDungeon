package com.noodlemire.chancelpixeldungeon.actors.blobs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;

public abstract class GasBlob extends Blob
{
	@Override
	protected void evolve()
	{
		super.evolve();

		Char ch;
		int cell;

		for(int i = area.left; i < area.right; i++)
			for(int j = area.top; j < area.bottom; j++)
			{
				cell = i + j * Dungeon.level.width();
				if(cur[cell] > 0 && (ch = Actor.findChar(cell)) != null && !ch.isImmune(this.getClass()))
					affect(ch, cell);
				else if(cur[cell] > 0)
					affect(cell);
			}
	}

	abstract void affect(Char ch, int cell);

	abstract void affect(int cell); //TODO: Implement extra environmental effects for gases that don't use this
}
