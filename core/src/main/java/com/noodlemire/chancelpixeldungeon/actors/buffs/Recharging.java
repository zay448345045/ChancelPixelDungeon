/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Electricity;
import com.noodlemire.chancelpixeldungeon.actors.hero.Belongings;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.artifacts.Artifact;
import com.noodlemire.chancelpixeldungeon.items.bags.MagicalHolster;
import com.noodlemire.chancelpixeldungeon.items.wands.Wand;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.MagesStaff;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.noodlemire.chancelpixeldungeon.ui.QuickSlotButton;
import com.watabou.noosa.Image;

public class Recharging extends DurationBuff implements Expulsion
{
	@Override
	public int icon()
	{
		return BuffIndicator.RECHARGING;
	}

	@Override
	public void tintIcon(Image icon)
	{
		FlavourBuff.greyIcon(icon, 5f, left());
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	@Override
	public boolean act()
	{
		if (target instanceof Hero)
		{
			Hero hero = ((Hero) target);
			Belongings b = hero.belongings;

			if(b.misc1 instanceof Artifact)
				((Artifact) b.misc1).charge(hero, remainder());
			if(b.misc2 instanceof Artifact)
				((Artifact) b.misc2).charge(hero, remainder());

			for(Item i : b.backpack.items)
				if(i instanceof Wand)
					((Wand) i).gainCharge(remainder() / 4);
				else if(i instanceof MagesStaff)
					((MagesStaff) i).gainCharge(remainder() / 4);
				else if(i instanceof MagicalHolster)
					for(Item w : ((MagicalHolster)i).items)
						if(w instanceof Wand)
							((Wand)w).gainCharge(remainder() / 4);

			if(b.weapon instanceof MagesStaff)
				((MagesStaff) b.weapon).gainCharge(remainder() / 4);
		}

		shorten(TICK);
		spend(TICK);

		QuickSlotButton.refresh();

		return true;
	}

	//want to process partial turns for this buff, and not count it when it's expiring.
	//firstly, if this buff has half a turn left, should give out half the benefit.
	//secondly, recall that buffs execute in random order, so this can cause a problem where we can't simply check
	//if this buff is still attached, must instead directly check its remaining time, and act accordingly.
	//otherwise this causes inconsistent behaviour where this may detach before, or after, a wand charger acts.
	private float remainder()
	{
		return Math.min(1f, this.left());
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc", left());
	}

	@Override
	public Class<? extends Blob> expulse()
	{
		return Electricity.class;
	}
}
