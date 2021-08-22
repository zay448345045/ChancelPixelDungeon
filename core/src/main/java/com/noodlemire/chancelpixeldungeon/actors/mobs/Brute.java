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
import com.noodlemire.chancelpixeldungeon.items.Gold;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.BruteSprite;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Brute extends Mob
{
	{
		spriteClass = BruteSprite.class;

		EXP = Random.IntRange(10, 14);

		setHT(EXP * 4 + 9, true);

		loot = Gold.class;
		lootChance = 0.5f;

		properties.add(Property.METALLIC);
	}

	private boolean enraged = false;

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		enraged = HP() < HT() / 4;
	}

	@Override
	public int damageRoll()
	{
		return enraged ?
				EXP * 5 - 5 :
				EXP * 2;
	}

	@Override
	public int attackSkill(Char target)
	{
		return EXP * 2;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, EXP-2);
	}

	@Override
	public int defenseSkill()
	{
		return EXP + 5;
	}

	@Override
	public void damage(int dmg, Object src)
	{
		super.damage(dmg, src);

		if(isAlive() && !enraged && HP() < HT() / 4)
		{
			enraged = true;
			spend(TICK);
			if(Dungeon.level.heroFOV[pos])
				sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "enraged"));
		}
	}

	@Override
	public void needRest()
	{
		if(enraged)
			needRest(3);
		else
			super.needRest();
	}
}
