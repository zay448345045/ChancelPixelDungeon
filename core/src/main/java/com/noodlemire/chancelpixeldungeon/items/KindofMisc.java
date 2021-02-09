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

package com.noodlemire.chancelpixeldungeon.items;

import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.utils.GLog;

public abstract class KindofMisc extends EquipableItem
{
	private static final float TIME_TO_EQUIP = 1f;

	@Override
	public boolean doEquip(final Hero hero)
	{
		if(hero.belongings.miscSlots.isFull())
		{
			GLog.w(Messages.get(this, "no_misc_slots"));
			return false;
		}
		else
		{
			collect(hero.belongings.miscSlots);

			afterEquip(hero);
			return true;
		}
	}

	public void afterEquip(final Hero hero)
	{
		detach(hero.belongings.backpack);

		activate(hero);

		cursedKnown = true;
		if(cursed)
		{
			equipCursed(hero);
			GLog.n(Messages.get(this, "equip_cursed", this));
		}

		hero.spendAndNext(TIME_TO_EQUIP);
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single)
	{
		if(super.doUnequip(hero, collect, single))
		{
			if(hero.belongings.classMisc == this)
				hero.belongings.classMisc = null;
			else
				detach(hero.belongings.miscSlots);

			return true;
		}
		else
			return false;
	}

	@Override
	public boolean isEquipped(Hero hero)
	{
		return hero.belongings.classMisc == this || hero.belongings.miscSlots.contains(this);
	}
}
