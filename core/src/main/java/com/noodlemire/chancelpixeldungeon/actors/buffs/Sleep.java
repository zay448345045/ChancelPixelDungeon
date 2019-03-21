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

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfExpulsion;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfLullaby;

public class Sleep extends FlavourBuff implements Expulsion
{
	public static final float SWS = 1.5f;

	@Override
	public void fx(boolean on)
	{
		if(on) target.sprite.idle();
	}

	@Override
	public Class<? extends Blob> expulse()
	{
		//If anyone finds a way to somehow drink a potion in their sleep, I'd love to know. 
		ScrollOfLullaby.singasong(PotionOfExpulsion.MAX_RANGE, false);
		return null;
	}
}
