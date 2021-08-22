package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.watabou.utils.Bundle;

//Not to be confused with flavourbuffs that don't actually use an internal duration variable,
//this is a type of buff that has some form of set duration or level, which is set seperately from when the buff begins.
//Note that left is not strictly the amount of turns left until a buff detaches; it can be regularly shortened by any amount.
public class DurationBuff extends Buff implements FadePercent
{
	private static final String LEFT = "left";
	private static final String MAX = "max";

	private float left;
	private float max;

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(LEFT, left);
		bundle.put(MAX, max);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		left = bundle.getFloat(LEFT);
		max = bundle.getFloat(MAX);
	}

	public void extend(float by)
	{
		if(by < 0)
		{
			shorten(-by);
			return;
		}

		left += by;
		max = Math.max(max, left);
	}

	public float left()
	{
		return left;
	}

	public void set(float to)
	{
		if(left < to)
		{
			left = to;
			max = Math.max(max, left);
		}
	}

	public void shorten(float by)
	{
		if(by < 0)
		{
			extend(-by);
			return;
		}

		left -= by;

		if(left <= 0)
			detach();
	}

	@Override
	public float fadePercent()
	{
		return 1 - left / max;
	}
}
