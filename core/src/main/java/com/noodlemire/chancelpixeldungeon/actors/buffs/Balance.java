package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Balance extends FlavourBuff
{
	public static final float DURATION = 50f;

	private int weapon = 0,
			armor = 0,
			misc1 = 0,
			misc2 = 0;

	private boolean empower = false;

	private static final String WEAPON = "weapon",
			ARMOR = "armor",
			MISC1 = "misc1",
			MISC2 = "misc2",
			EMPOWER = "empower";

	{
		type = buffType.NEUTRAL;
	}

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(WEAPON, weapon);
		bundle.put(ARMOR, armor);
		bundle.put(MISC1, misc1);
		bundle.put(MISC2, misc2);
		bundle.put(EMPOWER, empower);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		weapon = bundle.getInt(WEAPON);
		armor = bundle.getInt(ARMOR);
		misc1 = bundle.getInt(MISC1);
		misc2 = bundle.getInt(MISC2);
		empower = bundle.getBoolean(EMPOWER);
	}

	@Override
	public boolean attachTo(Char target)
	{
		boolean a = target instanceof Hero && super.attachTo(target);
		update();
		return a;
	}

	@Override
	public int icon()
	{
		return BuffIndicator.BALANCE;
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	@Override
	public String heroMessage()
	{
		return Messages.get(this, "heromsg");
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc", dispTurns());
	}

	//See EquippableItem.Level()
	public static int average()
	{
		Hero hero = Dungeon.hero;
		Balance b = hero != null ? hero.buff(Balance.class) : null;

		if(b != null)
		{
			float count = 0;

			if(hero.belongings.weapon != null) count++;
			if(hero.belongings.armor != null) count++;
			if(hero.belongings.misc1 != null) count++;
			if(hero.belongings.misc2 != null) count++;

			int average = (int) Math.ceil((b.weapon + b.armor + b.misc1 + b.misc2) / count);

			return b.empower ? average + 1 : average;
		}
		else
			return -1;
	}

	public void set(boolean empower)
	{
		this.empower = empower;
		update();
	}

	//See EquippableItem.Execute(), level(int value), upgrade(), and degrade()
	public static void update()
	{
		Hero hero = Dungeon.hero;
		Balance b = hero != null ? hero.buff(Balance.class) : null;

		if(b != null)
		{
			//Can't use level() or the average will be read instead before everything is set
			if(hero.belongings.weapon != null) b.weapon = hero.belongings.weapon.rawLevel();
			else b.weapon = 0;
			if(hero.belongings.armor != null) b.armor = hero.belongings.armor.rawLevel();
			else b.armor = 0;
			if(hero.belongings.misc1 != null) b.misc1 = hero.belongings.misc1.rawLevel();
			else b.misc1 = 0;
			if(hero.belongings.misc2 != null) b.misc2 = hero.belongings.misc2.rawLevel();
			else b.misc2 = 0;
		}
	}
}
