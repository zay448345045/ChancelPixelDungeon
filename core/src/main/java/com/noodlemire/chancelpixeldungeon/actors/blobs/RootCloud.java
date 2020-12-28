package com.noodlemire.chancelpixeldungeon.actors.blobs;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Roots;
import com.noodlemire.chancelpixeldungeon.effects.BlobEmitter;
import com.noodlemire.chancelpixeldungeon.effects.particles.LeafParticle;
import com.noodlemire.chancelpixeldungeon.messages.Messages;

public class RootCloud extends GasBlob
{
	{
		harmful = true;
	}

	@Override
	void affect(Char ch, int cell) {
		Buff.prolong(ch, Roots.class, TICK);
	}

	@Override
	void affect(int cell) {}

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
