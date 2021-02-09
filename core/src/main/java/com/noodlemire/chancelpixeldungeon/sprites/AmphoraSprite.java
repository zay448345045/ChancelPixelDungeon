package com.noodlemire.chancelpixeldungeon.sprites;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.PointF;

public class AmphoraSprite extends CharSprite
{
	private static final float FADE_TIME = 0.3f;
	private static final int FRAME_WIDTH = 16;
	private static final int FRAME_HEIGHT = 21;

	public AmphoraSprite()
	{
		texture(Assets.AMPHORA);

		TextureFilm frames = new TextureFilm(texture, FRAME_WIDTH, FRAME_HEIGHT);

		idle = new Animation(10, true);
		idle.frames(frames, 0);

		die = new Animation(10, false);
		die.frames(frames, 0);

		play(idle);
	}

	//Does nothing
	@Override
	public void bloodBurstA(PointF from, int damage) {}
	@Override
	public void bloodBurstB(PointF from, int damage) {}

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
					AmphoraSprite.this.killAndErase();
					parent.erase(this);
				}
			});
		}
	}
}
