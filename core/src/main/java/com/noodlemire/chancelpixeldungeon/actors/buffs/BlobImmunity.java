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
import com.noodlemire.chancelpixeldungeon.actors.blobs.ConfusionGas;
import com.noodlemire.chancelpixeldungeon.actors.blobs.CorrosiveGas;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Darkness;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Electricity;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Fire;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Freezing;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Magma;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ParalyticGas;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Regrowth;
import com.noodlemire.chancelpixeldungeon.actors.blobs.RootCloud;
import com.noodlemire.chancelpixeldungeon.actors.blobs.StenchGas;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ThunderCloud;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ToxicGas;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Web;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfPurity;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;

public class BlobImmunity extends FlavourBuff implements Expulsion
{
	public static final float DURATION = 20f;

	@Override
	public int icon()
	{
		return BuffIndicator.IMMUNITY;
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	{
		//all harmful blobs
		immunities.add(ConfusionGas.class);
		immunities.add(CorrosiveGas.class);
		immunities.add(Darkness.class);
		immunities.add(Electricity.class);
		immunities.add(Fire.class);
		immunities.add(Freezing.class);
		immunities.add(Magma.class);
		immunities.add(ParalyticGas.class);
		immunities.add(Regrowth.class);
		immunities.add(RootCloud.class);
		immunities.add(StenchGas.class);
		immunities.add(ThunderCloud.class);
		immunities.add(ToxicGas.class);
		immunities.add(Web.class);
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc", dispTurns());
	}

	@Override
	public Class<? extends Blob> expulse()
	{
		PotionOfPurity.purifyBlobs(target.pos);
		return null;
	}
}
