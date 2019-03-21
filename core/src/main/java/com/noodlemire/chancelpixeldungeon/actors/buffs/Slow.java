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
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Slow extends FlavourBuff implements Expulsion
{
	{
		type = buffType.NEGATIVE;
	}

	public static final float DURATION = 10f;

	@Override
	public int icon()
	{
		return BuffIndicator.SLOW;
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
		Buff.affect(target, Freeze.class).set(cooldown() + 1);
		return null;
	}

	public static class Freeze extends DurationBuff
	{
		{
			type = buffType.SILENT;
		}

		float partialTime = 1f;

		ArrayList<Integer> presses = new ArrayList<>();

		public void setDelayedPress(int cell)
		{
			if(!presses.contains(cell))
				presses.add(cell);
		}

		private void triggerPresses()
		{
			for(int cell : presses)
				Dungeon.level.press(cell, null, true);

			presses = new ArrayList<>();
		}

		@Override
		public boolean attachTo(Char target)
		{
			if(Dungeon.level != null)
				for(Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
					mob.sprite.add(CharSprite.State.PARALYSED);
			GameScene.freezeEmitters = true;
			return super.attachTo(target);
		}

		@Override
		public void detach()
		{
			for(Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
				mob.sprite.remove(CharSprite.State.PARALYSED);
			GameScene.freezeEmitters = false;

			super.detach();
			triggerPresses();
		}

		private static final String PRESSES = "presses";
		private static final String PARTIALTIME = "partialtime";

		@Override
		public void storeInBundle(Bundle bundle)
		{
			super.storeInBundle(bundle);

			int[] values = new int[presses.size()];
			for(int i = 0; i < values.length; i++)
				values[i] = presses.get(i);
			bundle.put(PRESSES, values);

			bundle.put(PARTIALTIME, partialTime);
		}

		@Override
		public void restoreFromBundle(Bundle bundle)
		{
			super.restoreFromBundle(bundle);

			int[] values = bundle.getIntArray(PRESSES);
			for(int value : values)
				presses.add(value);

			partialTime = bundle.getFloat(PARTIALTIME);
		}
	}
}
