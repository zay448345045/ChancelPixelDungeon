package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;

public class Transparency extends FlavourBuff
{
	public static final float DURATION = 1f;

	{
		type = buffType.SILENT;
	}

	@Override
	public void detach()
	{
		super.detach();
		Dungeon.observe();
		GameScene.updateFog();
	}
}
