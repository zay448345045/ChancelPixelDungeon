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

package com.noodlemire.chancelpixeldungeon.items.weapon.missiles.darts;

import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.PinCushion;
import com.noodlemire.chancelpixeldungeon.actors.hero.HeroSubClass;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.Recipe;
import com.noodlemire.chancelpixeldungeon.plants.BlandfruitBush;
import com.noodlemire.chancelpixeldungeon.plants.Blindweed;
import com.noodlemire.chancelpixeldungeon.plants.Deadnettle;
import com.noodlemire.chancelpixeldungeon.plants.Dreamfoil;
import com.noodlemire.chancelpixeldungeon.plants.Earthroot;
import com.noodlemire.chancelpixeldungeon.plants.Fadeleaf;
import com.noodlemire.chancelpixeldungeon.plants.Firebloom;
import com.noodlemire.chancelpixeldungeon.plants.Icecap;
import com.noodlemire.chancelpixeldungeon.plants.Plant;
import com.noodlemire.chancelpixeldungeon.plants.Rotberry;
import com.noodlemire.chancelpixeldungeon.plants.Sorrowmoss;
import com.noodlemire.chancelpixeldungeon.plants.Stormvine;
import com.noodlemire.chancelpixeldungeon.plants.Sungrass;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class TippedDart extends Dart
{
	{
		tier = 2;

		//so that slightly more than 1.5x durability is needed for 2 uses
		baseUses = 0.65f;
	}

	//exact same damage as regular darts, despite being higher tier.

	@Override
	protected void rangedHit(Char enemy, int cell)
	{
		super.rangedHit( enemy, cell);

		//need to spawn a dart
		if (durability <= 0)
		{
			//attempt to stick the dart to the enemy, just drop it if we can't.
			Dart d = new Dart();
			if (enemy.isAlive() && sticky)
			{
				PinCushion p = Buff.affect(enemy, PinCushion.class);
				if (p.target == enemy)
				{
					p.stick(d);
					return;
				}
			}

			Dungeon.level.drop( d, enemy.pos ).sprite.drop();
		}
	}

	@Override
	protected float durabilityPerUse()
	{
		float use = super.durabilityPerUse();

		if (Dungeon.hero.subClass == HeroSubClass.WARDEN)
			use /= 2f;

		return use;
	}

	@Override
	public int price()
	{
		//value of regular dart plus half of the seed
		return 8 * quantity;
	}

	private static HashMap<Class<? extends Plant.Seed>, Class<? extends TippedDart>> types = new HashMap<>();

	static
	{
		types.put(BlandfruitBush.Seed.class, HeavyDart.class);
		types.put(Blindweed.Seed.class, BlindingDart.class);
		types.put(Dreamfoil.Seed.class, SleepDart.class);
		types.put(Earthroot.Seed.class, ParalyticDart.class);
		types.put(Fadeleaf.Seed.class, DisplacingDart.class);
		types.put(Firebloom.Seed.class, IncendiaryDart.class);
		types.put(Icecap.Seed.class, ChillingDart.class);
		types.put(Rotberry.Seed.class, RotDart.class);
		types.put(Sorrowmoss.Seed.class, PoisonDart.class);
		types.put(Deadnettle.Seed.class, DemonDart.class);
		types.put(Stormvine.Seed.class, ShockingDart.class);
		types.put(Sungrass.Seed.class, HealingDart.class);
	}

	public static TippedDart randomTipped()
	{
		Plant.Seed s;
		do
		{
			s = (Plant.Seed) Generator.random(Generator.Category.SEED);
		}
		while(!types.containsKey(s.getClass()));

		try
		{
			return (TippedDart) types.get(s.getClass()).newInstance().quantity(2);
		}
		catch(Exception e)
		{
			ChancelPixelDungeon.reportException(e);
			return null;
		}
	}

	public static class TipDart extends Recipe
	{
		@Override
		//also sorts ingredients if it can
		public boolean testIngredients(ArrayList<Item> ingredients)
		{
			if(ingredients.size() != 2) return false;

			if(ingredients.get(0).getClass() == Dart.class)
			{
				if(!(ingredients.get(1) instanceof Plant.Seed))
					return false;
			}
			else if(ingredients.get(0) instanceof Plant.Seed)
			{
				if(ingredients.get(1).getClass() == Dart.class)
				{
					Item temp = ingredients.get(0);
					ingredients.set(0, ingredients.get(1));
					ingredients.set(1, temp);
				}
				else
					return false;
			}
			else
				return false;

			Plant.Seed seed = (Plant.Seed) ingredients.get(1);

			return ingredients.get(0).quantity() >= 2
			       && seed.quantity() >= 1
			       && types.containsKey(seed.getClass());
		}

		@Override
		public int cost(ArrayList<Item> ingredients)
		{
			return 2;
		}

		@Override
		public Item brew(ArrayList<Item> ingredients)
		{
			if(!testIngredients(ingredients)) return null;

			ingredients.get(0).quantity(ingredients.get(0).quantity() - 2);
			ingredients.get(1).quantity(ingredients.get(1).quantity() - 1);

			try
			{
				return types.get(ingredients.get(1).getClass()).newInstance().quantity(2);
			}
			catch(Exception e)
			{
				ChancelPixelDungeon.reportException(e);
				return null;
			}
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients)
		{
			if(!testIngredients(ingredients)) return null;

			try
			{
				return types.get(ingredients.get(1).getClass()).newInstance().quantity(2);
			}
			catch(Exception e)
			{
				ChancelPixelDungeon.reportException(e);
				return null;
			}
		}
	}
}
