package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class Linkage extends FlavourBuff implements Hero.Doom
{
	public static final float DURATION = 20f;

	public int object = 0;

	private static final String OBJECT = "object";

	{
		type = buffType.NEUTRAL;
	}

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(OBJECT, object);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		object = bundle.getInt(OBJECT);
	}

	@Override
	public int icon()
	{
		return BuffIndicator.PARALYSIS;
	}

	@Override
	public void tintIcon(Image icon)
	{
		icon.hardlight(1f, 0f, 0f);
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

	@Override
	public void onDeath()
	{
		Dungeon.fail(getClass());
		GLog.n(Messages.get(this, "ondeath"));
	}
}
