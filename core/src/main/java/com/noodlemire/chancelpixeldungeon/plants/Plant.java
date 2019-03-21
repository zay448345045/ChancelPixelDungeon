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

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Challenges;
import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Barkskin;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.hero.HeroSubClass;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.particles.LeafParticle;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.Transmutable;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public abstract class Plant implements Bundlable
{
	public String plantName = Messages.get(this, "name");

	public int image;
	public int pos;

	public void trigger()
	{
		Char ch = Actor.findChar(pos);

		if(ch instanceof Hero)
		{
			((Hero) ch).interrupt();
			if(((Hero) ch).subClass == HeroSubClass.WARDEN)
				Buff.affect(ch, Barkskin.class).set(ch.HT() / 3f);
		}

		wither();
		activate(ch);
	}

	public void activate(Char ch)
	{
		activate(ch, ch instanceof Hero && ((Hero)ch).subClass == HeroSubClass.WARDEN);
	}

	public abstract void activate(Char ch, boolean doWardenBonus);

	public void wither()
	{
		Dungeon.level.uproot(pos);

		if(Dungeon.level.heroFOV[pos])
			CellEmitter.get(pos).burst(LeafParticle.GENERAL, 6);
	}

	private static final String POS = "pos";

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		pos = bundle.getInt(POS);
	}

	@Override
	public void storeInBundle(Bundle bundle)
	{
		bundle.put(POS, pos);
	}

	public String desc()
	{
		return Messages.get(this, "desc");
	}

	public static class Seed extends Item implements Transmutable
	{
		static final String AC_PLANT = "PLANT";

		private static final float TIME_TO_PLANT = 1f;

		{
			defaultAction = AC_THROW;
		}

		protected Class<? extends Plant> plantClass;

		public Class<? extends Item> alchemyClass;
		public Class<? extends Item> alchemyClassSecondary;
		public Class<? extends Item> alchemyClassFinal;

		@Override
		public boolean stackable()
		{
			return true;
		}

		@Override
		public ArrayList<String> actions(Hero hero)
		{
			ArrayList<String> actions = super.actions(hero);
			actions.add(AC_PLANT);
			return actions;
		}

		@Override
		protected void onThrow(int cell)
		{
			if(Dungeon.level.map[cell] == Terrain.ALCHEMY
			   || Dungeon.level.pit[cell]
			   || Dungeon.level.traps.get(cell) != null
			   || Dungeon.isChallenged(Challenges.NO_HERBALISM))
			{
				super.onThrow(cell);
			}
			else
			{
				Dungeon.level.plant(this, cell);
			}
		}

		@Override
		public void execute(Hero hero, String action)
		{

			super.execute(hero, action);

			if(action.equals(AC_PLANT))
			{

				hero.spend(TIME_TO_PLANT);
				hero.busy();
				((Seed) detach(hero.belongings.backpack)).onThrow(hero.pos);

				hero.sprite.operate(hero.pos);

			}
		}

		public Plant couch(int pos, Level level)
		{
			try
			{
				if(level != null && level.heroFOV != null && level.heroFOV[pos])
				{
					Sample.INSTANCE.play(Assets.SND_PLANT);
				}
				Plant plant = plantClass.newInstance();
				plant.pos = pos;
				return plant;
			}
			catch(Exception e)
			{
				ChancelPixelDungeon.reportException(e);
				return null;
			}
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
		public int price()
		{
			return 10 * quantity;
		}

		@Override
		public String desc()
		{
			return Messages.get(plantClass, "desc");
		}

		@Override
		public String info()
		{
			return Messages.get(Seed.class, "info", desc());
		}

		@Override
		public Item transmute()
		{
			Item n;
			do
			{
				n = Generator.random(Generator.Category.SEED);
			}
			while(n.getClass() == getClass());

			return n;
		}
	}
}
