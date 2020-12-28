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
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.PinCushion;
import com.noodlemire.chancelpixeldungeon.actors.buffs.SnipersMark;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.hero.HeroClass;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.bags.Bag;
import com.noodlemire.chancelpixeldungeon.items.bags.MagicalHolster;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfSharpshooting;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Projecting;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

abstract public class MissileWeapon extends Weapon
{
	protected int tier;

	{
		levelKnown = true;

		defaultAction = AC_THROW;
		usesTargeting = true;
	}

	@Override
	public boolean stackable()
	{
		return true;
	}

	protected boolean sticky = true;

	private static final float MAX_DURABILITY = 100;
	protected float durability = MAX_DURABILITY;
	protected float baseUses = 10;

	public boolean holster;

	//used to reduce durability from the source weapon stack, rather than the one being thrown.
	protected MissileWeapon parent;

	@Override
	public ArrayList<String> actions(Hero hero)
	{
		ArrayList<String> actions = super.actions(hero);
		actions.remove(AC_EQUIP);
		return actions;
	}

	@Override
	public boolean canBeCursed()
	{
		return false;
	}

	@Override
	public boolean collect(Bag container)
	{
		if(container instanceof MagicalHolster) holster = true;
		return super.collect(container);
	}

	@Override
	public int throwPos(Hero user, int dst)
	{
		if(hasEnchant(Projecting.class)
		   && !Dungeon.level.solid[dst] && Dungeon.level.distance(user.pos, dst) <= 4)
			return dst;
		else
			return super.throwPos(user, dst);
	}

	@Override
	protected void onThrow(int cell)
	{
		Char enemy = Actor.findChar(cell);
		if(enemy == null || enemy == curUser)
		{
			parent = null;
			super.onThrow(cell);
		}
		else if(!curUser.shoot(enemy, this))
			rangedMiss(cell);
		else
			rangedHit(enemy, cell);
	}

	@Override
	public Item random()
	{
		if(!stackable()) return this;

		//2: 66.67% (2/3)
		//3: 26.67% (4/15)
		//4: 6.67%  (1/15)
		quantity = 2;
		if(Random.Int(3) == 0)
		{
			quantity++;
			if(Random.Int(5) == 0)
			{
				quantity++;
			}
		}
		return this;
	}

	@Override
	public float castDelay(Char user, int dst)
	{
		float delay = speedFactor(user);

		Char enemy = Actor.findChar(dst);

		if(enemy != null)
		{
			SnipersMark mark = user.buff(SnipersMark.class);
			if(mark != null)
				if(mark.object == enemy.id())
					delay *= 0.5f;
		}

		return delay;
	}

	protected void rangedHit(Char enemy, int cell)
	{
		rangedHit(enemy, cell, false);
	}

	public void rangedHit(Char enemy, int cell, boolean returnToHero)
	{
		//if this weapon was thrown from a source stack, degrade that stack.
		//unless a weapon is about to break, then break the one being thrown
		if (parent != null)
		{
			if(parent.durability <= parent.durabilityPerUse())
			{
				durability = 0;
				parent.durability = MAX_DURABILITY;
			}
			else
			{
				parent.durability -= parent.durabilityPerUse();
				if(parent.durability > 0 && parent.durability <= parent.durabilityPerUse())
				{
					if(level() <= 0)
						GLog.w(Messages.get(this, "about_to_break"));
					else
						GLog.n(Messages.get(this, "about_to_break"));
				}
			}

			parent = null;
		}
		else
		{
			durability -= durabilityPerUse();
			if(durability > 0 && durability <= durabilityPerUse())
			{
				if(level() <= 0)
					GLog.w(Messages.get(this, "about_to_break"));
				else
					GLog.n(Messages.get(this, "about_to_break"));
			}
		}

		if(durability > 0)
		{
			if(returnToHero)
			{
				if(!collect())
					Dungeon.level.drop(this, Dungeon.hero.pos).sprite.drop();

				return;
			}

			//attempt to stick the missile weapon to the enemy, just drop it if we can't.
			if(enemy.isAlive() && enemy instanceof Mob && sticky)
			{
				PinCushion p = Buff.affect(enemy, PinCushion.class);
				if(p.target == enemy)
					p.stick(this);
			}
			else super.onThrow(cell);
		}
	}

	protected void rangedMiss(int cell)
	{
		parent = null;
		super.onThrow(cell);
	}

	protected float durabilityPerUse()
	{
		float usages = baseUses * (float) (Math.pow(3, level()));

		if(Dungeon.hero.heroClass == HeroClass.HUNTRESS) usages *= 1.5f;
		if(holster) usages *= MagicalHolster.HOLSTER_DURABILITY_FACTOR;

		usages *= RingOfSharpshooting.durabilityMultiplier(Dungeon.hero);

		//at 100 uses, items just last forever.
		if(usages >= 100f) return 0;

		//add a tiny amount to account for rounding error for calculations like 1/3
		return (MAX_DURABILITY / usages) + 0.001f;
	}

	@Override
	public void reset()
	{
		super.reset();
		durability = MAX_DURABILITY;
	}

	@Override
	public int min()
	{
		return Math.max(0, min(level() + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)));
	}

	@Override
	public int min(int lvl)
	{
		return 2 * tier +                      //base
		       (tier == 1 ? lvl : 2 * lvl);      //level scaling
	}

	@Override
	public int max()
	{
		return Math.max(0, max(level() + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)));
	}

	@Override
	public int max(int lvl)
	{
		return 5 * tier +                      //base
		       (tier == 1 ? 2 * lvl : tier * lvl); //level scaling
	}

	private int dispMin()
	{
		return augment.damageFactor(min());
	}

	private int dispMax()
	{
		return augment.damageFactor(max());
	}

	public int STRReq(int lvl){
		lvl = Math.max(0, lvl);
		//strength req decreases at +1,+3,+6,+10,etc.
		return (7 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
	}

	@Override
	//FIXME some logic here assumes the items are in the player's inventory. Might need to adjust
	public Item upgrade()
	{
		if(!bundleRestoring)
		{
			if(quantity > 1)
			{
				MissileWeapon upgraded = (MissileWeapon) split(1);
				upgraded.parent = null;

				upgraded = (MissileWeapon) upgraded.upgrade();

				//try to put the upgraded into inventory, if it didn't already merge
				if(upgraded.quantity() == 1 && !upgraded.collect())
					Dungeon.level.drop(upgraded, Dungeon.hero.pos);

				updateQuickslot();
				return upgraded;
			}
			else
			{
				durability = MAX_DURABILITY;
				super.upgrade();

				Item similar = Dungeon.hero.belongings.getSimilar(this);
				if(similar != null)
				{
					detach(Dungeon.hero.belongings.backpack);
					return similar.merge(this);
				}
				updateQuickslot();
				return this;
			}
		}
		else
			return super.upgrade();
	}

	@Override
	public Item merge(Item other)
	{
		super.merge(other);
		if(isSimilar(other))
		{
			durability += ((MissileWeapon) other).durability;
			durability -= MAX_DURABILITY;
			while(durability <= 0)
			{
				quantity -= 1;
				durability += MAX_DURABILITY;
			}
		}
		return this;
	}

	@Override
	public Item split(int amount)
	{
		bundleRestoring = true;
		Item split = super.split(amount);
		bundleRestoring = false;

		//unless the thrown weapon will break, split off a max durability item and
		//have it reduce the durability of the main stack. Cleaner to the player this way
		if(split != null)
		{
			MissileWeapon m = (MissileWeapon) split;
			m.durability = MAX_DURABILITY;
			m.parent = this;
		}

		return split;
	}

	@Override
	public boolean doPickUp(Hero hero)
	{
		parent = null;
		return super.doPickUp(hero);
	}

	@Override
	public boolean isIdentified()
	{
		return true;
	}

	@Override
	public int visiblyUpgraded()
	{
		return level();
	}

	@Override
	public String info()
	{
		String info = desc();

		info += "\n\n" + Messages.get(MissileWeapon.class, "stats", tier, dispMin(), dispMax(), STRReq());

		if(STRReq() > Dungeon.hero.STR())
		{
			info += " " + Messages.get(Weapon.class, "too_heavy");
		}

		if(enchantment != null && (cursedKnown || !enchantment.curse()))
		{
			info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
			info += " " + Messages.get(enchantment, "desc");
		}

		if(cursed && isEquipped(Dungeon.hero))
		{
			info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
		}
		else if(cursedKnown && cursed)
		{
			info += "\n\n" + Messages.get(Weapon.class, "cursed");
		}

		info += "\n\n" + Messages.get(MissileWeapon.class, "distance");

		info += "\n\n" + Messages.get(this, "durability");

		if (durabilityPerUse() > 0)
		{
			info += " " + Messages.get(this, "uses_left",
					(int) Math.ceil(durability / durabilityPerUse()),
					(int) Math.ceil(MAX_DURABILITY / durabilityPerUse()));
		}
		else
			info += " " + Messages.get(this, "unlimited_uses");

		if(preservations() > 0)
			info += "\n\n" + Messages.get(Weapon.class, "preserved", preservations());

		return info;
	}

	private static final String DURABILITY = "durability";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(DURABILITY, durability);
	}

	private static boolean bundleRestoring = false;

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		bundleRestoring = true;
		super.restoreFromBundle(bundle);
		bundleRestoring = false;

		durability = bundle.getInt(DURABILITY);
	}

	@Override
	public int price()
	{
		return 6 * tier * quantity * (level()+1);
	}
}