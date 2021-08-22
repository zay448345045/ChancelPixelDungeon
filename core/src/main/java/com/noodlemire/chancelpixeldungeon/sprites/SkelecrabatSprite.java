package com.noodlemire.chancelpixeldungeon.sprites;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;

public class SkelecrabatSprite extends MobSprite
{
	public SkelecrabatSprite()
	{
		super();

		texture(Assets.SKELECRABAT);

		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(8, true);
		idle.frames(frames, 0, 1);

		run = new Animation(12, true);
		run.frames(frames, 0, 1);

		attack = new Animation(12, false);
		attack.frames(frames, 2, 3, 4);

		die = new Animation(12, false);
		die.frames(frames, 5, 6, 7, 8);

		play(idle);
	}

	@Override
	public void die()
	{
		super.die();
		if(Dungeon.level.heroFOV[ch.pos])
		{
			emitter().burst(Speck.factory(Speck.BONE), 6);
		}
	}
}
