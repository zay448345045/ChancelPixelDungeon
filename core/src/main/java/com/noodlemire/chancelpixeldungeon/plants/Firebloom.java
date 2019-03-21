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

package com.noodlemire.chancelpixeldungeon.plants;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Fire;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.FireImbue;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.particles.FlameParticle;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHydrocombustion;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class Firebloom extends Plant
{
	{
		image = 0;
	}

	@Override
	public void activate(Char ch, boolean doWardenBonus)
	{
		if(doWardenBonus)
			Buff.affect(ch, FireImbue.class).set(15f);

		GameScene.add(Blob.seed(pos, 2, Fire.class));

		if(Dungeon.level.heroFOV[pos])
			CellEmitter.get(pos).burst(FlameParticle.FACTORY, 5);
	}

	public static class Seed extends Plant.Seed
	{
		{
			image = ItemSpriteSheet.SEED_FIREBLOOM;

			plantClass = Firebloom.class;
			alchemyClass = PotionOfHydrocombustion.class;
		}
	}
}
