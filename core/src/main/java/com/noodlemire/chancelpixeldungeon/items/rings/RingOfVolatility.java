package com.noodlemire.chancelpixeldungeon.items.rings;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.text.DecimalFormat;
import java.util.HashSet;

public class RingOfVolatility extends Ring
{
	private static final int DURATION = 300;

	private final HashSet<Ring> rings = new HashSet<>();
	private int left = 0;

	{
		icon = ItemSpriteSheet.Icons.RING_VOLATILITY;
	}

	@Override
	protected RingBuff buff()
	{
		return new Volatility();
	}

	private static float extraChance(Char target)
	{
		return (float)Math.pow(1.06, Math.abs(getBonus(target, Volatility.class)));
	}

	@Override
	public String statsInfo()
	{
		String chance = new DecimalFormat("#.##").format(100 * (Math.pow(1.06, Math.abs(soloBonus())) - 1));

		if(isIdentified())
		{
			if(rings.isEmpty())
				return Messages.get(this, "no_effect", chance);

			StringBuilder stats = new StringBuilder(Messages.get(this, "stats", chance, left));

			for(Ring r : rings)
				stats.append("\n\n").append(r.statsInfo());

			return stats.toString();
		}
		else
			return Messages.get(this, "typical_stats", "6", DURATION);
	}

	@Override
	public void activate(Char ch)
	{
		super.activate(ch);
		for(Ring r : rings)
			r.activate(ch);
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single)
	{
		if (super.doUnequip(hero, collect, single))
		{
			for(Ring r : rings)
				r.buff.detach();

			return true;
		}
		else
			return false;
	}

	private static final String LEFT = "left";
	private static final String RINGS = "rings";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);

		bundle.put(LEFT, left);
		bundle.put(RINGS, rings);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);

		left = bundle.getInt(LEFT);

		if(bundle.contains(RINGS))
		{
			for(Bundlable b : bundle.getCollection(RINGS))
			{
				if(b instanceof Ring)
				{
					rings.add((Ring)b);
					((Ring)b).ownedByRing = true;
				}
			}
		}
	}

	public class Volatility extends RingBuff
	{
		private boolean hasAnInstanceOf(Ring inst)
		{
			for(Ring r : rings)
				if(r.getClass().isInstance(inst))
					return true;

			return false;
		}

		private void transform()
		{
			float chanceLeft = GameMath.gate(1, extraChance(target), Generator.Category.RING.classes.length - 1);

			left = DURATION;

			for(Ring r : rings)
				r.buff.detach();

			rings.clear();

			while(chanceLeft > 1 || (chanceLeft > 0 && Random.Float() < chanceLeft))
			{
				Ring ring;
				chanceLeft--;

				do
				{
					ring = (Ring)Generator.randomNormalized(Generator.Category.RING);
				}
				while(ring == null || ring instanceof RingOfVolatility || hasAnInstanceOf(ring));

				ring.ownedByRing = true;
				ring.cursed = cursed;
				ring.level(rawLevel());
				ring.activate(target);
				rings.add(ring);
			}

			if(isIdentified())
			{
				StringBuilder ringNames = new StringBuilder();

				for(Ring r : rings)
					ringNames.append(r.name()).append(", ");

				String rn = ringNames.substring(0, ringNames.length() - 2);

				GLog.h(Messages.get(RingOfVolatility.class, "transformed"), RingOfVolatility.this.toString(), rn);
			}
		}

		@Override
		public boolean act()
		{
			for(Ring r : rings)
			{
				r.level(rawLevel());
				r.cursed = cursed;
			}

			left--;
			if(left <= 0)
				transform();

			return super.act();
		}
	}
}
