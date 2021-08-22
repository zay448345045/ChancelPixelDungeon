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
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon.Enchantment;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Grim;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.noodlemire.chancelpixeldungeon.journal.Notes;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.StatueSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Statue extends Mob
{
	{
		spriteClass = StatueSprite.class;

		state = PASSIVE;

		properties.add(Property.INORGANIC);
	}

	protected Weapon weapon;

	public Statue()
	{
		super();

		EXP = Dungeon.depth;

		weapon = (MeleeWeapon) Generator.random(Generator.Category.WEAPON);

		weapon.enchant(Enchantment.random());
		weapon.cursed = false;

		setRestTime();

		setHT(20 + EXP * 5, true);
	}

	private void setRestTime()
	{
		if(weapon.speedFactor(this) >= 1.5f)
			TIME_TO_REST = 4;
		else
		{
			setAttacksBeforeRest(weapon.speedFactor(this) <= 0.7f ? 2 : 1);

			TIME_TO_REST = 2;
		}
	}

	private static final String WEAPON = "weapon";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(WEAPON, weapon);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		//weapon = (Weapon) bundle.get(WEAPON);
		setRestTime();
	}

	@Override
	protected boolean act()
	{
		if(Dungeon.level.heroFOV[pos])
			Notes.add(Notes.Landmark.STATUE);
		return super.act();
	}

	@Override
	public int damageRoll()
	{
		int dmg = weapon.damageRoll(this);

		if(weapon.speedFactor(this) > 0.7f)
			return dmg;

		int base = (int)(dmg * 0.75f);
		int bonus = (int)Math.ceil(dmg * 0.25f);

		return base + bonus * restTimeNeeded(false);
	}

	@Override
	public int attackSkill(Char target)
	{
		return (int) ((9 + EXP) * weapon.accuracyFactor(this));
	}

	@Override
	protected float attackDelay()
	{
		return weapon.speedFactor(this);
	}

	@Override
	protected boolean canAttack(Char enemy)
	{
		return Dungeon.level.distance(pos, enemy.pos) <= weapon.reachFactor(this);
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, EXP + weapon.defenseFactor(this));
	}

	@Override
	public int defenseSkill()
	{
		return 4 + EXP;
	}

	@Override
	public void damage(int dmg, Object src)
	{
		if(state == PASSIVE)
			state = HUNTING;

		super.damage(dmg, src);
	}

	@Override
	public int attackProc(Char enemy, int damage)
	{
		damage = super.attackProc(enemy, damage);
		return weapon.proc(this, enemy, damage);
	}

	@Override
	public void beckon(int cell)
	{
		// Do nothing
	}

	@Override
	public void die(Object cause)
	{
		weapon.identify();
		Dungeon.level.drop(weapon, pos).sprite.drop();
		super.die(cause);
	}

	@Override
	public void destroy()
	{
		Notes.remove(Notes.Landmark.STATUE);
		super.destroy();
	}

	@Override
	public boolean reset()
	{
		state = PASSIVE;
		return true;
	}

	@Override
	public String description()
	{
		weapon.identify();

		return Messages.get(this, "desc", weapon.name());
	}

	public Weapon getWeapon()
	{
		return weapon;
	}

	{
		resistances.add(Grim.class);
	}

}
