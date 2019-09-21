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
import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Blindness;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Invisibility;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MagicImmunity;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.ItemExistenceHandler;
import com.noodlemire.chancelpixeldungeon.items.Recipe;
import com.noodlemire.chancelpixeldungeon.items.Transmutable;
import com.noodlemire.chancelpixeldungeon.items.artifacts.UnstableSpellbook;
import com.noodlemire.chancelpixeldungeon.items.stones.Runestone;
import com.noodlemire.chancelpixeldungeon.items.stones.StoneOfAugmentation;
import com.noodlemire.chancelpixeldungeon.items.stones.StoneOfBinding;
import com.noodlemire.chancelpixeldungeon.items.stones.StoneOfBlast;
import com.noodlemire.chancelpixeldungeon.items.stones.StoneOfChallenge;
import com.noodlemire.chancelpixeldungeon.items.stones.StoneOfDistortion;
import com.noodlemire.chancelpixeldungeon.items.stones.StoneOfEquity;
import com.noodlemire.chancelpixeldungeon.items.stones.StoneOfFlock;
import com.noodlemire.chancelpixeldungeon.items.stones.StoneOfHypnotism;
import com.noodlemire.chancelpixeldungeon.items.stones.StoneOfIllusion;
import com.noodlemire.chancelpixeldungeon.items.stones.StoneOfIntuition;
import com.noodlemire.chancelpixeldungeon.items.stones.StoneOfPreservation;
import com.noodlemire.chancelpixeldungeon.items.stones.StoneOfShock;
import com.noodlemire.chancelpixeldungeon.journal.Catalog;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.HeroSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.noodlemire.chancelpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Scroll extends Item implements Transmutable
{
	private static final String AC_READ = "READ";
	static final String AC_SHOUT = "SHOUT";

	static final float TIME_TO_READ = 1f;

	protected int initials;

	private static final Class<?>[] scrolls =
			{
					ScrollOfRecharging.class,
					ScrollOfTeleportation.class,
					ScrollOfRage.class,
					ScrollOfTerror.class,
					ScrollOfLullaby.class,
					ScrollOfSupernova.class,
					ScrollOfReflection.class,
					ScrollOfDecay.class,
					ScrollOfBalance.class,
					ScrollOfDarkness.class,
					ScrollOfNecromancy.class,
					ScrollOfTransmutation.class,
					ScrollOfCharm.class,
					ScrollOfInsulation.class,
					ScrollOfTaunt.class,
					ScrollOfMagma.class
			};

	private static final Class<?>[] scrollsConstant =
			{
					ScrollOfUpgrade.class,
					ScrollOfBlessing.class,
					ScrollOfIdentify.class,
					ScrollOfCleansing.class,
			};

	//Credit to Chinook for Elder Futhark scroll rune names and sprites
	private static final HashMap<String, Integer> runes = new HashMap<String, Integer>()
	{
		{
			put("HAGLAZ", ItemSpriteSheet.SCROLL_HAGLAZ);
			put("SOWILO", ItemSpriteSheet.SCROLL_SOWILO);
			put("LAGUZ", ItemSpriteSheet.SCROLL_LAGUZ);
			put("DAGAZ", ItemSpriteSheet.SCROLL_DAGAZ);
			put("GYFU", ItemSpriteSheet.SCROLL_GYFU);
			put("RAIDO", ItemSpriteSheet.SCROLL_RAIDO);
			put("ISAZ", ItemSpriteSheet.SCROLL_ISAZ);
			put("KAUNAN", ItemSpriteSheet.SCROLL_KAUNAN);
			put("NAUDIZ", ItemSpriteSheet.SCROLL_NAUDIZ);
			put("JERA", ItemSpriteSheet.SCROLL_JERA);
			put("EHWAZ", ItemSpriteSheet.SCROLL_EHWAZ);
			put("TIWAZ", ItemSpriteSheet.SCROLL_TIWAZ);
		}
	};

	private static ItemExistenceHandler<Scroll> handler;

	private String rune;

	public boolean ownedByBook = false;

	{
		defaultAction = AC_READ;
		bones = true;
	}

	@Override
	public boolean stackable()
	{
		return image != ItemSpriteSheet.SCROLL_MYSTERY;
	}

	@SuppressWarnings("unchecked")
	public static void initLabels()
	{
		handler = new ItemExistenceHandler<>((Class<? extends Scroll>[]) scrolls, (Class<? extends Scroll>[]) scrollsConstant,
				runes, "mystery", ItemSpriteSheet.SCROLL_MYSTERY);
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
		handler = new ItemExistenceHandler<>((Class<? extends Scroll>[]) scrolls, (Class<? extends Scroll>[]) scrollsConstant,
				runes, "mystery", ItemSpriteSheet.SCROLL_MYSTERY, bundle);
	}

	public Scroll()
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
			rune = handler.label(this);
		}
	}

	@Override
	public ArrayList<String> actions(Hero hero)
	{
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_READ);
		actions.add(AC_SHOUT);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action)
	{
		super.execute(hero, action);

		if(action.equals(AC_READ) || action.equals(AC_SHOUT))
		{
			if(curUser.buff(MagicImmunity.class) != null)
				GLog.w(Messages.get(MagicImmunity.class, "no_magic"));
			else if(hero.buff(Blindness.class) != null)
				GLog.w(Messages.get(this, "blinded"));
			else if(hero.buff(UnstableSpellbook.bookRecharge.class) != null
			        && hero.buff(UnstableSpellbook.bookRecharge.class).isCursed()
			        && !(this instanceof ScrollOfCleansing))
				GLog.n(Messages.get(this, "cursed"));
			else
			{
				curUser = hero;
				curItem = this;

				if(action.equals(AC_READ))
					tryRead();
				else
				{
					Invisibility.dispel();
					tryShout();
				}

				setKnown();
			}
		}
	}

	private void tryRead()
	{
		if(isKnown() && (
				this instanceof ScrollOfDarkness ||
				this instanceof ScrollOfDecay ||
				this instanceof ScrollOfMagma ||
				this instanceof ScrollOfNecromancy
				))
		{
			GameScene.show(
					new WndOptions(Messages.get(this, "for_shouting"),
							Messages.get(this, "sure_read"),
							Messages.get(this, "yes"), Messages.get(this, "no"))
					{
						@Override
						protected void onSelect(int index)
						{
							if(index == 0)
							{
								curItem = detach(curUser.belongings.backpack);
								((Scroll)curItem).doRead();
							}
						}
					}
			);
		}
		else
		{
			curItem = detach(curUser.belongings.backpack);
			((Scroll)curItem).doRead();
		}
	}

	public abstract void doRead();

	//currently only used in scrolls owned by the unstable spellbook
	public abstract void empoweredRead();

	private void tryShout()
	{
		if(isKnown() && (
				this instanceof ScrollOfUpgrade ||
				this instanceof ScrollOfCharm ||
				this instanceof ScrollOfLullaby ||
				this instanceof ScrollOfBalance
				))
		{
			GameScene.show(
					new WndOptions(Messages.get(this, "for_reading"),
							Messages.get(this, "sure_shout"),
							Messages.get(this, "yes"), Messages.get(this, "no"))
					{
						@Override
						protected void onSelect(int index)
						{
							if(index == 0)
							{
								detach(curUser.belongings.backpack);
								doShout();
							}
						}
					}
			);
		}
		else
		{
			detach(curUser.belongings.backpack);
			doShout();
		}
	}

	public void doShout()
	{
		doRead();
		Sample.INSTANCE.play(Assets.SND_CHALLENGE);
	}

	protected void readAnimation()
	{
		curUser.spend(TIME_TO_READ);
		curUser.busy();
		((HeroSprite) curUser.sprite).read();
	}

	public boolean isKnown()
	{
		return handler != null && handler.isKnown(this);
	}

	public void setKnown()
	{
		if(!ownedByBook)
		{
			if(!isKnown() && image != ItemSpriteSheet.SCROLL_MYSTERY)
			{
				handler.know(this);
				updateQuickslot();
			}

			if(Dungeon.hero.isAlive())
				Catalog.setSeen(getClass());
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
		return isKnown() ? name : Messages.get(Scroll.class, rune);
	}

	@Override
	public String info()
	{
		return isKnown() ?
				desc() :
				image != ItemSpriteSheet.SCROLL_MYSTERY ?
						Messages.get(this, "unknown_desc") :
						Messages.get(this, "mystery_desc");
	}

	public Integer initials()
	{
		return isKnown() ? initials : null;
	}

	@Override
	public boolean isUpgradable()
	{
		return false;
	}

	@Override
	public boolean isIdentified()
	{
		return isKnown();
	}

	public static HashSet<Class<? extends Scroll>> getKnown()
	{
		return handler.known();
	}

	public static HashSet<Class<? extends Scroll>> getUnknown()
	{
		return handler.unknown();
	}

	@Override
	public Item transmute()
	{
		Scroll n;
		do
		{
			n = (Scroll) Generator.random(Generator.Category.SCROLL);
		}
		while(n == null || n.getClass().equals(getClass()) || n.getClass().equals(ScrollOfTransmutation.class));
		return n;
	}

	@Override
	public int price()
	{
		return 30 * quantity;
	}

	public static class ScrollToStone extends Recipe
	{
		private static HashMap<Class<? extends Scroll>, Class<? extends Runestone>> stones = new HashMap<>();

		static
		{
			stones.put(ScrollOfUpgrade.class, StoneOfPreservation.class);

			stones.put(ScrollOfBlessing.class, StoneOfAugmentation.class);

			stones.put(ScrollOfIdentify.class, StoneOfIntuition.class);

			stones.put(ScrollOfCleansing.class, StoneOfBinding.class);

			stones.put(ScrollOfLullaby.class, StoneOfHypnotism.class);
			stones.put(ScrollOfCharm.class, StoneOfHypnotism.class);

			stones.put(ScrollOfMagma.class, StoneOfBlast.class);
			stones.put(ScrollOfDecay.class, StoneOfBlast.class);

			stones.put(ScrollOfRecharging.class, StoneOfShock.class);
			stones.put(ScrollOfSupernova.class, StoneOfShock.class);

			stones.put(ScrollOfReflection.class, StoneOfFlock.class);
			stones.put(ScrollOfNecromancy.class, StoneOfFlock.class);

			stones.put(ScrollOfTerror.class, StoneOfIllusion.class);
			stones.put(ScrollOfDarkness.class, StoneOfIllusion.class);

			stones.put(ScrollOfTeleportation.class, StoneOfDistortion.class);
			stones.put(ScrollOfTransmutation.class, StoneOfDistortion.class);

			stones.put(ScrollOfRage.class, StoneOfChallenge.class);
			stones.put(ScrollOfTaunt.class, StoneOfChallenge.class);

			stones.put(ScrollOfBalance.class, StoneOfEquity.class);
			stones.put(ScrollOfInsulation.class, StoneOfEquity.class);
		}

		@Override
		public boolean testIngredients(ArrayList<Item> ingredients)
		{
			return ingredients.size() == 1
			       && ingredients.get(0) instanceof Scroll
			       && stones.containsKey(ingredients.get(0).getClass());
		}

		@Override
		public int cost(ArrayList<Item> ingredients)
		{
			return 0;
		}

		@Override
		public Item brew(ArrayList<Item> ingredients)
		{
			if(!testIngredients(ingredients)) return null;

			Scroll s = (Scroll) ingredients.get(0);

			s.quantity(s.quantity() - 1);

			try
			{
				return stones.get(s.getClass()).newInstance().quantity(3);
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
				Scroll s = (Scroll) ingredients.get(0);
				return stones.get(s.getClass()).newInstance().quantity(3);
			}
			catch(Exception e)
			{
				ChancelPixelDungeon.reportException(e);
				return null;
			}
		}
	}
}
