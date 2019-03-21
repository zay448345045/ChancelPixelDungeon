package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;

public class Haste extends FlavourBuff implements Expulsion
{
	public static final float DURATION = 20f;

	{
		type = buffType.POSITIVE;
	}

	@Override
	public int icon()
	{
		return BuffIndicator.HASTE;
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
	public Class<? extends Blob> expulse()
	{
		System.out.println("Expulsing.");
		new Stasis().attachTo(target);
		System.out.println("Done.");
		return null;
	}

	public class Stasis extends Buff
	{
		{
			type = buffType.SILENT;
		}

		@Override
		public boolean attachTo(Char target)
		{
			System.out.println("Attaching...");
			if(super.attachTo(target))
			{
				float duration = Haste.this.cooldown() + 1;
				System.out.println("Duration = " + duration);

				//buffs always act last, so the stasis buff should end a turn early.
				spend(duration - 1);
				((Hero) target).spendAndNext(duration);

				//shouldn't punish the player for going into stasis frequently
				Hunger hunger = target.buff(Hunger.class);
				if(hunger != null && !hunger.isStarving())
					hunger.satisfy(duration);

				target.invisible++;

				Dungeon.observe();

				return true;
			}
			else
				return false;
		}

		@Override
		public boolean act()
		{
			detach();
			return true;
		}

		@Override
		public void detach()
		{
			if(target.invisible > 0)
				target.invisible--;
			super.detach();
			Dungeon.observe();
		}
	}
}
