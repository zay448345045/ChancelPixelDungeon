package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfPurity;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class MagicShield extends DurationBuff implements Expulsion
{
	private float reduction = 0;

	private static final String REDUCTION = "reduction";

	{
		type = buffType.POSITIVE;
	}

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(REDUCTION, reduction);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		reduction = bundle.getFloat(REDUCTION);
	}

	@Override
	public boolean act()
	{
		if(target.isAlive() && left() > 0 && target.SHLD() > 0)
		{
			//Shield decays faster at high amounts and slower at low amounts
			reduction += .5f * (target.HT() / 5f) * left() / target.HT();

			//Because shielding is an integer, it must be reduced by integer amounts
			if(reduction >= 1)
			{
				//Converting reduction to an int automatically rounds it down
				target.SHLD((int) -reduction);
				shorten((int) reduction);
				reduction -= (int) reduction;
			}

			//If the target lost shielding i.e. due to being hit, then subtract that shielding lost from amount
			if(target.SHLD() < left())
				shorten(left() - target.SHLD());
		}
		else
			detach();

		spend(TICK);

		return true;
	}

	@Override
	public int icon()
	{
		return BuffIndicator.MAGICSHIELD;
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc", left());
	}

	@Override
	public void fx(boolean on)
	{
		if(on) target.sprite.add(CharSprite.State.FORCEFIELD);
		else target.sprite.remove(CharSprite.State.FORCEFIELD);
	}

	public void set(int amount)
	{
		extend(amount);
		target.SHLD(amount);
	}

	@Override
	public void detach()
	{
		super.detach();
		target.SHLD((int) -left());
	}

	@Override
	public Class<? extends Blob> expulse()
	{
		PotionOfPurity.purifyBlobs(target.pos);
		return null;
	}
}
