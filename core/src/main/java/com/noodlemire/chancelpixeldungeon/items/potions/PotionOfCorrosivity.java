package com.noodlemire.chancelpixeldungeon.items.potions;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.CorrosiveGas;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;

public class PotionOfCorrosivity extends Potion
{
	{
		initials = 15;

		if(isIdentified()) defaultAction = AC_THROW;
	}

	@Override
	public void shatter(int cell)
	{

		if(Dungeon.level.heroFOV[cell])
		{
			splash(cell);
			Sample.INSTANCE.play(Assets.SND_SHATTER);
		}

		GameScene.add(Blob.seed(cell, 500, CorrosiveGas.class));
	}

	@Override
	public void setKnown()
	{
		super.setKnown();
		if(isIdentified()) defaultAction = AC_THROW;
	}

	@Override
	public int price()
	{
		return isKnown() ? 30 * quantity : super.price();
	}
}
