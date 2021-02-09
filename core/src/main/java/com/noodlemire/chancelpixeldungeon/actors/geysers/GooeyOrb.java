package com.noodlemire.chancelpixeldungeon.actors.geysers;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Ooze;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Goo;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.effects.Splash;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.sprites.GooeyOrbSprite;
import com.watabou.utils.PathFinder;

public class GooeyOrb extends ManualGeyser
{
	{
		randomlySpew = false;
	}

	@Override
	public Class<? extends Blob> blobClass()
	{
		return null;
	}

	@Override
	public CharSprite sprite()
	{
		return new GooeyOrbSprite();
	}

	@Override
	public int defenseProc(Char enemy, int damage)
	{
		spew();
		return damage;
	}

	@Override
	protected void spew()
	{
		if (buff(Disabled.class) != null)
			return;

		for(int n : PathFinder.NEIGHBOURS9)
		{
			Splash.at(pos+n, 0xFF000000, 5);
			Char ch = Actor.findChar(pos+n);

			if(ch != null && !(ch instanceof Goo))
				Buff.affect(ch, Ooze.class);
		}
	}

	@Override
	public void damage(int dmg, Object src)
	{
		observe();

		for(Mob m : Dungeon.level.mobs)
		{
			if(m instanceof Goo)
			{
				Goo goo = (Goo)m;

				if(goo.closest_char != this && !fieldOfView[goo.pos])
				{
					goo.closest_char = this;
					goo.track(pos);
				}

				break;
			}
		}

		super.damage(dmg, src);
	}
}
