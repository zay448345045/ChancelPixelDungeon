package com.noodlemire.chancelpixeldungeon.items.weapon.missiles.darts;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class HeavyDart extends TippedDart
{
	{
		image = ItemSpriteSheet.HEAVY_DART;
	}

	@Override
	public int damageRoll(Char owner)
	{
		return super.damageRoll(owner) * 2;
	}
}
