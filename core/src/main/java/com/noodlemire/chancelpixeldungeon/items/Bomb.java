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
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.particles.BlastParticle;
import com.noodlemire.chancelpixeldungeon.effects.particles.SmokeParticle;
import com.noodlemire.chancelpixeldungeon.items.scrolls.EnvironmentScroll;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Bomb extends Item
{
	{
		image = ItemSpriteSheet.BOMB;

		defaultAction = AC_LIGHTTHROW;
		usesTargeting = true;
	}

	public Fuse fuse;

	//FIXME using a static variable for this is kinda gross, should be a better way
	private static boolean lightingFuse = false;

	private static final String AC_LIGHTTHROW = "LIGHTTHROW";

	@Override
	public boolean stackable()
	{
		return true;
	}

	@Override
	public boolean isSimilar(Item item)
	{
		return item instanceof Bomb && this.fuse == ((Bomb) item).fuse;
	}

	@Override
	public ArrayList<String> actions(Hero hero)
	{
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_LIGHTTHROW);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action)
	{
		if(action.equals(AC_LIGHTTHROW))
		{
			lightingFuse = true;
			action = AC_THROW;
		}
		else
			lightingFuse = false;

		super.execute(hero, action);
	}

	@Override
	protected void onThrow(int cell)
	{
		if(!Dungeon.level.pit[cell] && lightingFuse)
		{
			Actor.addDelayed(fuse = new Fuse().ignite(this), 2);
		}
		if(Actor.findChar(cell) != null && !(Actor.findChar(cell) instanceof Hero))
		{
			ArrayList<Integer> candidates = new ArrayList<>();
			for(int i : PathFinder.NEIGHBOURS8)
				if(Dungeon.level.passable[cell + i])
					candidates.add(cell + i);
			int newCell = candidates.isEmpty() ? cell : Random.element(candidates);
			Dungeon.level.drop(this, newCell).sprite.drop(cell);
		}
		else
			super.onThrow(cell);
	}

	@Override
	public boolean doPickUp(Hero hero)
	{
		if(fuse != null)
		{
			GLog.w(Messages.get(this, "snuff_fuse"));
			fuse = null;
		}
		return super.doPickUp(hero);
	}

	public void explode(int cell)
	{
		explode(cell, 1);
	}

	public void explode(int cell, int range)
	{
		//We're blowing up, so no need for a fuse anymore.
		this.fuse = null;

		Dungeon.playAt(Assets.SND_BLAST, cell);

		if(Dungeon.level.heroFOV[cell])
		{
			CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
		}

		boolean terrainAffected = false;
		boolean[] area = EnvironmentScroll.fovAt(cell, range, false);
		for(int c = 0; c < Dungeon.level.length(); c++)
		{
			if(area[c])
			{
				if(Dungeon.level.heroFOV[c])
					CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);

				if(Dungeon.level.flamable[c])
				{
					Dungeon.level.destroy(c);
					GameScene.updateMap(c);
					terrainAffected = true;
				}

				//destroys items / triggers bombs caught in the blast.
				Heap heap = Dungeon.level.heaps.get(c);
				if(heap != null)
					heap.explode();

				Char ch = Actor.findChar(c);
				if(ch != null)
				{
					//those not at the center of the blast take damage less consistently.
					int maxDamage = 10 + Dungeon.depth * 3;
					int minDamage = (int)Math.round(maxDamage * Math.pow(0.75, Dungeon.level.distance(c, ch.pos)));

					int dmg = Random.NormalIntRange(minDamage, maxDamage) - ch.drRoll();
					if(dmg > 0)
						ch.damage(dmg, this);

					if(ch == Dungeon.hero && !ch.isAlive())
						Dungeon.fail(getClass());
				}
			}
		}

		if(terrainAffected)
			Dungeon.observe();
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

	@Override
	public Item random()
	{
		switch(Random.Int(2))
		{
			case 0:
			default:
				return this;
			case 1:
				return new DoubleBomb();
		}
	}

	@Override
	public ItemSprite.Glowing glowing()
	{
		return fuse != null ? new ItemSprite.Glowing(0xFF0000, 0.6f) : null;
	}

	@Override
	public int price()
	{
		return 20 * quantity;
	}

	@Override
	public String desc()
	{
		if(fuse == null)
			return super.desc();
		else
			return Messages.get(this, "desc_burning");
	}

	private static final String FUSE = "fuse";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(FUSE, fuse);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		if(bundle.contains(FUSE))
			Actor.add(fuse = ((Fuse) bundle.get(FUSE)).ignite(this));
	}


	public static class Fuse extends Actor
	{

		{
			actPriority = BUFF_PRIO; //as if it were a buff
		}

		private Bomb bomb;

		public Fuse ignite(Bomb bomb)
		{
			this.bomb = bomb;
			return this;
		}

		@Override
		protected boolean act()
		{
			//something caused our bomb to explode early, or be defused. Do nothing.
			if(bomb.fuse != this)
			{
				Actor.remove(this);
				return true;
			}

			//look for our bomb, remove it from its heap, and blow it up.
			for(Heap heap : Dungeon.level.heaps.values())
			{
				if(heap.items.contains(bomb))
				{
					heap.items.remove(bomb);

					bomb.explode(heap.pos, 2);

					Actor.remove(this);
					return true;
				}
			}

			//can't find our bomb, something must have removed it, do nothing.
			bomb.fuse = null;
			Actor.remove(this);
			return true;
		}
	}


	public static class DoubleBomb extends Bomb
	{
		{
			image = ItemSpriteSheet.DBL_BOMB;
		}

		@Override
		public boolean stackable()
		{
			return false;
		}

		@Override
		public boolean doPickUp(Hero hero)
		{
			Bomb bomb = new Bomb();
			bomb.quantity(2);
			if(bomb.doPickUp(hero))
			{
				//isaaaaac.... (don't bother doing this when not in english)
				if(Messages.get(this, "name").equals("two bombs"))
					hero.sprite.showStatus(CharSprite.NEUTRAL, "1+1 free!");
				return true;
			}
			return false;
		}
	}
}
