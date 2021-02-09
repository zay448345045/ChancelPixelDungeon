package com.noodlemire.chancelpixeldungeon.sprites;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.AlphaTweener;

public class GooeyOrbSprite extends CharSprite
{
	private static final float FADE_TIME = 3f;

	private static final int FRAME_WIDTH = 16;
	private static final int FRAME_HEIGHT = 16;

	public GooeyOrbSprite()
	{
		super();

		texture(Assets.GOOEY_ORB);

		TextureFilm frames = new TextureFilm(texture, FRAME_WIDTH, FRAME_HEIGHT);

		idle = new Animation(10, true);
		idle.frames(frames, 0, 1, 2, 3, 4, 5);

		die = new Animation(10, false);
		die.frames(frames, 3);

		play(idle);
	}

	@Override
	public int blood()
	{
		return 0xFF000000;
	}

	@Override
	public void onComplete(Animation anim)
	{
		super.onComplete(anim);

		if(anim == die)
		{
			parent.add(new AlphaTweener(this, 0, FADE_TIME)
			{
				@Override
				protected void onComplete()
				{
					GooeyOrbSprite.this.killAndErase();
					parent.erase(this);
				}
			});
		}
	}
}
