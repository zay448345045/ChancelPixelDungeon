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

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.noodlemire.chancelpixeldungeon.levels.features.Chasm;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.SkeletonSprite;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Skeleton extends Mob
{
	{
		spriteClass = SkeletonSprite.class;

		EXP = Random.IntRange(6, 9);

		setHT(10 + 3 * EXP, true);

		loot = Generator.Category.WEAPON;
		lootChance = 0.125f;

		properties.add(Property.UNDEAD);
		properties.add(Property.INORGANIC);
	}

	@Override
	public int damageRoll()
	{
		return EXP * 2;
	}

	@Override
	public void die(Object cause)
	{
		super.die(cause);

		if(cause == Chasm.class) return;

		boolean heroKilled = false;
		for(int i = 0; i < PathFinder.NEIGHBOURS8.length; i++)
		{
			Char ch = findChar(pos + PathFinder.NEIGHBOURS8[i]);
			if(ch != null && ch.isAlive())
			{
				int damage = Random.NormalIntRange(EXP, EXP * 2);
				damage = Math.max(0, damage - (ch.drRoll() + ch.drRoll()));
				ch.damage(damage, this);
				if(ch == Dungeon.hero && !ch.isAlive())
				{
					heroKilled = true;
				}
			}
		}

		Dungeon.playAt(Assets.SND_BONES, pos);

		if(heroKilled)
		{
			Dungeon.fail(getClass());
			GLog.n(Messages.get(this, "explo_kill"));
		}
	}

	@Override
	protected Item createLoot()
	{
		Item loot;
		do
		{
			loot = Generator.randomWeapon();
			//50% chance of re-rolling tier 4 or 5 melee weapons
		}
		while(((MeleeWeapon) loot).tier >= 4 && Random.Int(2) == 0);
		loot.level(0);
		return loot;
	}

	@Override
	public int attackSkill(Char target)
	{
		return 2 + EXP * 2;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, EXP);
	}

	@Override
	public int defenseSkill()
	{
		return EXP * 2 - 2;
	}
}
