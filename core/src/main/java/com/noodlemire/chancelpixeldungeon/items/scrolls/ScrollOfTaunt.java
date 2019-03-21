package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Taunted;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Weakness;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Goo;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfTaunt extends Scroll
{
	{
		initials = 17;
	}

	@Override
	public void doRead()
	{
		taunt(false, false);
	}

	@Override
	public void doShout()
	{
		taunt(true, false);
	}

	@Override
	public void empoweredRead()
	{
		taunt(false, true);
	}

	private void taunt(boolean shouted, boolean empowered)
	{
		for(Mob m : Dungeon.level.mobs)
		{
			if(m.alignment != Char.Alignment.ALLY && (Dungeon.level.heroFOV[m.pos] || shouted))
			{
				Buff.affect(m, Taunted.class);

				if(m instanceof Goo)
					((Goo) m).startPumping();
				else
					m.sprite.showStatus(CharSprite.NEGATIVE, "!!!");

				//Wake up any sleeping mobs that were affected
				m.beckon(m.pos);

				if(empowered)
					Buff.affect(m, Weakness.class);
			}
		}

		Sample.INSTANCE.play(Assets.SND_MIMIC);
		readAnimation();
		GLog.i(Messages.get(this, "taunt"));
	}
}
