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

package com.noodlemire.chancelpixeldungeon.items.artifacts;

import com.noodlemire.chancelpixeldungeon.Challenges;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.hero.HeroClass;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.KindofMisc;
import com.noodlemire.chancelpixeldungeon.items.Transmutable;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Artifact extends KindofMisc implements Transmutable
{
	protected Buff passiveBuff;
	protected Buff activeBuff;

	//level is used internally to track upgrades to artifacts, size/logic varies per artifact.
	//already inherited from item superclass
	//exp is used to count progress towards levels for some artifacts
	protected int exp = 0;
	//levelCap is the artifact's maximum level
	protected int levelCap = 0;

	//the current artifact charge
	protected int charge = 0;
	//the build towards next charge, usually rolls over at 1.
	//better to keep charge as an int and use a separate float than casting.
	protected float partialCharge = 0;
	//the maximum charge, varies per artifact, not all artifacts use this.
	protected int chargeCap = 0;

	//used by some artifacts to keep track of duration of effects or cooldowns to use.
	protected int cooldown = 0;

	@Override
	public boolean doEquip(final Hero hero)
	{
		for(Item i : hero.belongings.miscSlots.items)
		{
			if (i.getClass() == this.getClass())
			{
				GLog.w(Messages.get(Artifact.class, "cannot_wear_two"));
				return false;
			}
		}

		if(hero.belongings.classMisc != null && hero.belongings.classMisc.getClass() == this.getClass())
		{
			GLog.w(Messages.get(Artifact.class, "cannot_wear_two"));
			return false;
		}

		if(hero.heroClass == HeroClass.ROGUE && hero.belongings.classMisc == null)
		{
			hero.belongings.classMisc = this;
			identify();
			afterEquip(hero);
			return true;
		}

		if(super.doEquip(hero))
		{
			identify();
			return true;
		}
		else
			return false;
	}

	public void activate(Char ch)
	{
		passiveBuff = passiveBuff();
		passiveBuff.attachTo(ch);
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single)
	{
		if(super.doUnequip(hero, collect, single))
		{
			if(passiveBuff != null)
			{
				passiveBuff.detach();
				passiveBuff = null;
			}

			if(activeBuff != null)
			{
				activeBuff.detach();
				activeBuff = null;
			}

			return true;
		}
		else
			return false;
	}

	@Override
	public boolean isUpgradable()
	{
		return false;
	}

	@Override
	public int visiblyUpgraded()
	{
		return levelKnown ? Math.round((level() * 10) / (float) levelCap) : 0;
	}

	//transfers upgrades from another artifact, transfer level will equal the displayed level
	private void transferUpgrade(int transferLvl)
	{
		upgrade(Math.round((float) (transferLvl * levelCap) / 10));
	}

	public void charge(Hero target, float amount)
	{
		if(charge < chargeCap)
		{
			partialCharge += amount;
			if(partialCharge >= 1)
			{
				partialCharge--;
				charge++;
				updateQuickslot();
			}
		}
	}

	@Override
	public String info()
	{
		if(cursed && cursedKnown && !isEquipped(Dungeon.hero))
			return desc() + "\n\n" + Messages.get(this, "curse_known");
		else if(cursedKnown && !isIdentified())
			return desc() + "\n\n" + Messages.get(this, "uncursed");
		else
			return desc();
	}

	@Override
	public String status()
	{
		//if the artifact isn't IDed, or is cursed, don't display anything
		if(!isIdentified() || (cursed && !isBound()))
			return null;

		//display the current cooldown
		if(cooldown != 0)
			return Messages.format("%d", cooldown);

		//display as percent
		if(chargeCap == 100)
			return Messages.format("%d%%", charge);

		//display as #/#
		if(chargeCap > 0)
			return Messages.format("%d/%d", charge, chargeCap);

		//if there's no cap -
		//- but there is charge anyway, display that charge
		if(charge != 0)
			return Messages.format("%d", charge);

		//otherwise, if there's no charge, return null.
		return null;
	}

	//converts class names to be more concise and readable.
	protected String convertName(String className)
	{
		//removes known redundant parts of names.
		className = className.replaceFirst("ScrollOf|PotionOf", "");

		//inserts a space infront of every uppercase character
		className = className.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");

		return className;
	}

	@Override
	public Item random()
	{
		//always +0

		//30% chance to be cursed
		if(Random.Int(2) == 0)
			cursed = true;

		return this;
	}

	@Override
	public int price()
	{
		int price = 100;
		if(level() > 0)
			price += 20 * visiblyUpgraded();
		if(cursed && cursedKnown)
			price /= 2;
		if(price < 1)
			price = 1;
		return price;
	}

	protected ArtifactBuff passiveBuff()
	{
		return null;
	}

	protected ArtifactBuff activeBuff()
	{
		return null;
	}

	@Override
	public Item transmute()
	{
		Artifact n = Generator.randomArtifact();

		if(n != null && !Challenges.isItemBlocked(n))
		{
			n.cursedKnown = cursedKnown;
			n.cursed = cursed;
			n.levelKnown = levelKnown;
			if(isBound())
				n.bind();
			n.transferUpgrade(visiblyUpgraded());
			return n;
		}

		return null;
	}

	@Override
	public void bind()
	{
		bind(1);
	}

	public class ArtifactBuff extends Buff
	{
		public int itemLevel()
		{
			return level();
		}

		public boolean isCursed()
		{
			return cursed && !isBound();
		}
	}

	private static final String EXP = "exp";
	private static final String CHARGE = "charge";
	private static final String PARTIALCHARGE = "partialcharge";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(EXP, exp);
		bundle.put(CHARGE, charge);
		bundle.put(PARTIALCHARGE, partialCharge);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		exp = bundle.getInt(EXP);
		if(chargeCap > 0) charge = Math.min(chargeCap, bundle.getInt(CHARGE));
		else charge = bundle.getInt(CHARGE);
		partialCharge = bundle.getFloat(PARTIALCHARGE);
	}
}
