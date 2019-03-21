package com.noodlemire.chancelpixeldungeon.items.potions;

import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MagicShield;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.utils.GLog;

public class PotionOfShielding extends Potion
{
	{
		initials = 14;
	}

	@Override
	public void apply(Hero hero)
	{
		Buff.affect(hero, MagicShield.class).set((int) (hero.HT() * 0.75));
		GLog.p(Messages.get(this, "shield"));
	}

	@Override
	public int price()
	{
		return isKnown() ? 30 * quantity : super.price();
	}
}
