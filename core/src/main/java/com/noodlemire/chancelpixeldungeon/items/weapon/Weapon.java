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

package com.noodlemire.chancelpixeldungeon.items.weapon;

import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MagicImmunity;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.KindOfWeapon;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfFuror;
import com.noodlemire.chancelpixeldungeon.items.weapon.curses.Annoying;
import com.noodlemire.chancelpixeldungeon.items.weapon.curses.Displacing;
import com.noodlemire.chancelpixeldungeon.items.weapon.curses.Elastic;
import com.noodlemire.chancelpixeldungeon.items.weapon.curses.Exhausting;
import com.noodlemire.chancelpixeldungeon.items.weapon.curses.Fragile;
import com.noodlemire.chancelpixeldungeon.items.weapon.curses.Friendly;
import com.noodlemire.chancelpixeldungeon.items.weapon.curses.Sacrificial;
import com.noodlemire.chancelpixeldungeon.items.weapon.curses.Wayward;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Blazing;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Chilling;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Dazzling;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Eldritch;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Grim;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Lucky;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Projecting;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Shocking;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Stunning;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Unstable;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Vampiric;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Venomous;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Vorpal;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

abstract public class Weapon extends KindOfWeapon
{
	private static final int HITS_TO_KNOW = 20;

	protected float ACC = 1f;    // Accuracy modifier
	protected float DLY = 1f;    // Speed modifier
	protected int RCH = 1;    // Reach modifier (only applies to melee hits)

	public enum Augment
	{
		SPEED(0.7f, 0.67f),
		DAMAGE(1.5f, 1.67f),
		NONE(1.0f, 1.00f);

		private final float damageFactor;
		private final float delayFactor;

		Augment(float dmg, float dly)
		{
			damageFactor = dmg;
			delayFactor = dly;
		}

		public int damageFactor(int dmg)
		{
			return Math.round(dmg * damageFactor);
		}

		public float delayFactor(float dly)
		{
			return dly * delayFactor;
		}
	}

	public Augment augment = Augment.NONE;

	private int hitsToKnow = HITS_TO_KNOW;

	public Enchantment enchantment;

	@Override
	public int proc(Char attacker, Char defender, int damage)
	{
		if(enchantment != null && defender.buff(MagicImmunity.class) == null && !isBound())
			damage = enchantment.proc(this, attacker, defender, damage);

		if(attacker instanceof Hero && defender instanceof Mob && ((Hero)attacker).critBoost(this))
		{
			defender.sprite.emitter().burst(Speck.factory(Speck.STAR), 5);
			damage = crit(attacker, defender, damage);
		}

		unBind();

		if(!levelKnown)
		{
			if(--hitsToKnow <= 0)
			{
				identify();
				GLog.i(Messages.get(Weapon.class, "identify"));
				Badges.validateItemLevelAquired(this);
			}
		}

		return damage;
	}

	public int crit(Char attacker, Char defender, int damage)
	{
		return (int)Math.ceil(damage * 1.25);
	}

	private static final String UNFAMILIRIARITY = "unfamiliarity";
	private static final String ENCHANTMENT = "enchantment";
	private static final String AUGMENT = "augment";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(UNFAMILIRIARITY, hitsToKnow);
		bundle.put(ENCHANTMENT, enchantment);
		bundle.put(AUGMENT, augment);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		hitsToKnow = bundle.getInt(UNFAMILIRIARITY);
		enchantment = (Enchantment) bundle.get(ENCHANTMENT);
		augment = bundle.getEnum(AUGMENT, Augment.class);
	}

	@Override
	public float accuracyFactor(Char owner)
	{
		int encumbrance = 0;

		if(owner instanceof Hero)
			encumbrance = STRReq() - ((Hero) owner).STR();

		if(hasEnchant(Wayward.class))
			encumbrance = Math.max(2, encumbrance + 2);

		float ACC = this.ACC;

		return encumbrance > 0 ? (float) (ACC / Math.pow(1.5, encumbrance)) : ACC;
	}

	@Override
	public float speedFactor(Char owner)
	{
		int encumbrance = 0;
		if(owner instanceof Hero)
			encumbrance = STRReq() - ((Hero) owner).STR();

		float DLY = augment.delayFactor(this.DLY);

		DLY *= RingOfFuror.modifyAttackDelay(owner);

		return (encumbrance > 0 ? (float) (DLY * Math.pow(1.2, encumbrance)) : DLY);
	}

	@Override
	public int reachFactor(Char owner)
	{
		return hasEnchant(Projecting.class) ? RCH + 1 : RCH;
	}

	public int STRReq()
	{
		return STRReq(rawLevel());
	}

	public abstract int STRReq(int lvl);

	@Override
	public Item upgrade()
	{
		return upgrade(false);
	}

	public Item upgrade(boolean enchant)
	{
		if(enchant && (enchantment == null || enchantment.curse()))
			enchant(Enchantment.random());
		else if(!enchant && !preserved() && Random.Float() > Math.pow(0.9, level()))
			enchant(null);

		cursed = false;

		return super.upgrade();
	}

	@Override
	public String name()
	{
		return enchantment != null && cursedKnown ? enchantment.name(super.name()) : super.name();
	}

	@Override
	public Item random()
	{
		switch(Random.Int(4))
		{
			//case 0: normal
			case 1:
				enchant();
				break;
			case 2:
				augment = Random.Int(2) == 0 ? Augment.DAMAGE : Augment.SPEED;
				break;
			case 3:
				upgrade();
				break;
		}

		if(Random.Int(2) == 0)
		{
			cursed = true;
			enchant(Enchantment.randomCurse());
		}

		return this;
	}

	public Weapon enchant(Enchantment ench)
	{
		enchantment = ench;
		return this;
	}

	public Weapon enchant()
	{
		Class<? extends Enchantment> oldEnchantment = enchantment != null ? enchantment.getClass() : null;
		Enchantment ench = Enchantment.random();
		while(ench.getClass() == oldEnchantment)
		{
			ench = Enchantment.random();
		}

		return enchant(ench);
	}

	public boolean hasEnchant(Class<? extends Enchantment> type)
	{
		return enchantment != null && (enchantment.getClass() == type || enchantment.getClass().getSuperclass() == type)
				&& !isBound();
	}

	public boolean hasGoodEnchant()
	{
		return enchantment != null && !enchantment.curse();
	}

	public boolean hasCurseEnchant()
	{
		return enchantment != null && enchantment.curse();
	}

	@Override
	public ItemSprite.Glowing glowing()
	{
		return enchantment != null && cursedKnown ? enchantment.glowing() : null;
	}

	@Override
	public void bind()
	{
		bind(HITS_TO_KNOW);
	}

	public static abstract class Enchantment implements Bundlable
	{
		private static final Class<?>[] enchants = new Class<?>[]{
				Blazing.class, Venomous.class, Vorpal.class, Shocking.class,
				Chilling.class, Eldritch.class, Lucky.class, Projecting.class, Unstable.class, Dazzling.class,
				Grim.class, Stunning.class, Vampiric.class,};
		private static final float[] chances = new float[]{
				10, 10, 10, 10,
				5, 5, 5, 5, 5, 5,
				2, 2, 2};

		private static final Class<?>[] curses = new Class<?>[]{
				Annoying.class, Displacing.class, Exhausting.class, Fragile.class,
				Sacrificial.class, Wayward.class, Elastic.class, Friendly.class
		};

		public abstract boolean procChance(int level, Char attacker, Char defender, int damage);
		public abstract int proc(Weapon weapon, Char attacker, Char defender, int damage);

		public boolean doProc(Weapon weapon, Char attacker, Char defender, int damage)
		{
			int level = Math.max(0, weapon.level());

			if(weapon instanceof Bow && attacker instanceof Hero && ((Hero) attacker).critBoost(weapon))
				level = (level + 1) * 2;

			return procChance(level, attacker, defender, damage);
		}

		public String name()
		{
			if(!curse())
				return name(Messages.get(this, "enchant"));
			else
				return name(Messages.get(this, "curse"));
		}

		public String name(String weaponName)
		{
			return Messages.get(this, "name", weaponName);
		}

		public String desc()
		{
			return Messages.get(this, "desc");
		}

		public boolean curse()
		{
			return false;
		}

		@Override
		public void restoreFromBundle(Bundle bundle)
		{
		}

		@Override
		public void storeInBundle(Bundle bundle)
		{
		}

		public abstract ItemSprite.Glowing glowing();

		@SuppressWarnings("unchecked")
		public static Enchantment random()
		{
			try
			{
				return ((Class<Enchantment>) enchants[Random.chances(chances)]).newInstance();
			}
			catch(Exception e)
			{
				ChancelPixelDungeon.reportException(e);
				return null;
			}
		}

		@SuppressWarnings("unchecked")
		public static Enchantment randomCurse()
		{
			try
			{
				return ((Class<Enchantment>) Random.oneOf(curses)).newInstance();
			}
			catch(Exception e)
			{
				ChancelPixelDungeon.reportException(e);
				return null;
			}
		}
	}
}
