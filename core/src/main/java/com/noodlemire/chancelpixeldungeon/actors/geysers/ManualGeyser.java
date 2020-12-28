package com.noodlemire.chancelpixeldungeon.actors.geysers;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.sprites.GeyserSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class ManualGeyser extends Geyser
{
	private float nextSpewTime = 50f;

	@Override
	public boolean act()
	{
		super.act();

		if(nextSpewTime <= 0)
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
		nextSpewTime -= time;
		super.spend(time);
	}

	@Override
	public int defenseProc(Char enemy, int damage)
	{
		spew();
		((GeyserSprite)sprite).spew();
		nextSpewTime = Random.Int(50, 101);

		return damage;
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
