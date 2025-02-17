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

package com.noodlemire.chancelpixeldungeon.ui;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.items.Heap;
import com.noodlemire.chancelpixeldungeon.items.Item;

public class LootIndicator extends Tag
{

	private ItemSlot slot;

	private Item lastItem = null;
	private int lastQuantity = 0;

	public LootIndicator()
	{
		super(0x1F75CC);

		setSize(24, 24);

		visible = false;
	}

	@Override
	protected void createChildren()
	{
		super.createChildren();

		slot = new ItemSlot()
		{
			protected void onClick()
			{
				if(Dungeon.hero.handle(Dungeon.hero.pos))
				{
					Dungeon.hero.next();
				}

			}
		};
		slot.showParams(true, false, false);
		add(slot);
	}

	@Override
	protected void layout()
	{
		super.layout();

		slot.setRect(x + 2, y + 3, width - 2, height - 6);
	}

	@Override
	public void update()
	{

		if(Dungeon.hero.ready)
		{
			Heap heap = Dungeon.level.heaps.get(Dungeon.hero.pos);
			if(heap != null)
			{
				Item item;
				switch(heap.type)
				{
					case CHEST:
					case MIMIC:
						item = ItemSlot.CHEST;
						break;
					case LOCKED_CHEST:
						item = ItemSlot.LOCKED_CHEST;
						break;
					case CRYSTAL_CHEST:
						item = ItemSlot.CRYSTAL_CHEST;
						break;
					case EBONY_CHEST:
						item = ItemSlot.EBONY_CHEST;
						break;
					case TOMB:
						item = ItemSlot.TOMB;
						break;
					case SKELETON:
						item = ItemSlot.SKELETON;
						break;
					case REMAINS:
						item = ItemSlot.REMAINS;
						break;
					default:
						item = heap.peek();
				}

				if(item != lastItem || item.quantity() != lastQuantity)
				{
					lastItem = item;
					lastQuantity = item.quantity();

					slot.item(item);
					flash();
				}
				visible = true;

			}
			else
			{

				lastItem = null;
				visible = false;

			}
		}

		slot.enable(visible && Dungeon.hero.ready);

		super.update();
	}
}
