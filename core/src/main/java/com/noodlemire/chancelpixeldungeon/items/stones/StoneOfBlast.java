package com.noodlemire.chancelpixeldungeon.items.stones;


import com.noodlemire.chancelpixeldungeon.items.Bomb;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class StoneOfBlast extends Runestone
{
	{
		image = ItemSpriteSheet.STONE_GYFU;
	}

	@Override
	protected void activate(int cell)
	{
		new Bomb().explode(cell);
	}
}
