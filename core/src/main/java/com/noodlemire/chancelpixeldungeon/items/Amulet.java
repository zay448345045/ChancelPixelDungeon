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

import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.CPDSettings;
import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.Statistics;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.keys.Key;
import com.noodlemire.chancelpixeldungeon.items.potions.Potion;
import com.noodlemire.chancelpixeldungeon.items.rings.Ring;
import com.noodlemire.chancelpixeldungeon.items.scrolls.Scroll;
import com.noodlemire.chancelpixeldungeon.journal.Catalog;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.AmuletScene;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.scenes.PixelScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.ui.RedButton;
import com.noodlemire.chancelpixeldungeon.ui.RenderedTextMultiline;
import com.noodlemire.chancelpixeldungeon.ui.ScrollPane;
import com.noodlemire.chancelpixeldungeon.ui.Window;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Amulet extends Item
{
	public static final boolean DEBUG = false;

	private static final String AC_END = "END";

	private static final String AC_COLLECT = "COLLECT";
	private static final String AC_ENCHANT = "ENCHANT";
	private static final String AC_LEVEL = "LEVEL";
	private static final String AC_SUMMON = "SUMMON";
	private static final String AC_TRAVEL = "TRAVEL";

	{
		image = ItemSpriteSheet.AMULET;

		unique = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero)
	{
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_END);

		if(DEBUG)
		{
			actions.add(AC_COLLECT);
			actions.add(AC_ENCHANT);
			actions.add(AC_LEVEL);
			actions.add(AC_SUMMON);
			actions.add(AC_TRAVEL);
		}

		return actions;
	}

	@Override
	public void execute(Hero hero, String action)
	{
		super.execute(hero, action);

		if(action.equals(AC_END))
			showAmuletScene(false);

		if(action.equals(AC_COLLECT))
			GameScene.show(new WndCollect());
	}

	@Override
	public boolean doPickUp(Hero hero)
	{
		if(super.doPickUp(hero))
		{
			if(!Statistics.amuletObtained)
			{
				Statistics.amuletObtained = true;
				Badges.validateVictory();
				hero.spend(-TIME_TO_PICK_UP);

				//add a delayed actor here so pickup behaviour can fully process.
				Actor.addDelayed(new Actor()
				{
					@Override
					protected boolean act()
					{
						Actor.remove(this);
						showAmuletScene(true);
						return false;
					}
				}, -5);
			}

			return true;
		}
		else
			return false;
	}

	private void showAmuletScene(boolean showText)
	{
		try
		{
			Dungeon.saveAll();
			AmuletScene.noText = !showText;
			Game.switchScene(AmuletScene.class);
		}
		catch(IOException e)
		{
			ChancelPixelDungeon.reportException(e);
		}
	}

	@Override
	public boolean isIdentified()
	{
		return true;
	}

	@Override
	public boolean isUpgradable()
	{
		return false;
	}

	private static class ListItem extends Component
	{
		protected RenderedTextMultiline label;
		protected BitmapText depth;
		protected ColorBlock line;
		protected Image icon;

		public ListItem(Image icon, String text)
		{
			this(icon, text, -1);
		}

		public ListItem(Image icon, String text, int d)
		{
			super();

			this.icon.copy(icon);

			label.text(text);

			if(d >= 0)
			{
				depth.text(Integer.toString(d));
				depth.measure();

				if(d == Dungeon.depth)
				{
					label.hardlight(Window.TITLE_COLOR);
					depth.hardlight(Window.TITLE_COLOR);
				}
			}
		}

		@Override
		protected void createChildren()
		{
			label = PixelScene.renderMultiline(7);
			add(label);

			icon = new Image();
			add(icon);

			depth = new BitmapText(PixelScene.pixelFont);
			add(depth);

			line = new ColorBlock(1, 1, 0xFF222222);
			add(line);
		}

		@Override
		protected void layout()
		{
			icon.y = y + 1 + (height() - 1 - icon.height()) / 2f;
			PixelScene.align(icon);

			depth.x = icon.x + (icon.width - depth.width()) / 2f;
			depth.y = icon.y + (icon.height - depth.height()) / 2f + 1;
			PixelScene.align(depth);

			line.size(width, 1);
			line.x = 0;
			line.y = y;

			label.maxWidth((int) (width - icon.width() - 8 - 1));
			label.setPos(icon.x + icon.width() + 1, y + 1 + (height() - label.height()) / 2f);
			PixelScene.align(label);
		}
	}

	private static class WndCollect extends Window
	{
		private static final int WIDTH_P = 120;
		private static final int HEIGHT_P = 160;

		private static final int WIDTH_L = 160;
		private static final int HEIGHT_L = 128;

		private static final int ITEM_HEIGHT = 18;

		public WndCollect()
		{
			int width = CPDSettings.landscape() ? WIDTH_L : WIDTH_P;
			int height = CPDSettings.landscape() ? HEIGHT_L : HEIGHT_P;

			resize(width, height);

			CatalogList catalog = new CatalogList();
			add(catalog);
			catalog.setRect(0, 0, width, height);
			catalog.updateList();
		}

		private static class CatalogList extends Component
		{
			private RedButton[] itemButtons;
			private static final int NUM_BUTTONS = 14;

			private static int currentItemIdx = 0;

			private static final int WEAPON_IDX = 0;
			private static final int ARMOR_IDX = 1;
			private static final int WAND_IDX = 2;
			private static final int RING_IDX = 3;
			private static final int ARTIF_IDX = 4;
			private static final int POTION_IDX = 5;
			private static final int SCROLL_IDX = 6;
			private static final int SEED_IDX = 7;
			private static final int STONE_IDX = 8;
			private static final int FOOD_IDX = 9;
			private static final int KEY_IDX = 10;
			private static final int BAG_IDX = 11;
			private static final int MISSILE_IDX = 12;
			private static final int MISC_IDX = 13;

			private ScrollPane list;

			private ArrayList<Amulet.WndCollect.CatalogList.CatalogItem> items = new ArrayList<>();

			@Override
			protected void createChildren()
			{
				itemButtons = new RedButton[NUM_BUTTONS];
				for(int i = 0; i < NUM_BUTTONS; i++)
				{
					final int idx = i;
					itemButtons[i] = new RedButton("")
					{
						@Override
						protected void onClick()
						{
							currentItemIdx = idx;
							updateList();
						}
					};
					itemButtons[i].icon(new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER + i, null));
					add(itemButtons[i]);
				}

				list = new ScrollPane(new Component())
				{
					@Override
					public void onClick(float x, float y)
					{
						int size = items.size();
						for(int i = 0; i < size; i++)
							if(items.get(i).onClick(x, y))
								break;
					}
				};
				add(list);
			}

			private static final int BUTTON_HEIGHT = 17;

			@Override
			protected void layout()
			{
				super.layout();

				int perRow = NUM_BUTTONS / 2;
				float buttonWidth = width() / perRow;

				for(int i = 0; i < NUM_BUTTONS; i++)
				{
					itemButtons[i].setRect((i % perRow) * (buttonWidth), (i / perRow) * (BUTTON_HEIGHT + 1),
							buttonWidth, BUTTON_HEIGHT);
					PixelScene.align(itemButtons[i]);
				}

				list.setRect(0, itemButtons[NUM_BUTTONS - 1].bottom() + 1, width,
						height - itemButtons[NUM_BUTTONS - 1].bottom() - 1);
			}

			private void updateList()
			{
				items.clear();

				for(int i = 0; i < NUM_BUTTONS; i++)
				{
					if(i == currentItemIdx)
						itemButtons[i].icon().color(TITLE_COLOR);
					else
						itemButtons[i].icon().resetColor();
				}

				Component content = list.content();
				content.clear();
				list.scrollTo(0, 0);

				ArrayList<Class<? extends Item>> itemClasses;
				final HashMap<Class<? extends Item>, Boolean> known = new HashMap<>();
				if(currentItemIdx == WEAPON_IDX)
				{
					itemClasses = new ArrayList<>(Catalog.WEAPONS.items());
					for(Class<? extends Item> cls : itemClasses) known.put(cls, true);
				}
				else if(currentItemIdx == ARMOR_IDX)
				{
					itemClasses = new ArrayList<>(Catalog.ARMOR.items());
					for(Class<? extends Item> cls : itemClasses) known.put(cls, true);
				}
				else if(currentItemIdx == WAND_IDX)
				{
					itemClasses = new ArrayList<>(Catalog.WANDS.items());
					for(Class<? extends Item> cls : itemClasses) known.put(cls, true);
				}
				else if(currentItemIdx == RING_IDX)
				{
					itemClasses = new ArrayList<>(Catalog.RINGS.items());
					for(Class<? extends Item> cls : itemClasses)
						known.put(cls, Ring.getKnown().contains(cls));
				}
				else if(currentItemIdx == ARTIF_IDX)
				{
					itemClasses = new ArrayList<>(Catalog.ARTIFACTS.items());
					for(Class<? extends Item> cls : itemClasses) known.put(cls, true);
				}
				else if(currentItemIdx == POTION_IDX)
				{
					itemClasses = new ArrayList<>(Catalog.POTIONS.items());
					for(Class<? extends Item> cls : itemClasses)
						known.put(cls, Potion.getKnown().contains(cls));
				}
				else if(currentItemIdx == SCROLL_IDX)
				{
					itemClasses = new ArrayList<>(Catalog.SCROLLS.items());
					for(Class<? extends Item> cls : itemClasses)
						known.put(cls, Scroll.getKnown().contains(cls));
				}
				else if(currentItemIdx == SEED_IDX)
				{
					itemClasses = new ArrayList<>(Catalog.SEEDS.items());
					for(Class<? extends Item> cls : itemClasses)
						known.put(cls, true);
				}
				else if(currentItemIdx == STONE_IDX)
				{
					itemClasses = new ArrayList<>(Catalog.STONES.items());
					for(Class<? extends Item> cls : itemClasses)
						known.put(cls, true);
				}
				else if(currentItemIdx == FOOD_IDX)
				{
					itemClasses = new ArrayList<>(Catalog.FOODS.items());
					for(Class<? extends Item> cls : itemClasses)
						known.put(cls, true);
				}
				else if(currentItemIdx == KEY_IDX)
				{
					itemClasses = new ArrayList<>(Catalog.KEYS.items());
					for(Class<? extends Item> cls : itemClasses)
						known.put(cls, true);
				}
				else if(currentItemIdx == BAG_IDX)
				{
					itemClasses = new ArrayList<>(Catalog.BAGS.items());
					for(Class<? extends Item> cls : itemClasses)
						known.put(cls, true);
				}
				else if(currentItemIdx == MISSILE_IDX)
				{
					itemClasses = new ArrayList<>(Catalog.MISSILES.items());
					for(Class<? extends Item> cls : itemClasses)
						known.put(cls, true);
				}
				else if(currentItemIdx == MISC_IDX)
				{
					itemClasses = new ArrayList<>(Catalog.MISC.items());
					for(Class<? extends Item> cls : itemClasses)
						known.put(cls, true);
				}
				else
				{
					itemClasses = new ArrayList<>();
				}

				Collections.sort(itemClasses, new Comparator<Class<? extends Item>>()
				{
					@Override
					public int compare(Class<? extends Item> a, Class<? extends Item> b)
					{
						int result = 0;

						//specifically known items appear first, then seen items, then unknown items.
						if(known.get(a) && Catalog.isSeen(a)) result -= 2;
						if(known.get(b) && Catalog.isSeen(b)) result += 2;
						if(Catalog.isSeen(a)) result--;
						if(Catalog.isSeen(b)) result++;

						return result;
					}
				});

				float pos = 0;
				for(Class<? extends Item> itemClass : itemClasses)
				{
					try
					{
						Amulet.WndCollect.CatalogList.CatalogItem item = new Amulet.WndCollect.CatalogList.CatalogItem(itemClass.newInstance());
						item.setRect(0, pos, width, ITEM_HEIGHT);
						content.add(item);
						items.add(item);

						pos += item.height();
					}
					catch(Exception e)
					{
						ChancelPixelDungeon.reportException(e);
					}
				}

				content.setSize(width, pos);
				list.setSize(list.width(), list.height());
			}

			private static class CatalogItem extends ListItem
			{
				private Item item;

				public CatalogItem(Item item)
				{
					super(new ItemSprite(item), Messages.titleCase(item.trueName()));

					this.item = item;
				}

				public boolean onClick(float x, float y)
				{
					if(inside(x, y))
					{
						try
						{
							Item i = item.getClass().newInstance();
							if(i instanceof Key)
								((Key) i).depth = Dungeon.depth;

							if(!i.collect())
								Dungeon.level.drop(i, Dungeon.hero.pos).sprite.drop();
							else
								GLog.i(Messages.get(Hero.class, "you_now_have", item.name()));
						}
						catch(InstantiationException e)
						{
							ChancelPixelDungeon.reportException(e);
						}
						catch(IllegalAccessException e)
						{
							ChancelPixelDungeon.reportException(e);
						}

						return true;
					}
					else
						return false;
				}
			}
		}
	}
}
