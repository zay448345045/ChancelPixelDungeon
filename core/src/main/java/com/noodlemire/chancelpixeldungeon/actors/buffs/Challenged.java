package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class Challenged extends FlavourBuff
{
	public static float DURATION = 10f;

	{
		type = buffType.NEGATIVE;
	}

	@Override
	public int icon()
	{
		return BuffIndicator.TUNNEL_VISION;
	}

	@Override
	public void tintIcon(Image icon)
	{
		icon.hardlight(0.95f, 0.75f, 0.5f);
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc", dispTurns());
	}
}
