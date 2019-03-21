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

import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Fire;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Magma;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Thief;
import com.noodlemire.chancelpixeldungeon.effects.particles.ElmoParticle;
import com.noodlemire.chancelpixeldungeon.items.Heap;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.armor.glyphs.Brimstone;
import com.noodlemire.chancelpixeldungeon.items.food.ChargrilledMeat;
import com.noodlemire.chancelpixeldungeon.items.food.MysteryMeat;
import com.noodlemire.chancelpixeldungeon.items.scrolls.Scroll;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfBlessing;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Burning extends DurationBuff implements Hero.Doom, Expulsion
{
	private static final float DURATION = 8f;

	//for tracking burning of hero items
	private int burnIncrement = 0;

	private static final String BURN = "burnIncrement";

	{
		type = buffType.NEGATIVE;
	}

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(BURN, burnIncrement);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		burnIncrement = bundle.getInt(BURN);
	}

	@Override
	public boolean act()
	{

		if(target.isAlive())
		{

			int damage = Random.NormalIntRange(1, 3 + target.HT() / 40);
			Buff.detach(target, Chill.class);

			if(target instanceof Hero)
			{

				Hero hero = (Hero) target;

				if(hero.belongings.armor != null && hero.belongings.armor.hasGlyph(Brimstone.class))
				{
					Buff.affect(target, Brimstone.BrimstoneShield.class);

				}
				else
				{

					hero.damage(damage, this);
					burnIncrement++;

					//at 4+ turns, there is a (turns-3)/3 chance an item burns
					if(Random.Int(3) < (burnIncrement - 3))
					{
						burnIncrement = 0;

						ArrayList<Item> burnable = new ArrayList<>();
						//does not reach inside of containers
						for(Item i : hero.belongings.backpack.items)
						{
							if((i instanceof Scroll && !(i instanceof ScrollOfUpgrade || i instanceof ScrollOfBlessing))
							   || i instanceof MysteryMeat)
							{
								burnable.add(i);
							}
						}

						if(!burnable.isEmpty())
						{
							Item toBurn = Random.element(burnable).detach(hero.belongings.backpack);
							if(toBurn instanceof MysteryMeat)
							{
								ChargrilledMeat steak = new ChargrilledMeat();
								if(!steak.collect(hero.belongings.backpack))
								{
									Dungeon.level.drop(steak, hero.pos).sprite.drop();
								}
							}
							Heap.burnFX(hero.pos);
							GLog.w(Messages.get(this, "burnsup", Messages.capitalize(toBurn.toString())));
						}
					}
				}

			}
			else
			{
				target.damage(damage, this);
			}

			if(target instanceof Thief)
			{

				Item item = ((Thief) target).item;

				if(item instanceof Scroll &&
				   !(item instanceof ScrollOfUpgrade || item instanceof ScrollOfBlessing))
				{
					target.sprite.emitter().burst(ElmoParticle.FACTORY, 6);
					((Thief) target).item = null;
				}
				else if(item instanceof MysteryMeat)
				{
					target.sprite.emitter().burst(ElmoParticle.FACTORY, 6);
					((Thief) target).item = new ChargrilledMeat();
				}
			}
		}
		else
		{
			Brimstone.BrimstoneShield brimShield = target.buff(Brimstone.BrimstoneShield.class);
			if(brimShield != null)
				brimShield.startDecay();

			detach();
		}

		if(Dungeon.level.flamable[target.pos] && Blob.volumeAt(target.pos, Fire.class) == 0)
			GameScene.add(Blob.seed(target.pos, 4, Fire.class));

		spend(TICK);
		shorten(TICK);

		if(!target.flying && Dungeon.level.water[target.pos] &&
		   (Blob.volumeAt(target.pos, Magma.class) == 0) || target.isImmune(Magma.class))
			detach();

		return true;
	}

	public void reignite()
	{
		set(DURATION);
	}

	@Override
	public int icon()
	{
		return BuffIndicator.FIRE;
	}

	@Override
	public void fx(boolean on)
	{
		if(on) target.sprite.add(CharSprite.State.BURNING);
		else target.sprite.remove(CharSprite.State.BURNING);
	}

	@Override
	public String heroMessage()
	{
		return Messages.get(this, "heromsg");
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc", dispTurns(left()));
	}

	@Override
	public Class<? extends Blob> expulse()
	{
		return Fire.class;
	}

	@Override
	public void onDeath()
	{
		Badges.validateDeathFromFire();

		Dungeon.fail(getClass());
		GLog.n(Messages.get(this, "ondeath"));
	}
}
