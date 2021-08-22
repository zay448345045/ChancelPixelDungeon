package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class Following extends Buff
{
	{
		type = buffType.NEUTRAL;
	}

	@Override
	public int icon()
	{
		return BuffIndicator.MOMENTUM;
	}

	@Override
	public void tintIcon(Image icon)
	{
		icon.hardlight(0.5f, 0.5f, 0.5f);
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc");
	}
}
