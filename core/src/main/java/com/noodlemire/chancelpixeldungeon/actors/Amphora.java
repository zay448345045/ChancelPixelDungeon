package com.noodlemire.chancelpixeldungeon.actors;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.effects.Splash;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.AmphoraSprite;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.ColorMath;

public class Amphora extends Char
{
	private Item item;

	{
		name = Messages.get(this, "name");
		actPriority = MOB_PRIO;

		alignment = Alignment.NEUTRAL;

		setHT(1, true);

		properties.add(Property.IMMOVABLE);
		properties.add(Property.BLOB_IMMUNE);
		properties.add(Property.INORGANIC);

		sprite = sprite();
	}

	@Override
	public CharSprite sprite()
	{
		return new AmphoraSprite();
	}

	public void insertItem(Item i)
	{
		item = i;
	}

	@Override
	protected boolean act()
	{
		spend(TICK);
		return true;
	}

	@Override
	public void die(Object src)
	{
		Dungeon.level.drop(item, pos).sprite.drop();
		Splash.at(pos, ColorMath.random(0x424242, 0x935f22), 12);
		Dungeon.playAt(Assets.SND_SMASH, pos);

		for(Mob m : Dungeon.level.mobs)
			if(Dungeon.level.trueDistance(pos, m.pos) < 12)
				m.beckon(pos);

		super.die(src);
	}

	@Override
	public void destroy()
	{
		super.destroy();

		Dungeon.level.others.remove(this);
	}

	private final static String ITEM = "item";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);

		bundle.put(ITEM, item);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);

		item = (Item)bundle.get(ITEM);
	}
}
