package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.CorrosiveGas;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class CorrosionImbue extends DurationBuff implements Expulsion
{
	public static final float DURATION = 30f;

	@Override
	public boolean act()
	{
		GameScene.add(Blob.seed(target.pos, 50, CorrosiveGas.class));

		spend(TICK);
		shorten(TICK);

		if(left() < 5)
			BuffIndicator.refreshHero();

		return true;
	}

	@Override
	public int icon()
	{
		return BuffIndicator.POISON;
	}

	@Override
	public void tintIcon(Image icon)
	{
		if(!FlavourBuff.greyIcon(icon, 5f, left()))
			icon.hardlight(1f, 0.5f, 0f);
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc", dispTurns(left()));
	}

	@Override
	public Class<? extends Blob> expulse()
	{
		return CorrosiveGas.class;
	}

	{
		immunities.add(CorrosiveGas.class);
		immunities.add(Corrosion.class);
	}
}
