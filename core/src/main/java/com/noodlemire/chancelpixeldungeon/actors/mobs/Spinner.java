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
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Web;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Poison;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Terror;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.food.MysteryMeat;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.SpinnerSprite;
import com.watabou.utils.Random;

public class Spinner extends Mob
{
	{
		spriteClass = SpinnerSprite.class;

		EXP = Random.IntRange(14, 16);

		setHT(EXP * 4, true);

		loot = new MysteryMeat();
		lootChance = 0.5f; //Only the base chance, see rollToDropLoot()

		FLEEING = new Fleeing();
	}

	@Override
	public int damageRoll()
	{
		return Random.NormalIntRange(EXP - 4, EXP * 2 - 4);
	}

	@Override
	public int attackSkill(Char target)
	{
		return EXP;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, EXP / 2 - 1);
	}

	@Override
	public int defenseSkill()
	{
		return EXP * 2 - 2;
	}

	@Override
	protected boolean act()
	{
		boolean result = super.act();

		if(state == FLEEING && buff(Terror.class) == null &&
		   enemy != null && enemySeen() && enemy.buff(Poison.class) == null)
		{
			state = HUNTING;
		}
		return result;
	}

	@Override
	public int attackProc(Char enemy, int damage)
	{
		damage = super.attackProc(enemy, damage);
		if(Random.Int(2) == 0)
		{
			Buff.affect(enemy, Poison.class).set(Random.Int(7, 9));
			state = FLEEING;
		}

		return damage;
	}

	@Override
	public void move(int step)
	{
		int curWeb = Blob.volumeAt(pos, Web.class);
		if(state == FLEEING && curWeb < 5)
		{
			GameScene.add(Blob.seed(pos, Random.Int(5, 7) - curWeb, Web.class));
		}
		super.move(step);
	}

	@Override
	public void rollToDropLoot()
	{
		lootChance *= ((4 - Dungeon.LimitedDrops.SPINNER_MEAT.count) / 4f);
		super.rollToDropLoot();
	}

	@Override
	public Item createLoot()
	{
		Dungeon.LimitedDrops.SPINNER_MEAT.count++;
		return super.createLoot();
	}

	{
		resistances.add(Poison.class);
	}

	{
		immunities.add(Web.class);
	}

	private class Fleeing extends Mob.Fleeing
	{
		@Override
		protected void nowhereToRun()
		{
			if(buff(Terror.class) == null)
			{
				state = HUNTING;
			}
			else
			{
				super.nowhereToRun();
			}
		}
	}
}
