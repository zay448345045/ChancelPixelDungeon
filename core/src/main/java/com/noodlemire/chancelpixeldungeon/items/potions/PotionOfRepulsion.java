package com.noodlemire.chancelpixeldungeon.items.potions;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Repulsion;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class PotionOfRepulsion extends Potion
{
	{
		icon = ItemSpriteSheet.Icons.POTION_REPULSION;
	}

	@Override
	public void shatter(int cell)
	{
		if(Dungeon.level.heroFOV[cell])
			splash(cell);

		Dungeon.playAt(Assets.SND_SHATTER, cell);

		super.shatter(cell);
		if(Actor.findChar(cell) != null)
			Buff.prolong(Actor.findChar(cell), Repulsion.class, Repulsion.DURATION);
	}

	@Override
	public int price()
	{
		return isKnown() ? 40 * quantity : super.price();
	}
}
