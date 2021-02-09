package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Darkness;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfDarkness extends EnvironmentScroll
{
	{
		should_shout = true;
		initials = 14;
		if(isDangerKnown()) defaultAction = AC_SHOUT;
	}

	@Override
	public void doRead()
	{
		Sample.INSTANCE.play(Assets.SND_READ);
		readAnimation();

		darkenAt(curUser.pos);
	}

	@Override
	protected void onSelect(int pos)
	{
		darkenAt(pos);
	}

	private void darkenAt(int pos)
	{
		GameScene.add(Blob.seed(pos, 750, Darkness.class));

		GLog.i(Messages.get(this, "darken"));
	}

	@Override
	public void empoweredRead()
	{
		for(Mob mob : Dungeon.level.mobs)
			if(Dungeon.level.heroFOV[mob.pos])
				GameScene.add(Blob.seed(mob.pos, 250, Darkness.class));

		GLog.i(Messages.get(this, "darken"));
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
