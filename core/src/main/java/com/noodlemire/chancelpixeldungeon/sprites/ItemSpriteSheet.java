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

package com.noodlemire.chancelpixeldungeon.sprites;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ItemSpriteSheet
{
	private static final int WIDTH = 16;

	public static TextureFilm film = new TextureFilm(Assets.ITEMS, 16, 16);

	private static int xy(int x, int y)
	{
		x -= 1;
		y -= 1;
		return x + WIDTH * y;
	}

	private static final int PLACEHOLDERS = xy(1, 1);   //16 slots
	//null warning occupies space 0, should only show up if there's a bug.
	public static final int NULLWARN = PLACEHOLDERS;
	public static final int WEAPON_HOLDER = PLACEHOLDERS + 1;
	public static final int ARMOR_HOLDER = PLACEHOLDERS + 2;
	public static final int WAND_HOLDER = PLACEHOLDERS + 3;
	public static final int RING_HOLDER = PLACEHOLDERS + 4;
	public static final int ARTIFACT_HOLDER = PLACEHOLDERS + 5;
	public static final int POTION_HOLDER = PLACEHOLDERS + 6;
	public static final int SCROLL_HOLDER = PLACEHOLDERS + 7;
	public static final int SEED_HOLDER = PLACEHOLDERS + 8;
	public static final int STONE_HOLDER = PLACEHOLDERS + 9;
	public static final int FOOD_HOLDER = PLACEHOLDERS + 10;
	public static final int KEY_HOLDER = PLACEHOLDERS + 11;
	public static final int BAG_HOLDER = PLACEHOLDERS + 12;
	public static final int MISSILE_HOLDER = PLACEHOLDERS + 13;
	public static final int SOMETHING = PLACEHOLDERS + 14;
	public static final int LOCKED = PLACEHOLDERS + 15;

	static
	{
		assignItemRect(NULLWARN, 16, 7);
		assignItemRect(WEAPON_HOLDER, 14, 14);
		assignItemRect(ARMOR_HOLDER, 14, 12);
		assignItemRect(WAND_HOLDER, 14, 14);
		assignItemRect(RING_HOLDER, 8, 10);
		assignItemRect(ARTIFACT_HOLDER, 15, 15);
		assignItemRect(POTION_HOLDER, 10, 14);
		assignItemRect(SCROLL_HOLDER, 15, 14);
		assignItemRect(SEED_HOLDER, 10, 10);
		assignItemRect(STONE_HOLDER, 14, 14);
		assignItemRect(FOOD_HOLDER, 16, 12);
		assignItemRect(KEY_HOLDER, 8, 14);
		assignItemRect(BAG_HOLDER, 14, 15);
		assignItemRect(MISSILE_HOLDER, 15, 15);
		assignItemRect(SOMETHING, 8, 13);
		assignItemRect(LOCKED, 11, 14);
	}

	private static final int UNCOLLECTIBLE = xy(1, 2);   //16 slots
	public static final int GOLD = UNCOLLECTIBLE;
	public static final int DEWDROP = UNCOLLECTIBLE + 1;
	public static final int PETAL = UNCOLLECTIBLE + 2;
	public static final int SANDBAG = UNCOLLECTIBLE + 3;
	public static final int DBL_BOMB = UNCOLLECTIBLE + 4;
	public static final int GUIDE_PAGE = UNCOLLECTIBLE + 5;

	static
	{
		assignItemRect(GOLD, 15, 13);
		assignItemRect(DEWDROP, 10, 10);
		assignItemRect(PETAL, 8, 8);
		assignItemRect(SANDBAG, 10, 10);
		assignItemRect(DBL_BOMB, 14, 13);
		assignItemRect(GUIDE_PAGE, 10, 11);
	}

	private static final int CONTAINERS = xy(1, 3);   //16 slots
	public static final int BONES = CONTAINERS;
	public static final int REMAINS = CONTAINERS + 1;
	public static final int TOMB = CONTAINERS + 2;
	public static final int GRAVE = CONTAINERS + 3;
	public static final int CHEST = CONTAINERS + 4;
	public static final int LOCKED_CHEST = CONTAINERS + 5;
	public static final int CRYSTAL_CHEST = CONTAINERS + 6;
	public static final int EBONY_CHEST = CONTAINERS + 7;

	static
	{
		assignItemRect(BONES, 14, 11);
		assignItemRect(REMAINS, 14, 11);
		assignItemRect(TOMB, 14, 15);
		assignItemRect(GRAVE, 14, 15);
		assignItemRect(CHEST, 16, 14);
		assignItemRect(LOCKED_CHEST, 16, 14);
		assignItemRect(CRYSTAL_CHEST, 16, 14);
		assignItemRect(EBONY_CHEST, 16, 14);
	}

	private static final int SINGLE_USE = xy(1, 4);   //16 slots
	public static final int ANKH = SINGLE_USE;
	public static final int STYLUS = SINGLE_USE + 1;
	public static final int GRASS_SEED = SINGLE_USE + 2;
	public static final int SEAL = SINGLE_USE + 3;
	public static final int TORCH = SINGLE_USE + 4;
	public static final int BEACON = SINGLE_USE + 5;
	public static final int BOMB = SINGLE_USE + 6;
	public static final int HONEYPOT = SINGLE_USE + 7;
	public static final int SHATTPOT = SINGLE_USE + 8;
	public static final int IRON_KEY = SINGLE_USE + 9;
	public static final int GOLDEN_KEY = SINGLE_USE + 10;
	public static final int CRYSTAL_KEY = SINGLE_USE + 11;
	public static final int SKELETON_KEY = SINGLE_USE + 12;
	public static final int MASTERY = SINGLE_USE + 13;
	public static final int KIT = SINGLE_USE + 14;
	public static final int AMULET = SINGLE_USE + 15;
	public static final int LITMUS_PAPER = SINGLE_USE + 16;
	public static final int NINJA_TRAP_ITEM = SINGLE_USE + 17;

	static
	{
		assignItemRect(ANKH, 10, 16);
		assignItemRect(STYLUS, 12, 13);
		assignItemRect(GRASS_SEED, 12, 12);
		assignItemRect(SEAL, 9, 15);
		assignItemRect(TORCH, 12, 15);
		assignItemRect(BEACON, 16, 15);
		assignItemRect(BOMB, 10, 13);
		assignItemRect(HONEYPOT, 14, 12);
		assignItemRect(SHATTPOT, 16, 16);
		assignItemRect(IRON_KEY, 8, 14);
		assignItemRect(GOLDEN_KEY, 8, 14);
		assignItemRect(CRYSTAL_KEY, 8, 14);
		assignItemRect(SKELETON_KEY, 8, 14);
		assignItemRect(MASTERY, 13, 16);
		assignItemRect(KIT, 16, 15);
		assignItemRect(AMULET, 16, 16);
		assignItemRect(LITMUS_PAPER, 16, 13);
		assignItemRect(NINJA_TRAP_ITEM, 9, 9);
	}

	//32 free slots

	private static final int WEP_TIER1 = xy(1, 7);   //8 slots
	public static final int WORN_SHORTSWORD = WEP_TIER1;
	//public static final int CUDGEL = WEP_TIER1 + 1;
	public static final int GLOVES = WEP_TIER1 + 2;
	//public static final int RAPIER = WEP_TIER1 + 3;
	public static final int DAGGER = WEP_TIER1 + 4;
	public static final int MAGES_STAFF = WEP_TIER1 + 5;

	static
	{
		assignItemRect(WORN_SHORTSWORD, 13, 13);
		assignItemRect(GLOVES, 11, 14);
		assignItemRect(DAGGER, 12, 13);
		assignItemRect(MAGES_STAFF, 15, 16);
	}

	private static final int WEP_TIER2 = xy(9, 7);   //8 slots
	public static final int SHORTSWORD = WEP_TIER2;
	public static final int HAND_AXE = WEP_TIER2 + 1;
	public static final int SPEAR = WEP_TIER2 + 2;
	public static final int QUARTERSTAFF = WEP_TIER2 + 3;
	public static final int DIRK = WEP_TIER2 + 4;

	static
	{
		assignItemRect(SHORTSWORD, 13, 13);
		assignItemRect(HAND_AXE, 12, 14);
		assignItemRect(SPEAR, 16, 16);
		assignItemRect(QUARTERSTAFF, 16, 16);
		assignItemRect(DIRK, 13, 14);
	}

	private static final int WEP_TIER3 = xy(1, 8);   //8 slots
	public static final int SWORD = WEP_TIER3;
	public static final int MACE = WEP_TIER3 + 1;
	public static final int SCIMITAR = WEP_TIER3 + 2;
	public static final int ROUND_SHIELD = WEP_TIER3 + 3; //Credit to hellocoolgame
	public static final int SAI = WEP_TIER3 + 4;
	public static final int WHIP = WEP_TIER3 + 5;

	static
	{
		assignItemRect(SWORD, 14, 14);
		assignItemRect(MACE, 15, 15);
		assignItemRect(SCIMITAR, 13, 16);
		assignItemRect(ROUND_SHIELD, 16, 16);
		assignItemRect(SAI, 16, 16);
		assignItemRect(WHIP, 14, 14);
	}

	private static final int WEP_TIER4 = xy(9, 8);   //8 slots
	public static final int LONGSWORD = WEP_TIER4;
	public static final int BATTLE_AXE = WEP_TIER4 + 1;
	public static final int FLAIL = WEP_TIER4 + 2;
	public static final int RUNIC_BLADE = WEP_TIER4 + 3;
	public static final int ASSASSINS_BLADE = WEP_TIER4 + 4;
	public static final int CROSSBOW = WEP_TIER4 + 5;

	static
	{
		assignItemRect(LONGSWORD, 15, 15);
		assignItemRect(BATTLE_AXE, 16, 16);
		assignItemRect(FLAIL, 14, 14);
		assignItemRect(RUNIC_BLADE, 14, 14);
		assignItemRect(ASSASSINS_BLADE, 14, 15);
		assignItemRect(CROSSBOW, 15, 15);
	}

	private static final int WEP_TIER5 = xy(1, 9);   //8 slots
	public static final int BROADSWORD = WEP_TIER5;
	public static final int WAR_HAMMER = WEP_TIER5 + 1;
	public static final int GLAIVE = WEP_TIER5 + 2;
	public static final int GREATAXE = WEP_TIER5 + 3;
	public static final int PAVISE = WEP_TIER5 + 4;
	public static final int GAUNTLETS = WEP_TIER5 + 5;

	static
	{
		assignItemRect(BROADSWORD, 16, 16);
		assignItemRect(WAR_HAMMER, 16, 16);
		assignItemRect(GLAIVE, 16, 16);
		assignItemRect(GREATAXE, 13, 16);
		assignItemRect(PAVISE, 12, 16);
		assignItemRect(GAUNTLETS, 13, 15);
	}

	//8 free slots

	private static final int MISSILE_WEP = xy(1, 10);  //16 slots, 3 per tier
	public static final int DART = MISSILE_WEP;
	public static final int THROWING_KNIFE = MISSILE_WEP + 1;
	public static final int THROWING_STONE = MISSILE_WEP + 2;

	public static final int FISHING_SPEAR = MISSILE_WEP + 3;
	public static final int SHURIKEN = MISSILE_WEP + 4;
	public static final int KUNAI = MISSILE_WEP + 5;

	public static final int THROWING_SPEAR = MISSILE_WEP + 6;
	public static final int BOLAS = MISSILE_WEP + 7;
	public static final int BOOMERANG = MISSILE_WEP + 8;

	public static final int JAVELIN = MISSILE_WEP + 9;
	public static final int TOMAHAWK = MISSILE_WEP + 10;
	public static final int KNOBKERRY = MISSILE_WEP + 11;

	public static final int TRIDENT = MISSILE_WEP + 12;
	public static final int THROWING_HAMMER = MISSILE_WEP + 13;
	public static final int KPINGA = MISSILE_WEP + 14;

	static
	{
		assignItemRect(DART, 15, 15);
		assignItemRect(THROWING_KNIFE, 12, 13);
		assignItemRect(THROWING_STONE, 12, 10);

		assignItemRect(FISHING_SPEAR, 11, 11);
		assignItemRect(SHURIKEN, 12, 12);
		assignItemRect(KUNAI, 13, 13);

		assignItemRect(THROWING_SPEAR, 13, 13);
		assignItemRect(BOLAS, 15, 14);
		assignItemRect(BOOMERANG, 14, 14);

		assignItemRect(JAVELIN, 16, 16);
		assignItemRect(TOMAHAWK, 13, 13);
		assignItemRect(KNOBKERRY, 15, 15);

		assignItemRect(TRIDENT, 16, 16);
		assignItemRect(THROWING_HAMMER, 12, 12);
		assignItemRect(KPINGA, 16, 16);
	}

	public static final int TIPPED_DARTS = xy(1, 11);  //16 slots
	public static final int ROT_DART = TIPPED_DARTS;
	public static final int INCENDIARY_DART = TIPPED_DARTS + 1;
	public static final int BLINDING_DART = TIPPED_DARTS + 2;
	public static final int HEALING_DART = TIPPED_DARTS + 3;
	public static final int CHILLING_DART = TIPPED_DARTS + 4;
	public static final int SHOCKING_DART = TIPPED_DARTS + 5;
	public static final int POISON_DART = TIPPED_DARTS + 6;
	public static final int SLEEP_DART = TIPPED_DARTS + 7;
	public static final int PARALYTIC_DART = TIPPED_DARTS + 8;
	public static final int DEMON_DART = TIPPED_DARTS + 9;
	public static final int HEAVY_DART = TIPPED_DARTS + 10;
	public static final int DISPLACING_DART = TIPPED_DARTS + 11;

	public static final int BOW_UNLOADED = TIPPED_DARTS + 13;
	public static final int BOW_LOADED = TIPPED_DARTS + 14;
	public static final int ARROW = TIPPED_DARTS + 15;

	static
	{
		for(int i = TIPPED_DARTS; i <= TIPPED_DARTS + 15; i++)
			assignItemRect(i, 15, 15);

		assignItemRect(BOW_UNLOADED, 16, 16);
		assignItemRect(BOW_LOADED, 16, 16);
	}

	private static final int ARMOR = xy(1, 12);  //16 slots
	public static final int ARMOR_CLOTH = ARMOR;
	public static final int ARMOR_LEATHER = ARMOR + 1;
	public static final int ARMOR_MAIL = ARMOR + 2;
	public static final int ARMOR_SCALE = ARMOR + 3;
	public static final int ARMOR_PLATE = ARMOR + 4;
	public static final int ARMOR_WARRIOR = ARMOR + 5;
	public static final int ARMOR_MAGE = ARMOR + 6;
	public static final int ARMOR_ROGUE = ARMOR + 7;
	public static final int ARMOR_HUNTRESS = ARMOR + 8;

	static
	{
		assignItemRect(ARMOR_CLOTH, 15, 12);
		assignItemRect(ARMOR_LEATHER, 14, 13);
		assignItemRect(ARMOR_MAIL, 14, 12);
		assignItemRect(ARMOR_SCALE, 14, 11);
		assignItemRect(ARMOR_PLATE, 12, 12);
		assignItemRect(ARMOR_WARRIOR, 12, 12);
		assignItemRect(ARMOR_MAGE, 15, 15);
		assignItemRect(ARMOR_ROGUE, 14, 12);
		assignItemRect(ARMOR_HUNTRESS, 13, 15);
	}

	//16 free slots

	private static final int WANDS = xy(1, 14);  //16 slots
	public static final int WAND_MAGIC_MISSILE = WANDS;
	public static final int WAND_FIREBOLT = WANDS + 1;
	public static final int WAND_FROST = WANDS + 2;
	public static final int WAND_LIGHTNING = WANDS + 3;
	public static final int WAND_DISINTEGRATION = WANDS + 4;
	public static final int WAND_PRISMATIC_LIGHT = WANDS + 5;
	public static final int WAND_CORROSION = WANDS + 6;
	public static final int WAND_LIVING_EARTH = WANDS + 7;
	public static final int WAND_BLAST_WAVE = WANDS + 8;
	public static final int WAND_CORRUPTION = WANDS + 9;
	public static final int WAND_WARDING = WANDS + 10;
	public static final int WAND_REGROWTH = WANDS + 11;
	public static final int WAND_TRANSFUSION = WANDS + 12;

	static
	{
		for(int i = WANDS; i < WANDS + 16; i++)
			assignItemRect(i, 14, 14);
	}

	private static final int RINGS = xy(1, 15);  //16 slots
	public static final int RING_GARNET = RINGS;
	public static final int RING_RUBY = RINGS + 1;
	public static final int RING_TOPAZ = RINGS + 2;
	public static final int RING_EMERALD = RINGS + 3;
	public static final int RING_ONYX = RINGS + 4;
	public static final int RING_OPAL = RINGS + 5;
	public static final int RING_TOURMALINE = RINGS + 6;
	public static final int RING_SAPPHIRE = RINGS + 7;
	public static final int RING_AMETHYST = RINGS + 8;
	public static final int RING_QUARTZ = RINGS + 9;
	public static final int RING_AGATE = RINGS + 10;
	public static final int RING_DIAMOND = RINGS + 11;

	static
	{
		for(int i = RINGS; i < RINGS + 16; i++)
			assignItemRect(i, 8, 10);
	}

	private static final int ARTIFACTS = xy(1, 16);  //32 slots
	public static final int ARTIFACT_CLOAK = ARTIFACTS;
	public static final int ARTIFACT_ARMBAND = ARTIFACTS + 1;
	public static final int ARTIFACT_CAPE = ARTIFACTS + 2;
	public static final int ARTIFACT_TALISMAN = ARTIFACTS + 3;
	public static final int ARTIFACT_HOURGLASS = ARTIFACTS + 4;
	public static final int ARTIFACT_TOOLKIT = ARTIFACTS + 5;
	public static final int ARTIFACT_SPELLBOOK = ARTIFACTS + 6;
	public static final int ARTIFACT_BEACON = ARTIFACTS + 7;
	public static final int ARTIFACT_CHAINS = ARTIFACTS + 8;
	public static final int ARTIFACT_HORN1 = ARTIFACTS + 9;
	public static final int ARTIFACT_HORN2 = ARTIFACTS + 10;
	public static final int ARTIFACT_HORN3 = ARTIFACTS + 11;
	public static final int ARTIFACT_HORN4 = ARTIFACTS + 12;
	public static final int ARTIFACT_CHALICE1 = ARTIFACTS + 13;
	public static final int ARTIFACT_CHALICE2 = ARTIFACTS + 14;
	public static final int ARTIFACT_CHALICE3 = ARTIFACTS + 15;
	public static final int ARTIFACT_SANDALS = ARTIFACTS + 16;
	public static final int ARTIFACT_SHOES = ARTIFACTS + 17;
	public static final int ARTIFACT_BOOTS = ARTIFACTS + 18;
	public static final int ARTIFACT_GREAVES = ARTIFACTS + 19;
	public static final int ARTIFACT_ROSE1 = ARTIFACTS + 20;
	public static final int ARTIFACT_ROSE2 = ARTIFACTS + 21;
	public static final int ARTIFACT_ROSE3 = ARTIFACTS + 22;
	public static final int ARTIFACT_BRACELET = ARTIFACTS + 23;

	static
	{
		assignItemRect(ARTIFACT_CLOAK, 9, 15);
		assignItemRect(ARTIFACT_ARMBAND, 16, 13);
		assignItemRect(ARTIFACT_CAPE, 16, 14);
		assignItemRect(ARTIFACT_TALISMAN, 15, 13);
		assignItemRect(ARTIFACT_HOURGLASS, 13, 16);
		assignItemRect(ARTIFACT_TOOLKIT, 15, 13);
		assignItemRect(ARTIFACT_SPELLBOOK, 13, 16);
		assignItemRect(ARTIFACT_BEACON, 16, 16);
		assignItemRect(ARTIFACT_CHAINS, 16, 16);
		assignItemRect(ARTIFACT_HORN1, 15, 15);
		assignItemRect(ARTIFACT_HORN2, 15, 15);
		assignItemRect(ARTIFACT_HORN3, 15, 15);
		assignItemRect(ARTIFACT_HORN4, 15, 15);
		assignItemRect(ARTIFACT_CHALICE1, 12, 15);
		assignItemRect(ARTIFACT_CHALICE2, 12, 15);
		assignItemRect(ARTIFACT_CHALICE3, 12, 15);
		assignItemRect(ARTIFACT_SANDALS, 16, 6);
		assignItemRect(ARTIFACT_SHOES, 16, 6);
		assignItemRect(ARTIFACT_BOOTS, 16, 9);
		assignItemRect(ARTIFACT_GREAVES, 16, 14);
		assignItemRect(ARTIFACT_ROSE1, 14, 14);
		assignItemRect(ARTIFACT_ROSE2, 14, 14);
		assignItemRect(ARTIFACT_ROSE3, 14, 14);
		assignItemRect(ARTIFACT_BRACELET, 12, 14);
	}

	//32 free slots
	//Credit to Chinook for Elder Futhark scroll sprites
	private static final int SCROLLS = xy(1, 20);  //16 slots
	public static final int SCROLL_HAGLAZ = SCROLLS;
	public static final int SCROLL_SOWILO = SCROLLS + 1;
	public static final int SCROLL_LAGUZ = SCROLLS + 2;
	public static final int SCROLL_DAGAZ = SCROLLS + 3;
	public static final int SCROLL_GYFU = SCROLLS + 4;
	public static final int SCROLL_RAIDO = SCROLLS + 5;
	public static final int SCROLL_ISAZ = SCROLLS + 6;
	public static final int SCROLL_KAUNAN = SCROLLS + 7;
	public static final int SCROLL_NAUDIZ = SCROLLS + 8;
	public static final int SCROLL_JERA = SCROLLS + 9;
	public static final int SCROLL_EHWAZ = SCROLLS + 10;
	public static final int SCROLL_TIWAZ = SCROLLS + 11;
	public static final int SCROLL_MYSTERY = SCROLLS + 12;

	static
	{
		for(int i = SCROLLS; i < SCROLLS + 16; i++)
			assignItemRect(i, 15, 14);
	}

	private static final int STONES = xy(1, 21);  //16 slots
	public static final int STONE_HAGLAZ = STONES;
	public static final int STONE_SOWILO = STONES + 1;
	public static final int STONE_LAGUZ = STONES + 2;
	public static final int STONE_DAGAZ = STONES + 3;
	public static final int STONE_GYFU = STONES + 4;
	public static final int STONE_RAIDO = STONES + 5;
	public static final int STONE_ISAZ = STONES + 6;
	public static final int STONE_KAUNAN = STONES + 7;
	public static final int STONE_NAUDIZ = STONES + 8;
	public static final int STONE_JERA = STONES + 9;
	public static final int STONE_EHWAZ = STONES + 10;
	public static final int STONE_TIWAZ = STONES + 11;

	static
	{
		assignItemRect(STONE_HAGLAZ, 14, 14);
		assignItemRect(STONE_SOWILO, 14, 14);
		assignItemRect(STONE_LAGUZ, 9, 15);
		assignItemRect(STONE_DAGAZ, 14, 14);
		assignItemRect(STONE_GYFU, 15, 15);
		assignItemRect(STONE_RAIDO, 11, 15);
		assignItemRect(STONE_ISAZ, 5, 14);
		assignItemRect(STONE_KAUNAN, 10, 13);
		assignItemRect(STONE_NAUDIZ, 13, 14);
		assignItemRect(STONE_JERA, 12, 15);
		assignItemRect(STONE_EHWAZ, 11, 14);
		assignItemRect(STONE_TIWAZ, 13, 15);
	}

	private static final int POTIONS = xy(1, 22);  //16 slots
	public static final int POTION_CRIMSON = POTIONS;
	public static final int POTION_AMBER = POTIONS + 1;
	public static final int POTION_GOLDEN = POTIONS + 2;
	public static final int POTION_JADE = POTIONS + 3;
	public static final int POTION_TURQUOISE = POTIONS + 4;
	public static final int POTION_AZURE = POTIONS + 5;
	public static final int POTION_INDIGO = POTIONS + 6;
	public static final int POTION_MAGENTA = POTIONS + 7;
	public static final int POTION_BISTRE = POTIONS + 8;
	public static final int POTION_CHARCOAL = POTIONS + 9;
	public static final int POTION_SILVER = POTIONS + 10;
	public static final int POTION_IVORY = POTIONS + 11;
	public static final int POTION_UNSTABLE = POTIONS + 12;

	static
	{
		for(int i = POTIONS; i < POTIONS + 16; i++)
			assignItemRect(i, 10, 14);
	}

	private static final int SEEDS = xy(1, 23);  //16 slots
	public static final int SEED_ROTBERRY = SEEDS;
	public static final int SEED_FIREBLOOM = SEEDS + 1;
	public static final int SEED_BLINDWEED = SEEDS + 2;
	public static final int SEED_SUNGRASS = SEEDS + 3;
	public static final int SEED_ICECAP = SEEDS + 4;
	public static final int SEED_STORMVINE = SEEDS + 5;
	public static final int SEED_SORROWMOSS = SEEDS + 6;
	public static final int SEED_DREAMFOIL = SEEDS + 7;
	public static final int SEED_EARTHROOT = SEEDS + 8;
	public static final int SEED_DEADNETTLE = SEEDS + 9;
	public static final int SEED_BLANDFRUIT = SEEDS + 10;
	public static final int SEED_FADELEAF = SEEDS + 11;

	static
	{
		for(int i = SEEDS; i < SEEDS + 16; i++)
			assignItemRect(i, 10, 10);
	}

	//16 free slots

	private static final int FOOD = xy(1, 25);  //16 slots
	public static final int MEAT = FOOD;
	public static final int STEAK = FOOD + 1;
	public static final int OVERPRICED = FOOD + 2;
	public static final int CARPACCIO = FOOD + 3;
	public static final int BLANDFRUIT = FOOD + 4;
	public static final int RATION = FOOD + 5;
	public static final int PASTY = FOOD + 6;
	public static final int PUMPKIN_PIE = FOOD + 7;
	public static final int CANDY_CANE = FOOD + 8;

	static
	{
		assignItemRect(MEAT, 15, 11);
		assignItemRect(STEAK, 15, 11);
		assignItemRect(OVERPRICED, 14, 11);
		assignItemRect(CARPACCIO, 15, 11);
		assignItemRect(BLANDFRUIT, 9, 12);
		assignItemRect(RATION, 16, 12);
		assignItemRect(PASTY, 16, 11);
		assignItemRect(PUMPKIN_PIE, 16, 12);
		assignItemRect(CANDY_CANE, 13, 16);
	}

	private static final int QUEST = xy(1, 26);  //32 slots
	public static final int SKULL = QUEST;
	public static final int DUST = QUEST + 1;
	public static final int CANDLE = QUEST + 2;
	public static final int EMBER = QUEST + 3;
	public static final int ROTBERRY = QUEST + 4; //Borrowed from SproutedPD's Dungeon Cloud Berry
	public static final int PICKAXE = QUEST + 5;
	public static final int ORE = QUEST + 6;
	public static final int TOKEN = QUEST + 7;

	static
	{
		assignItemRect(SKULL, 16, 11);
		assignItemRect(DUST, 12, 11);
		assignItemRect(CANDLE, 12, 12);
		assignItemRect(EMBER, 12, 11);
		assignItemRect(ROTBERRY, 9, 15);
		assignItemRect(PICKAXE, 14, 14);
		assignItemRect(ORE, 15, 15);
		assignItemRect(TOKEN, 12, 12);

	}

	private static final int BAGS = xy(1, 28);  //16 slots
	public static final int VIAL = BAGS;
	public static final int POUCH = BAGS + 1;
	public static final int HOLDER = BAGS + 2;
	public static final int BANDOLIER = BAGS + 3;
	public static final int HOLSTER = BAGS + 4;
	public static final int BACKPACK = BAGS + 5;

	static
	{
		assignItemRect(VIAL, 12, 12);
		assignItemRect(POUCH, 14, 15);
		assignItemRect(HOLDER, 16, 16);
		assignItemRect(BANDOLIER, 15, 16);
		assignItemRect(HOLSTER, 15, 16);
		assignItemRect(BACKPACK, 21, 21);
	}


	private static void assignItemRect(int item, int width, int height)
	{
		int x = (item % WIDTH) * WIDTH;
		int y = (item / WIDTH) * WIDTH;
		film.add(item, x, y, x + width, y + height);
	}

	//for smaller 7x7 icons that often accompany an item sprite
	public static class Icons
	{
		private static final int WIDTH = 20;
		public static final int SIZE = 7;

		public static TextureFilm film = new TextureFilm(Assets.ITEM_ICONS, SIZE, SIZE);

		private static int xy(int x, int y)
		{
			x -= 1;
			y -= 1;
			return x + WIDTH * y;
		}

		private static void assignIconRect(int item, int width, int height)
		{
			int x = (item % WIDTH) * SIZE;
			int y = (item / WIDTH) * SIZE;
			film.add(item, x, y, x + width, y + height);
		}

		public static final int POTIONS = xy(1, 1);
		public static final int POTION_CORROSIVITY = POTIONS;
		public static final int POTION_ENTICEMENT = POTIONS + 1;
		public static final int POTION_EXPERIENCE = POTIONS + 2;
		public static final int POTION_EXPULSION = POTIONS + 3;
		public static final int POTION_FROST = POTIONS + 4;
		public static final int POTION_HASTE = POTIONS + 5;
		public static final int POTION_HEALING = POTIONS + 6;
		public static final int POTION_HYDROCOMBUSTION = POTIONS + 7;
		public static final int POTION_INVISIBILITY = POTIONS + 8;
		public static final int POTION_LEVITATION = POTIONS + 9;
		public static final int POTION_MIGHT = POTIONS + 10;
		public static final int POTION_OVERGROWTH = POTIONS + 11;
		public static final int POTION_PLACEBO = POTIONS + 12;
		public static final int POTION_PURITY = POTIONS + 13;
		public static final int POTION_REPULSION = POTIONS + 14;
		public static final int POTION_SHIELDING = POTIONS + 15;
		public static final int POTION_SHOCKWAVE = POTIONS + 16;
		public static final int POTION_TELEPATHY = POTIONS + 17;
		public static final int POTION_THUNDERSTORM = POTIONS + 18;
		public static final int POTION_TOXICITY = POTIONS + 19;

		static
		{
			assignIconRect(POTION_CORROSIVITY, 7, 7);
			assignIconRect(POTION_ENTICEMENT, 7, 7);
			assignIconRect(POTION_EXPERIENCE, 5, 5);
			assignIconRect(POTION_EXPULSION, 7, 7);
			assignIconRect(POTION_FROST, 7, 7);
			assignIconRect(POTION_HASTE, 6, 6);
			assignIconRect(POTION_HEALING, 7, 7);
			assignIconRect(POTION_HYDROCOMBUSTION, 5, 7);
			assignIconRect(POTION_INVISIBILITY, 5, 7);
			assignIconRect(POTION_LEVITATION, 5 , 5);
			assignIconRect(POTION_MIGHT, 7, 7);
			assignIconRect(POTION_OVERGROWTH, 7, 6);
			assignIconRect(POTION_PLACEBO, 7, 7);
			assignIconRect(POTION_PURITY, 5, 5);
			assignIconRect(POTION_REPULSION, 7, 6);
			assignIconRect(POTION_SHIELDING, 7, 7);
			assignIconRect(POTION_SHOCKWAVE, 6, 6);
			assignIconRect(POTION_TELEPATHY, 7, 5);
			assignIconRect(POTION_THUNDERSTORM, 7, 7);
			assignIconRect(POTION_TOXICITY, 7, 7);
		}

		public static final int SCROLLS = xy(1, 2);
		public static final int SCROLL_BLESSING = SCROLLS;
		public static final int SCROLL_CHARM = SCROLLS + 1;
		public static final int SCROLL_CLEANSING = SCROLLS + 2;
		public static final int SCROLL_DARKNESS = SCROLLS + 3;
		public static final int SCROLL_DECAY = SCROLLS + 4;
		public static final int SCROLL_IDENTIFY = SCROLLS + 5;
		public static final int SCROLL_INSULATION = SCROLLS + 6;
		public static final int SCROLL_LULLABY = SCROLLS + 7;
		public static final int SCROLL_MAGMA = SCROLLS + 8;
		public static final int SCROLL_NECROMANCY = SCROLLS + 9;
		public static final int SCROLL_RAGE = SCROLLS + 10;
		public static final int SCROLL_RECHARGING = SCROLLS + 11;
		public static final int SCROLL_REFLECTION = SCROLLS + 12;
		public static final int SCROLL_SUNLIGHT = SCROLLS + 13;
		public static final int SCROLL_SUPERNOVA = SCROLLS + 14;
		public static final int SCROLL_TAUNT = SCROLLS + 15;
		public static final int SCROLL_TELEPORTATION = SCROLLS + 16;
		public static final int SCROLL_TERROR = SCROLLS + 17;
		public static final int SCROLL_TRANSMUTATION = SCROLLS + 18;
		public static final int SCROLL_UPGRADE = SCROLLS + 19;

		static
		{
			assignIconRect(SCROLL_BLESSING, 7, 7);
			assignIconRect(SCROLL_CHARM, 7, 6);
			assignIconRect(SCROLL_CLEANSING, 7, 7);
			assignIconRect(SCROLL_DARKNESS, 7, 7);
			assignIconRect(SCROLL_DECAY, 5, 6);
			assignIconRect(SCROLL_IDENTIFY, 4, 7);
			assignIconRect(SCROLL_INSULATION, 7, 7);
			assignIconRect(SCROLL_LULLABY, 4, 5);
			assignIconRect(SCROLL_MAGMA, 5, 7);
			assignIconRect(SCROLL_NECROMANCY, 7, 6);
			assignIconRect(SCROLL_RAGE, 5, 5);
			assignIconRect(SCROLL_RECHARGING, 7, 5);
			assignIconRect(SCROLL_REFLECTION, 7, 5);
			assignIconRect(SCROLL_SUNLIGHT, 7, 7);
			assignIconRect(SCROLL_SUPERNOVA, 7, 7);
			assignIconRect(SCROLL_TAUNT, 5, 5);
			assignIconRect(SCROLL_TELEPORTATION, 7, 7);
			assignIconRect(SCROLL_TERROR, 5, 7);
			assignIconRect(SCROLL_TRANSMUTATION, 7, 7);
			assignIconRect(SCROLL_UPGRADE, 7, 7);
		}

		public static final int RINGS = xy(1, 3);
		public static final int RING_ACCURACY = RINGS;
		public static final int RING_APTITUDE = RINGS + 1;
		public static final int RING_ELEMENTS = RINGS + 2;
		public static final int RING_ENERGY = RINGS + 3;
		public static final int RING_EVASION = RINGS + 4;
		public static final int RING_FUROR = RINGS + 5;
		public static final int RING_HASTE = RINGS + 6;
		public static final int RING_MIGHT = RINGS + 7;
		public static final int RING_SHARPSHOOTING = RINGS + 8;
		public static final int RING_TENACITY = RINGS + 9;
		public static final int RING_VOLATILITY = RINGS + 10;
		public static final int RING_WEALTH = RINGS + 11;

		static
		{
			assignIconRect(RING_ACCURACY, 7, 7);
			assignIconRect(RING_APTITUDE, 5, 5);
			assignIconRect(RING_ELEMENTS, 5, 5);
			assignIconRect(RING_ENERGY, 7, 5);
			assignIconRect(RING_EVASION, 5, 7);
			assignIconRect(RING_FUROR, 7, 6);
			assignIconRect(RING_HASTE, 6, 6);
			assignIconRect(RING_MIGHT, 7, 7);
			assignIconRect(RING_SHARPSHOOTING, 7, 7);
			assignIconRect(RING_TENACITY, 6, 6);
			assignIconRect(RING_VOLATILITY, 6, 6);
			assignIconRect(RING_WEALTH, 7, 7);
		}
	}
}
