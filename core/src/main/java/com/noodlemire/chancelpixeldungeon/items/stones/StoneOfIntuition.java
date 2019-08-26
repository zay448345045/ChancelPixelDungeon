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

package com.noodlemire.chancelpixeldungeon.items.stones;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.effects.Identification;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.potions.Potion;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfCorrosivity;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfEnticement;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfExperience;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfExpulsion;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfFrost;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHaste;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHealing;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHydrocombustion;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfInvisibility;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfLevitation;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfMight;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfOvergrowth;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfPlacebo;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfPurity;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfRepulsion;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfShielding;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfShockwave;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfTelepathy;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfThunderstorm;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfToxicity;
import com.noodlemire.chancelpixeldungeon.items.scrolls.Scroll;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfBalance;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfBlessing;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfCharm;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfCleansing;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfDarkness;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfDecay;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfInsulation;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfMagma;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfNecromancy;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfRage;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfReflection;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfSupernova;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTaunt;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTerror;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.scenes.PixelScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.ui.IconButton;
import com.noodlemire.chancelpixeldungeon.ui.RedButton;
import com.noodlemire.chancelpixeldungeon.ui.RenderedTextMultiline;
import com.noodlemire.chancelpixeldungeon.ui.Window;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.noodlemire.chancelpixeldungeon.windows.IconTitle;
import com.noodlemire.chancelpixeldungeon.windows.WndBag;
import com.watabou.noosa.Image;

import java.util.HashSet;

public class StoneOfIntuition extends InventoryStone
{
	{
		mode = WndBag.Mode.UNIDED_POTION_OR_SCROLL;
		image = ItemSpriteSheet.STONE_ISAZ;
	}

	@Override
	protected void onItemSelected(Item item)
	{
		GameScene.show( new WndGuess(item));
	}

	//Ordered by initials

	public static Class[] potions = new Class[]
			{
					PotionOfExperience.class,
					PotionOfFrost.class,
					PotionOfHealing.class,
					PotionOfInvisibility.class,
					PotionOfLevitation.class,
					PotionOfHydrocombustion.class,
					PotionOfMight.class,
					PotionOfTelepathy.class,
					PotionOfShockwave.class,
					PotionOfPurity.class,
					PotionOfHaste.class,
					PotionOfToxicity.class,
					PotionOfEnticement.class,
					PotionOfOvergrowth.class,
					PotionOfShielding.class,
					PotionOfCorrosivity.class,
					PotionOfThunderstorm.class,
					PotionOfExpulsion.class,
					PotionOfRepulsion.class,
					PotionOfPlacebo.class,
			};

	public static Class[] scrolls = new Class[]
			{
					ScrollOfIdentify.class,
					ScrollOfLullaby.class,
					ScrollOfBlessing.class,
					ScrollOfCharm.class,
					ScrollOfReflection.class,
					ScrollOfSupernova.class,
					ScrollOfRage.class,
					ScrollOfRecharging.class,
					ScrollOfCleansing.class,
					ScrollOfTeleportation.class,
					ScrollOfTerror.class,
					ScrollOfUpgrade.class,
					ScrollOfDecay.class,
					ScrollOfBalance.class,
					ScrollOfDarkness.class,
					ScrollOfNecromancy.class,
					ScrollOfInsulation.class,
					ScrollOfTaunt.class,
					ScrollOfMagma.class,
					ScrollOfTransmutation.class,
			};

	static Class curGuess = null;

	public class WndGuess extends Window
	{
		private static final int WIDTH = 120;
		private static final int BTN_SIZE = 20;

		public WndGuess(final Item item)
		{
			IconTitle titlebar = new IconTitle();
			titlebar.icon(new ItemSprite(ItemSpriteSheet.STONE_ISAZ, null));
			titlebar.label(Messages.get(StoneOfIntuition.class, "name"));
			titlebar.setRect(0, 0, WIDTH, 0);
			add(titlebar);

			RenderedTextMultiline text = PixelScene.renderMultiline(6);
			text.text(Messages.get(this, "text"));
			text.setPos(0, titlebar.bottom());
			text.maxWidth(WIDTH);
			add(text);

			final RedButton guess = new RedButton("")
			{
				@Override
				protected void onClick()
				{
					super.onClick();
					useAnimation();
					if(item.getClass() == curGuess)
					{
						item.identify();
						GLog.p(Messages.get(WndGuess.class, "correct"));
						curUser.sprite.parent.add(new Identification(curUser.sprite.center().offset(0, -16)));
					}
					else
						GLog.n(Messages.get(WndGuess.class, "incorrect"));

					curGuess = null;
					hide();
				}
			};

			guess.visible = false;
			guess.icon(new ItemSprite(item));
			guess.enable(false);
			guess.setRect(0, 80, WIDTH, 20);
			add(guess);

			float left;
			float top = text.bottom() + 5;
			int rows;
			int placed = 0;

			HashSet<Class<? extends Item>> unIDed = new HashSet<>();
			final Class[] all;

			final int row;
			if(item.isIdentified())
			{
				hide();
				return;
			}
			else if(item instanceof Potion)
			{
				unIDed.addAll(Potion.getUnknown());
				all = potions.clone();
				row = 0;
			}
			else if(item instanceof Scroll)
			{
				unIDed.addAll(Scroll.getUnknown());
				all = scrolls.clone();
				row = 7;
			}
			else
			{
				hide();
				return;
			}

			int sizefactor = unIDed.size() / 6;

			rows = sizefactor + 1;
			//top += BTN_SIZE * sizefactor / 2;
			left = (WIDTH - BTN_SIZE * ((unIDed.size() + 1) / (sizefactor + 1))) / (sizefactor + 1f);

			for(int i = 0; i < all.length; i++)
			{
				if(!unIDed.contains(all[i]))
					continue;

				final int j = i;
				IconButton btn = new IconButton()
				{
					@Override
					protected void onClick()
					{
						curGuess = all[j];
						guess.visible = true;
						guess.text(Messages.titleCase(Messages.get(curGuess, "name")));
						guess.enable(true);
						super.onClick();
					}
				};
				Image im = new Image(Assets.CONS_ICONS, 7 * i, row, 7, 7);
				im.scale.set(2f);
				btn.icon(im);
				btn.setRect(left + placed * BTN_SIZE, top, BTN_SIZE, BTN_SIZE);
				add(btn);

				placed++;
				if(rows >= 2 && placed == ((unIDed.size() + 1) / (sizefactor + 1)))
				{
					placed = 0;

					top += BTN_SIZE;
				}
			}

			resize(WIDTH, (int)Math.max(top + BTN_SIZE * 2, 100));
			if(top + BTN_SIZE * 2 > 100)
				guess.setPos(0, top + BTN_SIZE);
		}


		@Override
		public void onBackPressed()
		{
			super.onBackPressed();
			new StoneOfIntuition().collect();
		}
	}
}