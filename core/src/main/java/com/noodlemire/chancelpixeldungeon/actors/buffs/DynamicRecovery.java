package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfForce;

public class DynamicRecovery extends Buff
{
	@Override
	public boolean act()
	{
		if(!(target instanceof Hero))
		{
			detach();
			return true;
		}

		Hero hero = (Hero)target;

		if(hero.isAlive())
		{
			float max = hero.dynamax() + RingOfForce.dynamicBonus(hero);

			if (hero.buff(Combo.class) == null)
				if (hero.moved)
					hero.dynamic(max * 0.05f);
				else if (!hero.attacked)
					hero.dynamic(max * 0.25f);

			spend(TICK);

			hero.attacked = false;
			hero.moved = false;
		}
		else
			deactivate();

		return true;
	}
}