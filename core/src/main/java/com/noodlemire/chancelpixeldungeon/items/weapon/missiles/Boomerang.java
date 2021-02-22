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

package com.noodlemire.chancelpixeldungeon.items.weapon.missiles;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.sprites.MissileSprite;

public class Boomerang extends MissileWeapon
{
	{
		image = ItemSpriteSheet.BOOMERANG;

		tier = 3;
		sticky = false;
	}

	@Override
	protected void onThrow(int cell)
	{
		Char enemy = Actor.findChar(cell);
		if(enemy == null || enemy == curUser)
		{
			parent = null;
			rangedMiss(cell);
		}
		else if(!curUser.shoot(enemy, this))
			rangedMiss(cell);
		else
			rangedHit(enemy, cell);
	}

	@Override
	public int crit(Char attacker, Char defender, int damage)
	{
		//See afterThrow
		return damage;
	}

	@Override
	protected void rangedMiss(int cell)
	{
		circleBack(cell, curUser);
	}

	@Override
	public void afterThrow(Char enemy, int cell, boolean returnToHero)
	{
		if(curUser.critBoost(this))
			circleBack(cell, curUser);
		else
			super.afterThrow(enemy, cell, returnToHero);
	}

	private void circleBack(int from, Hero owner)
	{
		durability -= durabilityPerUse();

		if(durability <= 0)
			return;

		((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
				reset(from, owner.sprite, curItem, null);

		if(!collect(curUser.belongings.backpack))
			Dungeon.level.drop(this, owner.pos).sprite.drop();
	}

	@Override
	public String desc()
	{
		String info = super.desc();
		switch(augment)
		{
			case SPEED:
				info += "\n\n" + Messages.get(Weapon.class, "faster");
				break;
			case DAMAGE:
				info += "\n\n" + Messages.get(Weapon.class, "stronger");
				break;
			case NONE:
		}

		return info;
	}
}