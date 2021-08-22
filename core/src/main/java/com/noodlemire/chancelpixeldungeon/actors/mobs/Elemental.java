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

package com.noodlemire.chancelpixeldungeon.actors.mobs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Burning;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Chill;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Frost;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHydrocombustion;
import com.noodlemire.chancelpixeldungeon.sprites.ElementalSprite;
import com.watabou.utils.Random;

public class Elemental extends Mob
{
	{
		spriteClass = ElementalSprite.class;

		EXP = Random.IntRange(12, 17);

		setHT(EXP * 5, true);

		flying = true;

		loot = new PotionOfHydrocombustion();
		lootChance = 0.1f;

		properties.add(Property.FIERY);
	}

	@Override
	public int damageRoll()
	{
		return EXP * 3 - 10;
	}

	@Override
	public int attackSkill(Char target)
	{
		return EXP * 2 + 2;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, EXP / 2 - 1);
	}

	@Override
	public int defenseSkill()
	{
		return EXP * 2 - 4;
	}

	@Override
	public int attackProc(Char enemy, int damage)
	{
		damage = super.attackProc(enemy, damage);
		if(Random.Int(2) == 0)
		{
			Buff.affect(enemy, Burning.class).reignite();
		}

		return damage;
	}

	@Override
	public void add(Buff buff)
	{
		if(buff instanceof Frost || buff instanceof Chill)
			if(Dungeon.level.water[this.pos])
				damage(Random.NormalIntRange(HT() / 2, HT()), buff);
			else
				damage(Random.NormalIntRange(1, HT() * 2 / 3), buff);
		else
			super.add(buff);
	}
}
