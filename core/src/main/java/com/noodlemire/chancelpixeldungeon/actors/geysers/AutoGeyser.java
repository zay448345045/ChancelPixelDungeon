package com.noodlemire.chancelpixeldungeon.actors.geysers;

import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.sprites.GeyserSprite;

public abstract class AutoGeyser extends Geyser
{
	@Override
	public CharSprite sprite()
	{
		return new GeyserSprite(spriteX, spriteY, true);
	}

	@Override
	public boolean act()
	{
		super.act();

		spew();

		spend(TICK);
		return true;
	}
}
