package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Sunlight;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfSunlight extends Scroll
{
	{
		icon = ItemSpriteSheet.Icons.SCROLL_SUNLIGHT;
	}

	@Override
	public void doRead()
	{
		read(false);
		Sample.INSTANCE.play(Assets.SND_READ);
	}

	@Override
	public void doShout()
	{
		read(true);
		Sample.INSTANCE.play(Assets.SND_CHALLENGE);
	}

	@Override
	public void empoweredRead()
	{
		read(true);
		Sample.INSTANCE.play(Assets.SND_READ);
	}

	private void read(boolean empowered)
	{
		GameScene.add(Blob.seed(curUser.pos, empowered ? 5000 : 1000, Sunlight.class));

		GLog.i(Messages.get(this, "shine"));

		readAnimation();
	}
}
