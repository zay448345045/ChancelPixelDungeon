package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;

public class WellFed extends DurationBuff
{
	{
		type = buffType.POSITIVE;
	}

	@Override
	public boolean act()
	{
		if(left() % 10 == 0)
			target.heal(1, this);

		shorten(1);
		spend(TICK);
		return true;
	}

	public void reset()
	{
		//heals one HP every 10 turns for 450 turns
		set(Hunger.STARVING);
	}

	@Override
	public int icon()
	{
		return BuffIndicator.WELL_FED;
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc", dispTurns(left()));
	}
}
