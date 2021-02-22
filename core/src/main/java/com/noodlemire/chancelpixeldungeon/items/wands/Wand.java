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

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Challenges;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Invisibility;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MagicImmunity;
import com.noodlemire.chancelpixeldungeon.actors.buffs.SoulMark;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.hero.HeroClass;
import com.noodlemire.chancelpixeldungeon.actors.hero.HeroSubClass;
import com.noodlemire.chancelpixeldungeon.effects.MagicMissile;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.KindofMisc;
import com.noodlemire.chancelpixeldungeon.items.Transmutable;
import com.noodlemire.chancelpixeldungeon.items.bags.Bag;
import com.noodlemire.chancelpixeldungeon.items.bags.MagicalHolster;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfEnergy;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.MagesStaff;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.CellSelector;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.ui.QuickSlotButton;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class Wand extends KindofMisc implements Transmutable
{
	private static final int USAGES_TO_KNOW = 20;

	private static final String AC_ZAP = "ZAP";

	private static final float TIME_TO_ZAP = 1f;

	public int maxCharges = initialCharges();
	public int curCharges = maxCharges;
	public float partialCharge = 0f;

	private Charger charger;

	private boolean curChargeKnown = false;

	private int usagesToKnow = USAGES_TO_KNOW;

	public boolean ownedByStaff = false;

	protected boolean canCrit = false;

	int collisionProperties = Ballistica.MAGIC_BOLT;

	{
		defaultAction = AC_ZAP;
		usesTargeting = true;
		bones = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero)
	{
		ArrayList<String> actions = super.actions(hero);
		if(curCharges > 0 || !curChargeKnown)
			actions.add(AC_ZAP);

		return actions;
	}

	@Override
	public void execute(Hero hero, String action)
	{
		super.execute(hero, action);

		if(action.equals(AC_ZAP))
		{
			if(!isEquipped(hero))
			{
				GLog.w(Messages.get(this, "need_to_equip"));
				return;
			}

			curUser = hero;
			curItem = this;
			GameScene.selectCell(zapper);
		}
	}

	protected abstract void onZap(Ballistica attack);

	public abstract void onHit(MagesStaff staff, Char attacker, Char defender, int damage);

	@Override
	public boolean collect(Bag container)
	{
		if(super.collect(container))
		{
			if(container.owner != null)
				if(container instanceof MagicalHolster)
					charge(container.owner, MagicalHolster.HOLSTER_SCALE_FACTOR);
				else
					charge(container.owner);

			return true;
		}

		return false;
	}

	@Override
	public void activate(Char ch)
	{
		if(ch instanceof Hero)
		{
			Hero hero = (Hero)ch;

			if (hero.belongings.backpack.contains(MagicalHolster.class))
				charge(hero, MagicalHolster.HOLSTER_SCALE_FACTOR);
			else
				charge(hero);
		}
	}

	@Override
	public boolean doEquip(Hero hero)
	{
		if(hero.heroClass == HeroClass.MAGE && hero.belongings.classMisc == null)
		{
			hero.belongings.classMisc = this;
			afterEquip(hero);
			return true;
		}

		return super.doEquip(hero);
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single)
	{
		if(super.doUnequip(hero, collect, single))
		{
			stopCharging();
			return true;
		}
		else
			return false;
	}

	@Override
	public boolean isEquipped(Hero hero)
	{
		if(hero.belongings.weapon instanceof MagesStaff && ((MagesStaff)hero.belongings.weapon).ownedByStaff(this))
			return true;

		for(Item i : hero.belongings.backpack.items)
			if(i instanceof MagesStaff && ((MagesStaff)i).ownedByStaff(this))
				return true;

		return super.isEquipped(hero);
	}

	public void gainCharge(float amt)
	{
		partialCharge += amt;
		while(partialCharge >= 1)
		{
			curCharges = Math.min(maxCharges, curCharges + 1);
			partialCharge--;
			updateQuickslot();
		}
	}

	public void charge(Char owner)
	{
		if(charger == null) charger = new Charger();
		charger.attachTo(owner);
	}

	public void charge(Char owner, float chargeScaleFactor)
	{
		charge(owner);
		charger.setScaleFactor(chargeScaleFactor);
	}

	protected void processSoulMark(Char target, int chargesUsed)
	{
		if(target != Dungeon.hero &&
		   Dungeon.hero.subClass == HeroSubClass.WARLOCK &&
		   Random.Float() > Math.pow(0.9f, (level() * chargesUsed) + 1))
			SoulMark.prolong(target, SoulMark.class, SoulMark.DURATION + level());
	}

	public void stopCharging()
	{
		if(charger != null)
		{
			charger.detach();
			charger = null;
		}
	}

	public void level(int value)
	{
		super.level(value);
		updateLevel();
	}

	@Override
	public Item identify()
	{
		curChargeKnown = true;
		super.identify();

		updateQuickslot();

		return this;
	}

	@Override
	public void bind()
	{
		bind(USAGES_TO_KNOW);
	}

	@Override
	public String info()
	{
		String desc = desc();

		desc += "\n\n" + statsDesc();

		if(cursed && cursedKnown)
			desc += "\n\n" + Messages.get(this, "cursed");
		else if(cursedKnown && !isIdentified())
			desc += "\n\n" + Messages.get(this, "uncursed");

		return desc;
	}

	public String statsDesc()
	{
		String desc = Messages.get(this, "stats_desc");

		if(canCrit)
			desc += "\n\n" + Messages.get(this, "crit");

		return desc;
	}

	@Override
	public boolean isIdentified()
	{
		return super.isIdentified() && curChargeKnown;
	}

	@Override
	public String status()
	{
		if(levelKnown)
			return (curChargeKnown ? curCharges : "?") + "/" + maxCharges;
		else
			return null;
	}

	@Override
	public Item upgrade()
	{
		super.upgrade();

		if(Random.Float() > Math.pow(0.8, level()))
		{
			cursed = false;
		}

		updateLevel();
		curCharges = Math.min(curCharges + 1, maxCharges);
		updateQuickslot();

		return this;
	}

	@Override
	public Item degrade()
	{
		super.degrade();

		updateLevel();
		updateQuickslot();

		return this;
	}

	private void updateLevel()
	{
		maxCharges = Math.min(initialCharges() + level(), 10);
		curCharges = Math.min(curCharges, maxCharges);
	}

	protected int initialCharges()
	{
		return 2;
	}

	protected int chargesPerCast()
	{
		return 1;
	}

	public void staffCrit()
	{

	}

	protected void fx(Ballistica bolt, Callback callback)
	{
		MagicMissile.boltFromChar(curUser.sprite.parent,
				MagicMissile.MAGIC_MISSILE,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	public void staffFx(MagesStaff.StaffParticle particle)
	{
		particle.color(0xFFFFFF);
		particle.am = 0.3f;
		particle.setLifespan(1f);
		particle.speed.polar(Random.Float(PointF.PI2), 2f);
		particle.setSize(1f, 2f);
		particle.radiateXY(0.5f);
	}

	public static void critFx(Char ch)
	{
		ch.sprite.emitter().burst(Speck.factory(Speck.STAR), 5);
	}

	void wandUsed()
	{
		usagesToKnow -= cursed ? 1 : chargesPerCast();
		curCharges -= cursed ? 1 : chargesPerCast();
		if(!isIdentified() && usagesToKnow <= 0)
		{
			identify();
			GLog.w(Messages.get(Wand.class, "identify", name()));
		}
		else
		{
			if(curUser.heroClass == HeroClass.MAGE) levelKnown = true;
			updateQuickslot();
		}

		curUser.spendAndNext(TIME_TO_ZAP);
	}

	@Override
	public Item random()
	{
		//+0: 66.67% (2/3)
		//+1: 26.67% (4/15)
		//+2: 6.67%  (1/15)
		int n = 0;
		if(Random.Int(3) == 0)
		{
			n++;
			if(Random.Int(5) == 0)
			{
				n++;
			}
		}
		level(n);

		//30% chance to be cursed
		if(Random.Int(2) == 0)
			cursed = true;

		return this;
	}

	@Override
	public int price()
	{
		int price = 75;
		if(cursed && cursedKnown)
			price /= 2;

		if(levelKnown)
			if(level() > 0)
				price *= (level() + 1);
			else if(level() < 0)
				price /= (1 - level());

		if(price < 1)
			price = 1;

		return price;
	}

	private static final String UNFAMILIRIARITY = "unfamiliarity";
	private static final String CUR_CHARGES = "curCharges";
	private static final String CUR_CHARGE_KNOWN = "curChargeKnown";
	private static final String PARTIALCHARGE = "partialCharge";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(UNFAMILIRIARITY, usagesToKnow);
		bundle.put(CUR_CHARGES, curCharges);
		bundle.put(CUR_CHARGE_KNOWN, curChargeKnown);
		bundle.put(PARTIALCHARGE, partialCharge);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		if((usagesToKnow = bundle.getInt(UNFAMILIRIARITY)) == 0)
		{
			usagesToKnow = USAGES_TO_KNOW;
		}
		curCharges = bundle.getInt(CUR_CHARGES);
		curChargeKnown = bundle.getBoolean(CUR_CHARGE_KNOWN);
		partialCharge = bundle.getFloat(PARTIALCHARGE);
	}

	@Override
	public Item transmute()
	{
		Wand n;
		do
		{
			n = Generator.randomWand(false);
		}
		while(n == null || Challenges.isItemBlocked(n) || n.getClass() == getClass());

		n.upgrade(level());

		n.levelKnown = levelKnown;
		n.cursedKnown = cursedKnown;
		n.cursed = cursed;
		if(isBound())
			n.bind();

		return n;
	}

	private static CellSelector.Listener zapper = new CellSelector.Listener()
	{
		@Override
		public void onSelect(Integer target)
		{
			if(target != null)
			{
				//FIXME this safety check shouldn't be necessary
				//it would be better to eliminate the curItem static variable.
				final Wand curWand;
				if(curItem instanceof Wand)
					curWand = (Wand) Wand.curItem;
				else
					return;

				final Ballistica shot = new Ballistica(curUser.pos, target, curWand.collisionProperties);
				int cell = shot.collisionPos;

				if(curUser.buff(MagicImmunity.class) != null)
				{
					GLog.w(Messages.get(MagicImmunity.class, "no_magic"));
					return;
				}
				if(target == curUser.pos || cell == curUser.pos)
				{
					GLog.i(Messages.get(Wand.class, "self_target"));
					return;
				}

				curUser.sprite.zap(cell);

				//attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
				if(Actor.findChar(target) != null)
					QuickSlotButton.target(Actor.findChar(target));
				else
					QuickSlotButton.target(Actor.findChar(cell));

				if(curWand.curCharges >= (curWand.cursed ? 1 : curWand.chargesPerCast()))
				{
					curUser.busy();
					curWand.cursedKnown = true;

					if(curWand.cursed && !curWand.isBound())
					{
						CursedWand.cursedZap(curWand, curUser, new Ballistica(curUser.pos, target, Ballistica.MAGIC_BOLT));

						if(!curWand.cursedKnown)
							GLog.n(Messages.get(Wand.class, "curse_discover", curWand.name()));
					}
					else
					{

						curWand.unBind();

						curWand.fx(shot, new Callback()
						{
							public void call()
							{
								curWand.onZap(shot);
								curWand.wandUsed();
							}
						});
					}

					Invisibility.dispel();
				}
				else
				{

					GLog.w(Messages.get(Wand.class, "fizzles"));

				}

			}
		}

		@Override
		public String prompt()
		{
			return Messages.get(Wand.class, "prompt");
		}
	};

	public class Charger extends Buff
	{
		private static final float BASE_CHARGE_DELAY = 10f;
		private static final float SCALING_CHARGE_ADDITION = 40f;
		private static final float NORMAL_SCALE_FACTOR = 0.875f;

		float scalingFactor = NORMAL_SCALE_FACTOR;

		@Override
		public boolean attachTo(Char target)
		{
			super.attachTo(target);

			return true;
		}

		@Override
		public boolean act()
		{
			if(curCharges < maxCharges)
				recharge();

			if(partialCharge >= 1 && curCharges < maxCharges)
			{
				partialCharge--;
				curCharges++;
				updateQuickslot();
			}

			spend(TICK);

			return true;
		}

		private void recharge()
		{
			int missingCharges = maxCharges - curCharges;
			missingCharges = Math.max(0, missingCharges);

			float turnsToCharge = (float) (BASE_CHARGE_DELAY
			                               + (SCALING_CHARGE_ADDITION * Math.pow(scalingFactor, missingCharges)));

			partialCharge += (1f/turnsToCharge) * RingOfEnergy.wandChargeMultiplier(target);
		}

		public void gainCharge(float charge)
		{
			partialCharge += charge;
			while(partialCharge >= 1f)
			{
				curCharges++;
				partialCharge--;
			}
			curCharges = Math.min(curCharges, maxCharges);
			updateQuickslot();
		}

		private void setScaleFactor(float value)
		{
			this.scalingFactor = value;
		}
	}
}
