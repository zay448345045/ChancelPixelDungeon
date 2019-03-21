package com.noodlemire.chancelpixeldungeon.actors.blobs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Roots;
import com.noodlemire.chancelpixeldungeon.effects.BlobEmitter;
import com.noodlemire.chancelpixeldungeon.effects.particles.LeafParticle;
import com.noodlemire.chancelpixeldungeon.messages.Messages;

public class RootCloud extends Blob
{
	@Override
	protected void evolve()
	{
		super.evolve();

		if(volume > 0)
		{
			int cell;
			for(int i = area.left; i < area.right; i++)
				for(int j = area.top; j < area.bottom; j++)
				{
					cell = i + j * Dungeon.level.width();
					if(off[cell] > 0)
					{
						Char ch = Actor.findChar(cell);
						if(ch != null
						   && !ch.isImmune(this.getClass())
						   && off[cell] > 1)
						{
							Buff.prolong(ch, Roots.class, TICK);
						}
					}
				}

			Dungeon.observe();
		}
	}

	@Override
	public void use(BlobEmitter emitter)
	{
		super.use(emitter);

		emitter.start(LeafParticle.BROWN, 0.2f, 0);
	}

	@Override
	public String tileDesc()
	{
		return Messages.get(this, "desc");
	}
}
