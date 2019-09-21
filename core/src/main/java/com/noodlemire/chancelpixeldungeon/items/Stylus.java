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
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.Enchanting;
import com.noodlemire.chancelpixeldungeon.effects.particles.PurpleParticle;
import com.noodlemire.chancelpixeldungeon.items.scrolls.Scroll;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.noodlemire.chancelpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Stylus extends Item
{
	private static final float TIME_TO_INSCRIBE = 2;

	private static final String AC_INSCRIBE = "INSCRIBE";

	{
		image = ItemSpriteSheet.STYLUS;
		defaultAction = AC_INSCRIBE;
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
		actions.add(AC_INSCRIBE);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action)
	{
		super.execute(hero, action);

		if(action.equals(AC_INSCRIBE))
		{
			curUser = hero;
			GameScene.selectItem(itemSelector, WndBag.Mode.SCROLL, Messages.get(this, "prompt"));
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

	private void inscribe(Scroll scroll)
	{
		detach(curUser.belongings.backpack);

		GLog.w(Messages.get(this, "inscribed"));

		scroll.detach(curUser.belongings.backpack);
		scroll = (Scroll)scroll.transmute();
		if(!scroll.collect())
			Dungeon.level.drop(scroll, curUser.pos).sprite.drop();

		curUser.sprite.operate(curUser.pos);
		curUser.sprite.centerEmitter().start(PurpleParticle.BURST, 0.05f, 10);
		Enchanting.show(curUser, scroll);
		Sample.INSTANCE.play(Assets.SND_BURNING);

		curUser.spend(TIME_TO_INSCRIBE);
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
				Stylus.this.inscribe((Scroll) item);
		}
	};
}
