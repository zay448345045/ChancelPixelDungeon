package com.noodlemire.chancelpixeldungeon.actors.blobs;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.effects.BlobEmitter;
import com.noodlemire.chancelpixeldungeon.effects.particles.ShaftParticle;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

public class Sunlight extends GasBlob
{
	private int factor(float num)
	{
		int i = (int)Math.floor(num);
		float rem = num - i;

		if(Random.Float() < rem)
			i++;

		return i;
	}

	@Override
	void affect(Char ch, int cell)
	{
		if(ch.properties().contains(Char.Property.UNDEAD))
		{
			if(ch.properties().contains(Char.Property.BOSS))
				ch.damage(factor(ch.HT() / 100f), this);
			else
				ch.damage(factor(ch.HT() / 20f), this);
		}
		else if(!ch.properties().contains(Char.Property.INORGANIC))
			ch.heal(factor(ch.HT() / 20f), this);
	}

	@Override
	void affect(int cell) {}

	@Override
	public void use(BlobEmitter emitter)
	{
		super.use(emitter);

		emitter.pour(GoldenShaftParticle.FACTORY, 0.6f);
	}

	@Override
	public String tileDesc()
	{
		return Messages.get(this, "desc");
	}

	public static class GoldenShaftParticle extends ShaftParticle
	{
		public static final Emitter.Factory FACTORY = new Emitter.Factory()
		{
			@Override
			public void emit(Emitter emitter, int index, float x, float y)
			{
				((GoldenShaftParticle) emitter.recycle(GoldenShaftParticle.class)).reset(x, y);
			}

			@Override
			public boolean lightMode()
			{
				return true;
			}
		};

		public GoldenShaftParticle()
		{
			super();

			lifespan = 1.2f;
			speed.set(0, -6);
			color(1f, 1f, 0f);
		}
	}
}
