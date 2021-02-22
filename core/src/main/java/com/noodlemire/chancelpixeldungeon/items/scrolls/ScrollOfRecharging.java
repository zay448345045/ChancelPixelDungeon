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

package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Recharging;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.SpellSprite;
import com.noodlemire.chancelpixeldungeon.effects.particles.EnergyParticle;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRecharging extends Scroll
{
	private static final float BUFF_DURATION = 30f;

	{
		icon = ItemSpriteSheet.Icons.SCROLL_RECHARGING;
	}

	@Override
	public void doRead()
	{
		Sample.INSTANCE.play(Assets.SND_READ);
		recharge();
	}

	private void recharge()
	{
		Buff.affect(curUser, Recharging.class).set(BUFF_DURATION);
		charge(curUser);

		GLog.i(Messages.get(this, "surge"));
		SpellSprite.show(curUser, SpellSprite.CHARGE);

		readAnimation();
	}

	@Override
	public void empoweredRead()
	{
		doRead();
		Buff.append(curUser, Recharging.class).set(BUFF_DURATION);
	}

	public static void charge(Hero hero)
	{
		hero.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 15);
	}

	@Override
	public int price()
	{
		return isKnown() ? 40 * quantity : super.price();
	}
}
