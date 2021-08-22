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

package com.noodlemire.chancelpixeldungeon.windows;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.CPDSettings;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.hero.Belongings;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.hero.HeroClass;
import com.noodlemire.chancelpixeldungeon.items.EquipableItem;
import com.noodlemire.chancelpixeldungeon.items.Gold;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.Transmutable;
import com.noodlemire.chancelpixeldungeon.items.armor.Armor;
import com.noodlemire.chancelpixeldungeon.items.bags.Bag;
import com.noodlemire.chancelpixeldungeon.items.bags.MagicalHolster;
import com.noodlemire.chancelpixeldungeon.items.bags.MiscSlots;
import com.noodlemire.chancelpixeldungeon.items.bags.PotionBandolier;
import com.noodlemire.chancelpixeldungeon.items.bags.ScrollHolder;
import com.noodlemire.chancelpixeldungeon.items.bags.VelvetPouch;
import com.noodlemire.chancelpixeldungeon.items.food.Blandfruit;
import com.noodlemire.chancelpixeldungeon.items.food.Food;
import com.noodlemire.chancelpixeldungeon.items.potions.Potion;
import com.noodlemire.chancelpixeldungeon.items.scrolls.Scroll;
import com.noodlemire.chancelpixeldungeon.items.wands.Wand;
import com.noodlemire.chancelpixeldungeon.items.weapon.Bow;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.Boomerang;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.darts.Dart;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.plants.Plant.Seed;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.scenes.PixelScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.ui.Icons;
import com.noodlemire.chancelpixeldungeon.ui.ItemSlot;
import com.noodlemire.chancelpixeldungeon.ui.QuickSlotButton;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.RectF;

public class WndBag extends WndTabbed
{
	//FIXME this is getting cumbersome, there should be a better way to manage this
	public enum Mode
	{
		ALL,
		UNIDENTIFED,
		UNIDED_OR_CURSED_OR_ENCHANTED,
		UPGRADEABLE,
		QUICKSLOT,
		FOR_SALE,
		WEAPON,
		ARMOR,
		ENCHANTABLE,
		ENCHANTABLE_IDED,
		ENCHANTED,
		WAND,
		SEED,
		FOOD,
		POTION,
		SCROLL,
		UNIDED_POTION_OR_SCROLL,
		POTION_DANGER_UNKNOWN,
		IDED_POTION,
		EQUIPMENT,
		ALCHEMY,
		TRANSMUTABLE
	}

	private static WndBag INSTANCE;

	private static final int COLS_P = 4;
	private static final int COLS_L = 6;

	private static final int SLOT_WIDTH = 28;
	private static final int SLOT_HEIGHT = 28;
	private static final int SLOT_MARGIN = 1;

	private static final int TITLE_HEIGHT = 14;

	private final Listener listener;
	private final WndBag.Mode mode;
	private final String title;

	private final int nCols;
	private final int nRows;

	protected int count;
	protected int col;
	protected int row;

	private static Mode lastMode;
	private static Bag lastBag;

	public WndBag(Bag bag, Listener listener, Mode mode, String title)
	{
		super();

		if(INSTANCE != null)
			INSTANCE.hide();

		INSTANCE = this;

		this.listener = listener;
		this.mode = mode;
		this.title = title;

		lastMode = mode;
		lastBag = bag;

		nCols = CPDSettings.landscape() ? COLS_L : COLS_P;
		nRows = (int) Math.ceil((Belongings.BACKPACK_SIZE + 2) / (float) nCols);

		int slotsWidth = SLOT_WIDTH * nCols + SLOT_MARGIN * (nCols - 1);
		int slotsHeight = SLOT_HEIGHT * nRows + SLOT_MARGIN * (nRows - 1);

		placeTitle(bag, slotsWidth);

		placeItems(bag);

		resize(slotsWidth, slotsHeight + TITLE_HEIGHT);

		Belongings stuff = Dungeon.hero.belongings;
		Bag[] bags = {
				stuff.backpack,
				stuff.getItem(VelvetPouch.class),
				stuff.getItem(ScrollHolder.class),
				stuff.getItem(PotionBandolier.class),
				stuff.getItem(MagicalHolster.class),
				stuff.miscSlots};

		for(Bag b : bags)
		{
			if(b != null)
			{
				BagTab tab = new BagTab(b);
				add(tab);
				tab.select(b == bag);
			}
		}

		layoutTabs();
	}

	public static WndBag lastBag(Listener listener, Mode mode, String title)
	{
		if(mode == lastMode && lastBag != null &&
		   Dungeon.hero.belongings.backpack.contains(lastBag))
			return new WndBag(lastBag, listener, mode, title);
		else
			return new WndBag(Dungeon.hero.belongings.backpack, listener, mode, title);
	}

	public static WndBag getBag(Class<? extends Bag> bagClass, Listener listener, Mode mode, String title)
	{
		Bag bag = Dungeon.hero.belongings.getItem(bagClass);
		return bag != null ?
				new WndBag(bag, listener, mode, title) :
				lastBag(listener, mode, title);
	}

	@Override
	public void hide()
	{
		super.hide();

		if(INSTANCE == this)
			INSTANCE = null;
	}

	private void placeTitle(Bag bag, int width)
	{
		RenderedText txtTitle = PixelScene.renderText(
				title != null ? Messages.titleCase(title) : Messages.titleCase(bag.name()), 9);
		txtTitle.hardlight(TITLE_COLOR);
		txtTitle.x = 1;
		txtTitle.y = (int) (TITLE_HEIGHT - txtTitle.baseLine()) / 2f - 1;
		PixelScene.align(txtTitle);
		add(txtTitle);

		ItemSprite gold = new ItemSprite(ItemSpriteSheet.GOLD, null);
		gold.x = width - gold.width() - 1;
		gold.y = (TITLE_HEIGHT - gold.height()) / 2f - 1;
		PixelScene.align(gold);
		add(gold);

		BitmapText amt = new BitmapText(Integer.toString(Dungeon.gold), PixelScene.pixelFont);
		amt.hardlight(TITLE_COLOR);
		amt.measure();
		amt.x = width - gold.width() - amt.width() - 2;
		amt.y = (TITLE_HEIGHT - amt.baseLine()) / 2f - 1;
		PixelScene.align(amt);
		add(amt);
	}

	private void placeItems(Bag container)
	{
		// Equipped items
		Belongings stuff = Dungeon.hero.belongings;
		placeItem(stuff.weapon != null ? stuff.weapon : new Placeholder(ItemSpriteSheet.WEAPON_HOLDER));
		placeItem(stuff.armor != null ? stuff.armor : new Placeholder(ItemSpriteSheet.ARMOR_HOLDER));

		if(container instanceof MiscSlots)
		{
			// Class-specific extra misc slot
			if (stuff.classMisc != null)
			{
				placeItem(stuff.classMisc);
			}
			else
			{
				if (Dungeon.hero.heroClass == HeroClass.ROGUE)
					placeItem(new Placeholder(ItemSpriteSheet.ARTIFACT_HOLDER));
				else if (Dungeon.hero.heroClass == HeroClass.MAGE)
					placeItem(new Placeholder(ItemSpriteSheet.WAND_HOLDER));
				else if (Dungeon.hero.heroClass == HeroClass.HUNTRESS)
					placeItem(new Placeholder(ItemSpriteSheet.RING_HOLDER));
			}
		}

		// Items in the bag
		for(Item item : container.items.toArray(new Item[0]))
			placeItem(item);

		// Free Space
		int extraCount = 2;
		if(container instanceof MiscSlots && Dungeon.hero.heroClass != HeroClass.WARRIOR)
			extraCount = 3;

		while((count - extraCount) < container.size)
			if(container instanceof MiscSlots)
				placeItem(new Placeholder(ItemSpriteSheet.SOMETHING));
			else
				placeItem(null);

		while((count - extraCount) < Belongings.MAX_MISC_AMOUNT)
			placeItem(new Placeholder(ItemSpriteSheet.LOCKED));
	}

	private void placeItem(final Item item)
	{
		int x = col * (SLOT_WIDTH + SLOT_MARGIN);
		int y = TITLE_HEIGHT + row * (SLOT_HEIGHT + SLOT_MARGIN);

		add(new ItemButton(item).setPos(x, y));

		if(++col >= nCols)
		{
			col = 0;
			row++;
		}

		count++;
	}

	@Override
	public void onMenuPressed()
	{
		if(listener == null)
			hide();
	}

	@Override
	public void onBackPressed()
	{
		if(listener != null)
			listener.onSelect(null);
		super.onBackPressed();
	}

	@Override
	protected void onClick(Tab tab)
	{
		hide();
		GameScene.show(new WndBag(((BagTab) tab).bag, listener, mode, title));
	}

	@Override
	protected int tabHeight()
	{
		return 20;
	}

	private class BagTab extends Tab
	{
		private final Image icon;

		private final Bag bag;

		BagTab(Bag bag)
		{
			super();

			this.bag = bag;

			icon = icon();
			add(icon);
		}

		@Override
		protected void select(boolean value)
		{
			super.select(value);
			icon.am = selected ? 1.0f : 0.6f;
		}

		@Override
		protected void layout()
		{
			super.layout();

			icon.copy(icon());
			icon.x = x + (width - icon.width) / 2;
			icon.y = y + (height - icon.height) / 2 - 2 - (selected ? 0 : 1);
			if(!selected && icon.y < y + CUT)
			{
				RectF frame = icon.frame();
				frame.top += (y + CUT - icon.y) / icon.texture.height;
				icon.frame(frame);
				icon.y = y + CUT;
			}
		}

		private Image icon()
		{
			if(bag instanceof VelvetPouch)
				return Icons.get(Icons.SEED_POUCH);
			else if(bag instanceof ScrollHolder)
				return Icons.get(Icons.SCROLL_HOLDER);
			else if(bag instanceof MagicalHolster)
				return Icons.get(Icons.WAND_HOLSTER);
			else if(bag instanceof PotionBandolier)
				return Icons.get(Icons.POTION_BANDOLIER);
			else if(bag instanceof MiscSlots)
				return Icons.get(Icons.MISC_TAB);
			else
				return Icons.get(Icons.BACKPACK);
		}
	}

	public static class Placeholder extends Item
	{
		{
			name = null;
		}

		public Placeholder(int image)
		{
			this.image = image;
		}

		@Override
		public boolean isIdentified()
		{
			return true;
		}

		@Override
		public boolean isEquipped(Hero hero)
		{
			return image != ItemSpriteSheet.LOCKED;
		}
	}

	private class ItemButton extends ItemSlot
	{
		private static final int NORMAL = 0x9953564D;
		private static final int EQUIPPED = 0x9991938C;

		private final Item item;
		private ColorBlock bg;

		ItemButton(Item item)
		{
			super(item);

			this.item = item;
			if(item instanceof Gold)
				bg.visible = false;

			width = SLOT_WIDTH;
			height = SLOT_HEIGHT;
		}

		@Override
		protected void createChildren()
		{
			bg = new ColorBlock(SLOT_WIDTH, SLOT_HEIGHT, NORMAL);
			add(bg);

			super.createChildren();
		}

		@Override
		protected void layout()
		{
			bg.x = x;
			bg.y = y;

			super.layout();
		}

		@Override
		public void item(Item item)
		{
			super.item(item);
			if(item != null)
			{
				bg.texture(TextureCache.createSolid(item.isEquipped(Dungeon.hero) ? EQUIPPED : NORMAL));
				if((item.cursed && item.cursedKnown) ||
						(!item.isIdentified() && item instanceof Potion && ((Potion)item).isDangerKnown() && ((Potion)item).harmful) ||
						(!item.isIdentified() && item instanceof Scroll && ((Scroll)item).isDangerKnown() && ((Scroll)item).should_shout))
				{
					bg.ra = 0.3f;
					bg.ga = -0.15f;
				}
				else if(!item.isIdentified())
				{
					if((item.canBeCursed() && item.cursedKnown) ||
							(item instanceof Potion && ((Potion)item).isDangerKnown()) ||
							(item instanceof Scroll && ((Scroll)item).isDangerKnown()))
						bg.ba = 0.3f;
					else
					{
						bg.ra = 0.2f;
						bg.ba = 0.2f;
					}
				}

				if(item.name() == null)
					enable(false);
				else
				{
					enable(
							(mode == Mode.FOR_SALE && !item.unique && (item.price() > 0) && (!item.isEquipped(Dungeon.hero) || !item.cursed)) ||
							(mode == Mode.UPGRADEABLE && item.isUpgradable()) ||
							(mode == Mode.UNIDENTIFED && !item.isIdentified() && item.image != ItemSpriteSheet.POTION_UNSTABLE && item.image != ItemSpriteSheet.SCROLL_MYSTERY) ||
							(mode == Mode.UNIDED_OR_CURSED_OR_ENCHANTED && ((item instanceof EquipableItem || item instanceof Wand) && (!item.isIdentified() || item.cursed))) ||
							(mode == Mode.QUICKSLOT && (item.defaultAction != null)) ||
							(mode == Mode.WEAPON && (item instanceof MeleeWeapon)) ||
							(mode == Mode.ARMOR && item instanceof Armor) ||
							(mode == Mode.ENCHANTABLE && (item instanceof MeleeWeapon || item instanceof Bow || item instanceof Armor)) ||
							(mode == Mode.ENCHANTABLE_IDED && item.isIdentified() && (item instanceof MeleeWeapon || item instanceof Bow || item instanceof Boomerang || item instanceof Armor)) ||
							(mode == Mode.ENCHANTED && !(item instanceof Bow) && (item instanceof Weapon && (((Weapon) item).enchantment != null) || (item instanceof Armor && (((Armor) item).glyph != null)))) ||
							(mode == Mode.WAND && item instanceof Wand) ||
							(mode == Mode.SEED && item instanceof Seed) ||
							(mode == Mode.FOOD && item instanceof Food) ||
							(mode == Mode.POTION && item instanceof Potion) ||
							(mode == Mode.SCROLL && item instanceof Scroll) ||
							(mode == Mode.UNIDED_POTION_OR_SCROLL && (!item.isIdentified() && (item instanceof Scroll || item instanceof Potion) && item.image != ItemSpriteSheet.POTION_UNSTABLE && item.image != ItemSpriteSheet.SCROLL_MYSTERY)) ||
							(mode == Mode.POTION_DANGER_UNKNOWN && (item instanceof Potion && !((Potion)item).isDangerKnown())) ||
							(mode == Mode.IDED_POTION && (item.isIdentified() && item instanceof Potion)) ||
							(mode == Mode.EQUIPMENT && item instanceof EquipableItem) ||
							(mode == Mode.ALCHEMY && item.isIdentified() && (item instanceof Seed || item instanceof Scroll || (item instanceof Blandfruit && ((Blandfruit) item).potionAttrib == null) || (item.getClass() == Dart.class))) ||
							(mode == Mode.TRANSMUTABLE && item instanceof Transmutable) ||
							mode == Mode.ALL
					);
					//extra logic for cursed weapons or armor
					if(!active && (mode == Mode.UNIDED_OR_CURSED_OR_ENCHANTED))
					{
						if(item instanceof Weapon)
						{
							Weapon w = (Weapon) item;
							enable(w.hasCurseEnchant() || w.hasGoodEnchant());
						}
						if(item instanceof Armor)
						{
							Armor a = (Armor) item;
							enable(a.hasCurseGlyph() || a.hasGoodGlyph());
						}
					}
				}
			}
			else
				bg.color(NORMAL);
		}

		@Override
		protected void onTouchDown()
		{
			bg.brightness(1.5f);
			Sample.INSTANCE.play(Assets.SND_CLICK, 0.7f, 0.7f, 1.2f);
		}

		protected void onTouchUp()
		{
			bg.brightness(1.0f);
		}

		@Override
		protected void onClick()
		{
			if(!lastBag.contains(item) && !item.isEquipped(Dungeon.hero))
				hide();
			else if(listener != null)
			{
				hide();
				listener.onSelect(item);
			}
			else
				GameScene.show(new WndItem(Dungeon.hero.rankings ? null : WndBag.this, item));
		}

		@Override
		protected boolean onLongClick()
		{
			if(listener == null && item.defaultAction != null)
			{
				hide();
				Dungeon.quickslot.setSlot(0, item);
				QuickSlotButton.refresh();
				return true;
			}
			else
				return false;
		}
	}

	public interface Listener
	{
		void onSelect(Item item);
	}
}
