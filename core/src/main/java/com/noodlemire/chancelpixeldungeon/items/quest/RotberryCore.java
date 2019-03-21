package com.noodlemire.chancelpixeldungeon.items.quest;

import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class RotberryCore extends Item
{
	{
		image = ItemSpriteSheet.ROTBERRY;

		unique = true;
	}

	@Override
	public boolean isUpgradable()
	{
		return false;
	}

	@Override
	public boolean isIdentified()
	{
		return true;
	}
}
