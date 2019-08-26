package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Balance;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfBalance extends Scroll
{
	{
		initials = 13;
	}

	@Override
	public void doRead()
	{
		read(false);
		Sample.INSTANCE.play(Assets.SND_READ);
	}

	@Override
	public void doShout()
	{
		Sample.INSTANCE.play(Assets.SND_CHALLENGE);
		GLog.i(Messages.get(this, "too_loud"));
		readAnimation();
	}

	@Override
	public void empoweredRead()
	{
		read(true);
		Sample.INSTANCE.play(Assets.SND_READ);
	}

	private void read(boolean empowered)
	{
		Buff.affect(curUser, Balance.class, Balance.DURATION).set(empowered);

		GLog.i(Messages.get(this, "balance"));

		readAnimation();
	}
}
