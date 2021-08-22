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
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Darkness;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.hero.HeroSubClass;
import com.noodlemire.chancelpixeldungeon.items.artifacts.CloakOfShadows;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;

public class Invisibility extends FlavourBuff implements Expulsion
{

	public static final float DURATION = 20f;

	{
		type = buffType.POSITIVE;
	}

	@Override
	public boolean attachTo(Char target)
	{
		if(super.attachTo(target))
		{
			target.invisible++;
			if(target instanceof Hero && ((Hero) target).subClass == HeroSubClass.ASSASSIN)
				Buff.affect(target, Preparation.class);

			return true;
		}
		else
			return false;
	}

	@Override
	public void detach()
	{
		if(target.invisible > 0)
			target.invisible--;
		super.detach();
	}

	@Override
	public int icon()
	{
		return BuffIndicator.INVISIBLE;
	}

	@Override
	public void fx(boolean on)
	{
		if(on) target.sprite.add(CharSprite.State.INVISIBLE);
		else if(target.invisible == 0) target.sprite.remove(CharSprite.State.INVISIBLE);
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc", dispTurns());
	}

	@Override
	public Class<? extends Blob> expulse()
	{
		return Darkness.class;
	}

	public static void dispel()
	{
		dispel(Dungeon.hero);

		CloakOfShadows.cloakStealth cloakBuff = Dungeon.hero.buff(CloakOfShadows.cloakStealth.class);
		if(cloakBuff != null)
			cloakBuff.detach();

		//this isn't a form of invisibilty, but it is meant to dispel at the same time as it.
		Hero.detachTimeFreeze();
	}

	public static void dispel(Char c)
	{
		Invisibility buff = c.buff(Invisibility.class);
		if(buff != null)
			buff.detach();
	}
}
