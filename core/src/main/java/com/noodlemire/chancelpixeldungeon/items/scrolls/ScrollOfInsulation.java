package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.BlobImmunity;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MagicImmunity;
import com.noodlemire.chancelpixeldungeon.effects.Flare;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.utils.GLog;

public class ScrollOfInsulation extends EnvironmentScroll
{
	{
		initials = 16;
	}

	@Override
	public void doRead()
	{
		insulate(curUser.pos, false);
	}

	@Override
	protected void onSelect(int cell)
	{
		insulate(cell, false);
	}

	@Override
	public void empoweredRead()
	{
		insulate(curUser.pos, true);
	}

	private void insulate(int pos, boolean empowered)
	{
		readAnimation();
		new Flare(5, 32).color(0x880088, true).show(curUser.sprite, pos, 2f);

		Char ch = Actor.findChar(pos);

		if(ch == null)
		{
			GLog.i(Messages.get(this, "fail"));
			return;
		}

		Buff.affect(ch, MagicImmunity.class, MagicImmunity.DURATION);

		if(empowered)
			Buff.affect(ch, BlobImmunity.class, MagicImmunity.DURATION);
	}
}
