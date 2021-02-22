package com.noodlemire.chancelpixeldungeon.items.weapon.missiles.darts;

import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class HeavyDart extends TippedDart
{
	{
		image = ItemSpriteSheet.HEAVY_DART;
	}

	@Override
	public int max(int lvl)
	{
		return super.max(lvl) * 2;
	}
}
