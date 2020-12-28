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

package com.noodlemire.chancelpixeldungeon.items.potions;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.Challenges;
import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.Statistics;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Fire;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Burning;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Invisibility;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Ooze;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.Splash;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.ItemExistenceHandler;
import com.noodlemire.chancelpixeldungeon.items.Recipe;
import com.noodlemire.chancelpixeldungeon.items.Transmutable;
import com.noodlemire.chancelpixeldungeon.journal.Catalog;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.plants.Plant;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.noodlemire.chancelpixeldungeon.windows.WndBag;
import com.noodlemire.chancelpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Potion extends Item implements Transmutable
{
	private static final String AC_DRINK = "DRINK";

	private static final float TIME_TO_DRINK = 1f;

	protected Integer initials;

	private static final Class<?>[] potions =
			{
					PotionOfCorrosivity.class,
					PotionOfExperience.class,
					PotionOfToxicity.class,
					PotionOfEnticement.class,
					PotionOfHaste.class,
					PotionOfShockwave.class,
					PotionOfOvergrowth.class,
					PotionOfTelepathy.class,
					PotionOfPurity.class,
					PotionOfPlacebo.class,
					PotionOfMight.class,
					PotionOfFrost.class,
					PotionOfExpulsion.class,
					PotionOfRepulsion.class,
					PotionOfShielding.class,
					PotionOfThunderstorm.class
			};

	private static final Class<?>[] potionsConstant =
			{
					PotionOfHealing.class,
					PotionOfInvisibility.class,
					PotionOfLevitation.class,
					PotionOfHydrocombustion.class
			};

	private static final HashMap<String, Integer> colors = new HashMap<String, Integer>()
	{
		{
			put("crimson", ItemSpriteSheet.POTION_CRIMSON);
			put("amber", ItemSpriteSheet.POTION_AMBER);
			put("golden", ItemSpriteSheet.POTION_GOLDEN);
			put("jade", ItemSpriteSheet.POTION_JADE);
			put("turquoise", ItemSpriteSheet.POTION_TURQUOISE);
			put("azure", ItemSpriteSheet.POTION_AZURE);
			put("indigo", ItemSpriteSheet.POTION_INDIGO);
			put("magenta", ItemSpriteSheet.POTION_MAGENTA);
			put("bistre", ItemSpriteSheet.POTION_BISTRE);
			put("charcoal", ItemSpriteSheet.POTION_CHARCOAL);
			put("silver", ItemSpriteSheet.POTION_SILVER);
			put("ivory", ItemSpriteSheet.POTION_IVORY);
		}
	};

	private static ItemExistenceHandler<Potion> handler;

	private String color;

	public boolean ownedByFruit = false;

	{
		defaultAction = AC_DRINK;
	}

	@Override
	public boolean stackable()
	{
		return image != ItemSpriteSheet.POTION_UNSTABLE;
	}

	@SuppressWarnings("unchecked")
	public static void initColors()
	{
		handler = new ItemExistenceHandler<>((Class<? extends Potion>[]) potions, (Class<? extends Potion>[]) potionsConstant,
				colors, "unstable", ItemSpriteSheet.POTION_UNSTABLE);
	}

	public static void save(Bundle bundle)
	{
		handler.save(bundle);
	}

	public static void saveSelectively(Bundle bundle, ArrayList<Item> items)
	{
		handler.saveSelectively(bundle, items);
	}

	@SuppressWarnings("unchecked")
	public static void restore(Bundle bundle)
	{
		handler = new ItemExistenceHandler<>((Class<? extends Potion>[]) potions, (Class<? extends Potion>[]) potionsConstant,
				colors, "unstable", ItemSpriteSheet.POTION_UNSTABLE, bundle);
	}

	public Potion()
	{
		super();
		reset();
	}

	@Override
	public void reset()
	{
		super.reset();
		if(handler != null)
		{
			image = handler.image(this);
			color = handler.label(this);
		}
	}

	@Override
	public ArrayList<String> actions(Hero hero)
	{
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_DRINK);
		return actions;
	}

	@Override
	public void execute(final Hero hero, String action)
	{
		super.execute(hero, action);

		if(action.equals(AC_DRINK))
		{
			if(isKnown() && (
					this instanceof PotionOfHydrocombustion ||
					this instanceof PotionOfToxicity ||
					this instanceof PotionOfShockwave ||
					this instanceof PotionOfCorrosivity ||
					this instanceof PotionOfThunderstorm))
			{
				GameScene.show(
						new WndOptions(Messages.get(this, "harmful"),
								Messages.get(this, "sure_drink"),
								Messages.get(this, "yes"), Messages.get(this, "no"))
						{
							@Override
							protected void onSelect(int index)
							{
								if(index == 0)
									drink(hero);
							}
						}
				);
			}
			else
				drink(hero);
		}
	}

	@Override
	public void doThrow(final Hero hero)
	{
		if(isKnown() && (
				this instanceof PotionOfExperience ||
				this instanceof PotionOfHealing ||
				this instanceof PotionOfTelepathy ||
				this instanceof PotionOfHaste ||
				this instanceof PotionOfInvisibility ||
				this instanceof PotionOfMight ||
				this instanceof PotionOfShielding ||
				this instanceof PotionOfExpulsion))
		{
			GameScene.show(
					new WndOptions(Messages.get(this, "beneficial"),
							Messages.get(this, "sure_throw"),
							Messages.get(this, "yes"), Messages.get(this, "no"))
					{
						@Override
						protected void onSelect(int index)
						{
							if(index == 0)
								Potion.super.doThrow(hero);
						}
					}
			);
		}
		else
			super.doThrow(hero);
	}

	protected void drink(Hero hero)
	{
		detach(hero.belongings.backpack);

		hero.spend(TIME_TO_DRINK);
		hero.busy();
		apply(hero);
		setKnown();

		Sample.INSTANCE.play(Assets.SND_DRINK);

		hero.sprite.operate(hero.pos);
	}

	@Override
	protected void onThrow(int cell)
	{
		Invisibility.dispel();

		if(Dungeon.level.map[cell] == Terrain.WELL || Dungeon.level.pit[cell])
			super.onThrow(cell);
		else
		{
			Dungeon.level.press(cell, null, true);
			shatter(cell);
			setKnown();
		}
	}

	public void apply(Hero hero)
	{
		shatter(hero.pos);
	}

	public void shatter(int cell)
	{
		if(Dungeon.level.heroFOV[cell])
		{
			GLog.i(Messages.get(Potion.class, "shatter"));
			splash(cell);
		}

		Dungeon.playAt(Assets.SND_SHATTER, cell);
	}

	@Override
	public void cast(final Hero user, int dst)
	{
		super.cast(user, dst);
	}

	public boolean isKnown()
	{
		return handler != null && handler.isKnown(this);
	}

	public void setKnown()
	{
		if(!ownedByFruit)
		{
			if(!isKnown() && image != ItemSpriteSheet.POTION_UNSTABLE)
			{
				handler.know(this);
				updateQuickslot();
			}

			if(Dungeon.hero.isAlive())
			{
				Catalog.setSeen(getClass());
			}
		}
	}

	@Override
	public Item identify()
	{

		setKnown();
		return super.identify();
	}

	@Override
	public String name()
	{
		return isKnown() ? super.name() : Messages.get(Potion.class, color);
	}

	@Override
	public String info()
	{
		return isKnown() ?
				desc() :
				image != ItemSpriteSheet.POTION_UNSTABLE ?
						Messages.get(Potion.class, "unknown_desc") :
						Messages.get(Potion.class, "unstable_desc");
	}

	public Integer initials()
	{
		return isKnown() ? initials : null;
	}

	@Override
	public boolean isIdentified()
	{
		return isKnown();
	}

	@Override
	public boolean isUpgradable()
	{
		return false;
	}

	public static HashSet<Class<? extends Potion>> getKnown()
	{
		return handler.known();
	}

	public static HashSet<Class<? extends Potion>> getUnknown()
	{
		return handler.unknown();
	}

	protected void splash(int cell)
	{
		Fire fire = (Fire) Dungeon.level.blobs.get(Fire.class);
		if(fire != null)
			fire.clear(cell);

		final int color = ItemSprite.pick(image, 8, 10);

		Char ch = Actor.findChar(cell);
		if(ch != null)
		{
			Buff.detach(ch, Burning.class);
			Buff.detach(ch, Ooze.class);
			Splash.at(ch.sprite.center(), color, 5);
		}
		else
			Splash.at(cell, color, 5);
	}

	@Override
	public int price()
	{
		return 30 * quantity;
	}

	@Override
	public Item transmute()
	{
		Potion n;
		do
		{
			n = (Potion) Generator.random(Generator.Category.POTION);
		}
		while(n == null || n.getClass() == getClass());
		return n;
	}

	public static class RandomPotion extends Recipe
	{

		@Override
		public boolean testIngredients(ArrayList<Item> ingredients)
		{
			if(ingredients.size() != 3)
			{
				return false;
			}

			for(Item ingredient : ingredients)
			{
				if(!(ingredient instanceof Plant.Seed && ingredient.quantity() >= 1))
				{
					return false;
				}
			}
			return true;
		}

		@Override
		public int cost(ArrayList<Item> ingredients)
		{
			return 1;
		}

		@Override
		public Item brew(ArrayList<Item> ingredients)
		{
			if(!testIngredients(ingredients)) return null;

			for(Item ingredient : ingredients)
			{
				ingredient.quantity(ingredient.quantity() - 1);
			}

			Item result = null;

			if(Random.Int(3) == 0)
			{

				result = Generator.random(Generator.Category.POTION);

			}
			else
			{

				Class<? extends Item> itemClass = ((Plant.Seed) Random.element(ingredients)).alchemyClass;

				try
				{
					result = itemClass.newInstance();
				}
				catch(Exception e)
				{
					ChancelPixelDungeon.reportException(e);
				}

				if(result == null || result.image == ItemSpriteSheet.POTION_UNSTABLE)
				{
					itemClass = ((Plant.Seed) Random.element(ingredients)).alchemyClassSecondary;

					try
					{
						result = itemClass.newInstance();
					}
					catch(Exception e)
					{
						ChancelPixelDungeon.reportException(e);
					}
				}

				if(result == null || result.image == ItemSpriteSheet.POTION_UNSTABLE)
				{
					itemClass = ((Plant.Seed) Random.element(ingredients)).alchemyClassFinal;

					try
					{
						result = itemClass.newInstance();
					}
					catch(Exception e)
					{
						ChancelPixelDungeon.reportException(e);
					}
				}
			}

			while(result == null)
				result = Generator.random(Generator.Category.POTION);

			while(result instanceof PotionOfHealing
			      && (Dungeon.isChallenged(Challenges.NO_HEALING)
			          || Random.Int(10) < Dungeon.LimitedDrops.COOKING_HP.count))
			{
				result = Generator.random(Generator.Category.POTION);
			}

			if(result instanceof PotionOfHealing)
			{
				Dungeon.LimitedDrops.COOKING_HP.count++;
			}

			Statistics.potionsCooked++;
			Badges.validatePotionsCooked();

			return result;
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients)
		{
			return new WndBag.Placeholder(ItemSpriteSheet.POTION_HOLDER)
			{
				{
					name = Messages.get(RandomPotion.class, "name");
				}

				@Override
				public String info()
				{
					return "";
				}
			};
		}
	}
}
