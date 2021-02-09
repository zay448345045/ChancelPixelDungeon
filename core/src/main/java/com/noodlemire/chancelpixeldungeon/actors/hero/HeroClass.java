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

package com.noodlemire.chancelpixeldungeon.actors.hero;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.Challenges;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.items.Amulet;
import com.noodlemire.chancelpixeldungeon.items.BrokenSeal;
import com.noodlemire.chancelpixeldungeon.items.armor.ClothArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.GodArmor;
import com.noodlemire.chancelpixeldungeon.items.artifacts.CloakOfShadows;
import com.noodlemire.chancelpixeldungeon.items.bags.PotionBandolier;
import com.noodlemire.chancelpixeldungeon.items.bags.ScrollHolder;
import com.noodlemire.chancelpixeldungeon.items.bags.VelvetPouch;
import com.noodlemire.chancelpixeldungeon.items.food.Food;
import com.noodlemire.chancelpixeldungeon.items.food.SmallRation;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHealing;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfInvisibility;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfMagicMissile;
import com.noodlemire.chancelpixeldungeon.items.weapon.Bow;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Dagger;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Gloves;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.MagesStaff;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.WornShortsword;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.noodlemire.chancelpixeldungeon.journal.Catalog;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

public enum HeroClass
{
	WARRIOR("warrior", HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR),
	MAGE("mage", HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK),
	ROGUE("rogue", HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER),
	HUNTRESS("huntress", HeroSubClass.WARDEN, HeroSubClass.SNIPER);

	private String title;
	private HeroSubClass[] subClasses;

	HeroClass(String title, HeroSubClass... subClasses)
	{
		this.title = title;
		this.subClasses = subClasses;
	}

	public void initHero(Hero hero)
	{
		hero.heroClass = this;

		initCommon(hero);

		switch(this)
		{
			case WARRIOR:
				initWarrior(hero);
				break;

			case MAGE:
				initMage(hero);
				break;

			case ROGUE:
				initRogue(hero);
				break;

			case HUNTRESS:
				initHuntress(hero);
				break;
		}
	}

	private static void initCommon(Hero hero)
	{
		if(Amulet.DEBUG)
			(hero.belongings.armor = new GodArmor()).identify();
		else
			(hero.belongings.armor = new ClothArmor()).identify();

		if(Dungeon.isChallenged(Challenges.NO_FOOD))
			new SmallRation().collect();
		else
			new Food().collect();

		if(Amulet.DEBUG)
			new Amulet().collect();
	}

	public Badges.Badge masteryBadge()
	{
		switch(this)
		{
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
		}
		return null;
	}

	private static void initWarrior(Hero hero)
	{
		(hero.belongings.weapon = new WornShortsword()).identify();
		ThrowingStone stones = new ThrowingStone();
		stones.identify().quantity(3).collect();
		Dungeon.quickslot.setSlot(0, stones);

		if(hero.belongings.armor != null)
			hero.belongings.armor.affixSeal(new BrokenSeal());

		new PotionBandolier().collect();
		Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
		new PotionOfHealing().identify();

		hero.STR_lvl++;
	}

	private static void initMage(Hero hero)
	{
		MagesStaff staff;

		staff = new MagesStaff(new WandOfMagicMissile());

		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);

		Dungeon.quickslot.setSlot(0, staff);

		new ScrollHolder().collect();
		Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
		new ScrollOfUpgrade().identify();
	}

	private static void initRogue(Hero hero)
	{
		(hero.belongings.weapon = new Dagger()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.classMisc = cloak).identify();
		hero.belongings.classMisc.activate(hero);

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, cloak);
		Dungeon.quickslot.setSlot(1, knives);

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();
		new PotionOfInvisibility().identify();
	}

	private static void initHuntress(Hero hero)
	{
		(hero.belongings.weapon = new Gloves()).identify();

		Bow bow = new Bow();
		bow.collect();
		Dungeon.quickslot.setSlot(0, bow);
		Catalog.setSeen(Bow.class); //Bow is always identified, so this is necessary so it shows up in the catalog

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();

		new ScrollOfIdentify().identify();
	}

	public String title()
	{
		return Messages.get(HeroClass.class, title);
	}

	public HeroSubClass[] subClasses()
	{
		return subClasses;
	}

	public String spritesheet()
	{
		switch(this)
		{
			case WARRIOR:
				return Assets.WARRIOR;
			case MAGE:
				return Assets.MAGE;
			case ROGUE:
				return Assets.ROGUE;
			case HUNTRESS:
				return Assets.HUNTRESS;
		}

		return null;
	}

	public String[] perks()
	{

		switch(this)
		{
			case WARRIOR:
				return new String[]{
						Messages.get(HeroClass.class, "warrior_perk1"),
						Messages.get(HeroClass.class, "warrior_perk2"),
						Messages.get(HeroClass.class, "warrior_perk3"),
						Messages.get(HeroClass.class, "warrior_perk4"),
						Messages.get(HeroClass.class, "warrior_perk5"),
				};
			case MAGE:
				return new String[]{
						Messages.get(HeroClass.class, "mage_perk1"),
						Messages.get(HeroClass.class, "mage_perk2"),
						Messages.get(HeroClass.class, "mage_perk3"),
						Messages.get(HeroClass.class, "mage_perk4"),
						Messages.get(HeroClass.class, "mage_perk5"),
				};
			case ROGUE:
				return new String[]{
						Messages.get(HeroClass.class, "rogue_perk1"),
						Messages.get(HeroClass.class, "rogue_perk2"),
						Messages.get(HeroClass.class, "rogue_perk3"),
						Messages.get(HeroClass.class, "rogue_perk4"),
						Messages.get(HeroClass.class, "rogue_perk5"),
				};
			case HUNTRESS:
				return new String[]{
						Messages.get(HeroClass.class, "huntress_perk1"),
						Messages.get(HeroClass.class, "huntress_perk2"),
						Messages.get(HeroClass.class, "huntress_perk3"),
						Messages.get(HeroClass.class, "huntress_perk4"),
						Messages.get(HeroClass.class, "huntress_perk5"),
				};
		}

		return null;
	}

	private static final String CLASS = "class";

	public void storeInBundle(Bundle bundle)
	{
		bundle.put(CLASS, toString());
	}

	public static HeroClass restoreInBundle(Bundle bundle)
	{
		String value = bundle.getString(CLASS);
		return value.length() > 0 ? valueOf(value) : ROGUE;
	}
}
