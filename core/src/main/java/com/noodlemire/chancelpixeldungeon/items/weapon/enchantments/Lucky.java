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

package com.noodlemire.chancelpixeldungeon.items.weapon.enchantments;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Lucky extends Weapon.Enchantment
{
	private static final ItemSprite.Glowing GREEN = new ItemSprite.Glowing(0x00FF00);

	@Override
	public boolean procChance(int level, Char attacker, Char defender, int damage)
	{
		float zeroChance = 0.5f;

		Luck buff = attacker.buff(Luck.class);
		if(buff != null)
			zeroChance = buff.zeroChance;

		return Random.Float() >= zeroChance;
	}

	@Override
	public int proc(Weapon weapon, Char attacker, Char defender, int damage)
	{
		//Does nothing as a proc, rather it uses luckFactor as a "pre-proc" to work better with dynamic strength
		//See kindofweapon.damageroll
		return damage;
	}

	public int luckFactor(Weapon weapon, Char attacker)
	{
		if(weapon.isBound())
			return 1;

		int level = Math.max(0, weapon.level());

		float zeroChance = 0.5f;
		Luck buff = attacker.buff(Luck.class);
		if(buff != null)
			zeroChance = buff.zeroChance;

		if(doProc(weapon, attacker, null, 0))
		{
			if(buff != null)
				buff.detach();

			return 2;
		}
		else
		{
			buff = Buff.affect(attacker, Luck.class);
			buff.zeroChance = zeroChance * (0.5f - 0.001f * level);

			//If 0 is chosen due to lucky, act as if the player waited.
			if(attacker instanceof Hero)
				((Hero)attacker).dynamic(weapon.speedFactor(attacker));

			return 0;
		}
	}

	@Override
	public Glowing glowing()
	{
		return GREEN;
	}

	public static class Luck extends Buff
	{
		float zeroChance;

		@Override
		public boolean act()
		{
			zeroChance += 0.01f;

			if(zeroChance >= 0.5f)
				detach();
			else
				spend(TICK);

			return true;
		}

		private static final String CHANCE = "chance";

		@Override
		public void restoreFromBundle(Bundle bundle)
		{
			super.restoreFromBundle(bundle);
			zeroChance = bundle.getFloat(CHANCE);
		}

		@Override
		public void storeInBundle(Bundle bundle)
		{
			super.storeInBundle(bundle);
			bundle.put(CHANCE, zeroChance);
		}
	}
}
