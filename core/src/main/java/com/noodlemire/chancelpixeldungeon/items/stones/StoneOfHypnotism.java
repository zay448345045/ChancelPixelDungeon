package com.noodlemire.chancelpixeldungeon.items.stones;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Charm;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class StoneOfHypnotism extends Runestone
{
	{
		image = ItemSpriteSheet.STONE_HAGLAZ;
	}

	@Override
	protected void activate(int cell)
	{
		Char ch = Actor.findChar(cell);

		int amt = 10;
		if(ch != null)
		{
			Buff.affect(ch, Charm.class, amt).object = curUser.id();
			ch.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, amt);

			amt /= 2;
			Buff.affect(curUser, Charm.class, amt).object = ch.id();
			curUser.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, amt);
		}
		else
			CellEmitter.center(cell).start(Speck.factory(Speck.HEART), 0.2f, amt);

		Sample.INSTANCE.play(Assets.SND_CHARMS);
	}
}
