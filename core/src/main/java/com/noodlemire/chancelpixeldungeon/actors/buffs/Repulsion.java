package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfExpulsion;
import com.noodlemire.chancelpixeldungeon.items.scrolls.EnvironmentScroll;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfBlastWave;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;

public class Repulsion extends FlavourBuff implements Expulsion
{
	public static final float DURATION = 20f;

	{
		type = buffType.POSITIVE;
	}

	@Override
	public int icon()
	{
		return BuffIndicator.REPULSION;
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
		for(int d = PotionOfExpulsion.MAX_RANGE; d >= PotionOfExpulsion.MIN_RANGE; d--)
		{
			boolean[] aoe = EnvironmentScroll.fovAt(target.pos, d, false);
			for(int p = 0; p < aoe.length; p++)
			{
				Char ch = Actor.findChar(p);
				if(aoe[p] && ch != null && p != target.pos)
				{
					Ballistica trajectory = new Ballistica(p, p + (p - target.pos), Ballistica.MAGIC_BOLT);
					WandOfBlastWave.throwChar(ch, trajectory, PotionOfExpulsion.MAX_RANGE);
				}
			}
		}

		WandOfBlastWave.BlastWave.blast(target.pos);

		return null;
	}
}
