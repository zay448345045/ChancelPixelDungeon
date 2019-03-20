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

package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.effects.Enchanting;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.armor.Armor;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.noodlemire.chancelpixeldungeon.windows.WndBag;

public class ScrollOfMagicalInfusion extends InventoryScroll
{
	{
		initials = 2;
		mode = WndBag.Mode.ENCHANTABLE;
	}
	
	@Override
	protected void onItemSelected( Item item )
	{
		if (item instanceof Weapon)
			((Weapon)item).enchant();
		else
			((Armor)item).inscribe();
		
		GLog.p( Messages.get(this, "infuse", item.name()) );
		
		Badges.validateItemLevelAquired(item);

		Enchanting.show(curUser, item);
	}
	
	@Override
	public void empoweredRead() {
		//does nothing for now, this should never happen.
	}

	@Override
	public int price() {
		return isKnown() ? 100 * quantity : super.price();
	}
}
