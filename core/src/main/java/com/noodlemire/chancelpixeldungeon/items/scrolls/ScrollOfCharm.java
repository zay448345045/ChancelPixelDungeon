package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Charm;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfCharm extends Scroll
{
	{
		icon = ItemSpriteSheet.Icons.SCROLL_CHARM;
	}

	@Override
	public void doRead()
	{
		charm(false);
	}

	@Override
	public void doShout()
	{
		curUser.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.3f, 5);
		Sample.INSTANCE.play(Assets.SND_CHALLENGE);
		GLog.i(Messages.get(this, "too_loud"));
		readAnimation();
	}

	@Override
	public void empoweredRead()
	{
		charm(true);
	}

	private void charm(boolean empowered)
	{
		for(Mob m : Dungeon.level.mobs)
		{
			if(Dungeon.level.heroFOV[m.pos] || empowered)
			{
				Buff.affect(m, Charm.class, 10).object = curUser.id();

				m.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.3f, 5);
			}
		}

		curUser.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.3f, 5);
		Sample.INSTANCE.play(Assets.SND_CHARMS);
		GLog.i(Messages.get(this, "charm"));
		readAnimation();
	}
}
