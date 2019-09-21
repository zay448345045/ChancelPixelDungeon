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

package com.noodlemire.chancelpixeldungeon.items.potions;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.windows.WndBag;
import com.noodlemire.chancelpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

import static com.noodlemire.chancelpixeldungeon.Dungeon.hero;

public abstract class InventoryPotion extends Potion
{
	private String inventoryTitle = Messages.get(this, "inv_title");
	protected WndBag.Mode mode = WndBag.Mode.ALL;

	@Override
	public void drink(Hero hero)
	{
		identifiedByUse = !isKnown();
		setKnown();

		curUser = hero;
		curItem = detach(hero.belongings.backpack);

		GameScene.selectItem(itemSelector, mode, inventoryTitle);
	}

	private void confirmCancelation()
	{
		GameScene.show(new WndOptions(Messages.titleCase(name()), Messages.get(this, "warning"),
				Messages.get(this, "yes"), Messages.get(this, "no"))
		{
			@Override
			protected void onSelect(int index)
			{
				switch(index)
				{
					case 0:
						curUser.spendAndNext(1f);
						identifiedByUse = false;
						break;
					case 1:
						GameScene.selectItem(itemSelector, mode, inventoryTitle);
						break;
				}
			}

			public void onBackPressed()
			{
			}
		});
	}

	protected abstract void onItemSelected(Item item);

	private static boolean identifiedByUse = false;
	protected static WndBag.Listener itemSelector = new WndBag.Listener()
	{
		@Override
		public void onSelect(Item item)
		{

			//FIXME this safety check shouldn't be necessary
			//it would be better to eliminate the curItem static variable.
			if(!(curItem instanceof InventoryPotion))
			{
				return;
			}

			if(item != null)
			{

				((InventoryPotion) curItem).onItemSelected(item);

				Sample.INSTANCE.play(Assets.SND_DRINK);
				hero.sprite.operate(hero.pos);
			}
			else if(identifiedByUse)
			{
				((InventoryPotion) curItem).confirmCancelation();
			}
			else
			{
				curItem.collect(curUser.belongings.backpack);
			}
		}
	};
}
