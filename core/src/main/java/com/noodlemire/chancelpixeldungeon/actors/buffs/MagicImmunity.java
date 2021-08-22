package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.items.armor.Armor;
import com.noodlemire.chancelpixeldungeon.items.armor.glyphs.AntiMagic;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfPurity;
import com.noodlemire.chancelpixeldungeon.items.scrolls.Scroll;
import com.noodlemire.chancelpixeldungeon.items.wands.Wand;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;

public class MagicImmunity extends FlavourBuff implements Expulsion
{
	public static final float DURATION = 20f;

	{
		immunities.addAll(AntiMagic.RESISTS);

		immunities.add(Wand.class);
		immunities.add(Scroll.class);
		immunities.add(Armor.Glyph.class);
		immunities.add(Weapon.Enchantment.class);

		type = buffType.NEUTRAL;
	}

	@Override
	public int icon()
	{
		return BuffIndicator.ANTIMAGIC;
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc", dispTurns());
	}

	@Override
	public Class<? extends Blob> expulse()
	{
		PotionOfPurity.purifyBlobs(target.pos);
		return null;
	}
}
