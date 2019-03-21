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
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Weakness;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.Flare;
import com.noodlemire.chancelpixeldungeon.effects.particles.ShadowParticle;
import com.noodlemire.chancelpixeldungeon.items.Heap;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.armor.Armor;
import com.noodlemire.chancelpixeldungeon.items.bags.Bag;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.CellSelector;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.noodlemire.chancelpixeldungeon.windows.WndBag;
import com.noodlemire.chancelpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class ScrollOfCleansing extends InventoryScroll
{
	private static boolean identifiedByUse = false;

	{
		initials = 8;
		mode = WndBag.Mode.UNIDED_OR_CURSED_OR_ENCHANTED;
	}

	@Override
	public void doShout()
	{
		identifiedByUse = !isKnown();

		GameScene.selectCell(cleanser);
	}

	@Override
	public void empoweredRead()
	{
		for(Item item : curUser.belongings)
			item.cursedKnown = true;

		Sample.INSTANCE.play(Assets.SND_READ);
		doRead();
	}

	@Override
	protected void onItemSelected(Item item)
	{
		doUncurseItem(item, curUser.pos);
	}

	private void onTileSelect(int cell)
	{
		Sample.INSTANCE.play(Assets.SND_CHALLENGE);
		Char ch = Actor.findChar(cell);
		Heap hp = Dungeon.level.heaps.get(cell);

		if(ch != null && (ch.properties().contains(Char.Property.DEMONIC) ||
		                  ch.properties().contains(Char.Property.UNDEAD)))
			uncurseEnemy(ch);
		else if(hp != null && !hp.isEmpty())
		{
			doUncurseItem(getFirstCursedOrEnchanted(hp), cell);
			hp.sprite.update();
		}
		else
			visual(cell, false, false);
	}

	private void doUncurseItem(Item item, int pos)
	{
		boolean procced = uncurse(curUser, item);

		Weakness.detach(curUser, Weakness.class);

		visual(pos, procced, false);
	}

	private static Item getFirstCursedOrEnchanted(Heap heap)
	{
		for(Item item : heap.items)
			if(item.cursed ||
			   ((item instanceof Armor) && ((Armor) item).hasGlyph(Armor.Glyph.class)) ||
			   ((item instanceof Weapon) && ((Weapon) item).hasEnchant(Weapon.Enchantment.class)))
				return item;

		return null;
	}

	private void uncurseEnemy(Char enemy)
	{
		if(enemy.properties().contains(Char.Property.BOSS) ||
		   enemy.properties().contains(Char.Property.MINIBOSS))
			Buff.affect(enemy, Weakness.class, Weakness.DURATION);
		else
			enemy.damage(enemy.HT(), this);

		visual(enemy.pos, true, true);
	}

	private static boolean uncurse(Hero hero, Item... items)
	{
		return uncurse(hero, true, items);
	}

	public static boolean uncurse(Hero hero, boolean wipeEnchants, Item... items)
	{
		boolean procced = false;
		for(Item item : items)
		{
			if(item != null && item.cursed)
			{
				item.cursed = false;
				procced = true;
			}
			if(item instanceof Weapon)
			{
				Weapon w = (Weapon) item;
				if((w.hasCurseEnchant() || (wipeEnchants && w.hasGoodEnchant())) && !w.preserved())
				{
					w.enchant(null);
					w.cursed = false;
					procced = true;
				}
			}
			if(item instanceof Armor)
			{
				Armor a = (Armor) item;
				if((a.hasCurseGlyph() || (wipeEnchants && a.hasGoodGlyph())) && !a.preserved())
				{
					a.inscribe(null);
					a.cursed = false;
					procced = true;
				}
			}
			if(item instanceof Bag) //Wait what
			{
				for(Item bagItem : ((Bag) item).items)
				{
					if(bagItem != null && bagItem.cursed)
					{
						bagItem.cursed = false;
						procced = true;
					}
				}
			}
		}

		if(procced)
			hero.updateHT(false); //for ring of might

		return procced;
	}

	private void visual(int pos, boolean success, boolean mob)
	{
		new Flare(6, 32).show(curUser.sprite, pos, 2f);

		if(success)
		{
			GLog.p(mob ? Messages.get(this, "cleansed_mob")
					: Messages.get(this, "cleansed"));

			Emitter shadowEmitter = GameScene.emitter();
			shadowEmitter.pos(curUser.sprite.worldToCamera(pos)
					.offset(curUser.sprite.width / 2, curUser.sprite.height / 2));
			shadowEmitter.on = true;
			shadowEmitter.start(ShadowParticle.UP, 0.05f, 10);
		}
		else
			GLog.i(Messages.get(this, "not_cleansed"));
	}

	@Override
	public int price()
	{
		return isKnown() ? 30 * quantity : super.price();
	}

	//FIXME Copying EnvironmentScroll implementation like this isn't efficient, but this already inherits from InventoryScroll.
	private void confirmCancelation()
	{
		GameScene.show(new WndOptions(Messages.titleCase(name()), Messages.get(this, "warning"),
				Messages.get(this, "yes"), Messages.get(this, "no"))
		{
			@Override
			protected void onSelect(int index)
			{
				switch(index)
				{
					case 0:
						curUser.spendAndNext(TIME_TO_READ);
						identifiedByUse = false;
						break;
					case 1:
						GameScene.selectCell(cleanser);
						break;
				}
			}

			public void onBackPressed()
			{
			}
		});
	}

	private static CellSelector.Listener cleanser = new CellSelector.Listener()
	{
		@Override
		public void onSelect(Integer target)
		{
			//FIXME this safety check shouldn't be necessary
			//it would be better to eliminate the curItem static variable.
			if(!(curItem instanceof ScrollOfCleansing))
			{
				return;
			}

			if(target != null)
			{
				((ScrollOfCleansing) curItem)
						.onTileSelect(new Ballistica(curUser.pos, target, Ballistica.SCROLL_SPELL).collisionPos);
				((Scroll) curItem).readAnimation();
			}
			else if(identifiedByUse && !((Scroll) curItem).ownedByBook)
				((ScrollOfCleansing) curItem).confirmCancelation();
			else if(!((Scroll) curItem).ownedByBook)
				curItem.collect(curUser.belongings.backpack);
		}

		@Override
		public String prompt()
		{
			return Messages.get(ScrollOfCleansing.class, "prompt");
		}
	};
}
