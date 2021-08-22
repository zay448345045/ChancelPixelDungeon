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

package com.noodlemire.chancelpixeldungeon.items.rings;

import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.Challenges;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.hero.HeroClass;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.ItemStatusHandler;
import com.noodlemire.chancelpixeldungeon.items.KindofMisc;
import com.noodlemire.chancelpixeldungeon.items.Transmutable;
import com.noodlemire.chancelpixeldungeon.journal.Catalog;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashMap;
import java.util.HashSet;

public class Ring extends KindofMisc implements Transmutable
{
	public static final int TICKS_TO_KNOW = 200;

	public boolean ownedByRing = false;

	protected Buff buff;

	protected Integer icon;

	private static final Class<?>[] rings = {
			RingOfAccuracy.class,
			RingOfEvasion.class,
			RingOfElements.class,
			RingOfAptitude.class,
			RingOfFuror.class,
			RingOfHaste.class,
			RingOfEnergy.class,
			RingOfMight.class,
			RingOfSharpshooting.class,
			RingOfTenacity.class,
			RingOfVolatility.class,
			RingOfWealth.class,
	};

	private static final HashMap<String, Integer> gems = new HashMap<String, Integer>()
	{
		{
			put("garnet", ItemSpriteSheet.RING_GARNET);
			put("ruby", ItemSpriteSheet.RING_RUBY);
			put("topaz", ItemSpriteSheet.RING_TOPAZ);
			put("emerald", ItemSpriteSheet.RING_EMERALD);
			put("onyx", ItemSpriteSheet.RING_ONYX);
			put("opal", ItemSpriteSheet.RING_OPAL);
			put("tourmaline", ItemSpriteSheet.RING_TOURMALINE);
			put("sapphire", ItemSpriteSheet.RING_SAPPHIRE);
			put("amethyst", ItemSpriteSheet.RING_AMETHYST);
			put("quartz", ItemSpriteSheet.RING_QUARTZ);
			put("agate", ItemSpriteSheet.RING_AGATE);
			put("diamond", ItemSpriteSheet.RING_DIAMOND);
		}
	};

	private static ItemStatusHandler<Ring> handler;

	private String gem;

	private int ticksToKnow = TICKS_TO_KNOW;

	@SuppressWarnings("unchecked")
	public static void initGems()
	{
		handler = new ItemStatusHandler<>((Class<? extends Ring>[]) rings, gems);
	}

	public static void save(Bundle bundle)
	{
		handler.save(bundle);
	}

	@SuppressWarnings("unchecked")
	public static void restore(Bundle bundle)
	{
		handler = new ItemStatusHandler<>((Class<? extends Ring>[]) rings, gems, bundle);
	}

	public Ring()
	{
		super();
		reset();
	}

	public void reset()
	{
		super.reset();
		if(handler != null)
		{
			image = handler.image(this);
			gem = handler.label(this);
		}
	}

	@Override
	public void activate(Char ch)
	{
		buff = buff();
		buff.attachTo(ch);
	}

	@Override
	public boolean doEquip(Hero hero)
	{
		if(hero.heroClass == HeroClass.HUNTRESS && hero.belongings.classMisc == null)
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
			hero.remove(buff);
			buff = null;

			return true;
		}
		else
			return false;
	}

	public boolean isKnown()
	{
		return ownedByRing || handler.isKnown(this);
	}

	protected void setKnown()
	{
		if(ownedByRing)
			return;

		if(!isKnown())
			handler.know(this);

		if(Dungeon.hero.isAlive())
			Catalog.setSeen(getClass());
	}

	@Override
	public String name()
	{
		return isKnown() ? super.name() : Messages.get(Ring.class, gem);
	}

	@Override
	public String info()
	{
		String desc = isKnown() ? desc() : Messages.get(this, "unknown_desc");

		if(cursed && isEquipped(Dungeon.hero))
			desc += "\n\n" + Messages.get(this, "cursed_worn");
		else if(cursed && cursedKnown)
			desc += "\n\n" + Messages.get(this, "curse_known");
		else if(cursedKnown && !isIdentified())
			desc += "\n\n" + Messages.get(this, "uncursed");

		if (isKnown())
			desc += "\n\n" + statsInfo();

		return desc;
	}

	protected String statsInfo()
	{
		return "";
	}

	@Override
	public Integer icon()
	{
		return isKnown() ? icon : null;
	}

	@Override
	public Item upgrade()
	{
		super.upgrade();

		if(Random.Float() > Math.pow(0.8, level()))
		{
			cursed = false;
		}

		return this;
	}

	@Override
	public boolean isIdentified()
	{
		return ownedByRing || (super.isIdentified() && isKnown());
	}

	@Override
	public Item identify()
	{
		setKnown();
		return super.identify();
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
		{
			cursed = true;
		}

		return this;
	}

	public static HashSet<Class<? extends Ring>> getKnown()
	{
		return handler.known();
	}

	public static boolean allKnown()
	{
		return handler.known().size() == rings.length - 2;
	}

	@Override
	public int price()
	{
		int price = 75;
		if(cursed && cursedKnown)
		{
			price /= 2;
		}
		if(levelKnown)
		{
			if(level() > 0)
			{
				price *= (level() + 1);
			}
			else if(level() < 0)
			{
				price /= (1 - level());
			}
		}
		if(price < 1)
		{
			price = 1;
		}
		return price;
	}

	protected RingBuff buff()
	{
		return null;
	}

	private static final String UNFAMILIRIARITY = "unfamiliarity";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(UNFAMILIRIARITY, ticksToKnow);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		if((ticksToKnow = bundle.getInt(UNFAMILIRIARITY)) == 0)
			ticksToKnow = TICKS_TO_KNOW;
	}

	public static int getBonus(Char target, Class<? extends RingBuff> type)
	{
		int bonus = 0;
		for(RingBuff buff : target.buffs(type))
		{
			bonus += buff.level();
		}
		return bonus;
	}

	protected int soloBonus()
	{
		if(cursed)
			return Math.min(0, Ring.this.level() - 2);
		else
			return Ring.this.level() + 1;
	}

	@Override
	public Item transmute()
	{
		Ring n;
		do
		{
			n = Generator.randomRing(false);
		}
		while(n == null || Challenges.isItemBlocked(n) || n.getClass() == getClass());

		n.level(0);

		int level = level();

		if(level > 0)
			n.upgrade(level);
		else if(level < 0)
			n.degrade(-level);

		n.levelKnown = levelKnown;
		n.cursedKnown = cursedKnown;
		n.cursed = cursed;
		if(isBound())
			n.bind();

		return n;
	}

	@Override
	public void bind()
	{
		bind(TICKS_TO_KNOW);
	}

	public class RingBuff extends Buff
	{
		@Override
		public boolean act()
		{
			if(!isIdentified() && --ticksToKnow <= 0)
			{
				identify();
				GLog.w(Messages.get(Ring.class, "identify", Ring.this.toString()));
				Badges.validateItemLevelAquired(Ring.this);
			}

			spend(TICK);

			return true;
		}

		public int level()
		{
			if(Ring.this.cursed && !isBound())
				return Math.min(0, Ring.this.level() - 2);
			else
			{
				unBind();

				return Ring.this.level() + 1;
			}
		}
	}
}
