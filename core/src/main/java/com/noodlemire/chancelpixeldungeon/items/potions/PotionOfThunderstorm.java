package com.noodlemire.chancelpixeldungeon.items.potions;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ThunderCloud;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class PotionOfThunderstorm extends Potion
{
	{
		icon = ItemSpriteSheet.Icons.POTION_THUNDERSTORM;
		harmful = true;

		if(isDangerKnown()) defaultAction = AC_THROW;
	}

	@Override
	public void shatter(int cell)
	{
		if(Dungeon.level.heroFOV[cell])
			splash(cell);

		Dungeon.playAt(Assets.SND_SHATTER, cell);

		GameScene.add(Blob.seed(cell, 1000, ThunderCloud.class));
	}

	@Override
	public void setKnown()
	{
		super.setKnown();
		if(isDangerKnown()) defaultAction = AC_THROW;
	}

	@Override
	public void setDangerKnown()
	{
		super.setDangerKnown();
		if(isDangerKnown()) defaultAction = AC_THROW;
	}

	@Override
	public int price()
	{
		return isKnown() ? 30 * quantity : super.price();
	}
}
