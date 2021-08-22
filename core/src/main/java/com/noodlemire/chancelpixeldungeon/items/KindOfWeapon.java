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

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Lucky;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.utils.GLog;

abstract public class KindOfWeapon extends EquipableItem
{
	private static final float TIME_TO_EQUIP = 1f;

	@Override
	public boolean isEquipped(Hero hero)
	{
		return hero.belongings.weapon == this;
	}

	@Override
	public boolean doEquip(Hero hero)
	{
		detachAll(hero.belongings.backpack);

		if(hero.belongings.weapon == null || hero.belongings.weapon.doUnequip(hero, true))
		{
			hero.belongings.weapon = this;
			activate(hero);

			updateQuickslot();

			cursedKnown = true;
			if(cursed)
			{
				equipCursed(hero);
				GLog.n(Messages.get(KindOfWeapon.class, "equip_cursed"));
			}

			hero.spendAndNext(TIME_TO_EQUIP);
			return true;
		}
		else
		{
			collect(hero.belongings.backpack);
			return false;
		}
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single)
	{
		if(super.doUnequip(hero, collect, single))
		{
			hero.belongings.weapon = null;
			return true;
		}
		else
			return false;
	}

	public int min()
	{
		return min(level());
	}

	public int max()
	{
		return max(level());
	}

	abstract public int min(int lvl);

	abstract public int max(int lvl);

	public int damageRoll(Char owner)
	{
		int luckFactor = 1;

		int min = min();
		int max = max();

		if(this instanceof Weapon)
		{
			Weapon weapon = (Weapon)this;

			if(weapon.hasEnchant(Lucky.class))
				luckFactor = ((Lucky) weapon.enchantment).luckFactor(weapon, owner);

			min = weapon.augment.damageFactor(min) * luckFactor;
			max = weapon.augment.damageFactor(max) * luckFactor;
		}

		if(owner instanceof Hero)
			return ((Hero) owner).dynamicRoll(min, max, dynamicFactor(owner));
		else
			return max;
	}

	public float accuracyFactor(Char owner)
	{
		return 1f;
	}

	public float dynamicFactor(Char owner)
	{
		return 1f;
	}

	public float speedFactor(Char owner)
	{
		return 1f;
	}

	public int reachFactor(Char owner)
	{
		return 1;
	}

	public int defenseFactor(Char owner)
	{
		return 0;
	}

	public int proc(Char attacker, Char defender, int damage)
	{
		return damage;
	}
}
