package com.noodlemire.chancelpixeldungeon.actors.geysers;

import com.noodlemire.chancelpixeldungeon.sprites.GeyserSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class ManualGeyser extends Geyser
{
	private float nextSpewTime = 50f;
	protected boolean randomlySpew = true;

	@Override
	public boolean act()
	{
		super.act();

		if(randomlySpew && nextSpewTime <= 0)
		{
			spew();
			((GeyserSprite)sprite).spew();
			nextSpewTime = Random.Int(50, 101);
		}

		spend(TICK);
		return true;
	}

	@Override
	protected void spend(float time)
	{
		if(randomlySpew) nextSpewTime -= time;
		super.spend(time);
	}

	@Override
	public void damage(int dmg, Object src)
	{
		super.damage(dmg, src);

		spew();
		if(sprite instanceof GeyserSprite) ((GeyserSprite)sprite).spew();
		nextSpewTime = Random.IntRange(50, 100);
	}

	private static final String SPEWTIME = "nextSpewTime";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);

		bundle.put(SPEWTIME, nextSpewTime);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);

		nextSpewTime = bundle.getFloat(SPEWTIME);
	}
}
