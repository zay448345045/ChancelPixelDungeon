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
import com.noodlemire.chancelpixeldungeon.items.Heap;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.PixelScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;
import com.noodlemire.chancelpixeldungeon.ui.ItemSlot;
import com.noodlemire.chancelpixeldungeon.ui.RenderedTextMultiline;
import com.noodlemire.chancelpixeldungeon.ui.Window;

public class WndInfoItem extends Window
{

	private static final float GAP = 2;

	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 144;

	public WndInfoItem(Heap heap)
	{
		super();

		if(heap.type == Heap.Type.HEAP || heap.type == Heap.Type.FOR_SALE)
		{
			Item item = heap.peek();

			int color = TITLE_COLOR;

			if(item.visiblyUpgraded() > 0)
				color = ItemSlot.UPGRADED;

			fillFields(item.image(), item.glowing(), color, item.toString(), item.info());
		}
		else
			fillFields(heap.image(), heap.glowing(), TITLE_COLOR, heap.toString(), heap.info());
	}

	public WndInfoItem(Item item)
	{
		super();

		int color = TITLE_COLOR;

		if(item.visiblyUpgraded() > 0)
			color = ItemSlot.UPGRADED;

		fillFields(item.image(), item.glowing(), color, item.toString(), item.info());
	}

	private void fillFields(int image, ItemSprite.Glowing glowing, int titleColor, String title, String info)
	{

		int width = CPDSettings.landscape() ? WIDTH_L : WIDTH_P;

		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(image, glowing));
		titlebar.label(Messages.titleCase(title), titleColor);
		titlebar.setRect(0, 0, width, 0);
		add(titlebar);

		RenderedTextMultiline txtInfo = PixelScene.renderMultiline(info, 6);
		txtInfo.maxWidth(width);
		txtInfo.setPos(titlebar.left(), titlebar.bottom() + GAP);
		add(txtInfo);

		resize(width, (int) (txtInfo.top() + txtInfo.height()));
	}
}
