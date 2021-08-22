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
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHealing;
import com.noodlemire.chancelpixeldungeon.sprites.BatSprite;
import com.watabou.utils.Random;

public class Bat extends Mob
{
	{
		spriteClass = BatSprite.class;

		EXP = Random.IntRange(8, 12);

		TIME_TO_REST = 2;

		setHT(EXP * 4 + 5, true);
		baseSpeed = 2f;

		flying = true;

		loot = new PotionOfHealing();
		lootChance = 0.5f;
	}

	@Override
	public int damageRoll()
	{
		return EXP * 2 + 2;
	}

	@Override
	public int attackSkill(Char target)
	{
		return EXP * 2;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, EXP / 2);
	}

	@Override
	public int defenseSkill()
	{
		return EXP + 6;
	}

	@Override
	public int attackProc(Char enemy, int damage)
	{
		damage = super.attackProc(enemy, damage);
		int reg = damage;

		if(reg > 0 && !enemy.properties().contains(Property.INORGANIC))
		{
			heal(reg, this);
			sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
		}

		return damage;
	}

	@Override
	public void rollToDropLoot()
	{
		lootChance *= ((4 - Dungeon.LimitedDrops.BAT_HP.count) / 4f);
		super.rollToDropLoot();
	}

	@Override
	protected Item createLoot()
	{
		Dungeon.LimitedDrops.BAT_HP.count++;
		return super.createLoot();
	}
}
