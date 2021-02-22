package com.noodlemire.chancelpixeldungeon.items.rings;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class RingOfVolatility extends Ring
{
	private static final int DURATION = 300;

	private Ring ring;
	private int left = 0;

	{
		icon = ItemSpriteSheet.Icons.RING_VOLATILITY;
	}

	@Override
	protected RingBuff buff()
	{
		return new Volatility();
	}

	@Override
	public String statsInfo()
	{
		if(isIdentified())
		{
			if(ring == null)
				return Messages.get(this, "no_effect");

			return Messages.get(this, "stats", left) + "\n\n" + ring.statsInfo();
		}
		else
			return Messages.get(this, "typical_stats", DURATION);
	}

	@Override
	public void activate(Char ch)
	{
		super.activate(ch);
		if(ring != null)
			ring.activate(ch);
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single)
	{
		if (super.doUnequip(hero, collect, single))
		{
			if(ring != null)
				ring.buff.detach();

			return true;
		}
		else
			return false;
	}

	private static final String LEFT = "left";
	private static final String RING = "ring";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);

		bundle.put(LEFT, left);
		bundle.put(RING, ring);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);

		left = bundle.getInt(LEFT);

		if(bundle.contains(RING))
		{
			ring = (Ring) bundle.get(RING);
			ring.ownedByRing = true;
		}
	}

	public class Volatility extends RingBuff
	{
		private void transform()
		{
			Class oldType = ring == null ?
					null :
					ring.getClass();

			left = DURATION;

			if(ring != null)
				ring.buff.detach();

			do
			{
				ring = Generator.randomRing(false);
			}
			while(ring == null || ring instanceof RingOfVolatility || (oldType != null && oldType.isInstance(ring)));

			ring.ownedByRing = true;
			ring.cursed = cursed;
			ring.level(rawLevel());
			ring.activate(target);

			if(isIdentified())
				GLog.h(Messages.get(RingOfVolatility.class, "transformed"), RingOfVolatility.this.toString(), ring.name());
		}

		@Override
		public boolean act()
		{
			if(ring != null)
			{
				ring.level(rawLevel());
				ring.cursed = cursed;
			}

			left--;
			if(left <= 0)
				transform();

			return super.act();
		}
	}
}
