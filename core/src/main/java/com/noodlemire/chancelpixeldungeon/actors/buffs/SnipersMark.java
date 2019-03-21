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

package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.weapon.Bow;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.ui.ActionIndicator;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.noodlemire.chancelpixeldungeon.ui.QuickSlotButton;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class SnipersMark extends FlavourBuff implements ActionIndicator.Action
{
	public int object = 0;

	private static final String OBJECT = "object";

	@Override
	public boolean attachTo(Char target)
	{
		ActionIndicator.setAction(this);
		return super.attachTo(target);
	}

	@Override
	public void detach()
	{
		super.detach();
		ActionIndicator.clearAction(this);
	}

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(OBJECT, object);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		object = bundle.getInt(OBJECT);
	}

	@Override
	public int icon()
	{
		return BuffIndicator.MARK;
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc");
	}

	@Override
	public Image getIcon()
	{
		return new ItemSprite(ItemSpriteSheet.BOW_LOADED, null);
	}

	@Override
	public void doAction()
	{
		Hero hero = Dungeon.hero;
		if(hero == null) return;

		Bow bow = hero.belongings.getItem(Bow.class);
		if(bow == null) return;

		Bow.Arrow arrow = bow.knockArrow();
		if(arrow == null) return;

		Char ch = (Char) Actor.findById(object);
		if(ch == null) return;

		int cell = QuickSlotButton.autoAim(ch, arrow);
		if(cell == -1) return;

		bow.sniperSpecial = true;

		arrow.cast(hero, cell);
		detach();
	}
}
