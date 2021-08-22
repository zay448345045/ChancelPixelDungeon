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

package com.noodlemire.chancelpixeldungeon.actors.mobs.npcs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.effects.particles.ElmoParticle;
import com.noodlemire.chancelpixeldungeon.items.Heap;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ImpSprite;

public class ImpShopkeeper extends Shopkeeper
{
	{
		spriteClass = ImpSprite.class;

		EXP = 30;
	}

	private boolean seenBefore = false;

	@Override
	protected boolean act()
	{

		if(!seenBefore && Dungeon.level.heroFOV[pos])
		{
			yell(Messages.get(this, "greetings", Dungeon.hero.givenName()));
			seenBefore = true;
		}

		return super.act();
	}

	@Override
	public void flee()
	{
		for(Heap heap : Dungeon.level.heaps.values())
		{
			if(heap.type == Heap.Type.FOR_SALE)
			{
				CellEmitter.get(heap.pos).burst(ElmoParticle.FACTORY, 4);
				heap.destroy();
			}
		}

		destroy();

		sprite.emitter().burst(Speck.factory(Speck.WOOL), 15);
		sprite.killAndErase();
	}
}
