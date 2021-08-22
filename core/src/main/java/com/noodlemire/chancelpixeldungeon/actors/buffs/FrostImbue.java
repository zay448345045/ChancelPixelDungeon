package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Freezing;
import com.noodlemire.chancelpixeldungeon.effects.particles.SnowParticle;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;

public class FrostImbue extends FlavourBuff implements Expulsion, MeleeProc
{
	{
		type = buffType.POSITIVE;
	}

	public static final float DURATION	= 50f;

	@Override
	public void proc(Char enemy){
		Buff.affect(enemy, Chill.class, 2f);
		enemy.sprite.emitter().burst( SnowParticle.FACTORY, 2 );
	}

	@Override
	public int icon() {
		return BuffIndicator.FROST;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}

	@Override
	public Class<? extends Blob> expulse()
	{
		return Freezing.class;
	}

	{
		immunities.add( Frost.class );
		immunities.add( Chill.class );
	}
}
