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

package com.noodlemire.chancelpixeldungeon.journal;

import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.armor.ClothArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.HuntressArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.LeatherArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.MageArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.MailArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.PlateArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.RogueArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.ScaleArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.WarriorArmor;
import com.noodlemire.chancelpixeldungeon.items.artifacts.CapeOfThorns;
import com.noodlemire.chancelpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.noodlemire.chancelpixeldungeon.items.artifacts.CloakOfShadows;
import com.noodlemire.chancelpixeldungeon.items.artifacts.DriedRose;
import com.noodlemire.chancelpixeldungeon.items.artifacts.EtherealChains;
import com.noodlemire.chancelpixeldungeon.items.artifacts.HornOfPlenty;
import com.noodlemire.chancelpixeldungeon.items.artifacts.LloydsBeacon;
import com.noodlemire.chancelpixeldungeon.items.artifacts.MasterThievesArmband;
import com.noodlemire.chancelpixeldungeon.items.artifacts.SandalsOfNature;
import com.noodlemire.chancelpixeldungeon.items.artifacts.TalismanOfForesight;
import com.noodlemire.chancelpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.noodlemire.chancelpixeldungeon.items.artifacts.UnstableSpellbook;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfCorrosivity;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfEnticement;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfExperience;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfExpulsion;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfFrost;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHaste;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHealing;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHydrocombustion;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfInvisibility;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfLevitation;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfMight;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfOvergrowth;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfPlacebo;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfPurity;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfRepulsion;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfShielding;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfShockwave;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfTelepathy;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfThunderstorm;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfToxicity;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfAccuracy;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfElements;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfEnergy;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfEvasion;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfForce;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfFuror;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfHaste;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfMight;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfSharpshooting;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfTenacity;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfWealth;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfBalance;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfBlessing;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfCharm;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfCleansing;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfDarkness;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfDecay;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfInsulation;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfMagma;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfNecromancy;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfRage;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfReflection;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfSupernova;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTaunt;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTerror;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfBlastWave;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfCorrosion;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfCorruption;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfDisintegration;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfFireblast;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfFrost;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfLightning;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfMagicMissile;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfPrismaticLight;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfRegrowth;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfTransfusion;
import com.noodlemire.chancelpixeldungeon.items.weapon.Bow;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.AssassinsBlade;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.BattleAxe;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Broadsword;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Crossbow;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Dagger;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Dirk;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Flail;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Gauntlet;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Glaive;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Gloves;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Greataxe;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.HandAxe;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Longsword;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Mace;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.MagesStaff;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Pavise;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Quarterstaff;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.RoundShield;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.RunicBlade;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Sai;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Scimitar;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Shortsword;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Spear;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Sword;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.WarHammer;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Whip;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.WornShortsword;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public enum Catalog
{

	WEAPONS,
	ARMOR,
	WANDS,
	RINGS,
	ARTIFACTS,
	POTIONS,
	SCROLLS;

	private LinkedHashMap<Class<? extends Item>, Boolean> seen = new LinkedHashMap<>();

	public Collection<Class<? extends Item>> items()
	{
		return seen.keySet();
	}

	public boolean allSeen()
	{
		for(Class<? extends Item> item : items())
		{
			if(!seen.get(item))
			{
				return false;
			}
		}
		return true;
	}

	static
	{
		WEAPONS.seen.put(WornShortsword.class, false);
		WEAPONS.seen.put(Gloves.class, false);
		WEAPONS.seen.put(Dagger.class, false);
		WEAPONS.seen.put(MagesStaff.class, false);
		WEAPONS.seen.put(Bow.class, false);
		WEAPONS.seen.put(Shortsword.class, false);
		WEAPONS.seen.put(HandAxe.class, false);
		WEAPONS.seen.put(Spear.class, false);
		WEAPONS.seen.put(Quarterstaff.class, false);
		WEAPONS.seen.put(Dirk.class, false);
		WEAPONS.seen.put(Sword.class, false);
		WEAPONS.seen.put(Mace.class, false);
		WEAPONS.seen.put(Scimitar.class, false);
		WEAPONS.seen.put(RoundShield.class, false);
		WEAPONS.seen.put(Sai.class, false);
		WEAPONS.seen.put(Whip.class, false);
		WEAPONS.seen.put(Longsword.class, false);
		WEAPONS.seen.put(BattleAxe.class, false);
		WEAPONS.seen.put(Flail.class, false);
		WEAPONS.seen.put(RunicBlade.class, false);
		WEAPONS.seen.put(AssassinsBlade.class, false);
		WEAPONS.seen.put(Crossbow.class, false);
		WEAPONS.seen.put(Broadsword.class, false);
		WEAPONS.seen.put(WarHammer.class, false);
		WEAPONS.seen.put(Glaive.class, false);
		WEAPONS.seen.put(Greataxe.class, false);
		WEAPONS.seen.put(Pavise.class, false);
		WEAPONS.seen.put(Gauntlet.class, false);

		ARMOR.seen.put(ClothArmor.class, false);
		ARMOR.seen.put(LeatherArmor.class, false);
		ARMOR.seen.put(MailArmor.class, false);
		ARMOR.seen.put(ScaleArmor.class, false);
		ARMOR.seen.put(PlateArmor.class, false);
		ARMOR.seen.put(WarriorArmor.class, false);
		ARMOR.seen.put(MageArmor.class, false);
		ARMOR.seen.put(RogueArmor.class, false);
		ARMOR.seen.put(HuntressArmor.class, false);

		WANDS.seen.put(WandOfMagicMissile.class, false);
		WANDS.seen.put(WandOfLightning.class, false);
		WANDS.seen.put(WandOfDisintegration.class, false);
		WANDS.seen.put(WandOfFireblast.class, false);
		WANDS.seen.put(WandOfCorrosion.class, false);
		WANDS.seen.put(WandOfBlastWave.class, false);
		//WANDS.seen.put( WandOfLivingEarth.class,          false);
		WANDS.seen.put(WandOfFrost.class, false);
		WANDS.seen.put(WandOfPrismaticLight.class, false);
		//WANDS.seen.put( WandOfWarding.class,              false);
		WANDS.seen.put(WandOfTransfusion.class, false);
		WANDS.seen.put(WandOfCorruption.class, false);
		WANDS.seen.put(WandOfRegrowth.class, false);

		RINGS.seen.put(RingOfAccuracy.class, false);
		RINGS.seen.put(RingOfEnergy.class, false);
		RINGS.seen.put(RingOfElements.class, false);
		RINGS.seen.put(RingOfEvasion.class, false);
		RINGS.seen.put(RingOfForce.class, false);
		RINGS.seen.put(RingOfFuror.class, false);
		RINGS.seen.put(RingOfHaste.class, false);
		RINGS.seen.put(RingOfMight.class, false);
		RINGS.seen.put(RingOfSharpshooting.class, false);
		RINGS.seen.put(RingOfTenacity.class, false);
		RINGS.seen.put(RingOfWealth.class, false);

		//ARTIFACTS.seen.put( AlchemistsToolkit.class,      false);
		ARTIFACTS.seen.put(CapeOfThorns.class, false);
		ARTIFACTS.seen.put(ChaliceOfBlood.class, false);
		ARTIFACTS.seen.put(CloakOfShadows.class, false);
		ARTIFACTS.seen.put(DriedRose.class, false);
		ARTIFACTS.seen.put(EtherealChains.class, false);
		ARTIFACTS.seen.put(HornOfPlenty.class, false);
		ARTIFACTS.seen.put(LloydsBeacon.class, false);
		ARTIFACTS.seen.put(MasterThievesArmband.class, false);
		ARTIFACTS.seen.put(SandalsOfNature.class, false);
		ARTIFACTS.seen.put(TalismanOfForesight.class, false);
		ARTIFACTS.seen.put(TimekeepersHourglass.class, false);
		ARTIFACTS.seen.put(UnstableSpellbook.class, false);

		POTIONS.seen.put(PotionOfHealing.class, false);
		POTIONS.seen.put(PotionOfHaste.class, false);
		POTIONS.seen.put(PotionOfHydrocombustion.class, false);
		POTIONS.seen.put(PotionOfFrost.class, false);
		POTIONS.seen.put(PotionOfToxicity.class, false);
		POTIONS.seen.put(PotionOfShockwave.class, false);
		POTIONS.seen.put(PotionOfPurity.class, false);
		POTIONS.seen.put(PotionOfLevitation.class, false);
		POTIONS.seen.put(PotionOfTelepathy.class, false);
		POTIONS.seen.put(PotionOfInvisibility.class, false);
		POTIONS.seen.put(PotionOfExperience.class, false);
		POTIONS.seen.put(PotionOfMight.class, false);
		POTIONS.seen.put(PotionOfCorrosivity.class, false);
		POTIONS.seen.put(PotionOfEnticement.class, false);
		POTIONS.seen.put(PotionOfOvergrowth.class, false);
		POTIONS.seen.put(PotionOfPlacebo.class, false);
		POTIONS.seen.put(PotionOfExpulsion.class, false);
		POTIONS.seen.put(PotionOfRepulsion.class, false);
		POTIONS.seen.put(PotionOfShielding.class, false);
		POTIONS.seen.put(PotionOfThunderstorm.class, false);

		SCROLLS.seen.put(ScrollOfIdentify.class, false);
		SCROLLS.seen.put(ScrollOfUpgrade.class, false);
		SCROLLS.seen.put(ScrollOfCleansing.class, false);
		SCROLLS.seen.put(ScrollOfTeleportation.class, false);
		SCROLLS.seen.put(ScrollOfRecharging.class, false);
		SCROLLS.seen.put(ScrollOfReflection.class, false);
		SCROLLS.seen.put(ScrollOfTerror.class, false);
		SCROLLS.seen.put(ScrollOfLullaby.class, false);
		SCROLLS.seen.put(ScrollOfRage.class, false);
		SCROLLS.seen.put(ScrollOfSupernova.class, false);
		SCROLLS.seen.put(ScrollOfBlessing.class, false);
		SCROLLS.seen.put(ScrollOfDecay.class, false);
		SCROLLS.seen.put(ScrollOfBalance.class, false);
		SCROLLS.seen.put(ScrollOfDarkness.class, false);
		SCROLLS.seen.put(ScrollOfNecromancy.class, false);
		SCROLLS.seen.put(ScrollOfTransmutation.class, false);
		SCROLLS.seen.put(ScrollOfCharm.class, false);
		SCROLLS.seen.put(ScrollOfInsulation.class, false);
		SCROLLS.seen.put(ScrollOfTaunt.class, false);
		SCROLLS.seen.put(ScrollOfMagma.class, false);
	}

	public static LinkedHashMap<Catalog, Badges.Badge> catalogBadges = new LinkedHashMap<>();

	static
	{
		catalogBadges.put(WEAPONS, Badges.Badge.ALL_WEAPONS_IDENTIFIED);
		catalogBadges.put(ARMOR, Badges.Badge.ALL_ARMOR_IDENTIFIED);
		catalogBadges.put(WANDS, Badges.Badge.ALL_WANDS_IDENTIFIED);
		catalogBadges.put(RINGS, Badges.Badge.ALL_RINGS_IDENTIFIED);
		catalogBadges.put(ARTIFACTS, Badges.Badge.ALL_ARTIFACTS_IDENTIFIED);
		catalogBadges.put(POTIONS, Badges.Badge.ALL_POTIONS_IDENTIFIED);
		catalogBadges.put(SCROLLS, Badges.Badge.ALL_SCROLLS_IDENTIFIED);
	}

	public static boolean isSeen(Class<? extends Item> itemClass)
	{
		for(Catalog cat : values())
		{
			if(cat.seen.containsKey(itemClass))
			{
				return cat.seen.get(itemClass);
			}
		}
		return false;
	}

	public static void setSeen(Class<? extends Item> itemClass)
	{
		for(Catalog cat : values())
		{
			if(cat.seen.containsKey(itemClass) && !cat.seen.get(itemClass))
			{
				cat.seen.put(itemClass, true);
				Journal.saveNeeded = true;
			}
		}
		Badges.validateItemsIdentified();
	}

	private static final String CATALOGS = "catalogs";

	public static void store(Bundle bundle)
	{

		Badges.loadGlobal();

		ArrayList<String> seen = new ArrayList<>();

		//if we have identified all items of a set, we use the badge to keep track instead.
		if(!Badges.isUnlocked(Badges.Badge.ALL_ITEMS_IDENTIFIED))
		{
			for(Catalog cat : values())
			{
				if(!Badges.isUnlocked(catalogBadges.get(cat)))
				{
					for(Class<? extends Item> item : cat.items())
					{
						if(cat.seen.get(item)) seen.add(item.getSimpleName());
					}
				}
			}
		}

		bundle.put(CATALOGS, seen.toArray(new String[0]));

	}

	public static void restore(Bundle bundle)
	{

		Badges.loadGlobal();

		//logic for if we have all badges
		if(Badges.isUnlocked(Badges.Badge.ALL_ITEMS_IDENTIFIED))
		{
			for(Catalog cat : values())
			{
				for(Class<? extends Item> item : cat.items())
				{
					cat.seen.put(item, true);
				}
			}
			return;
		}

		//catalog-specific badge logic
		for(Catalog cat : values())
		{
			if(Badges.isUnlocked(catalogBadges.get(cat)))
			{
				for(Class<? extends Item> item : cat.items())
				{
					cat.seen.put(item, true);
				}
			}
		}

		//general save/load
		if(bundle.contains(CATALOGS))
		{
			List<String> seen = Arrays.asList(bundle.getStringArray(CATALOGS));

			//pre-0.6.3 saves
			//TODO should adjust this to tie into the bundling system's class array
			if(seen.contains("WandOfVenom"))
			{
				WANDS.seen.put(WandOfCorrosion.class, true);
			}

			for(Catalog cat : values())
			{
				for(Class<? extends Item> item : cat.items())
				{
					if(seen.contains(item.getSimpleName()))
					{
						cat.seen.put(item, true);
					}
				}
			}
		}
	}

}
