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

import com.noodlemire.chancelpixeldungeon.CPDSettings;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.PixelScene;
import com.noodlemire.chancelpixeldungeon.ui.ItemSlot;
import com.noodlemire.chancelpixeldungeon.ui.RedButton;
import com.noodlemire.chancelpixeldungeon.ui.RenderedTextMultiline;
import com.noodlemire.chancelpixeldungeon.ui.Window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WndItem extends Window
{
	private static final float BUTTON_HEIGHT = 16;

	private static final float GAP = 2;

	private static final int WIDTH_MIN = 120;
	private static final int WIDTH_MAX = 220;

	private static WndItem INSTANCE;

	public WndItem(final WndBag owner, final Item item)
	{
		this(owner, item, owner != null);
	}

	public WndItem(final WndBag owner, final Item item, final boolean options)
	{
		super();

		if(INSTANCE != null)
			INSTANCE.hide();

		INSTANCE = this;

		int width = WIDTH_MIN;

		RenderedTextMultiline info = PixelScene.renderMultiline(item.info(), 6);
		info.maxWidth(width);

		//info box can go out of the screen on landscape, so widen it
		while(CPDSettings.landscape()
		      && info.height() > 100
		      && width < WIDTH_MAX)
		{
			width += 20;
			info.maxWidth(width);
		}

		IconTitle titlebar = new IconTitle(item);
		titlebar.setRect(0, 0, width, 0);
		add(titlebar);

		if(item.visiblyUpgraded() > 0)
		{
			titlebar.color(ItemSlot.UPGRADED);
		}

		info.setPos(titlebar.left(), titlebar.bottom() + GAP);
		add(info);

		float y = info.top() + info.height() + GAP;
		float x = 0;

		if(Dungeon.hero.isAlive() && options)
		{
			ArrayList<RedButton> line = new ArrayList<>();
			for(final String action : item.actions(Dungeon.hero))
			{

				RedButton btn = new RedButton(Messages.get(item, "ac_" + action), 8)
				{
					@Override
					protected void onClick()
					{
						hide();
						if(owner != null && owner.parent != null) owner.hide();
						item.execute(Dungeon.hero, action);
					}
				};
				btn.setSize(btn.reqWidth(), BUTTON_HEIGHT);
				if(x + btn.width() > width || line.size() == 3)
				{
					layoutButtons(line, width - x, y);
					x = 0;
					y += BUTTON_HEIGHT + 1;
					line = new ArrayList<>();
				}
				x++;
				add(btn);
				line.add(btn);

				if(action.equals(item.defaultAction))
				{
					btn.textColor(TITLE_COLOR);
				}

				x += btn.width();
			}
			layoutButtons(line, width - x, y);
		}

		resize(width, (int) (y + (x > 0 ? BUTTON_HEIGHT : 0)));
	}

	@Override
	public void hide()
	{
		super.hide();

		if(INSTANCE == this)
			INSTANCE = null;
	}

	//this method assumes a max of 3 buttons per line
	//FIXME: this is really messy for just trying to make buttons fill the window. Gotta be a cleaner way.
	private static void layoutButtons(ArrayList<RedButton> line, float extraWidth, float y)
	{
		if(line == null || line.size() == 0 || extraWidth == 0) return;
		if(line.size() == 1)
		{
			line.get(0).setSize(line.get(0).width() + extraWidth, BUTTON_HEIGHT);
			line.get(0).setPos(0, y);
			return;
		}
		ArrayList<RedButton> lineByWidths = new ArrayList<>(line);
		Collections.sort(lineByWidths, widthComparator);
		RedButton smallest, middle, largest;
		smallest = lineByWidths.get(0);
		middle = lineByWidths.get(1);
		largest = null;
		if(lineByWidths.size() == 3)
		{
			largest = lineByWidths.get(2);
		}

		float btnDiff = middle.width() - smallest.width();
		smallest.setSize(smallest.width() + Math.min(btnDiff, extraWidth), BUTTON_HEIGHT);
		extraWidth -= btnDiff;
		if(extraWidth > 0)
		{
			if(largest == null)
			{
				smallest.setSize(smallest.width() + extraWidth / 2, BUTTON_HEIGHT);
				middle.setSize(middle.width() + extraWidth / 2, BUTTON_HEIGHT);
			}
			else
			{
				btnDiff = largest.width() - smallest.width();
				smallest.setSize(smallest.width() + Math.min(btnDiff, extraWidth / 2), BUTTON_HEIGHT);
				middle.setSize(middle.width() + Math.min(btnDiff, extraWidth / 2), BUTTON_HEIGHT);
				extraWidth -= btnDiff * 2;
				if(extraWidth > 0)
				{
					smallest.setSize(smallest.width() + extraWidth / 3, BUTTON_HEIGHT);
					middle.setSize(middle.width() + extraWidth / 3, BUTTON_HEIGHT);
					largest.setSize(largest.width() + extraWidth / 3, BUTTON_HEIGHT);
				}
			}
		}

		float x = 0;
		for(RedButton btn : line)
		{
			btn.setPos(x, y);
			x += btn.width() + 1;
		}
	}

	private static final Comparator<RedButton> widthComparator = new Comparator<RedButton>()
	{
		@Override
		public int compare(RedButton lhs, RedButton rhs)
		{
			if(lhs.width() < rhs.width())
			{
				return -1;
			}
			else if(lhs.width() == rhs.width())
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
	};
}
