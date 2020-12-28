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

package com.noodlemire.chancelpixeldungeon.scenes;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.Chrome;
import com.noodlemire.chancelpixeldungeon.effects.BadgeBanner;
import com.noodlemire.chancelpixeldungeon.items.Ankh;
import com.noodlemire.chancelpixeldungeon.items.DewVial;
import com.noodlemire.chancelpixeldungeon.items.Honeypot;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.Stylus;
import com.noodlemire.chancelpixeldungeon.items.Torch;
import com.noodlemire.chancelpixeldungeon.items.armor.PlateArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.curses.Bulk;
import com.noodlemire.chancelpixeldungeon.items.artifacts.CloakOfShadows;
import com.noodlemire.chancelpixeldungeon.items.artifacts.DriedRose;
import com.noodlemire.chancelpixeldungeon.items.artifacts.EtherealChains;
import com.noodlemire.chancelpixeldungeon.items.artifacts.HornOfPlenty;
import com.noodlemire.chancelpixeldungeon.items.artifacts.TalismanOfForesight;
import com.noodlemire.chancelpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.noodlemire.chancelpixeldungeon.items.artifacts.UnstableSpellbook;
import com.noodlemire.chancelpixeldungeon.items.food.Blandfruit;
import com.noodlemire.chancelpixeldungeon.items.food.Food;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHealing;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfElements;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfEnergy;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfEvasion;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfMight;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfSharpshooting;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfWealth;
import com.noodlemire.chancelpixeldungeon.items.stones.StoneOfAugmentation;
import com.noodlemire.chancelpixeldungeon.items.stones.StoneOfPreservation;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfCorrosion;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfCorruption;
import com.noodlemire.chancelpixeldungeon.items.weapon.curses.Wayward;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Lucky;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Projecting;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Dagger;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Flail;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Glaive;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Greataxe;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Longsword;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Quarterstaff;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.RunicBlade;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.ui.Archs;
import com.noodlemire.chancelpixeldungeon.ui.ExitButton;
import com.noodlemire.chancelpixeldungeon.ui.Icons;
import com.noodlemire.chancelpixeldungeon.ui.RenderedTextMultiline;
import com.noodlemire.chancelpixeldungeon.ui.ScrollPane;
import com.noodlemire.chancelpixeldungeon.ui.Window;
import com.noodlemire.chancelpixeldungeon.windows.WndTitledMessage;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

//TODO: update this class with relevant info as new versions come out.
public class ChangesScene extends PixelScene
{
	private final ArrayList<ChangeInfo> infos = new ArrayList<>();

	@Override
	public void create()
	{
		super.create();

		int w = Camera.main.width;
		int h = Camera.main.height;

		RenderedText title = PixelScene.renderText(Messages.get(this, "title"), 9);
		title.hardlight(Window.TITLE_COLOR);
		title.x = (w - title.width()) / 2f;
		title.y = (16 - title.baseLine()) / 2f;
		align(title);
		add(title);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos(Camera.main.width - btnExit.width(), 0);
		add(btnExit);

		NinePatch panel = Chrome.get(Chrome.Type.TOAST);

		int pw = 135 + panel.marginLeft() + panel.marginRight() - 2;
		int ph = h - 16;

		panel.size(pw, ph);
		panel.x = (w - pw) / 2f;
		panel.y = title.y + title.height();
		align(panel);
		add(panel);

		ScrollPane list = new ScrollPane(new Component())
		{
			@Override
			public void onClick(float x, float y)
			{
				for(ChangeInfo info : infos)
					if(info.onClick(x, y))
						return;
			}
		};

		add(list);

		//************************
		//    CPD v0.1 BETA 6
		//************************

		ChangeInfo changes = new ChangeInfo("CPD v0.1 BETA 6", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes = new ChangeInfo("New Content", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.GEYSERS, 48, 8, 8, 8), "Geysers",
				"A new type of object has been added to regular floors: Geysers.\n\n" +
						"Each geyser will spew a different kind of harmful gas or other area-bound effect. " +
						"In the first 3 regions of the dungeon, geysers tend to only activate upon being hit " +
						"by a physical weapon, and only rarely spewing anything on their own. However, " +
						"starting in the Dwarven Metropolis region, some geysers will constantly shoot their " +
						"substance until something destroys them!\n\n" +
						"Geysers have high health and never drop any loot, so it's usually best to ignore and " +
						"avoid them unless you have a way to use them to your advantage."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.KIT, null), "Class Armor Rework",
				"To adjust to the heighted difficulty that late-game spewers add, DM-300 now drops the Armor " +
						"Kit instead of the Dwarf King. In addition, class armor now uses up 100% of your Dynamic " +
						"Strength, rather than 33% of your remaining HP. Finally, and most importantly, all class " +
						"armor special abilities offer some kind of long range jump or teleport.\n\n" +
						"The DS requirement allows class armor to be used as a navigational tool outside of combat, " +
						"which is very important to be able to travel past rooms that are filled with very dangerous " +
						"gasses. However, in combat, it can be just as costly as that 33% HP, as you may be left " +
						"unable to deal a good amount of damage needed to deal with nearby foes."));

		changes = new ChangeInfo("Changes", false, "");
		changes.hardlight(CharSprite.WARNING);
		infos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_DAGAZ, null), "Scroll Nerfs",
				"_-_ Shouted Scrolls of Cleansing no longer instakill undead and demonic foes. Now weakens and " +
						"severely harms these foes instead.\n" +
						"_-_ Shouted Scrolls of Decay no longer doom directly targeted bosses.\n" +
						"_-_ Shouted Scrolls of Necromancy no longer corrupt directly targeted bosses."));

		changes.addButton(new ChangeButton(new Image(Assets.DM300, 0, 0, 22, 20), "Metallic Enemies",
				"More enemies count as metallic, causing them to attract lightning when inside thunderstorms:\n" +
						"_-_ DM-300 (However it also resists Thundercloud damage now)\n" +
						"_-_ Gnoll Brutes and Shielded Brutes\n" +
						"_-_ Prison Guards\n" +
						"_-_ Crazy Thieves and Bandits who have stolen metallic items"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Items can no longer be thrown underneath immovable characters. In addition to Yog-Dzewa, " +
						"this also includes the Rot Heart and all Geysers.\n" +
						"_-_ Players and mobs now try to avoid walking into harmful gasses, unless their target " +
						"is only 1 tile away, or they're already inside of that particular gas.\n" +
						"_-_ Many sounds effects now raise and lower their volume based on their proximity to the player.\n" +
						"_-_ Many sound effects can now be heard through and around walls.\n" +
						"_-_ Immune to Magic buff's icon now has higher contrast.\n" +
						"_-_ Thunderclouds now have a higher contrast, making them more visible in various areas.\n" +
						"_-_ Added a link to the _Chancel PD Discord Server_ in the About page."));

		changes.addButton(new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed (Caused by BETA):\n" +
						"_-_ Might buff's duration still being 200 instead of 300.\n" +
						"_-_ Might buff causing negative HP if the player dies before it runs out.\n" +
						"_-_ Forceful missile removal causing the player's melee weapon to proc its enchantment.\n" +
						"_-_ Magic Shielding buff not being updated properly when the player takes damage.\n" +
						"_-_ Magic Shielding buff's depletion rate being inconsistent depending on the player's level.\n" +
						"_-_ Wandmaker mentioning Seeds of Rotberry instead of the Rot Core in one of his lines.\n" +
						"_-_ Stone of Equity disabling Assassin's preparation if he remained invisible. He would " +
						"only be able to prepare again once he stopped and restarted his invisiblity.\n" +
						"_-_ Various scrolls taking 2 turns to shout and playing the sound effect twice.\n" +
						"_-_ Very broken behavior from the Immune to Magic buff.\n" +
						"_-_ Forceful missile removal playing no sound effects.\n" +
						"_-_ Missile weapons being able to be cursed in some situations."));

		//************************
		//    CPD v0.1 BETA 5
		//************************

		changes = new ChangeInfo("CPD v0.1 BETA 5", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Missiles now warn when they're one use away from breaking.\n" +
				"_-_ Maximum dynamic strength scaling per level up has been decreased by 33%. This means that in " +
				"the mid and late game, it will take fewer attacks to drain dynamic strength to minimum.\n" +
				"_-_ Unblessed ankhs have been buffed and now preserve all quickslotted items after resurrection.\n" +
				"_-_ Due to the greater number of potions and scrolls, Stones of Intuition were indirectly " +
				"nerfed. To compensate, they now tell you if the potion type you guessed does not exist. " +
				"When this happens, that potion type will permanently be removed from the list, making future " +
				"intuitions more likely to be correct. This also affects the Catalog."));

		changes.addButton(new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes") + " 1",
				"Fixed (Caused by BETA):\n" +
				"_-_ Lucky Spirit Bow crashes the game upon hitting an enemy.\n" +
				"_-_ Missing text from enhanced Teleport scrolls, the Talisman's fully charged message, and " +
				"descriptions of weapon curses.\n" +
				"_-_ Some artifacts not updating their quickslotted charge level during Recharging buff.\n" +
				"_-_ Missile weapon prices not increasing with level.\n" +
				"_-_ Rare cases where arcane styli would delete the rewritten scroll.\n" +
				"_-_ Huntress's description incorrectly referring to Knuckledusters.\n" +
				"_-_ Runestone creation missing from alchemy pots' recipe lists.\n" +
				"_-_ Potions of Placebo failing to be identified when consumed.\n" +
				"_-_ Tipped darts.\n" +
				"_-_ Missile weapons incorrectly showing no damage difference when Rings of Sharpshooting are equipped.\n" +
				"_-_ Rings of Sharpshooting having no effect on the Spirit Bow at all."));

		changes.addButton(new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes") + " 2",
				"Fixed (Caused by BETA):\n" +
				"_-_ Sniper's specials not working properly.\n" +
				"_-_ Upgraded missile weapons not returning to the quickslot once thrown.\n" +
				"_-_ Boomerangs being selectable by Scrolls of Blessing.\n" +
				"_-_ Stones of Intuition could be used to try and identify non-existent items in the inventory.\n" +
				"_-_ The Rot Heart still using toxic gas.\n" +
				"_-_ Rotfruits granting Earth Imbuement instead of Corrosive Imbuement when eaten.\n" +
				"_-_ Stones of Challenge not working properly on Piranhas\n" +
				"_-_ Most multi-target wands being severely weakened because dynamic strength would decrease the instant " +
				"each enemy is damaged."));

		//************************
		//    CPD v0.1 BETA 4
		//************************

		changes = new ChangeInfo("CPD v0.1 BETA 4", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes = new ChangeInfo("New Content", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.KUNAI, null), "New Throwing Weapons",
				"A few new missiles have been added to the game, one for each tier (except tier 1).\n\n" +
				"_-_ Kunai are tier-2 weapons with extra durability.\n" +
				"_-_ Boomerangs are tier-3 weapons that return to their user if they miss, but fall to the ground otherwise.\n" +
				"_-_ Knobkerries are tier-4 weapons with increased durability, but won't stick to their targets.\n" +
				"_-_ Kpingas are tier-5 weapons with high damage and accuracy, but low durability and high strength requirement.\n\n" +
				"(Resemblences to Shattered's Kunai or Heavy Boomerang are purely coincidential.)"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.KPINGA, null), "Forceful Missile Removal",
				"Players now have a new ability. When standing next to an enemy, a button " +
				"will appear at the side of the screen. After tapping it and selecting the specific enemy, the player " +
				"will violently rip said missile out of the enemy, as long as they don't miss. All of the factors " +
				"of a melee attack apply to this, such as player accuracy and enemy evasion skills. Note that " +
				"this method of missile removal will cost another use out of a missile's durability.\n\n" +
				"This is meant mostly to give missiles a better chance at being primary weapons once upgraded. At " +
				"low levels, this will typically only wear down the missile faster while dealing less damage than " +
				"your melee weapon would have. However, when upgraded, missiles might do more than available melee " +
				"options, while having infinite durability. Not to mention, a removed missile goes straight back to " +
				"your inventory and can be used again right away."));

		changes = new ChangeInfo("Changes", false, "");
		changes.hardlight(CharSprite.WARNING);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Scroll of Balance now fails if attempted to be shouted.\n" +
				"_-_ Transfusion buff imported from Shattered so that self damage is only applied when healing allies.\n" +
				"_-_ Imported bola buff from shattered\n" +
				"_-_ Wands and missiles going through grass other sight-blocking terrain was imported from shattered\n" +
				"_-_ Changed BETA 2 to a large title in this changelog for better visual clarity\n" +
				"_-_ Items can no longer be thrown underneath Yog-Dzewa. Instead, they bounce off onto an adjacent " +
				"square. Consequently, this means that Potions of Thunderstorm can no longer be used to effortlessly " +
				"beat this final boss.\n" +
				"_-_ Potion of Might's duration has been increased to 300, up from 200."));

		changes.addButton(new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes") + " 1",
				"Fixed (Existed in Shattered PD):\n" +
				"_-_ Cloak removing the STEALTH option from its description while at one charge.\n" +
				"_-_ Bleeding's larger icon being slightly off-center.\n\n" +
				"Fixed (Caused by BETA):\n" +
				"_-_ Transmutation, when read, freezing the game upon selecting an item\n" +
				"_-_ Uncursing items not marking them that they're known to be uncursed.\n" +
				"_-_ Various scroll descriptions not mentioning what happens if they're shouted, or having outdated descriptions.\n" +
				"_-_ Certain accidental ommisions in the BETA 3 section of the changelog; see Dynamic Strength, Quest " +
				"Reward Changes, Hero Bleeding, and Misc sections.\n" +
				"_-_ Numberous other changelog typos.\n" +
				"_-_ Scrolls of Taunt being the only item name capitalized in the Stone of Intuition's selection window.\n" +
				"_-_ Lucky's awkward and overly wordy description."));

		changes.addButton(new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes") + " 2",
				"Fixed (Caused by BETA):\n" +
				"_-_ The depth icon having a couple extra pixels from the Dynamic Strength icon\n" +
				"_-_ Players not being able to shoot while inside of darkness\n" +
				"_-_ Staircase icon in Ranking Screen showing a couple pixels form another icon.\n" +
				"_-_ Runestone sprite in Alchemy UI not being removed if the inputted scroll was removed.\n" +
				"_-_ Animated Statues not showing the names of their weapons' enchantments.\n" +
				"_-_ Poor wording in Lucky's description.\n" +
				"_-_ Missile weapons still being affected by excess strength bonus, which was removed.\n" +
				"_-_ Cancelling the affects of identified scrolls still consuming the scroll unless only one was left."));

		changes = new ChangeInfo("Buffs", false, "");
		changes.hardlight(CharSprite.POSITIVE);
		infos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.BUFFS_LARGE, 16, 96, 16, 16), "Potion of Might",
				"Duration has been increased to 300, up from 200."));

		changes.addButton(new ChangeButton(new Image(Assets.ROT_HEART, 0, 0, 16, 16), "Rot Heart",
				"Instead of plain toxic gas, the Rot Heart now spawns a mixture of Corrosive and Enticement Gas, " +
				"just like the Seed of Rotberry. However, Enticement Gas has been adjusted so that its range of effect " +
				"is reduced at lower quantities. In other words, you won't have to worry about the Rot Heart calling the " +
				"entire floor upon you every time you attack it."));

		changes = new ChangeInfo("From Shattered PD", false, "");
		changes.hardlight(Window.SHPX_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_TRANSFUSION, null), "Wand of Transfusion",
				"Wand of Transfusion changed significantly when used on enemies:\n" +
				"_-_ No longer self-harms, now grants a mild self-shield instead\n" +
				"_-_ Charm duration no longer scales with level, damage to undead enemies reduced"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Missile Weapons can now be upgraded. The specific mechanics are the same as they " +
				"were in Shattered.\n" +
				"_-_ Thrown weapons now show their tier.\n" +
				"_-_ Missiles and Wand zaps now travel through foilage, instead of being stopped by it."));

		//************************
		//    CPD v0.1 BETA 3
		//************************

		changes = new ChangeInfo("CPD v0.1 BETA 3", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes = new ChangeInfo("New Content", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.DYNAMIC), "Dynamic Strength",
				"A whole new gameplay mechanic has been added to the game: Dynamic Strength!\n\n" +
				"Instead of being completely random, damage you deal on hit is now influenced by this stat. While " +
				"this is at its max, you will also deal max damage. However, it decreases with each attack, and is " +
				"increased by doing anything other than dealing direct damage, including by using other consumable " +
				"items. More information can be found in a new journal page about this.\n\n" +
				"This icon shows the sparkle that represents this in-game, normally displayed in the upper-left corner, " +
				"over the fist of the player's avatar. It ranges in color according to the player's current dynamic " +
				"strength: red at maximum, yellow at medium, invisible at minimum."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_MYSTERY, null), "Scroll Existence System",
				"The existence system that applied to potions now also applies to scrolls!\n\n" +
				"_-_ Four scrolls will be present in every single game.\n" +
				"    _-_ Upgrade, Blessing (Renamed from Vanilla's Enchantment), Cleansing (Renamed from Remove Curse, Identify\n\n" +
				"_-_ Out of sixteen others, eight will be chosen when a run starts to be able to be generated.\n" +
				"    _-_ Balance (New), Charm (Renamed from imported Affection), Darkness (Imported from YAPD), Decay (New), " +
				"Insulation (Renamed from imported Anti-Magic), Lullaby, Magma (New), Necromancy (New), Rage, Recharging, " +
				"Reflection (Renamed from imported Prismatic Image), Supernova (Renamed from Psionic Blast), Taunt (New), " +
				"Teleportation, Terror, Transmutation (imported)"));

		changes.addButton(new ChangeButton(new Image(Assets.MAGE, 242, 15, 11, 15), "Shouting Scrolls",
				"There's a whole new way to use your scrolls: shouting them out loud instead of reading them!\n\n" +
				"Typically, shouting will allow you to use a scroll's effects at a distance, but this is far from the limit " +
				"of shouting. Certain scrolls, such as Identify, Cleansing, and Transmutation have entirely different effects. " +
				"A couple other scrolls, such as Supernova and rage, don't do anything different at all. But in the case of " +
				"a few scrolls, such as Charm, Lullaby, and most importantly Upgrade, will do nothing at all if you try to " +
				"shout them out- so be careful when shouting unidentified scrolls!"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.STONE_TIWAZ, null), "Runestones",
				"Runestones have been added, but less than half are identical to their shattered counterpart.\n\n" +
				"Completely new runestones include Binding, Challenge, Distortion, Equity, Illusion, and Preservation, with " +
				"Stones of Enchantment being replaced by Stones of Preservation.\n\n" +
				"Runestones are found throughout the dungeon, sold in stores (instead of only Augmentation, which is no " +
				"longer guaranteed), and made by putting scrolls into alchemy pots. Four are exclusive to the four constant " +
				"scrolls. The rest of the runestone can be made by two different scrolls each. As examples, Stones of Distortion " +
				"can be made by either a Scroll of Teleportation or a Scroll of Transmutation, while Stones of Challenge can be " +
				"made from either a Scroll of Rage or a Scroll of Taunt. A scroll always makes exactly three runestones."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GRASS_SEED, null), "Huntress's Grass Seeds",
				"While part of the Huntress rework from shattered has been imported from Shattered, mainly the " +
				"Spirit Bow, one aspect of this class is now completely different.\n\n" +
				"Instead of displaying an attunement to nature by furrowing grass, the huntress is now able to collect seeds " +
				"of grass from trampled vegetation. These seeds can be used either to grow some more grass (but not " +
				"infinitely), to root an enemy in place for a free bow shot, or to transform most non-solid tiles into grass.\n\n" +
				"Her subclasses have been imported as well, but without much direct change."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_RUBY, null), "Ring of Force",
				"To go alongside Dynamic Strength, Ring of Force has been completely overhauled.\n\n" +
				"No longer does it function as a pseudo-weapon that allows you to ignore several weapon-related gameplay " +
				"mechanics. Rather, it now boosts your maximum dynamic strength, allowing you to attack censecutively with a " +
				"weapon more times before having to rest again. In addition to that, as long as it's not cursed, it grants an " +
				"extra level of dynamic strength that allows your weapon to do up to 125% of what its max would normally be."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_SILVER, null), "Potion of Expulsion",
				"The old effect of Potions of Refreshment was quite boring and often useless. So, it has been granted an " +
				"overhaul.\n\n" +
				"Potions of Expulsion will still remove most buffs and debuffs, but now the don't simply vanish. Rather, " +
				"they will be ejected in the form of an environmental effect, typically a gas, in a ring around the " +
				"drinker. The specific effect varies from buff to buff, but it's almost always disadvantageous for anything " +
				"that is caught within. However, now it only applies to \"material\" buffs and debuffs; certain debuff which " +
				"aren't caused by some kind of substance, such as bleeding or crippling, are no longer removed."));

		changes.addButton(new ChangeButton(new Image(Assets.TILES_SEWERS, 49, 16, 14, 15), "Well of Exchange",
				"With the addition of Scrolls of Transmutation, Wells of Transmutation have been removed. In order " +
				"to not just have only two kinds of wells, a new well has been added.\n\n" +
				"Wells of Exchange elaborate on the most common use of Wells of Transmutation, swapping between Scrolls of " +
				"Upgrade and Scrolls of Magical Infusion (now Blessing instead). In addition to being able to do that, an " +
				"augmented item can be thrown in to switch to the other augment, and it can be drank if you've chosen a " +
				"subclass and want to switch to your class's other subclass. Use it wisely!\n\n" +
				"Note that one Scroll of Blessing spawns per chapter now, so it's possible to obtain a few extra Scrolls " +
				"of Upgrade in a run."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.STYLUS, null), "Arcane Styli",
				"Since Scrolls of Blessing are the new common way to enchant your gear, Arcane Styli have been " +
				"repurposed. Instead of writing glyphs, they now rewrite scrolls, effectively transmuting them. However, " +
				"they are no longer sold in stores."));

		changes = new ChangeInfo("Changes", false, "");
		changes.hardlight(CharSprite.WARNING);
		infos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.WARRIOR, 0, 90, 12, 15), "Gladiator",
				"Gladiator has recieved a mini-rework so that Dynamic Strength's nerf to attack-spamming does " +
				"not make it suddenly suck.\n\n" +
				"Whenever the combo buff is active, the combo's level / 10 becomes the Gladiator's dynamic strength. This " +
				"means a first hit is free, then he will gradually build up power through combos before every single " +
				"consecutive hit deals max damage. This is in addition to being able to use his usual finishers."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SHORTSWORD, new Lucky().glowing()), "Lucky Enchantment",
				"With Dynamic Strength in place, Lucky is able to offer a better consolidation prize for zero-damage hits.\n\n" +
				"Whenever Lucky forces your damage to become zero, it will behave as if you had just rested instead. In the " +
				"context of a certain new addition, this means that it's possible to turn a win-lose situation into more of " +
				"a win-win situation; in attacking while not at full strength, you can either deal double damage right away, " +
				"or build up for an even more powerful attack in addition to said attack being more likely to deal damage."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MEAT, null), "Mystery Meat Drops",
				"Due to the importance of hunger, random Mystery Meat drops from enemies are now limited in the same " +
				"way that health potion drops are. However, the initial chances of recieving mystery meat are quite high, so " +
				"it's extraordinarily unlikely to have games that randomly lack meat. This should help out against the harshness " +
				"of hunger in the short-term, but potentially make it more devastating in the long-term, at least to players " +
				"who got lucky with the old droprates.\n\n" +
				"Similarly, initial health potion drop rates are much higher, but the maximum limit is far stricter."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.STONE_JERA, null), "Augmentation",
				"Due to Stones of Augmentation no longer being guaranteed from stores, and also being quite rare " +
				"to randomly generate, augmentation is now more difficult to utilize in every run, at least not without " +
				"cooking a Scroll of Blessing at an alchemy pot. However, weapons and armors can now spawn randomly augmented.\n\n" +
				"As a side note, weapons and armor can now only spawn with a single pre-upgrade, and only if they're not " +
				"cursed or enchanted. This doesn't apply to when weapons or armors are special rewards.\n\n" +
				"Note that augments are now hidden if an item is unidentified, and it's no longer possible to use a Stone of " +
				"Augmentation on an unidentified item."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_HOLDER, null), "Wand and Ring generation",
		"There's now a limit to the amount of upgraded wands and rings that can be found per chapter.\n\n" +
		"To specify, the levels of randomly generated wands and rings are now separately tracked and limited like any " +
		"other limited item quantity. This limit is increased with each new chapter reached, not reset, to while there's a " +
		"limit to how much tremendous good luck you can have, bad luck in the short term means that there are more " +
		"opportunities for high level items to spawn in later levels."));

		changes.addButton(new ChangeButton(new Image(Assets.GHOST, 0, 0, 14, 15), "Quest reward changes",
				"_-_ The Sad Ghost now gives a regularly randomized weapon or armor that is upgraded once (aside " +
				"from the fact that it still can't be cursed).\n\n" +
				"_-_ The Wandmaker's wands are now always level +2.\n\n" +
				"_-_ The Ambitious Imp's ring is now always level +4."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Succubi now drop Stones of Hypnotism, since Scrolls of Lullaby don't appear every game.\n" +
				"_-_ The Blessing Buff has been deleted from the code and its sprite has been overwritten by another, " +
				"so that's why the section for BETA 1 will have the wrong blessed image for it for now.\n" +
				"_-_ Potions and scrolls dispelling invisibility has been made more consistent; it only occurs upon " +
				"throwing potions or shouting scrolls, not when drinking or reading.\n" +
				"_-_ Throwing a potion now identifies it, no matter what.\n" +
				"_-_ Enchantments are now hidden by default on unidentified items until they're equipped. This is to make " +
				"it harder to tell if an item is uncursed.\n" +
				"_-_ All healing shows as a green +number over whatever was just healed.\n" +
				"_-_ Since Scrolls of Lullaby don't appear every game anymore, Succubi now drop Stones of Hypnotism instead."));

		changes.addButton(new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed (Existed Shattered PD):\n" +
				"_-_ Health Potions from Prison Guards never being capped.\n" +
				"_-_ Tapping a quickslotted item while targeting with a different one will use the item tapped first instead.\n" +
				"_-_ Enemies not being shown if spotted by the Huntress's sense if she was standing still.\n" +
				"_-_ Players mysteriously being able to descend stairs while levitating if it's impossible to descend elsewhere.\n" +
				"_-_ The flame on the Torch sprite being drawn as if it casts a shadow.\n" +
				"_-_ Various specific errors when actions took more/less than 1 turn."));

		changes.addButton(new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed (Caused by BETA):\n" +
				"_-_ Rare chance for constant potions/scrolls to not exist in a game. (Hopefully...)\n" +
				"_-_ Recovering from Starvation's icon being displayed (but not tappable) a few turns after it's supposed to vanish.\n" +
				"_-_ Cases where Starvation damage would remain 0 while starving, effectively disabling hunger.\n" +
				"_-_ Deaths by Potions of Shockwave not being recorded in the rankings screen.\n" +
				"_-_ Various changelog typos.\n" +
				"_-_ Blob Immunity buff not granting immunity to thunder clouds.\n" +
				"_-_ Strength Badges not being attained immediately upon level up.\n" +
				"_-_ Nonexistent items stacking, when they're supposed to be unidentifiable.\n" +
				"_-_ Thunder clouds not charging the Mage's Staff.\n" +
				"_-_ Magic Shielding buff not detaching properly if depleted by damage.\n" +
				"_-_ Overgrowth curse not being able to activate deadnettle.\n" +
				"_-_ A couple in-game lines of text referring to Shattered Pixel Dungeon instead of Chancel pixel Dungeon.\n" +
				"_-_ Potions of Might being far too overpriced compared to other potions."));

		changes = new ChangeInfo("Nerfs", false, "");
		changes.hardlight(CharSprite.NEGATIVE);
		infos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER, null), "Bonus Strength Damage",
				"Because very high Dynamic Strength provides lower-damage weapons with more consecutive max damage attacks, " +
				"the extra random bonus to damage from excess strength has been removed."));

		changes = new ChangeInfo("Buffs", false, "");
		changes.hardlight(CharSprite.POSITIVE);
		infos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.RAT, 1, 3, 13, 12), "Mobs and Bosses",
				"Mobs and bosses in general have become just a bit beefier. Regular mobs have had their maximum " +
				"health increased by an average of (chapter where they spawn) ^ 2, meaning that while rats only gained 1 max " +
				"HP, Evil Eyes gained as much as 25. In particular, with Dynamic Strength, rats' 1 extra HP is fairly " +
				"significant; now, the Warrior is the only class who can one-shot a rat without upgrading his starting weapon, " +
				"since both the Mage's and Rogue's melee weapons only deal as much as 8 damage without upgrades.\n\n" +
				"Most bosses have also recieved a health buff, with the Tengu now having 200 max HP (up from 120) and each boss " +
				"after that gaining an additional 100 health. Goo's, however, is unchanged from 100."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BLANDFRUIT, new Projecting().glowing()), "Metafruit",
				"Metafruit now gives the Well Fed buff when eaten, so it isn't a complete waste to cook it. This " +
				"makes it functionally identical, but costlier, compared to the Meat Pie from Shattered PD."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BOMB, null), "Bombs",
				"Since otherwise bombs would be pretty much outclassed compared to Stones of Blast, the explosion " +
				"radius of Bombs has been doubled. This should make them a bit easier and a bit harder to use, at the same time."));

		changes.addButton(new ChangeButton(new Image(Assets.BUFFS_LARGE, 96, 64, 16, 16), "Recharging",
				"The recharging buff now applies to artifacts as well as wands."));

		changes = new ChangeInfo("Visuals", false, "");
		changes.hardlight(CharSprite.DEFAULT);
		infos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.MENU, 15, 0, 16, 15), "UI Improvements",
				"Almost the all of the UI has been completely redone. Instead of just being some eye-bleedingly " +
				"bright recolor, it now features a deeper, more saturated purple/blue color scheme with much more visually " +
				"interesting shapes and textures."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.PAVISE, null), "Weapon Sprites",
				"A majority a weapons have recieved a grand visual overhaul, which is mostly a series of recolors, " +
				"but also includes some new shapes as well.\n\n" +
				"Also, since there were previously three tier-5 weapons that had \"Great\" in their name, Greatsword has " +
				"been renamed to Broadsword, and Great Shield has been renamed to Pavise."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BOW_LOADED, null), "Spirit Bow",
				"The Huntress's Spirit Bow in particular has recieved a new look, which has separate sprites for " +
				"when you are and aren't aiming with it."));

		changes.addButton(new ChangeButton(new Image(Assets.BUFFS_LARGE, 80, 48, 16, 16), "Hero Bleeding",
				"The hero once again visibly bleeds on hit, similar to enemies. Also, the slow blood-dropping " +
				"particle effect when at low health has been restored from Vanilla as well."));

		changes = new ChangeInfo("From Shattered PD", false, "");
		changes.hardlight(Window.SHPX_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.HUNTRESS, 0, 90, 12, 15), "Huntress Rework",
				"The Spirit Bow, Studded Gloves, and Subclasses of Shattered PD's Huntress have been included, but " +
				"with a twist! See the Grass Seed in _New Content_ for more details."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_HOLDER, null), "Ring Changes",
				"Ring stat descriptions have been included, as well as the simplifications of the Rings of " +
				"Accuracy, and Furor."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ If an item is unidentified but confirmed to lack a curse, it will have a blue background.\n" +
				"_-_ There's a new partial turn indicator to show what percentage of a turn has passed.\n" +
				"_-_ It now takes a whole turn to drop items, instead of half of a turn.\n" +
				"_-_ Locked laboratory rooms (aka alchemy pot rooms) are now guaranteed to spawn once per chapter."));

		//**********************
		//    CPD v0.1 BETA 2
		//**********************

		changes = new ChangeInfo("CPD v0.1 BETA 2", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Rotberry Core now has its own sprite, rather than the old placeholder from SproutedPD"));

		changes.addButton(new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed (Caused by BETA):\n" +
				"_-_ The Welcome Scene's title not displaying properly\n" +
				"_-_ Lightning from Thunder Clouds increasing wand charges above maximum limit\n" +
				"_-_ Lasting Damage not decreasing if the player's health is full\n" +
				"_-_ Lasting Damage decreasing in locked floors\n" +
				"_-_ Starvation Recovery's icon rarely failing to update after the debuff is cleared\n" +
				"_-_ Lasting damage always being caused by starvation, even if the player had only just began starving"));

		//**********************
		//    CPD v0.1 BETA 1
		//**********************

		changes = new ChangeInfo("CPD v0.1 BETA 1", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes = new ChangeInfo("New Content", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_UNSTABLE, null), "Potion Existence System",
				"With the addition of several new potions, but no extra colors to fit those potions, there will be potions in each run that won't be able to spawn at all.\n\n" +
				"_-_ Four potions will be present in every single game.\n" +
				"    _-_ Healing, Hydrocombustion (Renamed from Liquid Flame.), Invisibility, Levitation\n\n" +
				"_-_ As for the other sixteen, eight will be chosen when a run starts to be able to be generated. Essentially, individual runs will be far different from each other than they were before, down to how you use your consumables.\n" +
				"    _-_ Corrosivity (Renamed from imported Corrosive Gas), Enticement (New), Experience, Frost, Haste (Imported), Might, Overgrowth (Imported from YAPD), " +
				"Placebo (New), Purity (Renamed from Purification.), Shielding (Imported), Shockwave (New), Telepathy (Renamed from Mind Vision.), " +
				"Refreshment (new), Repulsion (New), Thunderstorm (Imported from YAPD), Toxicity (Renamed from Toxic Gas.)"));

		changes.addButton(new ChangeButton(new Image(Assets.TERRAIN_FEATURES, 176, 112, 16, 16), "Deadnettle",
				"A new rare plant has been added to the game, replacing Starflower.\n\n" +
				"This dangerous yet highly beneficial plant has many uses, as it trades half of a creature's health " +
				"for temporary might, mind vision, and invisibility."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SEED_BLANDFRUIT, null), "Seed of Blandfruit Alchemy",
				"Seeds of Blandfruit can now be used in alchemy! However, their effects tend to be minimal, as they normally do nothing on their own.\n" +
				"_-_ Potion: Three seeds of Blandfruit can be made into Potions of Placebo, which mimic the effects of potions in your inventory.\n" +
				"_-_ Metafruit: Cooking a Blandfruit with a Seed of Blandfruit gives you this, which only lets you eat it without any bonus effect.\n" +
				"_-_ Heavy Dart: Tipping one or two darts with a Seed of Blandfruit will produce these, which deal double damage. This effect is weak on its own, but very strong with a Crossbow."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ROTBERRY, null), "Rotberry Core",
				"Now, the Rot Heart from the Wandmaker's quest drops a Rotberry Core, instead of a Seed of Rotberry.\n\n" +
				"What this means for Seeds of Rotberry is that they are now allowed to generate randomly, just like any other seed. " +
				"When trampled, instead of toxic gas, they spawn a mixture of corrosive and enticement gas, repeatedly luring a monster " +
				"trapped within to take a lot of damage, with the potential downside for players of also luring every other mob in the stage to its location."));

		changes = new ChangeInfo("Changes", false, "");
		changes.hardlight(CharSprite.WARNING);
		infos.add(changes);

		changes.addButton(new ChangeButton(BadgeBanner.image(Badges.Badge.STRENGTH_ATTAINED_1.image), "Strength Progression",
				"Now that Potions of Strength have been removed and Potions of Might have been made temporary, " +
				"strength progression is now done through another means: levelling. Every third level the player reaches " +
				"will award them with a new point of strength. This does mean that strength progression has slowed somewhat," +
				"increasing the amount of time it takes for players to be able to equip higher tier items."));

		changes.addButton(new ChangeButton(new Image(Assets.BUFFS_LARGE, 48, 80, 16, 16), "Starvation",
				"Starvation has been reworked to be more punishing to players who tend to avoid eating for very long periods of time. " +
				"At first, starvation will function mostly identically to how it did before. However, if the hero doesn't eat anything " +
				"at all for a long time, starvation damage will begin to quickly increase without a maximum cap. Additionally, it's " +
				"not enough to just eat a little bit, as remaining extra damage will take some time to wear off even after eating."));

		changes.addButton(new ChangeButton(new Image(Assets.TILES_SEWERS, 48, 96, 16, 16), "Potion Brewing",
				"With the new potion system, alchemy has recieved adjustments in regards to potion cooking.\n\n" +
				"Seeds now have three different preferences for what potions they can make.\n" +
				"_-_ Preference 1 will be chosen in any game where that particular potion exists, regardless of what others exist.\n" +
				"_-_ Preference 2 will only be chosen if it exists, but preference 1's potion doesn't.\n" +
				"_-_ Preference 3 is always one of the four potions that exist in every game, and is chosen if no other preferred potion exists.\n\n" +
				"Blandfruit will always give the effect of a seed's first preference for potion brewing. This means that because there is " +
				"no longer any seed that has Potion of Experience as its first preference, blandfruits can no longer be used for easy " +
				"experience farming."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Food can now be quickslotted.\n" +
				"_-_ Monks no longer drop food rations."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_AMBER, null), "Quickslotted Potions",
				"Potions now have \"THROW\" as their default action (from tapping them while quickslotted) if they are harmful and identified."));

		changes.addButton(new ChangeButton(new Image(Assets.HUNTRESS, 0, 15, 12, 15), "Huntress",
				"Because Potions of Telepathy won't appear every single game, Huntress now identifies Scrolls of Identify instead."));

		changes.addButton(new ChangeButton(new Image(Assets.ROGUE, 0, 15, 12, 15), "Rogue",
				"Because otherwise three different heroes would identify potions, Rogue now identifies Potions of Invisibility."));

		changes.addButton(new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed:\n" +
				"_-_ Various rare cases where item windows could stack\n" +
				"_-_ Wands never appearing in hero's remains\n" +
				"_-_ 'Faith is my armor' deleting class armors\n" +
				"_-_ Albino Rat's ears being crooked in a couple of its frames"));

		changes = new ChangeInfo("Nerfs", false, "");
		changes.hardlight(CharSprite.NEGATIVE);
		infos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.BUFFS_LARGE, 16, 96, 16, 16), "Potion of Might",
				"Potion of Might's effect has been made temporarily, only lasting 200 turns. As an upside, " +
				"its maximum health bonus has been increased to +25%, instead of a flat 5, and it can now be found fairly commonly throughout " +
				"the dungeon, though it can be hard to brew due to being Deadnettle's preferred potion, not Rotberry's."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ROT_DART, null), "Rot Dart",
				"As Rot Darts can now be made fairly often, they now inflict 5 turns of corrosion instead of potentially infinite turns of caustic ooze. " +
				"This makes them still capable of dealing large amounts of damage in total, but not free wins against mobs unable to wash themselves."));

		changes = new ChangeInfo("Buffs", false, "");
		changes.hardlight(CharSprite.POSITIVE);
		infos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARMOR_CLOTH, null), "Armor",
				"Armor now needs 1 less strength to be used properly than before."));

		changes = new ChangeInfo("Removed Content", false, "");
		changes.hardlight(CharSprite.NEGATIVE);
		infos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_BISTRE, null), "Removed Potions",
				"Two potions types have been removed from the game, without direct replacements.\n" +
				"_-_ Potion of Strength\n" +
				"_-_ Potion of Paralytic Gas"));

		changes.addButton(new ChangeButton(new Image(Assets.BUFFS_LARGE, 32, 80, 16, 16), "Blessed",
				"The absolute player level cap of 30 has been removed.\n\n" +
				"Because of this, and also because of the removal of the Starflower plant, the Blessed buff has effectively been removed " +
				"from the game, even if it still technically exists in the code."));

		changes = new ChangeInfo("Visuals", false, "");
		changes.hardlight(CharSprite.DEFAULT);
		infos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SHATTPOT, null), "Shattered Honeypot",
				"Shattered Honeypots now have their own unique sprites, instead of just being heavily cracked honeypots."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ROUND_SHIELD, null), "Round Shield",
				"Round Shield now has a new and far better sprite. (Credit to hellocoolgame#8751)"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_AZURE, null), "Potions",
				"Potion colors now match seed colors, and their lighting has been mirrored to be consistant with other item sprites.\n" +
				"_-_ Azure is now Indigo\n" +
				"_-_ Old Indigo is now Lavender"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_HAGLAZ, null), "Elder Futhark Runes",
				"All scroll runes are now consistant with Elder Futhark runes. (Credit to Chinook#8926)"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.STONE_DAGAZ, null), "Runestones",
				"Runestones now have the sprites of rune-shaped stones, each with six pink gems in them. " +
				"(However, Runestones themselves haven't been implemented yet, other than the pre-existing Augmentation and Enchantment.)"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DISPLACING_DART, null), "Tipped Darts",
				"Tipping darts will no longer magically change the color of their shaft."));

		//**********************
		//       v0.6.5
		//**********************

		changes = new ChangeInfo("From ShatteredPD", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes = new ChangeInfo("v0.6.5c", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed (Caused by 0.6.5):\n" +
				"_-_ Exploit involving the timekeeper's hourglass that allowed for free attacks"));

		changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), Messages.get(this, "language"),
				"Updated Translations"));

		changes = new ChangeInfo("v0.6.5a & v0.6.5b", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.MAGE, 0, 90, 12, 15), "Warlock",
				"Soul mark chance changed. Now has a 10% chance to activate per wand level, stacking multiplicatively, with a base of 10% at +0.\n" +
				"e.g. +0 is 10%, +1 is 19%, +2 is 27%, etc.\n\n" +
				"Previous soul mark chance was 9% at base plus 6% per level, stacking linearly.\n\n" +
				"This substantially increases soul mark chance at wand levels +1 to +5"));

		changes.addButton(new ChangeButton(new Image(Assets.HUNTRESS, 0, 15, 12, 15), "Huntress",
				"Huntress ranged weapon durability boost now stacks with magical holster durability boost, for a total of 180% durability."));

		changes.addButton(new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed (Caused by 0.6.5):\n" +
				"_-_ Korean language crashes\n" +
				"_-_ Viscocity deferring damage before it is blocked by armor\n" +
				"_-_ Various rare crash bugs\n\n" +
				"Fixed (Existed prior to 0.6.5):\n" +
				"_-_ Piranha incorrectly being affect by vertigo\n" +
				"_-_ Ambitious imp spawning on top of traps\n" +
				"_-_ Enemies spawning faster than intended in specific cases"));

		changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), Messages.get(this, "language"),
				"Updated Translations"));

		changes = new ChangeInfo("v0.6.5", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes = new ChangeInfo(Messages.get(this, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released May 3rd, 2018\n" +
				"_-_ 32 days after Shattered v0.6.4\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(new StoneOfAugmentation(),
				"The weightstone is now the runestone of augmentation!\n\n" +
				"Usability on weapons unchanged, can still be used to enhance either speed or damage at the cost of the other.\n\n" +
				"Can now be used on armor! Armor can be modified to enhance either defense or evasion, at the cost of the other.\n\n" +
				"Every shop now stocks a runestone of augmentation and an ankh, instead of one or the other."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARMOR_CLOTH, new Bulk().glowing()), "New Curses",
				"Added 4 new curses:\n\n" +
				"_-_ Friendly curse makes weapons sometimes charm both you and the enemy.\n" +
				"_-_ Elastic curse lets weapons apply knockback, but reduces damage to 0.\n\n" +
				"_-_ Bulk curse makes armor large, slowing movement through doorways.\n" +
				"_-_ Overgrowth curse causes random plant effects when you are struck."));

		changes.addButton(new ChangeButton(BadgeBanner.image(Badges.Badge.CHAMPION_3.image), "New and Changed Badges",
				"_-_ Added badges for winning with 3 challenges at once and 6 challenges at once.\n\n" +
				"_-_ 'Death by glyph' badge is now 'death by deferred damage'.\n\n" +
				"_-_ Removed rare monster slayer badge."));

		changes.addButton(new ChangeButton(new Image(Assets.WARRIOR, 0, 90, 12, 15), "Berserker",
				"Even with recent nerfs the berserker is still much stronger than other subclasses. Rather than continually nerfing his existing mechanics, which makes the subclass unfun, I have instead opted to give him a small rework.\n\n" +
				"These changes focus on giving the berserker some of his old power back, but making it more difficult to access that power.\n\n" +
				"_-_ Rage is built by taking physical damage\n" +
				"_-_ Rage fades over time, lasts longer at low HP\n" +
				"_-_ Rage builds faster with better armor\n" +
				"_-_ Rage grants bonus damage, max of +50%\n" +
				"_-_ Berserker now needs full rage to berserk\n" +
				"_-_ Berserking no longer reduces max hp\n" +
				"_-_ Berserk bonus shielding doubled\n" +
				"_-_ Berserk bonus damage reduced to +50%\n" +
				"_-_ Removed exhaustion damage penalty\n" +
				"_-_ Berserker can't gain rage while recovering"));

		changes = new ChangeInfo(Messages.get(this, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		infos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.STYLUS, null), "Glyph Changes",
				"Glyphs were originally designed with the intention that taking no glyph should be a valid option. Now with augmenting armor, glyphs can be more about added bonuses, somewhat like enchantments. Several glyphs have been adjusted:\n\n" +
				"_-_ Entanglement now only roots if you stand still.\n\n" +
				"_-_ Potential no longer self-damages and grants charge more consistently.\n\n" +
				"_-_ Viscocity now always defers some damage, instead of sometimes deferring all damage.\n\n" +
				"_-_ Stone reworked. Now sets evasion to 0 and grants armor in proportion to evasion.\n\n" +
				"_-_ Swiftness reworked. Now grants movement speed when no enemies are near.\n\n" +
				"_-_ Viscocity is now a common glyph, Stone is now uncommon."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ All bags now have 20 spaces. Previously only the default bag had 20, and the others had 12.\n\n" +
				"_-_ Updated the sprites for runestones and throwing stones\n\n" +
				"_-_ Loading screen transitions are now faster in many cases\n\n" +
				"_-_ Improved the layout of translator credits in landscape"));

		changes.addButton(new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed:\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Antimagic applying to elemental damage\n" +
				"_-_ 'Happy end' badge not appearing in rankings\n" +
				"_-_ 'Death from falling' badge not triggering\n" +
				"_-_ Hero rarely appearing alive when dead\n" +
				"_-_ Sungrass not interrupting resting at full hp\n" +
				"_-_ Timekeeper's hourglass unusable at 1 charge\n" +
				"_-_ Artifacts rarely appearing when blocked by a challenge\n" +
				"_-_ Hero spending a turn before actually opening a lock\n" +
				"_-_ Specific cases where an invisible hero would not surprise attack\n" +
				"_-_ Shields granting full defense when hero does not have enough strength."));

		changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), Messages.get(this, "language"),
				"Updated Translations"));

		changes = new ChangeInfo(Messages.get(this, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		infos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SWORD, new Lucky().glowing()), "Lucky Enchantment",
				"The Lucky enchant is a nice overall DPS increase, but comes at the cost of consistency. The problem is that with a bit of bad luck it's possible to do 0x damage many times in a row.\n\n" +
				"Lucky has been adjusted to reign in the extremes of bad luck, and to give a little more strategy to using it.\n\n" +
				"_-_ Base chance to deal 2x damage reduced to 50% from 60%\n" +
				"_-_ Each time 0x damage is dealt, the next hit will be much more likely to deal 2x damage"));

		changes = new ChangeInfo(Messages.get(this, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		infos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SHORTSWORD, new Wayward().glowing()), "Wayward curse",
				"Wayward's accuracy penalty was very extreme, often making it impossible to win fights without doors. Wayward should punish non-guaranteed attacks, but this extent of this has been lessened.\n\n" +
				"_-_ Reduced wayward accuracy penalty by 50%"));

		changes.addButton(new ChangeButton(new Image(Assets.SKELETON, 0, 0, 12, 15), "Skeletons",
				"Skeletons have been adjusted to be more counterable with armor, and to give less inventory-clogging loot.\n\n" +
				"_-_ Bone explosion damage up to 6-12 from 2-10\n" +
				"_-_ Armor is now 2x effective against bone explosion, up from 0.5x\n\n" +
				"_-_ Loot drop chance reduced to 1/8, from 1/5"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.KIT, null), "Rogue Garb and Huntress Cloak",
				"Eventually I want to totally overhaul class armors. In the meantime though, two of the armors are disproportionately powerful with mind vision, and need to be adjusted:\n\n" +
				"_-_ Rogue's smoke bomb now has a max range of 8 and does not go through walls\n\n" +
				"_-_ Huntress's spectral blades now have a max range of 12"));

		//**********************
		//       v0.6.4
		//**********************

		changes = new ChangeInfo("v0.6.4", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes = new ChangeInfo(Messages.get(this, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released April 1st, 2018\n" +
				"_-_ 46 days after Shattered v0.6.3\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), "Challenges",
				"Challenges have received several major changes, with the goal of making them more fair and interesting.\n" +
				"\n" +
				"_-_ Challenges now have descriptions\n" +
				"\n" +
				"_-_ On diet now provides small rations, rather than removing all food\n" +
				"_-_ Cloth armor is now allowed on faith is my armor\n" +
				"_-_ Pharmacophobia is unchanged\n" +
				"_-_ Barren land now allows seeds to drop, but they cannot be planted\n" +
				"_-_ Swarm intelligence now draws nearby enemies, not all enemies\n" +
				"_-_ Into darkness now limits light more harshly, but provides torches\n" +
				"_-_ Forbidden runes now removes 50% of upgrade scrolls, and no other scrolls"));

		changes.addButton(new ChangeButton(Icons.get(Icons.INFO), "Start game UI",
				"The interface for starting and loading a game has been completely overhauled!\n" +
				"\n" +
				"_-_ Game now supports 4 save slots of any hero class, rather than 1 slot per class\n" +
				"_-_ Hero select and challenge select are now more streamlined and informative\n" +
				"_-_ Hero select is now a window, offering more flexibility of where games can be started\n" +
				"_-_ More details are now shown for games in progress before they are loaded"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CROSSBOW, null), "New Weapons",
				"Three new weapons have been added!\n" +
				"\n" +
				"Throwing spears are a basic tier 3 missile weapon, fishing spears have been reduced to tier 2. Tiers 2-5 now each have a basic missile weapon.\n" +
				"\n" +
				"The crossbow is a tier 4 melee weapon which enhances darts! You'll do less damage up-front, but thrown darts will pack a punch!\n" +
				"\n" +
				"The gauntlet is a tier 5 fast melee weapon, similar to the sai. Excellent for chaining combos or enchantments."));

		changes = new ChangeInfo(Messages.get(this, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		infos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HOLSTER, null), "Inventory changes",
				"Since the ranged weapon improvements in 0.6.3, inventory space has become a bit too congested. Rather than make a small change that only helps the issue for a few more updates, I have decided to make a larger-scale adjustment to available inventory space:\n" +
				"\n" +
				"_-_ The wand holster is now the magical holster, and can store missile weapons as well as wands.\n" +
				"\n" +
				"_-_ The seed pouch is now the velvet pouch, and can store runestones as well as seeds.\n" +
				"\n" +
				"_-_ Every hero now starts the game with an extra container."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ It is now possible to back up game data using ADB backup on android 4.0+ and android auto-backup on android 6.0+. Runs in progress are not backed up to prevent cheating.\n" +
				"\n" +
				"_-_ Firebloom plants will no longer appear in garden rooms. Accidentally running into them is massively more harmful than any other plant.\n" +
				"\n" +
				"_-_ Mage and Warrior tutorial functionality has been removed, as more players found it confusing than helpful.\n" +
				"\n" +
				"_-_ Added a new visual effect to the loading screen\n" +
				"\n" +
				"_-_ Piranha treasure rooms now have a one tile wide buffer\n" +
				"\n" +
				"_-_ Bags are now unsellable\n" +
				"\n" +
				"_-_ The dwarf king is now immune to blindness\n" +
				"\n" +
				"_-_ Made adjustments to sending gameplay data. Data use should be slightly reduced."));

		changes.addButton(new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed:\n" +
				"_-_ Crashes involving corrupted mimics\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Various minor visual bugs\n" +
				"_-_ Skeletons exploding when falling in chasms\n" +
				"_-_ Thrown weapons lost when used on sheep\n" +
				"_-_ Warden gaining benefits from rotberry bush\n" +
				"_-_ Rare cases where music wouldn't play\n" +
				"_-_ Unstable enchant not being able to activate venom"));

		changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), Messages.get(this, "language"),
				"Updated Translations"));

		changes = new ChangeInfo(Messages.get(this, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		infos.add(changes);

		changes.addButton(new ChangeButton(new TimekeepersHourglass(),
				"The timekeeper's hourglass has been adjusted to cap at 10 charges, instead of 20, and to have a bit more power without upgrades:\n" +
				"\n" +
				"_-_ Number of charges halved\n" +
				"_-_ 2x freeze duration per charge\n" +
				"_-_ 5x stasis duration per charge\n" +
				"_-_ Overall recharge speed increased at +0, unchanged at +10"));

		changes.addButton(new ChangeButton(new TalismanOfForesight(),
				"The talisman of foresight now builds power for scrying slightly faster\n" +
				"\n" +
				"_-_ Charge speed increased by 20% at +0, scaling to 50% increased at +10\n" +
				"_-_ Charge gain when discovering traps unchanged"));

		changes = new ChangeInfo(Messages.get(this, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		infos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.BUFFS_LARGE, 64, 0, 16, 16), "Paralysis changes",
				"Paralysis is an extremely powerful debuff, and its ability to completely immobilize the player or an enemy while they are killed needs to be adjusted.\n" +
				"\n" +
				"Chance to resist paralysis is now based on all recent damage taken while paralyzed, instead of each specific instance of damage separately.\n" +
				"\n" +
				"This means that after taking around half current HP in damage, breaking from paralysis becomes very likely, and immediately re-applying paralysis will not reset this resist chance."));

		changes.addButton(new ChangeButton(new Image(Assets.TILES_SEWERS, 48, 48, 16, 16), "Chasm changes",
				"Dropping enemies into chasms is a very fun way to deal with enemies, but killing an enemy instantly and getting almost the full reward is simply too strong. This change should keep killing via chasms fun and useful, without it being as strong.\n" +
				"\n" +
				"_-_ Enemies killed via chasms now only award 50% exp"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SEED_SUNGRASS, null), "Seed adjustments",
				"Sungrass is almost as effective as a potion of healing when used properly, which is extremely strong for a seed. I am increasing the time it takes to heal, so that hunger and combat while waiting can add some cost to the otherwise very powerful healing sungrass provides.\n" +
				"\n" +
				"_-_ Sungrass now grants healing significantly more slowly, scaling to ~40% speed at high levels\n" +
				"_-_ Taking damage no longer reduces sungrass healing\n" +
				"_-_ Sungrass healing no longer dissapears at full HP\n" +
				"\n" +
				"Earthroot is also problematic, as its 50% damage resist makes it an extremely potent tool against bosses, yet not so useful against regular enemies. My hope is that this change levels its power out over both situations.\n" +
				"\n" +
				"_-_ Earthroot now blocks up to a certain amount of damage, based on depth, rather than 50% damage"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_CRIMSON, null), new PotionOfHealing().trueName(),
				"Heal potion drops have had their RNG bounded in shattered for a long time, but this bound was always fairly lax. This meant that people who wanted to slowly farm for potions could still amass large numbers of them. I have decided to reign this in more harshly.\n" +
				"\n" +
				"_-_ Health potion drops now lower in probability more quickly after potions have already been dropped from a given enemy type\n" +
				"\n" +
				"This change should not seriously affect the average player, but does make hoarding health potions much less feasible."));

		//**********************
		//       v0.6.3
		//**********************

		changes = new ChangeInfo("v0.6.3", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes = new ChangeInfo(Messages.get(this, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released February 14th, 2018\n" +
				"_-_ 113 days after Shattered v0.6.2\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.TRIDENT, null), "Ranged Weapons Overhaul!",
				"Ranged weapons have been completely overhauled!\n\n" +
				"_-_ Quantity of ranged weapons decreased, however most ranged weapons now last for several uses before breaking.\n\n" +
				"_-_ Ranged weapon effectiveness increased significantly.\n\n" +
				"_-_ Ranged weapons are now dropped in more situations, and do not replace melee weapons.\n\n" +
				"_-_ Existing ranged weapons reworked, 5 new ranged weapons added.\n\n" +
				"_-_ Warrior now starts with throwing stones, rogue starts with throwing knives"));

		changes.addButton(new ChangeButton(new Image(Assets.HUNTRESS, 0, 15, 12, 15), "Huntress",
				"Huntress adjusted due to ranged weapon changes (note that this is not a full class rework):\n\n" +
				"_-_ Huntress no longer has a chance to reclaim a single ranged weapon.\n\n" +
				"_-_ Missile weapons now have 50% greater durability when used by the huntress.\n\n" +
				"_-_ Boomerang dmg increased to 1-6 from 1-5\n" +
				"_-_ Boomerang str req reduced to 9 from 10\n" +
				"_-_ Knuckleduster dmg reduced to 1-5 from 1-6"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHILLING_DART, null), "Expanded Alchemy",
				"It is now possible to use alchemy to tip darts!\n\n" +
				"_-_ Every seed (except blandfruit) can now be combined with two darts to make two tipped darts.\n\n" +
				"_-_ Tipped dart effects are similar to their potion/seed counterparts.\n\n" +
				"_-_ Curare darts are now paralytic darts, and paralyze for 5 turns, up from 3\n\n" +
				"_-_ Alchemy interface now features a recipes button to show you what you can create."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_TOPAZ, null), Messages.get(RingOfSharpshooting.class, "name"),
				"Ring of Sharpshooting overhauled\n\n" +
				"_-_ No longer grants bonus accuracy\n\n" +
				"_-_ Now increases ranged weapon durability, instead of giving a chance to not consume them\n\n" +
				"_-_ Now increases ranged weapon damage, scaling based on the weapon's initial damage."));

		changes = new ChangeInfo(Messages.get(this, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		infos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.BUFFS_LARGE, 32, 0, 16, 16), "Changes to debuffs and resistances",
				"The game's resistance system has been totally overhauled, to allow for more flexibility and consistency.\n\n" +
				"Previously, if a character was resistant to something, its effect would be reduced by a random amount between 0% and 100%.\n\n" +
				"Now, resistances are much less random, applying a specific reduction to harmful effects. Currently all resistances are 50%.\n\n" +
				"Resistances are also now much more consistent between different creatures. e.g. all non-organic enemies are now immune to bleed.\n\n" +
				"A few things have been adjusted due to this:\n\n" +
				"_-_ The Rotting Fist is now immune to paralysis.\n" +
				"_-_ Psionic blast now deals 100% of current HP, instead of 100% of max HP.\n" +
				"_-_ Damage from fire now scales with max HP, and is slightly lower below 40 max HP."));

		changes.addButton(new ChangeButton(new WandOfCorrosion(),
				"Wand of venom is now wand of corrosion. This is primarily a visual rework, with only some changes to functionality:\n\n" +
				"_-_ Wand now shoots bolts of caustic gas, instead of venom gas\n" +
				"_-_ Venom debuff is now corrosion debuff, functionality unchanged\n\n" +
				"_-_ Battlemage now inflicts ooze with a staff of corrosion, instead of poison."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Performance improvements to the fog of war & mind vision.\n\n" +
				"_-_ Improved the consistency of how ranged traps pick targets.\n\n" +
				"_-_ Weapons and armor can now be found upgraded and cursed. Overall curse chance unchanged.\n\n" +
				"_-_ Each shop now always stocks 2 random tipped darts\n\n" +
				"_-_ Starting weapons can no longer appear in hero's remains\n\n" +
				"_-_ The ghost hero is no longer unaffected by all buffs, and is also immune to corruption"));

		changes.addButton(new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed:\n" +
				"_-_ Various crash bugs\n" +
				"_-_ Serious memory leaks on android 8.0+\n" +
				"_-_ Rankings not retaining challenges completed\n" +
				"_-_ Scroll of psionic blast debuffing a dead hero\n" +
				"_-_ Rot lashers not being considered minibosses\n" +
				"_-_ Wand of corruption ignoring NPCs\n" +
				"_-_ NPCs being valid targets for assassin\n" +
				"_-_ Wand of frost battlemage effect not activating as often as it should.\n" +
				"_-_ Items in the alchemy window rarely being lost\n" +
				"_-_ Various minor visual bugs"));

		changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), Messages.get(this, "language"),
				"In English:\n" +
				"_-_ Fixed inconsistent text when equipping cursed artifacts\n\n" +
				"Updated Translations"));

		changes = new ChangeInfo(Messages.get(this, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		infos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_EMERALD, null), Messages.get(RingOfElements.class, "name"),
				"Thanks to the increased flexibility of the improved resistance system, buffing the ring of elements is now possible!\n\n" +
				"_-_ Now reduces the duration and damage of harmful effects significantly more at higher levels.\n\n" +
				"_-_ Rather than granting a chance to resist elemental/magic damage, ring now grants a set percentage resistance to these effects, which increases each level.\n\n" +
				"_-_ Ring now applies to more elemental/magical effects than before."));

		changes.addButton(new ChangeButton(new Image(Assets.MAGE, 0, 90, 12, 15), "Warlock",
				"The warlock is underperforming relative to the battlemage at the moment, and so he is getting an adjustment to his ability.\n\n" +
				"This should hopefully both increase his power, and further encourage investing upgrades in wands.\n\n" +
				"_-_ Reduced the base soul mark chance by 40%\n" +
				"_-_ Increased soul mark chance scaling by 100%\n\n" +
				"Soul mark chance reaches pre-adjustment levels at a +2 wand, and grows from there."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_MAGIC_MISSILE, null), "Minor Wand buffs",
				"Wand of Corruption:\n" +
				"_-_ Reduced the corruption resistance of wraiths by ~40%\n" +
				"_-_ Enemies now drop their loot (including ranged weapons) when corrupted.\n" +
				"_-_ If an enemy is immune to a particular debuff, corruption will now try to give a different debuff, instead of doing nothing.\n\n" +
				"Wand of Corrosion:\n" +
				"_-_ Corrosion damage growth will continue at 1/2 speed when the damage cap is reached, rather than stopping completely."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FLAIL, null), "Weapon and Glyph buffs",
				"Weapons with non-standard accuracy are generally weak, so they have been buffed across the board:\n\n" +
				"_-_ Flail accuracy penalty reduced by 10%\n" +
				"_-_ Handaxe accuracy bonus increased by 9.5%\n" +
				"_-_ Mace accuracy bonus increased by 8%\n" +
				"_-_ BattleAxe accuracy bonus increased by 6.5%\n" +
				"_-_ WarHammer accuracy bonus increased by 5%\n\n" +
				"Glyph Buffs:\n" +
				"_-_ Glyph of obfuscation no longer reduces damage blocking, but is also less powerful.\n" +
				"_-_ Glyph of entanglement now gives more herbal armor, and root duration decreases at higher armor levels."));

		changes = new ChangeInfo(Messages.get(this, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		infos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.WARRIOR, 0, 90, 12, 15), "Berserker",
				"The previous berserker nerf from 0.6.2 had little effect on his overall winrate, so I'm trying again with a different approach, based around having a permanent penalty for each use of berserk.\n\n" +
				"_-_ Reverted exhaustion nerf from 0.6.2\n\n" +
				"_-_ Decreased lvls to recover rage to 2 from 3\n" +
				"_-_ Berserking now reduces max health by 20%"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_ONYX, null), new RingOfEvasion().trueName(),
				"The ring of evasion has always been a very powerful ring, but the recent freerunner rework has increased the power of evasiveness in general, making the ring overbearingly strong.\n\n" +
				"Evasion synergy has been adjusted:\n" +
				"_-_ Ring of evasion no longer synergizes as strongly with freerunner or armor of swiftness.\n" +
				"_-_ Previously their affects would multiply together, they now add to eachother instead.\n\n" +
				"And the ring itself has been nerf/simplified:\n" +
				"_-_ Ring of evasion no longer grants stealth"));

		//**********************
		//       v0.6.2
		//**********************

		changes = new ChangeInfo("v0.6.2", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes = new ChangeInfo(Messages.get(this, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released October 24th, 2017\n" +
				"_-_ 70 days after Shattered v0.6.1\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(Icons.get(Icons.DEPTH), "Dungeon Secrets!",
				"The secrets of the dungeon have been totally redesigned!\n\n" +
				"_-_ Regular rooms can no longer be totally hidden\n\n" +
				"_-_ 12 new secret rooms added, which are always hidden\n\n" +
				"_-_ Hidden doors are now harder to find automatically\n\n" +
				"_-_ Searching now consumes 6 turns of hunger, up from 2.\n\n" +
				"This is a big adjustment to how secrets work in the dungeon. The goal is to make secrets more interesting, harder to find, and also more optional."));

		changes.addButton(new ChangeButton(new Image(Assets.ROGUE, 0, 15, 12, 15), "Rogue Rework!",
				"The rogue has been reworked! His abilities have received a number of changes to make his strengths more pronounced and focused.\n\n" +
				"These abilities have been _removed:_\n" +
				"_-_ Gains evasion from excess strength on armor\n" +
				"_-_ Gains hunger 20% more slowly\n" +
				"_-_ Identifies rings by wearing them\n" +
				"_-_ Has an increased chance to detect secrets\n\n" +
				"These abilities have been _added:_\n" +
				"_-_ Searches in a wider radius than other heroes\n" +
				"_-_ Is able to find more secrets in the dungeon\n\n" +
				"Make sure to check out the Cloak of Shadows and Dagger changes as well."));

		changes.addButton(new ChangeButton(new Image(Assets.ROGUE, 0, 90, 12, 15), "Rogue Subclasses Rework!",
				"Both of the rogue's subclasses has been reworked, with an emphasis on more powerful abilities that need more interaction from the player.\n\n" +
				"_The Assassin:_\n" +
				"_-_ No longer gains a free +25% damage on surprise attacks\n" +
				"_-_ Now prepares for a deadly strike while invisible, the longer he waits the more powerful the strike becomes.\n" +
				"_-_ Charged attacks have special effects, such as blinking to the target and dealing bonus damage to weakened enemies.\n\n" +
				"_The Freerunner:_\n" +
				"_-_ No longer gains movement speed when not hungry or encumbered\n" +
				"_-_ Now gains 'momentum' as he runs. Momentum increases evasion and movement speed as it builds.\n" +
				"_-_ Momentum is rapidly lost when standing still.\n" +
				"_-_ Evasion gained from momentum scales with excess strength on armor."));

		changes.addButton(new ChangeButton(new Image(Assets.TERRAIN_FEATURES, 16, 0, 16, 16), "Trap Overhaul!",
				"Most of the game's traps have received changes, some have been overhauled entirely!\n\n" +
				"_-_ Removed Spear and Paralytic Gas Traps\n" +
				"_-_ Lightning Trap is now Shocking and Storm traps\n" +
				"_-_ Elemental Traps now all create fields of their element\n" +
				"_-_ Worn and Poison Trap are now Worn and Poison Dart Trap\n" +
				"_-_ Dart traps, Rockfall Trap, and Disintegration Trap are now always visible, but attack at a range\n" +
				"_-_ Warping Trap reworked, no longer sends to previous floors\n" +
				"_-_ Gripping and Flashing Traps now never deactivate, but are less harmful\n\n" +
				"_-_ Tengu now uses Gripping Traps\n\n" +
				"_-_ Significantly reduced instances of items appearing ontop of item-destroying traps"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LOCKED_CHEST, null), "Chest Adjustments",
				"_-_ Crystal chests are now opened by crystal keys.\n\n" +
				"_-_ Golden chests now sometimes appear in the dungeon, containing more valuable items."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.IRON_KEY, null), "New Key Display",
				"The key display has been overhauled!\n\n" +
				"_-_ Each key type now has its own icon, instead of all special keys being shown as golden.\n\n" +
				"_-_ Can now display up to 6 keys, up from 3. After 3 keys the key icons will become smaller.\n\n" +
				"_-_ Button background now dims as keys are collected, for added visual clarity."));


		changes = new ChangeInfo(Messages.get(this, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		infos.add(changes);

		changes.addButton(new ChangeButton(new WandOfCorruption(),
				"The Wand of Corruption has been reworked!\n\n" +
				"_-_ Corruption resistance is now based on enemy exp values, not max HP. Low HP and debuffs still reduce enemy corruption resistance.\n\n" +
				"_-_ Wand now only spends 1 charge per shot, and inflicts debuffs on enemies if it fails to corrupt. Debuffs become stronger the closer the wand got to corrupting.\n\n" +
				"_-_ Corrupted enemies are now considered by the game to be ally characters.\n\n" +
				"_-_ Corrupted enemies award exp immediately as they are corrupted.\n\n" +
				"These changes are aimed at making the wand more powerful, and also less of an all-in wand. Wand of Corruption is now useful even if it doesn't corrupt an enemy."));

		changes.addButton(new ChangeButton(new Image(Assets.STATUE, 0, 0, 12, 15), "AI and Enemy Changes",
				"_-_ Characters now have an internal alignment and choose enemies based on that. Friendly characters should now never attack eachother.\n\n" +
				"_-_ Injured characters will now always have a persistent health bar, even if they aren't being targeted.\n\n" +
				"_-_ Improved enemy emote visuals, they now appear more frequently and there is now one for losing a target.\n\n" +
				"_-_ Enemies now always lose their target after being teleported."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Buff icons can now be tinted, several buffs take advantage of this to better display their state.\n\n" +
				"_-_ Wands that fire magical bolts now push on their detonation area, opening doors and trampling grass.\n\n" +
				"_-_ Crystal chest rooms will now always have a different item type in each chest.\n\n" +
				"_-_ Burning and freezing now destroy held items in a more consistent manner.\n\n" +
				"_-_ Reduced enemies in dark floors to 1.5x, from 2x.\n" +
				"_-_ Increased the brightness of the fog of war.\n" +
				"_-_ Various internal code improvements.\n" +
				"_-_ Added a new interface and graphics for alchemy.\n" +
				"_-_ Zooming is now less stiff at low resolutions.\n" +
				"_-_ Improved VFX when items are picked up.\n" +
				"_-_ Improved older updates in the changes list.\n" +
				"_-_ Game now mutes during phone calls on android 6.0+"));

		changes.addButton(new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed:\n" +
				"_-_ Various crash bugs\n" +
				"_-_ Various exploits players could use to determine map shape\n" +
				"_-_ Artifacts sometimes removed from quickslot when equipped\n" +
				"_-_ Items removed from quickslots when containers are bought\n" +
				"_-_ Swapping misc items not working with a full inventory\n" +
				"_-_ Enemies sometimes not waking from sleep\n" +
				"_-_ Swarms not duplicating over hazards\n" +
				"_-_ Black bars on certain modern phones\n" +
				"_-_ Action button not persisting between floors\n" +
				"_-_ Various bugs with multiplicity curse\n" +
				"_-_ Various minor visual bugs\n" +
				"_-_ Plants not updating terrain correctly\n" +
				"_-_ Enemies spawning ontop of exit stairs\n" +
				"_-_ Evil Eyes sometimes skipping beam chargeup\n" +
				"_-_ Warrior's seal being disabled by armor kit\n" +
				"_-_ Gladiator being able to combo non-visible enemies\n" +
				"_-_ Music volume being ignored in certain cases\n" +
				"_-_ Game music not correctly pausing on android 2.2/2.3\n" +
				"_-_ Game failing to save in rare cases"));

		changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), Messages.get(this, "language"),
				"In English:\n" +
				"_-_ Improved some common game log entries\n" +
				"_-_ Fixed a typo when enemies die out of view\n" +
				"_-_ Fixed a typo in the ghost hero's introduction\n" +
				"_-_ Fixed a typo in dirk description\n" +
				"_-_ Fixed various text errors with venom\n" +
				"\n" +
				"_-_ Translation Updates\n" +
				"_-_ Various Translation Updates\n" +
				"_-_ Added new language: _Turkish_\n" +
				"_-_ New Language: _Czech_"));

		changes = new ChangeInfo(Messages.get(this, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		infos.add(changes);

		changes.addButton(new ChangeButton(new CloakOfShadows(),
				"As part of the rogue rework, the cloak of shadows has been significantly buffed:\n\n" +
				"_-_ Max charges have been halved, however each charge is roughly twice as useful.\n" +
				"_-_ As there are half as many charges total, charge rate is effectively increased.\n" +
				"_-_ Cooldown mechanic removed, cloak can now be used multiple times in a row.\n" +
				"_-_ Cloak levelling progression changed, it is now much more dependant on hero level\n\n" +
				"These changes should let the rogue go invisible more often, and with more flexibility."));

		changes.addButton(new ChangeButton(new Dagger(),
				"As part of the rogue rework, sneak attack weapons have been buffed:\n\n" +
				"_-_ Dagger sneak attack minimum damage increased to 75% from 50%.\n" +
				"_-_ Dirk sneak attack minimum damage increased to 67% from 50%\n" +
				"_-_ Assassin's blade sneak attack minimum damage unchanged at 50%\n\n" +
				"This change should hopefully give the rogue some needed earlygame help, and give him a more clear choice as to what item he should upgrade, if no items were found in the dungeon."));

		changes.addButton(new ChangeButton(new Flail(),
				"The flail's downsides were too harsh, so one of them has been changed both to make its weaknesses more centralized and to hopefully increase its power.\n\n" +
				"_-_ Flail no longer attacks at 0.8x speed, instead it has 20% reduced accuracy."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_GOLDEN, null), "Potion Adjustments",
				"Potion of Purification buffed:\n" +
				"_-_ Drinking effect now lasts for 20 turns, up from 15.\n" +
				"_-_ Drinking now provides immunity to all area-bound effects, not just gasses.\n\n" +
				"Potion of Frost buffed:\n" +
				"_-_ Now creates a freezing field which lasts for several turns."));

		changes = new ChangeInfo(Messages.get(this, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		infos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.WARRIOR, 0, 90, 12, 15), "Berserker",
				"The Berserker's survivability and power have been reduced to help bring him into line with the other subclasses:\n\n" +
				"_-_ Bonus damage from low health reduced significantly when below 50% HP. 2x damage while berserking is unchanged.\n\n" +
				"_-_ Turns of exhaustion after berserking increased to 60 from 40. Damage reduction from exhaustion stays higher for longer."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.REMAINS, null), "Heroes Remains",
				"_-_ Remains can no longer contain progression items, such as potions of strength or scrolls of upgrade.\n\n" +
				"_-_ All upgradeable items dropped by remains are now capped at +3 (+0 for artifacts)\n\n" +
				"The intention for remains is so a previously failed run can give a nice surprise and tiny boost to the next one, but these items are both too strong and too easy to abuse.\n\n" +
				"In compensation, it is now much less likely to receive gold from remains, unless that character died with very few items."));

		//**********************
		//       v0.6.1
		//**********************

		changes = new ChangeInfo("v0.6.1", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes = new ChangeInfo(Messages.get(this, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released August 15th, 2017\n" +
				"_-_ 72 days after Shattered v0.6.0\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GUIDE_PAGE, null), "Journal Additions",
				"_-_ Overhauled the Journal window with loads of new functionality\n\n" +
				"_-_ Added a completely overhauled tutorial experience, which replaces the existing signpost system.\n\n" +
				"_-_ Massively expanded the items catalog, now contains every identifiable item in the game."));
		changes.addButton(new ChangeButton(BadgeBanner.image(Badges.Badge.ALL_ITEMS_IDENTIFIED.image), "Badge Changes",
				"_-_ Added new badges for identifying all weapons, armor, wands, and artifacts.\n\n" +
				"_-_ All identification-based badges are now tied to the new item list system, and progress for them will persist between runs.\n\n" +
				"_-_ Removed the Night Hunter badge\n\n" +
				"_-_ The 'Many Deaths' badge now covers all death related badges, previously it was not covering 2 of them.\n\n" +
				"_-_ Reduced the numbers of games needed for the 'games played' badges from 10/100/500/2000 to 10/50/250/1000\n\n" +
				"_-_ Blank badges shown in the badges menu are now accurate to how many badges you have left to unlock."));
		changes.addButton(new ChangeButton(Icons.get(Icons.DEPTH), "Dungeon Changes",
				"_-_ Added 5 new regional rooms\n" +
				"_-_ Added two new uncommon room types\n" +
				"_-_ Added a new type of tunnel room\n\n" +
				"_-_ Level layouts now more likely to be dense and interconnected.\n\n" +
				"_-_ Tunnels will now appear more consistently.\n\n" +
				"_-_ Ascending stairs, descending stairs, and mining no longer increase hunger."));
		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_TOPAZ, null), new RingOfEnergy().trueName(),
				"_-_ Added the ring of energy."));
		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHEST, null), "Sprites",
				"New sprites for the following:\n" +
				"_-_ Chests & Mimics\n" +
				"_-_ Darts\n" +
				"_-_ Javelins\n" +
				"_-_ Tomahawks"));

		changes = new ChangeInfo(Messages.get(this, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		infos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_DIAMOND, null), "Ring Mechanics Changes",
				"Rings now handle upgrades and curses more similarly to other items:\n\n" +
				"_-_ Rings are now found at +0, down from +1, but are more powerful to compensate.\n\n" +
				"_-_ Curses no longer affect ring upgrades, it is now impossible to find negatively upgraded rings.\n\n" +
				"_-_ Cursed rings are now always harmful regardless of their level, until the curse is cleansed.\n\n" +
				"_-_ Scrolls of upgrade have a chance to remove curses on a ring, scrolls of remove curse will always remove the curse."));
		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_AMETHYST, null), new RingOfWealth().trueName(),
				"The ring of wealth is getting a change in emphasis, moving away from affecting items generally, and instead affecting item drops more strongly.\n\n" +
				"_-_ No longer grants any benefit to item spawns when levels are generated.\n\n" +
				"_-_ Now has a chance to generate extra loot when defeating enemies.\n\n" +
				"I'm planning to make further tweaks to this item in future updates."));
		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_CRIMSON, null), new PotionOfHealing().trueName(),
				"Health Potions are getting a changeup to make hoarding and chugging them less effective, and to encourage a bit more strategy than to just drink them on the verge of death.\n\n" +
				"_-_ Health potions now heal in a burst that fades over time, rather than instantly.\n\n" +
				"_-_ Health potions now heal more than max HP at low levels, and slightly less than max HP at high levels.\n\n" +
				"Make sure to read the dew vial changes as well."));
		changes.addButton(new ChangeButton(new DewVial(),
				"The dew vial (and dew) are having their healing abilities enhanced to improve the availability of healing in the sewers, and to help offset the health potion changes.\n\n" +
				"_-_ Dew drops now heal 5% of max HP\n\n" +
				"_-_ Dew vial now always spawns on floor 1\n\n" +
				"_-_ The dew vial is now full at 20 drops, drinking heals 5% per drop and is instantaneous.\n\n" +
				"_-_ Dew will always be collected into an available vial, even if the hero is below full HP.\n\n" +
				"_-_ When drinking from the vial, the hero will now only drink as many drops as they need to reach full HP."));
		changes.addButton(new ChangeButton(new Image(Assets.STATUE, 0, 0, 12, 15), "AI Changes",
				"_-_ Improvements to pathfinding. Characters are now more prone to take efficient paths to their targets, and will prefer to wait instead of taking a very inefficient path.\n\n" +
				"_-_ Characters will now more consistently decide who to attack based on distance and who they are being attacked by."));
		changes.addButton(new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed:\n" +
				"_-_ Issues with Android 7.0+ multi-window\n" +
				"_-_ Rare stability issues on certain devices\n" +
				"_-_ Numerous rare crash and freeze bugs\n" +
				"_-_ Chasm death not showing in rankings\n" +
				"_-_ Resting icon sometimes not appearing\n" +
				"_-_ Various minor graphical bugs\n" +
				"_-_ The ambitious imp rarely blocking paths\n" +
				"_-_ Berserk prematurely ending after loading\n" +
				"_-_ Mind vision not updating while waiting\n" +
				"_-_ Troll blacksmith destroying broken seal\n" +
				"_-_ Wands always being uncursed by upgrades\n" +
				"_-_ Various bugs with Evil Eyes\n" +
				"_-_ Thieves being able to escape while visible\n" +
				"_-_ Enemies not visually dying in rare cases\n" +
				"_-_ Visuals flickering while zooming on low resolution devices.\n" +
				"_-_ Various minor bugs with the buff indicator\n" +
				"_-_ Sleep-immune enemies being affected by magical sleep\n" +
				"_-_ Sad Ghost being affected by corruption\n" +
				"_-_ Switching places with the Sad Ghost over chasms causing the hero to fall"));
		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Completely overhauled the changes scene (which you're currently reading!)\n" +
				"_-_ Item and enemy spawn RNG is now more consistent. Should prevent things like finding 4 crabs on floor 3.\n" +
				"_-_ The compass marker now points toward entrances after the amulet has been acquired.\n" +
				"_-_ Improved quickslot behaviour when items are removed by monks or thieves.\n" +
				"_-_ Allies are now better able to follow you through bosses.\n" +
				"_-_ Lloyd's Beacon's return function is no longer blocked by none-hostile creatures.\n" +
				"_-_ Performance tweaks on devices with 2+ cpu cores\n" +
				"_-_ Stepping on plants now interrupts the hero\n" +
				"_-_ Improved potion and scroll inventory icons\n" +
				"_-_ Increased landscape width of some windows\n" +
				"_-_ Un-IDed artifacts no longer display charge"));
		changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), Messages.get(this, "language"),
				"Fixed in English:\n" +
				"_-_ Missing capitalization in Prison Guard text\n" +
				"_-_ Typo when trying a locked chest with no key\n" +
				"_-_ Missing period in alarm trap description\n\n" +
				"_-_ Added new Language: _Catalan_\n\n" +
				"_-_ Various translation updates"));

		changes = new ChangeInfo(Messages.get(this, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		infos.add(changes);

		changes.addButton(new ChangeButton(new UnstableSpellbook(),
				"The Unstable spellbook wasn't really worth upgrading, so it's getting some new effects to make it worth investing in!\n\n" +
				"_-_ Infusing a scroll into the unstable spellbook will now grant a unique empowered effect whenever that scroll's spell is cast from the book.\n\n" +
				"To compensate, charge mechanics have been adjusted:\n\n" +
				"_-_ Max charges reduced from 3-8 to 2-6\n\n" +
				"_-_ Recharge speed has been reduced slightly"));
		changes.addButton(new ChangeButton(new DriedRose().upgrade(10),
				"The ghost hero summoned by the rose is now much more capable and is also much less temporary:\n\n" +
				"_-_ Ghost can now be equipped with a weapon and armor and uses them just like the hero.\n" +
				"_-_ Ghost no longer takes damage over time as long as the rose remains equipped.\n" +
				"_-_ Ghost health increased by 10\n" +
				"_-_ Ghost now has a persistent HP bar\n" +
				"_-_ Ghost can now follow you between floors\n\n" +
				"However:\n\n" +
				"_-_ Ghost no longer gains damage and defense from rose levels, instead gains strength to use better equipment.\n" +
				"_-_ Rose no longer recharges while ghost is summoned\n" +
				"_-_ Rose takes 25% longer to fully charge"));
		changes.addButton(new ChangeButton(Icons.get(Icons.BACKPACK), "Inventory",
				"_-_ Inventory space increased from 19 slots to 20 slots.\n\n" +
				"_-_ Gold indicator has been moved to the top-right of the inventory window to make room for the extra slot."));

		changes = new ChangeInfo(Messages.get(this, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		infos.add(changes);

		changes.addButton(new ChangeButton(new HornOfPlenty(),
				"The Horn of Plenty was providing a bit too much value in the earlygame, especially without upgrades:\n\n" +
				"_-_ Charge Speed reduced, primarily at lower levels:\n-20% at +0\n-7.5% at +10\n\n" +
				"_-_ Upgrade rate adjusted, Food now contributes towards upgrades exactly in line with how much hunger it restores. This means smaller food items will contribute more, larger ones will contribute less. Rations still grant exactly 1 upgrade each."));
		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_GARNET, null), new RingOfMight().trueName(),
				"The Ring of Might's strength bonus is already extremely valuable, having it also provide an excellent health boost was simply too much:\n\n" +
				"_-_ Health granted reduced from +5 per upgrade to +3.5% of max hp per upgrade.\n\n" +
				"This is a massive reduction to its earlygame health boosting power, however as the player levels up this will improve. By hero level 26 it will be as strong as before this change."));
		changes.addButton(new ChangeButton(new EtherealChains(),
				"The ability for Ethereal Chains to pull you literally anywhere limits design possibilities for future updates, so I've added a limitation.\n\n" +
				"_-_ Ethereal chains now cannot reach locations the player cannot reach by walking or flying. e.g. the chains can no longer reach into a locked room.\n\n" +
				"_-_ Ethereal chains can now reach through walls on boss floors, but the above limitation still applies."));

		//**********************
		//       v0.6.0
		//**********************

		changes = new ChangeInfo("v0.6.0", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released June 4th, 2017\n" +
				"_-_ 116 days after Shattered v0.5.0\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(Icons.get(Icons.DEPTH), "Levelgen Overhaul!",
				"Level creation algorithm overhauled!\n\n" +
				"_-_ Levels are now much less box-shaped\n" +
				"_-_ Sewers are now smaller, caves+ are now larger\n" +
				"_-_ Some rooms can now be much larger than before\n" +
				"_-_ Added 8 new standard room types, and loads of new standard room layouts\n\n" +
				"_-_ Reduced number of traps in later chapters"));

		changes.addButton(new ChangeButton(new ItemSprite(new Torch()), "Light Source Buffs",
				"_-_ Light sources now grant significantly more vision\n" +
				"_-_ Light from torches now lasts 20% longer\n" +
				"_-_ Slightly increased visibility on floor 22+\n" +
				"_-_ Floor 21 shop now sells 3 torches, up from 2"));

		changes.addButton(new ChangeButton(new ItemSprite(new Food()), "Food Buffs",
				"_-_ Meat and small rations are 50% more filling\n" +
				"_-_ Pasties and blandfruit are 12.5% more filling"));

		changes.addButton(new ChangeButton(new ItemSprite(new Greataxe()), "Tier-5 Weapon Buffs",
				"_-_ Greataxe base damage increased by ~22%\n" +
				"_-_ Greatshield base damage increased by ~17%"));

		changes.addButton(new ChangeButton(new ItemSprite(new StoneOfPreservation()), "Enchant and Glyph Balance Changes",
				"_-_ Vampiric enchant lifesteal reduced by 20%\n\n" +
				"Lucky enchant rebalanced:\n" +
				"_-_ now deals 2x/0x damage, instead of min/max\n" +
				"_-_ base chance to deal 2x increased by ~10%\n\n" +
				"Glyph of Viscosity rebalanced:\n" +
				"_-_ proc chance reduced by ~25% \n" +
				"_-_ damage over time reverted from 15% to 10%\n\n" +
				"_-_ Glyph of Entanglement root time reduced by 40%\n\n" +
				"Glyph of Potential rebalanced:\n" +
				"_-_ self-damage no longer scales with max hp\n" +
				"_-_ grants more charge at higher levels"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Visiting floor 21 before completing the imp quest no longer prevents his shop from spawning\n\n" +
				"_-_ Floor 2 entry doors are now only hidden for new players\n\n" +
				"_-_ Falling damage tweaked to be less random\n\n" +
				"_-_ Resume indicator now appears in more cases"));

		//**********************
		//       v0.5.0
		//**********************

		changes = new ChangeInfo("v0.5.0", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released February 8th, 2017\n" +
				"_-_ 233 days after Shattered v0.4.0\n" +
				"_-_ 115 days after Shattered v0.4.3\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(Icons.get(Icons.DEPTH), "New Dungeon Visual Style!",
				"_-_ Walls and some terrain now have depth\n" +
				"_-_ Characters & items are raised & cast shadows\n" +
				"_-_ Added a visible tile grid in the settings menu"));

		changes.addButton(new ChangeButton(new ItemSprite(new Quarterstaff()), "Equipment Balance Changes",
				"_-_ Quarterstaff armor bonus increased from 2 to 3\n\n" +
				"_-_ Wand of Frost damage against chilled enemies reduced from -7.5% per turn of chill to -10%\n\n" +
				"_-_ Wand of Transfusion self-damage reduced from 15% max hp to 10% max hp per zap\n\n" +
				"_-_ Dried Rose charges 20% faster and the ghost hero is stronger, especially at low levels"));

		changes.addButton(new ChangeButton(new ItemSprite(new Stylus()), "Glyph Balance Changes",
				"_-_ Glyph of Entanglement activates less often but grants significantly more herbal armor\n\n" +
				"_-_ Glyph of Stone armor bonus reduced from 2+level to 0+level\n\n" +
				"_-_ Glyph of Antimagic magical damage resist reduced from 50% of armor to 33% of armor\n\n" +
				"_-_ Glyph of Viscosity damage rate increased from 10% of deferred damage to 15%"));

		changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), Messages.get(this, "language"),
				"_-_ Added new Language: Esperanto\n" +
				"_-_ Added new Language: Indonesian\n"));

		//**********************
		//       v0.4.X
		//**********************

		changes = new ChangeInfo("v0.4.X", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes = new ChangeInfo("v0.4.3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released October 16th, 2016\n" +
				"_-_ 37 days after Shattered v0.4.2\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), "Technical Improvements",
				"_-_ Added rankings and hall of heroes sync via Google Play Games, for the Google Play version of Shattered.\n\n" +
				"_-_ Added Power Saver mode in settings\n" +
				"_-_ Download size reduced by ~25%\n" +
				"_-_ Game now supports small screen devices\n" +
				"_-_ Performance improvements\n" +
				"_-_ Improved variety of level visuals"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FLAIL, null), "Balance Changes",
				"_-_ Flail max damage increased by ~15%\n" +
				"_-_ Wand of Frost damage reduction increased from 5% per turn of chill to 7.5%\n" +
				"_-_ Ring of Furor speed bonus reduced by ~15% for slow weapons, ~0% for fast weapons\n" +
				"_-_ Reduced sacrificial curse bleed by ~33%\n" +
				"_-_ Reworked glyph of brimstone, now grants shielding instead of healing\n" +
				"_-_ Reworked glyph of stone, now reduces speed in doorways\n" +
				"_-_ Thrown potions now trigger traps and plants"));

		changes = new ChangeInfo("v0.4.2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released September 9th, 2016\n" +
				"_-_ 46 days after Shattered v0.4.1\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), "Technical Improvements",
				"_-_ Many general performance improvements\n" +
				"_-_ Game now uses 2 CPU cores, up from 1\n" +
				"_-_ Reduced hitching on many devices\n" +
				"_-_ Framerate improvements for older devices\n" +
				"_-_ Game size reduced by ~10%"));

		changes.addButton(new ChangeButton(new ItemSprite(new Glaive()), "Balance Changes",
				"_-_ Spear and Glaive damage reduced\n" +
				"_-_ Runic blade damage reduced\n" +
				"_-_ Grim enchant now procs more often\n" +
				"_-_ Glyph of stone adds more weight\n" +
				"_-_ Glyph of potential procs less often\n" +
				"_-_ Wand of Fireblast less dangerous to caster\n" +
				"_-_ Wand of Pris. Light reveal area reduced\n" +
				"_-_ Ring of Wealth slightly more effective\n" +
				"_-_ Ring of Sharpshooting gives more accuracy"));

		changes = new ChangeInfo("v0.4.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released July 25th, 2016\n" +
				"_-_ 35 days after Shattered v0.4.0\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(new ItemSprite(new PlateArmor()), "Item Changes pt.1",
				"Armor and Enemy Balance Changes:\n" +
				"_-_ Armor now has a min damage block value\n" +
				"_-_ Armor gains more blocking from upgrades\n" +
				"_-_ Prison+ enemy damage increased\n" +
				"_-_ Evil Eyes reworked\n" +
				"_-_ Brimstone glyph healing reduced\n" +
				"\n" +
				"Class Balance Changes:\n" +
				"_-_ Mage's Staff melee damage increased\n" +
				"_-_ Mage's Staff can now preserve one upgrade\n" +
				"_-_ Cloak of Shadows buffed at lower levels\n" +
				"_-_ Some Battlemage effects changed\n" +
				"\n" +
				"Wand Balance Changes:\n" +
				"_-_ All wands damage adjusted/increased\n" +
				"_-_ Upgraded wands appear slightly less often\n" +
				"_-_ Wand of Lightning bonus damage reduced\n" +
				"_-_ Wand of Fireblast uses fewer charges\n" +
				"_-_ Wand of Venom damage increases over time\n" +
				"_-_ Wand of Prismatic Light bonus damage reduced\n" +
				"_-_ Corrupted enemies live longer & no longer attack eachother\n" +
				"_-_ Wands in the holster now charge faster"));

		changes.addButton(new ChangeButton(new ItemSprite(new RunicBlade()), "Item Changes pt.2",
				"Ring Balance Changes:\n" +
				"_-_ Ring of Force weaker at 18+ strength, stronger otherwise\n" +
				"_-_ Ring of Tenacity reduces more damage\n" +
				"_-_ Ring of Wealth secret rewards adjusted\n" +
				"_-_ Ring of Evasion now works consistently\n" +
				"\n" +
				"Artifact Balance Changes:\n" +
				"_-_ Dried Rose charges faster, ghost HP up\n" +
				"_-_ Horn of Plenty now charges on exp gain\n" +
				"_-_ Master Thieves Armband levels faster\n" +
				"_-_ Sandals of Nature level faster\n" +
				"_-_ Hourglass uses fewer charges at a time\n" +
				"_-_ Horn of Plenty adjusted, now stronger\n" +
				"\n" +
				"Weapon Balance Changes:\n" +
				"_-_ Lucky enchant deals max dmg more often\n" +
				"_-_ Dazzling enchant now cripples & blinds\n" +
				"_-_ Flail now can't surprise attack, damage increased\n" +
				"_-_ Extra reach weapons no longer penetrate\n" +
				"_-_ Runic blade damage decreased"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Added a new journal button with key display\n" +
				"_-_ Keys now exist in the journal, not inventory\n" +
				"_-_ Improved donator menu button visuals\n" +
				"_-_ Increased the efficiency of data storage\n" +
				"_-_ Chasms now deal less damage, but bleed\n" +
				"_-_ Many shop prices adjusted\n" +
				"_-_ Pirahna rooms no longer give cursed gear"));

		changes = new ChangeInfo("v0.4.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released June 20th, 2016\n" +
				"_-_ 391 days after Shattered v0.3.0\n" +
				"_-_ 50 days after Shattered v0.3.5\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(new ItemSprite(new Longsword()), "Equipment Overhaul!",
				"_-_ 13 new weapons, 12 rebalanced weapons\n" +
				"\n" +
				"Equipment Balance:\n" +
				"_-_ Tier 2-4 weapons do more base damage\n" +
				"_-_ All weapons gain more dmg from upgrades\n" +
				"_-_ Upgrades now remove enchants less often\n" +
				"_-_ Upgrades reduce str requirements less\n" +
				"_-_ All armors require 1 more str\n" +
				"_-_ Encumbered characters can't sneak attack\n" +
				"\n" +
				"Droprate Changes:\n" +
				"_-_ Powerful equipment less common early\n" +
				"_-_ +3 and +2 equipment less common\n" +
				"_-_ Equipment curses more common\n" +
				"_-_ Tier 1 equipment no longer drops\n" +
				"_-_ Arcane styli slightly more common\n" +
				"_-_ Better item drops on floors 22-24"));

		changes.addButton(new ChangeButton(new ItemSprite(new Stylus()), "Curse, Enchant, & Glyph Overhaul!",
				"_-_ 3 new enchants, 10 rebalanced enchants\n" +
				"_-_ 8 new glyphs, 5 rebalanced glyphs\n" +
				"_-_ 12 new curse effects\n" +
				"\n" +
				"Equipment Curses:\n" +
				"_-_ Curses now give negative effects\n" +
				"_-_ Curses no longer give negative levels\n" +
				"_-_ Upgrades now weaken curses\n" +
				"_-_ Remove curse scrolls now affect 1 item"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"Class Balance:\n" +
				"_-_ Huntress now starts with knuckleduster\n" +
				"_-_ Assassin sneak bonus damage reduced\n" +
				"_-_ Fixed a bug where berserker was immune when enraged\n" +
				"_-_ Gladiator's clobber now inflicts vertigo, not stun\n" +
				"\n" +
				"Enemy Balance:\n" +
				"_-_ Tengu damage increased\n" +
				"_-_ Prison Guard health and DR increased\n" +
				"\n" +
				"Misc:\n" +
				"_-_ Scrolls of upgrade no longer burn\n" +
				"_-_ Potions of strength no longer freeze\n" +
				"_-_ Translation updates\n" +
				"_-_ Various bugfixes"));

		//**********************
		//       v0.3.X
		//**********************

		changes = new ChangeInfo("v0.3.X", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes = new ChangeInfo("v0.3.5", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released May 1st, 2016\n" +
				"_-_ 81 days after Shattered v0.3.4\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(new Image(Assets.WARRIOR, 0, 15, 12, 15), "Warrior Rework!",
				"Warrior Rework:\n" +
				"_-_ Starting STR down to 10, from 11\n" +
				"_-_ Short sword dmg down to 1-10, from 1-12\n" +
				"_-_ Short sword can no longer be reforged\n" +
				"_-_ Now IDs potions of health, not STR\n" +
				"_-_ Now starts with a unique seal for armor\n" +
				"_-_ Seal grants shielding ontop of health\n" +
				"_-_ Seal allows for one upgrade transfer"));

		changes.addButton(new ChangeButton(new Image(Assets.WARRIOR, 0, 90, 12, 15), "Warrior Subclass Rework!",
				"Berserker Rework:\n" +
				"_-_ Bonus damage now scales with lost HP, instead of a flat 50% at 50% hp\n" +
				"_-_ Berserker can now endure through death for a short time, with caveats\n" +
				"\n" +
				"Gladiator Rework:\n" +
				"_-_ Combo no longer grants bonus damage\n" +
				"_-_ Combo is now easier to stack\n" +
				"_-_ Combo now unlocks special finisher moves"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"Balance Tweaks:\n" +
				"_-_ Spears can now reach enemies 1 tile away\n" +
				"_-_ Wand of Blast Wave now pushes bosses less\n" +
				"\n" +
				"Misc:\n" +
				"_-_ Can now examine multiple things in one tile\n" +
				"_-_ Pixelated font now available for cyrillic languages\n" +
				"_-_ Added Hungarian language"));

		changes = new ChangeInfo("v0.3.4", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released February 10th, 2016\n" +
				"_-_ 54 days after Shattered v0.3.3\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), "Translations!",
				"Shattered Pixel Dungeon now supports multiple languages, thanks to a new community translation project!\n\n" +
				"The Following languages are supported at release:\n" +
				"_-_ English\n" +
				"_-_ Russian\n" +
				"_-_ Korean\n" +
				"_-_ Chinese\n" +
				"_-_ Portugese\n" +
				"_-_ German\n" +
				"_-_ French\n" +
				"_-_ Italian\n" +
				"_-_ Polish\n" +
				"_-_ Spanish"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"Completely redesigned the text rendering system to support none-english characters\n\n" +
				"New text system renders using either the default system font, or the original pixelated game font. None-latin languages must use system font.\n\n" +
				"Balance Changes:\n" +
				"_-_ Hunger now builds ~10% slower\n" +
				"_-_ Sad Ghost no longer gives tier 1 loot\n" +
				"_-_ Sad Ghost gives tier 4/5 loot less often\n" +
				"_-_ Burning now deals less damage at low HP\n" +
				"_-_ Weakness no longer discharges wands\n" +
				"_-_ Rockfall traps rebalanced"));

		changes = new ChangeInfo("v0.3.3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released December 18th, 2015\n" +
				"_-_ 44 days after Shattered v0.3.2\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), "Google Play Games",
				"Added support for Google Play Games in the Google Play version:\n\n" +
				"- Badges can now sync across devices\n" +
				"- Five Google Play Achievements added\n" +
				"- Rankings sync will come in future\n\n" +
				"Shattered remains a 100% offline game if Google Play Games is not enabled"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"Gameplay Changes:\n" +
				"- Tengu's maze is now different each time\n" +
				"- Items no longer auto-pickup when enemies are near\n" +
				"\n" +
				"Fixes:\n" +
				"- Fixed several bugs with prison enemies\n" +
				"- Fixed some landscape window size issues\n" +
				"- Fixed other minor bugs\n" +
				"\n" +
				"Misc:\n" +
				"- Added support for reverse landscape\n" +
				"- Added a small holiday treat ;)\n" +
				"- Thieves now disappear when they get away"));

		changes = new ChangeInfo("v0.3.2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released November 4th, 2015\n" +
				"_-_ 79 days after Shattered v0.3.1\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(new Image(Assets.TENGU, 0, 0, 14, 16), "Prison Rework",
				"_-_ Tengu boss fight completely redone\n" +
				"_-_ Corpse dust quest overhauled\n" +
				"_-_ Rotberry quest overhauled\n" +
				"_-_ NEW elemental embers quest\n" +
				"_-_ NEW prison mob: insane prison guards!\n" +
				"_-_ Thieves can escape with a stolen item\n" +
				"_-_ Gnoll shaman attack speed increased"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MASTERY, null), "Balance Changes",
				"_-_ Mastery Book now always at floor 10, even when unlocked\n" +
				"_-_ Hunger damage now increases with hero level, starts lower\n" +
				"\n" +
				"Sewers rebalance: \n" +
				"_-_ Marsupial rat dmg and evasion reduced\n" +
				"_-_ Gnoll scout accuracy reduced\n" +
				"_-_ Sewer crabs less likely to spawn on floor 3, grant more exp\n" +
				"_-_ Fly swarms rebalanced, moved to the sewers\n" +
				"_-_ Great Crab HP reduced \n" +
				"_-_ Goo fight rebalanced \n" +
				" \n" +
				"Base Class Changes: \n" +
				"_-_ Mage's staff base damage increased \n" +
				"_-_ Huntress now starts with 20 hp \n" +
				"_-_ Huntress no longer heals more from dew \n" +
				"_-_ Rogue's cloak of shadows now drains less while invisible\n" +
				" \n" +
				"Subclass Changes: \n" +
				"_-_ Berserker now starts raging at 50% hp (up from 40%) \n" +
				"_-_ Warden now heals 2 extra HP from dew \n" +
				"_-_ Warlock completely overhauled"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Visual improvements from 1.9.1 source\n" +
				"_-_ Improved golden UI for donators\n" +
				"_-_ Fixed 'white line' graphical artifacts\n" +
				"_-_ Floor locking now pauses all passive effects, not just hunger\n" +
				"_-_ Cursed chains now only cripple, do not root\n" +
				"_-_ Warping trap rebalanced, much less harsh\n" +
				"_-_ Various other bugfixes"));

		changes = new ChangeInfo("v0.3.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released August 17th, 2015\n" +
				"_-_ 83 days after Shattered v0.3.0\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(new Image(Assets.TERRAIN_FEATURES, 112, 96, 16, 16), "Trap Overhaul",
				"_-_ Over 20 new traps + tweaks to existing ones\n" +
				"_-_ Trap visuals overhauled\n" +
				"_-_ Traps now get trickier deeper in the dungeon\n" +
				"_-_ Trap room reworked to make use of new traps"));

		changes.addButton(new ChangeButton(new Image(Assets.MENU, 15, 0, 16, 15), "Interface Improvements",
				"_-_ Adjusted display scaling\n" +
				"_-_ Search and Examine merged into one button (double tap to search)\n" +
				"_-_ New max of 4 Quickslots!\n" +
				"_-_ Multiple toolbar modes for large display and landscape users\n" +
				"_-_ Ability to flip toolbar and indicators (left-handed mode)\n" +
				"_-_ Better settings menu\n" +
				"_-_ Graphics settings are now accessible ingame\n" +
				"_-_ More consistent text rendering\n" +
				"_-_ Recent changes can now be viewed from the title screen\n" +
				"_-_ Added a health bar for bosses"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"Balance changes:\n" +
				"_-_ Ethereal chains now gain less charge the more charges they have\n" +
				"_-_ Staff of regrowth grants more herbal healing\n" +
				"_-_ Monks now disarm less randomly, but not less frequently\n" +
				"_-_ Invisibility potion effect now lasts for 20 turns, up from 15\n\n" +
				"QOL improvements:\n" +
				"_-_ Quickslots now autotarget enemies\n" +
				"_-_ Resting now works while hungry & at max HP\n" +
				"_-_ Dew drops no longer collect when at full health with no dew vial\n" +
				"_-_ Items now stay visible in the fog of war\n" +
				"_-_ Added a visual hint to weak floor rooms\n" +
				"_-_ Many bugfixes"));

		changes = new ChangeInfo("v0.3.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released May 26th, 2015\n" +
				"_-_ 253 days after Shattered v0.2.0\n" +
				"_-_ 92 days after Shattered v0.2.4\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(new Image(Assets.MAGE, 0, 15, 12, 15), "Mage Rework!",
				"_-_ No longer starts with knuckledusters or a wand\n" +
				"_-_ Can no longer equip wands\n" +
				"_-_ Now starts with a unique mages staff, empowered with magic missile to start.\n\n" +
				"_-_ Battlemage reworked, staff now deals bonus effects when used as a melee weapon.\n\n" +
				"_-_ Warlock reworked, gains more health and fullness from gaining exp, but food no longer restores hunger."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_DISINTEGRATION, null), "Wand Rework!",
				"Removed Wands:\n" +
				"Flock, Blink, Teleportation, Avalanche\n" +
				"\n" +
				"Reworked Wands:\n" +
				"Magic Missile, Lightning, Disintegration,\n" +
				"Fireblast (was Firebolt), Venom (was Poison),\n" +
				"Frost (was Slowing), Corruption (was Amok),\n" +
				"Blast Wave (was Telekinesis), Regrowth\n" +
				"\n" +
				"New Wands:\n" +
				"Prismatic Light, Transfusion\n" +
				"\n" +
				"_-_ Wand types are now known by default.\n" +
				"_-_ Wands now each have unique sprites.\n" +
				"_-_ Wands now cap at 10 charges instead of 9\n" +
				"_-_ Wands now recharge faster the more charges are missing.\n" +
				"_-_ Self-targeting with wands is no longer possible.\n" +
				"_-_ Wand recharge effects now give charge over time.\n" +
				"_-_ Wands can now be cursed!"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"New Artifacts:\n" +
				"_-_ Ethereal Chains (replaces wand of blink)\n" +
				"_-_ Lloyd's Beacon (replaces wand of teleportation)\n" +
				"\n" +
				"Misc. Balance changes:\n" +
				"_-_ Blessed Ankhs now revive at 1/4hp, but also grant initiative.\n" +
				"_-_ Alchemist's Toolkit removed (will be reworked)\n" +
				"_-_ Chalice of blood nerfed, now regens less hp at high levels.\n" +
				"_-_ Cape of Thorns buffed, now absorbs all damage, but only deflects adjacent attacks.\n" +
				"_-_ Sandals of nature adjusted, now give more seeds, less dew.\n" +
				"_-_ Hunger no longer increases while fighting bosses.\n" +
				"_-_ Floor 1 now spawns 10 rats, exactly enough to level up.\n" +
				"_-_ Scrolls of recharging and mirror image now more common.\n" +
				"_-_ Mimics are now less common, stronger, & give better loot.\n" +
				"\n" +
				"UI tweaks:\n" +
				"_-_ New app icon!\n" +
				"_-_ Shading added to main game interface\n" +
				"_-_ Buffs now have descriptions, tap their icons!\n" +
				"_-_ Visual indicator added for surprising enemies"));

		//**********************
		//       v0.2.X
		//**********************

		changes = new ChangeInfo("v0.2.X", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes = new ChangeInfo("v0.2.4", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released February 23rd, 2015\n" +
				"_-_ 48 days after Shattered v0.2.3\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(new ItemSprite(new Honeypot()), "Pixel Dungeon v1.7.5",
				"v1.7.3 - v1.7.5 Source Implemented, with exceptions:\n" +
				"_-_ Degredation not implemented.\n\n" +
				"_-_ Badge syncing not implemented.\n\n" +
				"_-_ Scroll of Weapon Upgrade renamed to Magical Infusion, works on armor.\n\n" +
				"_-_ Scroll of Enchantment not implemented, Arcane stylus has not been removed.\n\n" +
				"_-_ Honey pots now shatter in a new item: shattered honeypot. A bee will defend its shattered pot to the death against anything that gets near.\n\n" +
				"_-_ Bombs have been reworked/nerfed: they explode after a delay, no longer stun, deal more damage at the center of the blast, affect the world (destroy items, blow up other bombs)."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BANDOLIER, null), "New Content",
				"_-_ The huntress has been buffed: starts with Potion of Mind Vision identified, now benefits from strength on melee attacks, and has a chance to reclaim a single used ranged weapon from each defeated enemy.\n\n" +
				"_-_ A new container: The Potion Bandolier! Potions can now shatter from frost, but the bandolier can protect them.\n\n" +
				"_-_ Shops now stock a much greater variety of items, some item prices have been rebalanced.\n\n" +
				"_-_ Added Merchant's Beacon.\n\n" +
				"_-_ Added initials for IDed scrolls/potions."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Going down stairs no longer increases hunger, going up still does.\n\n" +
				"_-_ Many, many bugfixes.\n" +
				"_-_ Some UI improvements.\n" +
				"_-_ Ingame audio quality improved.\n" +
				"_-_ Unstable spellbook buffed.\n" +
				"_-_ Psionic blasts deal less self-damage.\n" +
				"_-_ Potions of liquid flame affect a 3x3 grid."));

		changes = new ChangeInfo("v0.2.3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released January 6th, 2015\n" +
				"_-_ 64 days after Shattered v0.2.2\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(new ItemSprite(new TimekeepersHourglass()), "Artifact Changes",
				"Added 4 new artifacts:\n" +
				"_-_ Alchemist's Toolkit\n" +
				"_-_ Unstable Spellbook\n" +
				"_-_ Timekeeper's Hourglass\n" +
				"_-_ Dried Rose\n\n" +
				"_-_ Artifacts are now unique over each run\n" +
				"_-_ Artifacts can now be cursed!\n" +
				"_-_ Cloak of Shadows is now exclusive to the rogue\n" +
				"_-_ Smaller Balance Changes and QOL improvements to almost every artifact"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_CRIMSON, null), "Balance Changes",
				"_-_ Health potion farming has been nerfed from all sources\n" +
				"_-_ Freerunner now moves at very high speeds when invisible\n" +
				"_-_ Ring of Force buffed significantly\n" +
				"_-_ Ring of Evasion reworked again\n" +
				"_-_ Improved the effects of some blandfruit types\n" +
				"_-_ Using throwing weapons now cancels stealth"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Implemented a donation system in the Google Play version of Shattered\n\n" +
				"_-_ Significantly increased the stability of the save system\n\n" +
				"_-_ Increased the number of visible rankings to 11 from 6\n\n" +
				"_-_ Improved how the player is interrupted by harmful events"));

		changes = new ChangeInfo("v0.2.2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released November 3rd, 2014\n" +
				"_-_ 21 days after Shattered v0.2.1\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.STONE_HAGLAZ, null), "Pixel Dungeon v1.7.2",
				"Implemented directly from v1.7.2:\n" +
				"_-_ Synchronous Movement\n" +
				"_-_ Challenges\n" +
				"_-_ UI improvements\n" +
				"_-_ Vertigo debuff\n\n" +
				"Implement with changes:\n" +
				"_-_ Weightstone: Increases either speed or damage, at the cost of the other, instead of increasing either speed or accuracy.\n\n" +
				"Not Implemented:\n" +
				"_-_ Key ring and unstackable keys\n" +
				"_-_ Blindweed has not been removed"));

		changes.addButton(new ChangeButton(new Image(Assets.TERRAIN_FEATURES, 144, 112, 16, 16), "New Plants",
				"Added two new plants:\n" +
				"_-_ Stormvine, which brews into levitation\n" +
				"_-_ Dreamfoil, which brews into purity\n\n" +
				"_-_ Potion of levitation can now be thrown to make a cloud of confusion gas\n\n" +
				"_-_ Removed gas collision logic, gasses can now stack without limitation."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.REMAINS, null), "Heroes Remains",
				"Heroes remains have been significantly adjusted to prevent strategies that exploit them, but also to increase their average loot.\n\n" +
				"Remains have additional limitations:\n" +
				"_-_ Heros will no longer drop remains if they have obtained the amulet of yendor, or die 5 or more floors above the deepest floor they have reached\n" +
				"_-_ Class exclusive items can no longer appear in remains\n" +
				"_-_ Items found in remains are now more harshly level-capped\n" +
				"_-_ Remains are not dropped, and cannot be found, when challenges are enabled.\n\n" +
				"However:\n" +
				"_-_ Remains can now contain useful items from the inventory, not just equipped items.\n" +
				"_-_ Remains are now less likely to contain gold."));

		changes = new ChangeInfo("v0.2.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released October 13th, 2014\n" +
				"_-_ 28 days after Shattered v0.2.0\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(new Image(Assets.GHOST, 0, 0, 14, 15), "New Sewer Quests",
				"_-_ Removed the dried rose quest (the rose will return...)\n\n" +
				"_-_ Tweaked the mechanics of the fetid rat quest\n\n" +
				"_-_ Added a gnoll trickster quest\n\n" +
				"_-_ Added a great crab quest"));

		changes.addButton(new ChangeButton(new Image(Assets.GOO, 43, 3, 14, 11), "Goo Changes",
				"Goo's animations have been overhauled, including a particle effect for the area of its pumped up attack.\n\n" +
				"Goo's arena has been updated to give more room to maneuver, and to be more variable."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GUIDE_PAGE, null), "Story & Signpost Changes",
				"Most text in the sewers has been overhauled, including descriptions, quest dialogues, signposts, and story scrolls"));

		changes = new ChangeInfo("v0.2.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released September 15th, 2014\n" +
				"_-_ 31 days after Shattered v0.1.1"));

		changes.addButton(new ChangeButton(new ItemSprite(new HornOfPlenty()), "Artifacts!",
				"Added artifacts to the game!\n\n" +
				"Artifacts are unique items which offer new gameplay opportunities and grow stronger through unique means.\n\n" +
				"Removed 7 Rings... And Replaced them with 7 Artifacts!\n" +
				"_-_ Ring of Shadows becomes Cloak of Shadows\n" +
				"_-_ Ring of Satiety becomes Horn of Plenty\n" +
				"_-_ Ring of Mending becomes Chalice of Blood\n" +
				"_-_ Ring of Thorns becomes Cape of Thorns\n" +
				"_-_ Ring of Haggler becomes Master Thieves' Armband\n" +
				"_-_ Ring of Naturalism becomes Sandals of Nature"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_DIAMOND, null), "New Rings!",
				"To replace the lost rings, 6 new rings have been added:\n" +
				"_-_ Ring of Force\n" +
				"_-_ Ring of Furor\n" +
				"_-_ Ring of Might\n" +
				"_-_ Ring of Tenacity\n" +
				"_-_ Ring of Sharpshooting\n" +
				"_-_ Ring of Wealth\n\n" +
				"The 4 remaining rings have also been tweaked or reworked entirely:\n" +
				"_-_ Ring of Accuracy\n" +
				"_-_ Ring of Elements\n" +
				"_-_ Ring of Evasion\n" +
				"_-_ Ring of Haste"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"-Nerfed farming health potions from fly swarms.\n\n" +
				"-Buffed crazed bandit and his drops.\n\n" +
				"-Made Blandfruit more common.\n\n" +
				"-Nerfed Assassin bonus damage slightly, to balance with him having an artifact now.\n\n" +
				"-Added a welcome page!"));

		changes = new ChangeInfo(" ", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		//**********************
		//       v0.1.X
		//**********************

		changes = new ChangeInfo("v0.1.X", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes = new ChangeInfo("v0.1.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released August 15th, 2014\n" +
				"_-_ 10 days after Shattered v0.1.0\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(new Blandfruit(),
				"Players who chance upon gardens or who get lucky while trampling grass may come across a new plant: the _Blandfruit._\n\n" +
				"As the name implies, the fruit from this plant is pretty unexceptional, and will barely do anything for you on its own. Perhaps there is some way to prepare the fruit with another ingredient..."));

		changes.addButton(new ChangeButton(new ItemSprite(new Ankh()), "Revival Item Changes",
				"When the Dew Vial was initially added to Pixel Dungeon, its essentially free revive made ankhs pretty useless by comparison. " +
				"To fix this, both items have been adjusted to combine to create a more powerful revive.\n\n" +
				"Dew Vial nerfed:\n" +
				"_-_ Still grants a full heal at full charge, but grants less healing at partial charge.\n" +
				"_-_ No longer revives the player if they die.\n\n" +
				"Ankh buffed:\n" +
				"_-_ Can now be blessed with a full dew vial, to gain the vial's old revive effect."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_JERA, null), "Misc Item Changes",
				"Sungrass buffed:\n" +
				"_-_ Heal scaling now scales with max hp.\n\n" +
				"Scroll of Psionic Blast rebalanced:\n" +
				"_-_ Now deals less self damage, and damage is more consistent.\n" +
				"_-_ Duration of self stun/blind effect increased.\n\n" +
				"Scroll of lullaby reworked:\n" +
				"_-_ No longer instantly sleeps enemies, now afflicts them with drowsy, which turns into magical sleep.\n" +
				"_-_ Magically slept enemies will only wake up when attacked.\n" +
				"_-_ Hero is also affected, and will be healed by magical sleep."));

		changes = new ChangeInfo("v0.1.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released August 5th, 2014\n" +
				"_-_ 69 days after Pixel Dungeon v1.7.1\n" +
				"_-_ 9 days after v1.7.1 source release\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SEED_EARTHROOT, null), "Seed Changes",
				"_-_ Blindweed buffed, now cripples as well as blinds.\n\n" +
				"_-_ Sungrass nerfed, heal scales up over time, total heal reduced by combat.\n\n" +
				"_-_ Earthroot nerfed, damage absorb down to 50% from 100%, total shield unchanged.\n\n" +
				"_-_ Icecap buffed, freeze effect is now much stronger in water."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_SILVER, null), "Potion Changes",
				"_-_ Potion of Purity buffed, immunity duration increased to 10 turns from 5, clear effect radius increased.\n\n" +
				"_-_ Potion of Frost buffed, freeze effect is now much stronger in water."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_JERA, null), "Scroll Changes",
				"_-_ Scroll of Psionic blast reworked, now rarer and much stronger, but deals damage to the hero.\n\n" +
				"_-_ Scroll of Challenge renamed to Scroll of Rage, now amoks nearby enemies."));

		Component content = list.content();
		content.clear();

		float posY = 0;
		float nextPosY = 0;
		boolean second = false;
		for(ChangeInfo info : infos)
		{
			if(info.major)
			{
				posY = nextPosY;
				second = false;
				info.setRect(0, posY, panel.innerWidth(), 0);
				content.add(info);
				posY = nextPosY = info.bottom();
			}
			else
			{
				if(!second)
				{
					second = true;
					info.setRect(0, posY, panel.innerWidth() / 2f, 0);
					content.add(info);
					nextPosY = info.bottom();
				}
				else
				{
					second = false;
					info.setRect(panel.innerWidth() / 2f, posY, panel.innerWidth() / 2f, 0);
					content.add(info);
					nextPosY = Math.max(info.bottom(), nextPosY);
					posY = nextPosY;
				}
			}
		}


		content.setSize(panel.innerWidth(), (int) Math.ceil(posY));

		list.setRect(
				panel.x + panel.marginLeft(),
				panel.y + panel.marginTop() - 1,
				panel.innerWidth(),
				panel.innerHeight() + 2);
		list.scrollTo(0, 0);

		Archs archs = new Archs();
		archs.setSize(Camera.main.width, Camera.main.height);
		addToBack(archs);

		fadeIn();
	}

	@Override
	protected void onBackPressed()
	{
		ChancelPixelDungeon.switchNoFade(TitleScene.class);
	}

	private static class ChangeInfo extends Component
	{

		protected ColorBlock line;

		private RenderedText title;
		private boolean major;

		private RenderedTextMultiline text;

		private ArrayList<ChangeButton> buttons = new ArrayList<>();

		public ChangeInfo(String title, boolean majorTitle, String text)
		{
			super();

			if(majorTitle)
			{
				this.title = PixelScene.renderText(title, 9);
				line = new ColorBlock(1, 1, 0xFF222222);
				add(line);
			}
			else
			{
				this.title = PixelScene.renderText(title, 6);
				line = new ColorBlock(1, 1, 0xFF333333);
				add(line);
			}
			major = majorTitle;

			add(this.title);

			if(text != null && !text.equals(""))
			{
				this.text = PixelScene.renderMultiline(text, 6);
				add(this.text);
			}

		}

		public void hardlight(int color)
		{
			title.hardlight(color);
		}

		public void addButton(ChangeButton button)
		{
			buttons.add(button);
			add(button);

			button.setSize(16, 16);
			layout();
		}

		public boolean onClick(float x, float y)
		{
			for(ChangeButton button : buttons)
			{
				if(button.inside(x, y))
				{
					button.onClick();
					return true;
				}
			}
			return false;
		}

		@Override
		protected void layout()
		{
			float posY = this.y + 2;
			if(major) posY += 2;

			title.x = x + (width - title.width()) / 2f;
			title.y = posY;
			PixelScene.align(title);
			posY += title.baseLine() + 2;

			if(text != null)
			{
				text.maxWidth((int) width());
				text.setPos(x, posY);
				posY += text.height();
			}

			float posX = x;
			float tallest = 0;
			for(ChangeButton change : buttons)
			{

				if(posX + change.width() >= right())
				{
					posX = x;
					posY += tallest;
					tallest = 0;
				}

				//centers
				if(posX == x)
				{
					float offset = width;
					for(ChangeButton b : buttons)
					{
						offset -= b.width();
						if(offset <= 0)
						{
							offset += b.width();
							break;
						}
					}
					posX += offset / 2f;
				}

				change.setPos(posX, posY);
				posX += change.width();
				if(tallest < change.height())
				{
					tallest = change.height();
				}
			}
			posY += tallest + 2;

			height = posY - this.y;

			if(major)
			{
				line.size(width(), 1);
				line.x = x;
				line.y = y + 2;
			}
			else if(x == 0)
			{
				line.size(1, height());
				line.x = width;
				line.y = y;
			}
			else
			{
				line.size(1, height());
				line.x = x;
				line.y = y;
			}
		}
	}

	//not actually a button, but functions as one.
	private static class ChangeButton extends Component
	{

		protected Image icon;
		protected String title;
		protected String message;

		public ChangeButton(Image icon, String title, String message)
		{
			super();

			this.icon = icon;
			add(this.icon);

			this.title = Messages.titleCase(title);
			this.message = message;

			layout();
		}

		public ChangeButton(Item item, String message)
		{
			this(new ItemSprite(item), item.name(), message);
		}

		protected void onClick()
		{
			ChancelPixelDungeon.scene().add(new ChangesWindow(new Image(icon), title, message));
		}

		@Override
		protected void layout()
		{
			super.layout();

			icon.x = x + (width - icon.width) / 2f;
			icon.y = y + (height - icon.height) / 2f;
			PixelScene.align(icon);
		}
	}

	private static class ChangesWindow extends WndTitledMessage
	{

		public ChangesWindow(Image icon, String title, String message)
		{
			super(icon, title, message);

			add(new TouchArea(chrome)
			{
				@Override
				protected void onClick(Touchscreen.Touch touch)
				{
					hide();
				}
			});

		}

	}
}
