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

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.potions.Potion;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.noodlemire.chancelpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class LitmusPaper extends Item
{
	private static final float TIME_TO_CHECK = 1;

	private static final String AC_CHECK = "CHECK";

	{
		image = ItemSpriteSheet.LITMUS_PAPER;
		defaultAction = AC_CHECK;
		bones = true;
	}

	@Override
	public boolean stackable()
	{
		return true;
	}

	@Override
	public ArrayList<String> actions(Hero hero)
	{
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_CHECK);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action)
	{
		super.execute(hero, action);

		if(action.equals(AC_CHECK))
		{
			curUser = hero;
			GameScene.selectItem(itemSelector, WndBag.Mode.POTION_DANGER_UNKNOWN, Messages.get(this, "prompt"));
		}
	}

	@Override
	public boolean isUpgradable()
	{
		return false;
	}

	@Override
	public boolean isIdentified()
	{
		return true;
	}

	private void check(Potion potion)
	{
		detach(curUser.belongings.backpack);

		potion.setDangerKnown();

		if(potion.harmful)
			GLog.w(Messages.get(this, "dangerous"));
		else
			GLog.h(Messages.get(this, "safe"));

		curUser.sprite.operate(curUser.pos);
		Sample.INSTANCE.play(Assets.SND_DRINK);

		curUser.spend(TIME_TO_CHECK);
		curUser.busy();
	}

	@Override
	public int price()
	{
		return 15 * quantity;
	}

	private final WndBag.Listener itemSelector = new WndBag.Listener()
	{
		@Override
		public void onSelect(Item item)
		{
			if(item != null)
				LitmusPaper.this.check((Potion) item);
		}
	};
}
