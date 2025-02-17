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

package com.noodlemire.chancelpixeldungeon.items.wands;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Recharging;
import com.noodlemire.chancelpixeldungeon.effects.SpellSprite;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.MagesStaff;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class WandOfMagicMissile extends DamageWand
{
	{
		image = ItemSpriteSheet.WAND_MAGIC_MISSILE;

		canCrit = true;
	}

	public int min(int lvl)
	{
		return 2 + lvl / 2;
	}

	public int max(int lvl)
	{
		return 8 + 2 * lvl;
	}

	@Override
	protected void onZap(Ballistica bolt)
	{
		Char ch = Actor.findChar(bolt.collisionPos);
		if(ch != null)
		{
			processSoulMark(ch, chargesPerCast());
			ch.damage(damageRoll(), this);

			if(curUser.critBoost(null))
			{
				Buff.affect(curUser, Recharging.class).set(2);
				SpellSprite.show(curUser, SpellSprite.CHARGE);
				critFx(ch);
			}

			ch.sprite.burst(0xFFFFFFFF, level() / 2 + 2);
		}
		else
			Dungeon.level.press(bolt.collisionPos, null, true);
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage)
	{
		Buff.affect(attacker, Recharging.class).set(1 + staff.level() / 2f);
	}

	protected int initialCharges()
	{
		return 3;
	}
}
