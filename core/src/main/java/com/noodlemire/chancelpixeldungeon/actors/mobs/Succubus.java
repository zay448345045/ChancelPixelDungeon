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
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Charm;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Light;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Sleep;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.noodlemire.chancelpixeldungeon.items.stones.StoneOfHypnotism;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.sprites.SuccubusSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Succubus extends Mob
{
	private static final int BLINK_DELAY = 5;

	private int delay = 0;

	{
		spriteClass = SuccubusSprite.class;

		EXP = Random.IntRange(16, 22);

		setHT(90 + EXP, true);
		viewDistance = Light.DISTANCE;

		loot = new StoneOfHypnotism();
		lootChance = 0.15f;

		properties.add(Property.DEMONIC);
	}

	@Override
	public int damageRoll()
	{
		return Random.NormalIntRange(EXP + 8, EXP * 2);
	}

	@Override
	public int attackProc(Char enemy, int damage)
	{
		damage = super.attackProc(enemy, damage);

		if(Random.Int(3) == 0)
		{
			Buff.affect(enemy, Charm.class, Random.IntRange(3, 7)).object = id();
			enemy.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
			Dungeon.playAt(Assets.SND_CHARMS, enemy.pos);
		}

		return damage;
	}

	@Override
	protected boolean getCloser(int target)
	{
		if(fieldOfView[target] && Dungeon.level.distance(pos, target) > 2 && delay <= 0)
		{
			blink(target);
			spend(-1 / speed());
			return true;
		}
		else
		{
			delay--;
			return super.getCloser(target);
		}
	}

	private void blink(int target)
	{
		Ballistica route = new Ballistica(pos, target, Ballistica.PROJECTILE);
		int cell = route.collisionPos;

		//can't occupy the same cell as another char, so move back one.
		if(Actor.findChar(cell) != null && cell != this.pos)
			cell = route.path.get(route.dist - 1);

		if(Dungeon.level.avoid[cell])
		{
			ArrayList<Integer> candidates = new ArrayList<>();
			for(int n : PathFinder.NEIGHBOURS8)
			{
				cell = route.collisionPos + n;
				if(Dungeon.level.passable[cell] && Actor.findChar(cell) == null)
				{
					candidates.add(cell);
				}
			}
			if(candidates.size() > 0)
				cell = Random.element(candidates);
			else
			{
				delay = BLINK_DELAY;
				return;
			}
		}

		ScrollOfTeleportation.appear(this, cell);

		delay = BLINK_DELAY;
	}

	@Override
	public int attackSkill(Char target)
	{
		return EXP * 3;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, EXP / 2);
	}

	@Override
	public int defenseSkill()
	{
		return EXP + 10;
	}

	{
		immunities.add(Sleep.class);
	}
}
