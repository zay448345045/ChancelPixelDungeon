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
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.mobs.npcs.Imp;
import com.noodlemire.chancelpixeldungeon.items.KindOfWeapon;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Gauntlet;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Gloves;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.MonkSprite;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Monk extends Mob
{
	{
		spriteClass = MonkSprite.class;

		EXP = Random.IntRange(15, 19);

		setHT(EXP * 5 + 1, true);

		//loot = new Food(); //New loot coming soon.
		//lootChance = 0.083f;

		properties.add(Property.UNDEAD);
	}

	@Override
	public int damageRoll()
	{
		return Random.NormalIntRange(EXP - 3, EXP * 2 - 5);
	}

	@Override
	public int attackSkill(Char target)
	{
		return EXP * 2;
	}

	@Override
	protected float attackDelay()
	{
		return 0.5f;
	}

	@Override
	public int defenseSkill()
	{
		return EXP * 3;
	}

	@Override
	public void rollToDropLoot()
	{
		Imp.Quest.process(this);

		super.rollToDropLoot();
	}

	private int hitsToDisarm = 0;

	@Override
	public int attackProc(Char enemy, int damage)
	{
		damage = super.attackProc(enemy, damage);

		if(enemy == Dungeon.hero)
		{

			Hero hero = Dungeon.hero;
			KindOfWeapon weapon = hero.belongings.weapon;

			if(weapon != null
			   && !(weapon instanceof Gloves)
			   && !(weapon instanceof Gauntlet)
			   && !weapon.cursed)
			{
				if(hitsToDisarm == 0) hitsToDisarm = Random.NormalIntRange(4, 8);

				if(--hitsToDisarm == 0)
				{
					hero.belongings.weapon = null;
					Dungeon.quickslot.convertToPlaceholder(weapon);
					weapon.updateQuickslot();
					Dungeon.level.drop(weapon, hero.pos).sprite.drop();
					GLog.w(Messages.get(this, "disarm", weapon.name()));
				}
			}
		}

		return damage;
	}

	private static String DISARMHITS = "hitsToDisarm";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(DISARMHITS, hitsToDisarm);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		hitsToDisarm = bundle.getInt(DISARMHITS);
	}
}
