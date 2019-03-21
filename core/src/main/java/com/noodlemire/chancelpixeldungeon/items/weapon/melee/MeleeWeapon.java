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

package com.noodlemire.chancelpixeldungeon.items.weapon.melee;

import com.noodlemire.chancelpixeldungeon.Challenges;
import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.Transmutable;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.watabou.utils.Random;

public class MeleeWeapon extends Weapon implements Transmutable
{
	public int tier;

	@Override
	public int min(int lvl)
	{
		return tier +  //base
				lvl;    //level scaling
	}

	@Override
	public int max(int lvl)
	{
		return 5 * (tier + 1) +    //base
				lvl * (tier + 1);   //level scaling
	}

	private int dispMin()
	{
		return isIdentified() ? augment.damageFactor(min(level())) : min(0);
	}

	private int dispMax()
	{
		return isIdentified() ? augment.damageFactor(max(level())) : max(0);
	}

	public int STRReq(int lvl)
	{
		//strength req decreases at +1,+3,+6,+10,etc.
		return (8 + tier * 2) - (int) (Math.sqrt(8 * lvl + 1) - 1) / 2;
	}

	@Override
	public String info()
	{
		String info = super.info();

		if(levelKnown)
		{
			info += "\n\n" + Messages.get(this, "stats_known", tier, dispMin(), dispMax(), STRReq());
			if(STRReq() > Dungeon.hero.STR())
				info += " " + Messages.get(this, "too_heavy");
		}
		else
		{
			info += "\n\n" + Messages.get(this, "stats_unknown", tier, dispMin(), dispMax(), STRReq(0));
			if(STRReq(0) > Dungeon.hero.STR())
				info += " " + Messages.get(this, "probably_too_heavy");
		}

		String stats_desc = Messages.get(this, "stats_desc");
		if(!stats_desc.equals("")) info += "\n\n" + stats_desc;

		if(isIdentified())
			switch(augment)
			{
				case SPEED:
					info += "\n\n" + Messages.get(this, "faster");
					break;
				case DAMAGE:
					info += "\n\n" + Messages.get(this, "stronger");
					break;
				case NONE:
			}

		if(enchantment != null && cursedKnown)
		{
			info += "\n\n" + Messages.get(this, "enchanted", enchantment.name());
			info += " " + Messages.get(enchantment, "desc");
		}

		if(cursed && isEquipped(Dungeon.hero))
			info += "\n\n" + Messages.get(this, "cursed_worn");
		else if(cursedKnown && cursed)
			info += "\n\n" + Messages.get(this, "cursed");
		else if(cursedKnown && !isIdentified())
			info += "\n\n" + Messages.get(this, "uncursed");

		if(preservations() > 0)
			info += "\n\n" + Messages.get(this, "preserved", preservations());

		return info;
	}

	@Override
	public int price()
	{
		int price = 20 * tier;

		if(hasGoodEnchant())
			price *= 1.5;
		if(cursedKnown && (cursed || hasCurseEnchant()))
			price /= 2;
		if(levelKnown && level() > 0)
			price *= (level() + 1);
		if(price < 1)
			price = 1;

		return price;
	}

	@Override
	public Item transmute()
	{
		Weapon n;
		Generator.Category c = Generator.wepTiers[tier - 1];

		do
		{
			try
			{
				n = (MeleeWeapon) c.classes[Random.chances(c.probs)].newInstance();
			}
			catch(Exception e)
			{
				ChancelPixelDungeon.reportException(e);
				return null;
			}
		}
		while(Challenges.isItemBlocked(n) || n.getClass() == getClass());

		int level = level();

		if(level > 0)
			n.upgrade(level);
		else if(level < 0)
			n.degrade(-level);

		n.enchantment = enchantment;
		n.levelKnown = levelKnown;
		n.cursedKnown = cursedKnown;
		n.cursed = cursed;
		n.augment = augment;
		n.preserve(preservations());
		if(isBound())
			n.bind();

		return n;
	}
}
