package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Magma;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class ScrollOfMagma extends EnvironmentScroll
{
	{
		should_shout = true;
		icon = ItemSpriteSheet.Icons.SCROLL_MAGMA;
		if(isDangerKnown()) defaultAction = AC_SHOUT;
	}

	@Override
	public void doRead()
	{
		Sample.INSTANCE.play(Assets.SND_READ);
		readAnimation();

		GameScene.add(Blob.seed(curUser.pos, 1000, Magma.class));
	}

	@Override
	protected void onSelect(int cell)
	{
		GameScene.add(Blob.seed(cell, 1000, Magma.class));
	}

	@Override
	public void empoweredRead()
	{
		for(Mob mob : Dungeon.level.mobs)
			if(Dungeon.level.heroFOV[mob.pos])
				GameScene.add(Blob.seed(mob.pos, 250, Magma.class));

		readAnimation();
		Sample.INSTANCE.play(Assets.SND_READ);
	}

	@Override
	public void setKnown()
	{
		super.setKnown();
		if(isDangerKnown()) defaultAction = AC_SHOUT;
	}

	@Override
	public void setDangerKnown()
	{
		super.setDangerKnown();
		if(isDangerKnown()) defaultAction = AC_SHOUT;
	}
}
