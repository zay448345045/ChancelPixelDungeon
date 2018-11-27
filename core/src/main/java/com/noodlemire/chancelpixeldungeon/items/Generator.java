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

package com.noodlemire.chancelpixeldungeon.items;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.items.armor.Armor;
import com.noodlemire.chancelpixeldungeon.items.armor.ClothArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.LeatherArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.MailArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.PlateArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.ScaleArmor;
import com.noodlemire.chancelpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.noodlemire.chancelpixeldungeon.items.artifacts.Artifact;
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
import com.noodlemire.chancelpixeldungeon.items.bags.Bag;
import com.noodlemire.chancelpixeldungeon.items.food.Food;
import com.noodlemire.chancelpixeldungeon.items.food.MysteryMeat;
import com.noodlemire.chancelpixeldungeon.items.food.Pasty;
import com.noodlemire.chancelpixeldungeon.items.potions.Potion;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfCorrosivity;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfEnticement;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfExperience;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfFrost;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHealing;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfInvisibility;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfLevitation;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHydrocombustion;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfMight;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfOvergrowth;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfPlacebo;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfRefreshment;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfRepulsion;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfShielding;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfTelepathy;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfShockwave;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfPurity;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHaste;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfThunderstorm;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfToxicity;
import com.noodlemire.chancelpixeldungeon.items.rings.Ring;
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
import com.noodlemire.chancelpixeldungeon.items.scrolls.Scroll;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfRage;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTerror;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.noodlemire.chancelpixeldungeon.items.wands.Wand;
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
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.AssassinsBlade;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.BattleAxe;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Crossbow;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Dagger;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Dirk;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Flail;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Gauntlet;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Glaive;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Greataxe;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Greatshield;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Greatsword;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.HandAxe;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Knuckles;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Longsword;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Mace;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.MagesStaff;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.MeleeWeapon;
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
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.Bolas;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.FishingSpear;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.Javelin;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.Shuriken;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.ThrowingHammer;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.ThrowingSpear;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.Tomahawk;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.Trident;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.darts.Dart;
import com.noodlemire.chancelpixeldungeon.plants.BlandfruitBush;
import com.noodlemire.chancelpixeldungeon.plants.Blindweed;
import com.noodlemire.chancelpixeldungeon.plants.Dreamfoil;
import com.noodlemire.chancelpixeldungeon.plants.Earthroot;
import com.noodlemire.chancelpixeldungeon.plants.Fadeleaf;
import com.noodlemire.chancelpixeldungeon.plants.Firebloom;
import com.noodlemire.chancelpixeldungeon.plants.Icecap;
import com.noodlemire.chancelpixeldungeon.plants.Plant;
import com.noodlemire.chancelpixeldungeon.plants.Rotberry;
import com.noodlemire.chancelpixeldungeon.plants.Sorrowmoss;
import com.noodlemire.chancelpixeldungeon.plants.Deadnettle;
import com.noodlemire.chancelpixeldungeon.plants.Stormvine;
import com.noodlemire.chancelpixeldungeon.plants.Sungrass;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Generator {

	public enum Category {
		WEAPON	( 6,    MeleeWeapon.class),
		WEP_T1	( 0,    MeleeWeapon.class),
		WEP_T2	( 0,    MeleeWeapon.class),
		WEP_T3	( 0,    MeleeWeapon.class),
		WEP_T4	( 0,    MeleeWeapon.class),
		WEP_T5	( 0,    MeleeWeapon.class),
		
		ARMOR	( 4,    Armor.class ),
		
		MISSILE ( 3,    MissileWeapon.class ),
		MIS_T1  ( 0,    MissileWeapon.class ),
		MIS_T2  ( 0,    MissileWeapon.class ),
		MIS_T3  ( 0,    MissileWeapon.class ),
		MIS_T4  ( 0,    MissileWeapon.class ),
		MIS_T5  ( 0,    MissileWeapon.class ),
		
		POTION	( 20,   Potion.class ),
		SCROLL	( 20,   Scroll.class ),
		
		WAND	( 3,    Wand.class ),
		RING	( 1,    Ring.class ),
		ARTIFACT( 1,    Artifact.class),
		
		SEED	( 0,    Plant.Seed.class ),
		
		FOOD	( 0,    Food.class ),
		
		GOLD	( 20,   Gold.class );
		
		public Class<?>[] classes;
		public float[] probs;
		
		public float prob;
		public Class<? extends Item> superClass;
		
		Category(float prob, Class<? extends Item> superClass) {
			this.prob = prob;
			this.superClass = superClass;
		}
		
		public static int order( Item item ) {
			for (int i=0; i < values().length; i++) {
				if (values()[i].superClass.isInstance( item )) {
					return i;
				}
			}
			
			return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
		}
		
		private static final float[] INITIAL_ARTIFACT_PROBS = new float[]{ 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1};
		
		static {
			GOLD.classes = new Class<?>[]{
					Gold.class };
			GOLD.probs = new float[]{ 1 };
			
			SCROLL.classes = new Class<?>[]{
					ScrollOfIdentify.class,
					ScrollOfTeleportation.class,
					ScrollOfRemoveCurse.class,
					ScrollOfUpgrade.class,
					ScrollOfRecharging.class,
					ScrollOfMagicMapping.class,
					ScrollOfRage.class,
					ScrollOfTerror.class,
					ScrollOfLullaby.class,
					ScrollOfMagicalInfusion.class,
					ScrollOfPsionicBlast.class,
					ScrollOfMirrorImage.class };
			SCROLL.probs = new float[]{ 30, 10, 20, 0, 15, 15, 12, 8, 8, 0, 4, 10 };
			
			POTION.classes = new Class<?>[]{
					PotionOfHealing.class,
					PotionOfExperience.class,
					PotionOfToxicity.class,
					PotionOfShockwave.class,
					PotionOfHydrocombustion.class,
					PotionOfLevitation.class,
					PotionOfHaste.class,
					PotionOfTelepathy.class,
					PotionOfPurity.class,
					PotionOfInvisibility.class,
					PotionOfMight.class,
					PotionOfFrost.class,
					PotionOfCorrosivity.class,
					PotionOfEnticement.class,
					PotionOfOvergrowth.class,
					PotionOfPlacebo.class,
					PotionOfRefreshment.class,
					PotionOfRepulsion.class,
					PotionOfShielding.class,
					PotionOfThunderstorm.class};
			POTION.probs = new float[]{ 45, 4, 15, 10, 15, 10, 15, 20, 12, 10, 8, 10, 8, 20, 12, 10, 9, 15, 8, 14 };
			
			//TODO: add last ones when implemented
			WAND.classes = new Class<?>[]{
					WandOfMagicMissile.class,
					WandOfLightning.class,
					WandOfDisintegration.class,
					WandOfFireblast.class,
					WandOfCorrosion.class,
					WandOfBlastWave.class,
					//WandOfLivingEarth.class,
					WandOfFrost.class,
					WandOfPrismaticLight.class,
					//WandOfWarding.class,
					WandOfTransfusion.class,
					WandOfCorruption.class,
					WandOfRegrowth.class };
			WAND.probs = new float[]{ 5, 4, 4, 4, 4, 3, /*3,*/ 3, 3, /*3,*/ 3, 3, 3 };
			
			//see generator.randomWeapon
			WEAPON.classes = new Class<?>[]{};
			WEAPON.probs = new float[]{};
			
			WEP_T1.classes = new Class<?>[]{
					WornShortsword.class,
					Knuckles.class,
					Dagger.class,
					MagesStaff.class
			};
			WEP_T1.probs = new float[]{ 1, 1, 1, 0 };
			
			WEP_T2.classes = new Class<?>[]{
					Shortsword.class,
					HandAxe.class,
					Spear.class,
					Quarterstaff.class,
					Dirk.class
			};
			WEP_T2.probs = new float[]{ 6, 5, 5, 4, 4 };
			
			WEP_T3.classes = new Class<?>[]{
					Sword.class,
					Mace.class,
					Scimitar.class,
					RoundShield.class,
					Sai.class,
					Whip.class
			};
			WEP_T3.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			WEP_T4.classes = new Class<?>[]{
					Longsword.class,
					BattleAxe.class,
					Flail.class,
					RunicBlade.class,
					AssassinsBlade.class,
					Crossbow.class
			};
			WEP_T4.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			WEP_T5.classes = new Class<?>[]{
					Greatsword.class,
					WarHammer.class,
					Glaive.class,
					Greataxe.class,
					Greatshield.class,
					Gauntlet.class
			};
			WEP_T5.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			//see Generator.randomArmor
			ARMOR.classes = new Class<?>[]{
					ClothArmor.class,
					LeatherArmor.class,
					MailArmor.class,
					ScaleArmor.class,
					PlateArmor.class };
			ARMOR.probs = new float[]{ 0, 0, 0, 0, 0 };
			
			//see Generator.randomMissile
			MISSILE.classes = new Class<?>[]{};
			MISSILE.probs = new float[]{};
			
			MIS_T1.classes = new Class<?>[]{
					Dart.class,
					ThrowingKnife.class
			};
			MIS_T1.probs = new float[]{ 1, 1 };
			
			MIS_T2.classes = new Class<?>[]{
					FishingSpear.class,
					Shuriken.class
			};
			MIS_T2.probs = new float[]{ 4, 3 };
			
			MIS_T3.classes = new Class<?>[]{
					ThrowingSpear.class,
					Bolas.class
			};
			MIS_T3.probs = new float[]{ 4, 3 };
			
			MIS_T4.classes = new Class<?>[]{
					Javelin.class,
					Tomahawk.class
			};
			MIS_T4.probs = new float[]{ 4, 3 };
			
			MIS_T5.classes = new Class<?>[]{
					Trident.class,
					ThrowingHammer.class
			};
			MIS_T5.probs = new float[]{ 4, 3 };
			
			FOOD.classes = new Class<?>[]{
					Food.class,
					Pasty.class,
					MysteryMeat.class };
			FOOD.probs = new float[]{ 4, 1, 0 };
			
			RING.classes = new Class<?>[]{
					RingOfAccuracy.class,
					RingOfEvasion.class,
					RingOfElements.class,
					RingOfForce.class,
					RingOfFuror.class,
					RingOfHaste.class,
					RingOfEnergy.class,
					RingOfMight.class,
					RingOfSharpshooting.class,
					RingOfTenacity.class,
					RingOfWealth.class};
			RING.probs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			
			ARTIFACT.classes = new Class<?>[]{
					CapeOfThorns.class,
					ChaliceOfBlood.class,
					CloakOfShadows.class,
					HornOfPlenty.class,
					MasterThievesArmband.class,
					SandalsOfNature.class,
					TalismanOfForesight.class,
					TimekeepersHourglass.class,
					UnstableSpellbook.class,
					AlchemistsToolkit.class, //currently removed from drop tables, pending rework.
					DriedRose.class,
					LloydsBeacon.class,
					EtherealChains.class
			};
			ARTIFACT.probs = INITIAL_ARTIFACT_PROBS.clone();
			
			SEED.classes = new Class<?>[]{
					Firebloom.Seed.class,
					Icecap.Seed.class,
					Sorrowmoss.Seed.class,
					Blindweed.Seed.class,
					Sungrass.Seed.class,
					Earthroot.Seed.class,
					Fadeleaf.Seed.class,
					Rotberry.Seed.class,
					BlandfruitBush.Seed.class,
					Dreamfoil.Seed.class,
					Stormvine.Seed.class,
					Deadnettle.Seed.class};
			SEED.probs = new float[]{ 10, 10, 10, 10, 10, 10, 10, 10, 2, 10, 10, 1 };
		}
	}

	private static final float[][] floorSetTierProbs = new float[][] {
			{0, 70, 20,  8,  2},
			{0, 25, 50, 20,  5},
			{0, 10, 40, 40, 10},
			{0,  5, 20, 50, 25},
			{0,  2,  8, 20, 70}
	};
	
	private static HashMap<Category,Float> categoryProbs = new LinkedHashMap<>();
	
	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );
		}
	}
	
	public static Item random() {
		Category cat = Random.chances( categoryProbs );
		if (cat == null){
			reset();
			cat = Random.chances( categoryProbs );
		}
		categoryProbs.put( cat, categoryProbs.get( cat ) - 1);
		return random( cat );
	}
	
	public static Item random( Category cat ) {
		try {
			
			switch (cat) {
			case ARMOR:
				return randomArmor();
			case WEAPON:
				return randomWeapon();
			case MISSILE:
				return randomMissile();
			case POTION:
				return randomPotion();
			case ARTIFACT:
				Item item = randomArtifact();
				//if we're out of artifacts, return a ring instead.
				return item != null ? item : random(Category.RING);
			default:
				return ((Item)cat.classes[Random.chances( cat.probs )].newInstance()).random();
			}
			
		} catch (Exception e) {

			ChancelPixelDungeon.reportException(e);
			return null;
			
		}
	}
	
	public static Item random( Class<? extends Item> cl ) {
		try {
			
			return cl.newInstance().random();
			
		} catch (Exception e) {

			ChancelPixelDungeon.reportException(e);
			return null;
			
		}
	}

	public static Armor randomArmor(){
		return randomArmor(Dungeon.depth / 5);
	}
	
	public static Armor randomArmor(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);

		try {
			Armor a = (Armor)Category.ARMOR.classes[Random.chances(floorSetTierProbs[floorSet])].newInstance();
			a.random();
			return a;
		} catch (Exception e) {
			ChancelPixelDungeon.reportException(e);
			return null;
		}
	}

	public static final Category[] wepTiers = new Category[]{
			Category.WEP_T1,
			Category.WEP_T2,
			Category.WEP_T3,
			Category.WEP_T4,
			Category.WEP_T5
	};

	public static MeleeWeapon randomWeapon(){
		return randomWeapon(Dungeon.depth / 5);
	}
	
	public static MeleeWeapon randomWeapon(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);

		try {
			Category c = wepTiers[Random.chances(floorSetTierProbs[floorSet])];
			MeleeWeapon w = (MeleeWeapon)c.classes[Random.chances(c.probs)].newInstance();
			w.random();
			return w;
		} catch (Exception e) {
			ChancelPixelDungeon.reportException(e);
			return null;
		}
	}
	
	public static final Category[] misTiers = new Category[]{
			Category.MIS_T1,
			Category.MIS_T2,
			Category.MIS_T3,
			Category.MIS_T4,
			Category.MIS_T5
	};
	
	public static MissileWeapon randomMissile(){
		return randomMissile(Dungeon.depth / 5);
	}
	
	public static MissileWeapon randomMissile(int floorSet) {
		
		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		try {
			Category c = misTiers[Random.chances(floorSetTierProbs[floorSet])];
			MissileWeapon w = (MissileWeapon)c.classes[Random.chances(c.probs)].newInstance();
			w.random();
			return w;
		} catch (Exception e) {
			ChancelPixelDungeon.reportException(e);
			return null;
		}
	}

	public static Potion randomPotion()
	{
		try
		{
			Category cat = Category.POTION;
			Class<? extends Potion> pot;
			Potion potion;

			do
			{
				pot = (Class<? extends Potion>)cat.classes[Random.chances(cat.probs)];
				potion = pot.newInstance();
			} while (potion.image == ItemSpriteSheet.POTION_UNSTABLE);

			return potion;
		}
		catch (Exception e)
		{
			ChancelPixelDungeon.reportException(e);
			return null;
		}
	}

	//enforces uniqueness of artifacts throughout a run.
	public static Artifact randomArtifact() {

		try {
			Category cat = Category.ARTIFACT;
			int i = Random.chances( cat.probs );

			//if no artifacts are left, return null
			if (i == -1){
				return null;
			}
			
			Class<?extends Artifact> art = (Class<? extends Artifact>) cat.classes[i];

			if (removeArtifact(art)) {
				Artifact artifact = art.newInstance();
				
				artifact.random();
				
				return artifact;
			} else {
				return null;
			}

		} catch (Exception e) {
			ChancelPixelDungeon.reportException(e);
			return null;
		}
	}

	public static boolean removeArtifact(Class<?extends Artifact> artifact) {
		if (spawnedArtifacts.contains(artifact))
			return false;

		Category cat = Category.ARTIFACT;
		for (int i = 0; i < cat.classes.length; i++)
			if (cat.classes[i].equals(artifact)) {
				if (cat.probs[i] == 1){
					cat.probs[i] = 0;
					spawnedArtifacts.add(artifact);
					return true;
				} else
					return false;
			}

		return false;
	}

	//resets artifact probabilities, for new dungeons
	public static void initArtifacts() {
		Category.ARTIFACT.probs = Category.INITIAL_ARTIFACT_PROBS.clone();
		spawnedArtifacts = new ArrayList<>();
	}

	private static ArrayList<Class<?extends Artifact>> spawnedArtifacts = new ArrayList<>();
	
	private static final String GENERAL_PROBS = "general_probs";
	private static final String SPAWNED_ARTIFACTS = "spawned_artifacts";
	
	public static void storeInBundle(Bundle bundle) {
		Float[] genProbs = categoryProbs.values().toArray(new Float[0]);
		float[] storeProbs = new float[genProbs.length];
		for (int i = 0; i < storeProbs.length; i++){
			storeProbs[i] = genProbs[i];
		}
		bundle.put( GENERAL_PROBS, storeProbs);
		
		bundle.put( SPAWNED_ARTIFACTS, spawnedArtifacts.toArray(new Class[0]));
	}

	public static void restoreFromBundle(Bundle bundle) {
		if (bundle.contains(GENERAL_PROBS)){
			float[] probs = bundle.getFloatArray(GENERAL_PROBS);
			for (int i = 0; i < probs.length; i++){
				categoryProbs.put(Category.values()[i], probs[i]);
			}
		} else {
			reset();
		}
		
		initArtifacts();
		if (bundle.contains(SPAWNED_ARTIFACTS)){
			for ( Class<?extends Artifact> artifact : bundle.getClassArray(SPAWNED_ARTIFACTS) ){
				removeArtifact(artifact);
			}
		//pre-0.6.1 saves
		} else if (bundle.contains("artifacts")) {
			String[] names = bundle.getStringArray("artifacts");
			Category cat = Category.ARTIFACT;

			for (String artifact : names)
				for (int i = 0; i < cat.classes.length; i++)
					if (cat.classes[i].getSimpleName().equals(artifact))
						cat.probs[i] = 0;
		}
	}
}
