package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.BlobImmunity;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MagicImmunity;
import com.noodlemire.chancelpixeldungeon.effects.Flare;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfInsulation extends EnvironmentScroll
{
	{
		icon = ItemSpriteSheet.Icons.SCROLL_INSULATION;
	}

	@Override
	public void doRead()
	{
		insulate(curUser.pos, false);
		readAnimation();
		Sample.INSTANCE.play(Assets.SND_READ);
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
		readAnimation();
		Sample.INSTANCE.play(Assets.SND_READ);
	}

	private void insulate(int pos, boolean empowered)
	{
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
