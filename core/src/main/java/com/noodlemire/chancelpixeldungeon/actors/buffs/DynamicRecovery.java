package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.artifacts.BraceletOfForce;
import com.watabou.utils.Bundle;

public class DynamicRecovery extends Buff
{
	private int resetTimer = 0;
	private int lastPos = -1;

	public void resetTimer()
	{
		resetTimer = 5;
	}

	@Override
	public boolean act()
	{
		if(lastPos == -1)
			lastPos = target.pos;

		if(!(target instanceof Hero))
		{
			detach();
			return true;
		}

		Hero hero = (Hero)target;

		if(hero.isAlive())
		{
			int curPos = hero.pos;
			float max = hero.dynamax();

			if (hero.buff(Combo.class) == null && !hero.moved && curPos == lastPos && !hero.attacked)
			{
				float oldFactor = hero.dynamicFactor();
				hero.dynamic(max * 0.25f);
				float newFactor = hero.dynamicFactor();

				if(newFactor > oldFactor)
				{
					if(newFactor >= Hero.DYNAMIC_MILESTONES[3])
						hero.sprite.flash(1f, 0f, 0f);
					else if(newFactor >= Hero.DYNAMIC_MILESTONES[2])
						hero.sprite.flash(0.5f, 0f, 0.25f);
					else if(newFactor >= Hero.DYNAMIC_MILESTONES[1])
						hero.sprite.flash(1f, 0f, 1f);
					else if(newFactor >= Hero.DYNAMIC_MILESTONES[0])
						hero.sprite.flash(1f, 1f, 1f);
				}
			}

			if(resetTimer > 0 && !hero.attacked)
			{
				resetTimer--;
				if(resetTimer == 0 && hero.dynamicFactor() < 1)
				{
					hero.dynamic(hero.dynamax(), false);

					if (BraceletOfForce.cursedHero(hero))
						hero.sprite.flash(1f, 0f, 0.5f);
					else
						hero.sprite.flash(1f, 0f, 0f);
				}
			}
			else if(hero.dynamicFactor() < 1)
				resetTimer();

			spend(TICK);

			hero.attacked = false;
			hero.moved = false;
			lastPos = curPos;
		}
		else
			deactivate();

		hero.critBoost = false;

		return true;
	}

	private static final String RESETTIMER = "resetTimer";
	private static final String LASTPOS = "lastPos";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);

		bundle.put(RESETTIMER, resetTimer);
		bundle.put(LASTPOS, lastPos);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);

		resetTimer = bundle.getInt(RESETTIMER);

		if(bundle.contains(LASTPOS))
			lastPos = bundle.getInt(LASTPOS);
	}
}