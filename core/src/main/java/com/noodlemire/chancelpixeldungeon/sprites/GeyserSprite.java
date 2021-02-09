package com.noodlemire.chancelpixeldungeon.sprites;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.AlphaTweener;

public class GeyserSprite extends CharSprite
{
	private static final float FADE_TIME = 3f;

	private static final int FRAME_WIDTH = 8;
	private static final int FRAME_HEIGHT = 8;

	public GeyserSprite(int x, int y, boolean auto)
	{
		super();

		int a = 3 * x + 12 * y;

		texture(Assets.GEYSERS);

		TextureFilm frames = new TextureFilm(texture, FRAME_WIDTH, FRAME_HEIGHT);

		idle = new Animation(10, true);
		if(auto)
			idle.frames(frames, a, a+1, a, a+2);
		else
			idle.frames(frames, a);

		attack = new Animation(10, false);
		attack.frames(frames, a, a+1, a, a+2);

		die = new Animation(10, false);
		die.frames(frames, a, a+1);

		play(idle);
	}

	@Override
	public int blood()
	{
		return 0xFF889988;
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
					GeyserSprite.this.killAndErase();
					parent.erase(this);
				}
			});
		}
	}

	public void spew()
	{
		play(attack);
	}
}
