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
import com.noodlemire.chancelpixeldungeon.actors.buffs.Burning;
import com.noodlemire.chancelpixeldungeon.effects.particles.FlameParticle;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Blazing extends Weapon.Enchantment
{
	private static final ItemSprite.Glowing ORANGE = new ItemSprite.Glowing(0xFF4400);

	@Override
	public boolean procChance(int level, Char attacker, Char defender, int damage)
	{
		// lvl 0 - 33%
		// lvl 1 - 50%
		// lvl 2 - 60%
		return Random.Int(level + 3) >= 2;
	}

	@Override
	public int proc(Weapon weapon, Char attacker, Char defender, int damage)
	{
		int level = Math.max(0, weapon.level());

		if(doProc(weapon, attacker, defender, damage))
		{
			if(Random.Int(2) == 0)
			{
				Buff.affect(defender, Burning.class).reignite();
			}
			defender.damage(Random.Int(1, level + 2), this);

			defender.sprite.emitter().burst(FlameParticle.FACTORY, level + 1);
		}

		return damage;
	}

	@Override
	public Glowing glowing()
	{
		return ORANGE;
	}
}
