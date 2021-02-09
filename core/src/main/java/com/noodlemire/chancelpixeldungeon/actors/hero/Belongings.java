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

package com.noodlemire.chancelpixeldungeon.actors.hero;

import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.GamesInProgress;
import com.noodlemire.chancelpixeldungeon.QuickSlot;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.KindOfWeapon;
import com.noodlemire.chancelpixeldungeon.items.KindofMisc;
import com.noodlemire.chancelpixeldungeon.items.armor.Armor;
import com.noodlemire.chancelpixeldungeon.items.bags.Bag;
import com.noodlemire.chancelpixeldungeon.items.bags.MiscSlots;
import com.noodlemire.chancelpixeldungeon.items.keys.GoldenKey;
import com.noodlemire.chancelpixeldungeon.items.keys.IronKey;
import com.noodlemire.chancelpixeldungeon.items.keys.Key;
import com.noodlemire.chancelpixeldungeon.items.keys.SkeletonKey;
import com.noodlemire.chancelpixeldungeon.items.potions.Potion;
import com.noodlemire.chancelpixeldungeon.items.scrolls.Scroll;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfCleansing;
import com.noodlemire.chancelpixeldungeon.items.wands.Wand;
import com.noodlemire.chancelpixeldungeon.journal.Notes;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.Iterator;

public class Belongings implements Iterable<Item>
{
	public static final int BACKPACK_SIZE = 22;
	public static final int MAX_MISC_AMOUNT = 21;

	private final Hero owner;

	public Bag backpack;

	public MiscSlots miscSlots;

	public KindOfWeapon weapon = null;
	public Armor armor = null;
	public KindofMisc classMisc = null;

	public Belongings(Hero owner)
	{
		this.owner = owner;

		backpack = new Bag()
		{{
			name = Messages.get(Bag.class, "name");
			size = BACKPACK_SIZE;
		}};
		backpack.owner = owner;

		miscSlots = new MiscSlots(){{
			name = Messages.get(MiscSlots.class, "name");
			size = 0;
		}};
		miscSlots.owner = owner;
	}

	private static final String WEAPON = "weapon";
	private static final String ARMOR = "armor";
	private static final String CLASS_MISC = "class_misc";
	private static final String MISC_SLOTS = "misc_slots";

	public void storeInBundle(Bundle bundle)
	{
		backpack.storeInBundle(bundle);

		bundle.put(WEAPON, weapon);
		bundle.put(ARMOR, armor);
		bundle.put(CLASS_MISC, classMisc);

		Bundle miscs = new Bundle();
		miscSlots.storeInBundle(miscs);
		bundle.put(MISC_SLOTS, miscs);
	}

	public void restoreFromBundle(Bundle bundle)
	{
		//moving keys to Notes, for pre-0.6.1 saves
		if(bundle.contains("ironKeys"))
		{
			int[] ironKeys = bundle.getIntArray("ironKeys");
			for(int i = 0; i < ironKeys.length; i++)
			{
				if(ironKeys[i] > 0)
				{
					Notes.add((Key) new IronKey(i).quantity(ironKeys[i]));
				}
			}
		}

		if(bundle.contains("specialKeys"))
		{
			int[] specialKeys = bundle.getIntArray("specialKeys");
			for(int i = 0; i < specialKeys.length; i++)
			{
				if(specialKeys[i] > 0)
				{
					if(i % 5 == 0)
					{
						Notes.add((Key) new SkeletonKey(i).quantity(specialKeys[i]));
					}
					else
					{
						Notes.add((Key) new GoldenKey(i).quantity(specialKeys[i]));
					}
				}
			}
		}

		backpack.clear();
		backpack.restoreFromBundle(bundle);

		miscSlots.clear();
		miscSlots.size = owner.slot_lvl;
		if(bundle.contains(MISC_SLOTS))
			miscSlots.restoreFromBundle(bundle.getBundle(MISC_SLOTS));

		weapon = (KindOfWeapon) bundle.get(WEAPON);
		if(weapon != null)
			weapon.activate(owner);

		armor = (Armor) bundle.get(ARMOR);
		if(armor != null)
			armor.activate(owner);

		classMisc = (KindofMisc) bundle.get(CLASS_MISC);
		if(classMisc != null)
			classMisc.activate(owner);
	}

	static void preview(GamesInProgress.Info info, Bundle bundle)
	{
		if(bundle.contains(ARMOR))
			info.armorTier = ((Armor) bundle.get(ARMOR)).tier;
		else
			info.armorTier = 0;
	}

	@SuppressWarnings("unchecked")
	public <T extends Item> T getItem(Class<T> itemClass)
	{
		for(Item item : this)
			if(itemClass.isInstance(item))
				return (T) item;

		return null;
	}

	public Item getSimilar(Item similar)
	{
		for(Item item : this)
			if(item != similar && item.isSimilar(similar))
				return item;

		return null;
	}

	public void identify()
	{
		for(Item item : this)
			item.identify();
	}

	public void observe()
	{
		if(weapon != null)
		{
			weapon.identify();
			Badges.validateItemLevelAquired(weapon);
		}

		if(armor != null)
		{
			armor.identify();
			Badges.validateItemLevelAquired(armor);
		}

		for(Item misc : miscSlots)
		{
			misc.identify();
			Badges.validateItemLevelAquired(misc);
		}

		for(Item item : backpack)
		{
			item.cursedKnown = true;

			if(item instanceof Potion)
				((Potion)item).setDangerKnown();
			else if(item instanceof Scroll)
				((Scroll)item).setDangerKnown();
		}
	}

	public void uncurseEquipped()
	{
		ScrollOfCleansing.uncurse(owner, false, armor, weapon);//, misc1, misc2);
	}

	public Item randomUnequipped()
	{
		return Random.element(backpack.items);
	}

	public void resurrect(int depth)
	{
		for(Item item : backpack.items.toArray(new Item[0]))
		{
			if(item instanceof Key)
			{
				if(((Key) item).depth == depth)
					item.detachAll(backpack);
			}
			else if(item.unique)
			{
				item.detachAll(backpack);
				//you keep the bag itself, not its contents.
				if(item instanceof Bag)
				{
					((Bag) item).resurrect();
				}
				item.collect();
			}
			else if(!item.isEquipped(owner) && !QuickSlot.hasItem(item))
			{
				item.detachAll(backpack);
			}
		}

		if(weapon != null)
		{
			weapon.cursed = false;
			weapon.activate(owner);
		}

		if(armor != null)
		{
			armor.cursed = false;
			armor.activate(owner);
		}

		for(Item misc : miscSlots)
		{
			misc.cursed = false;
			((KindofMisc)misc).activate(owner);
		}
	}

	public int charge(float charge)
	{
		int count = 0;

		for(Wand.Charger charger : owner.buffs(Wand.Charger.class))
		{
			charger.gainCharge(charge);
			count++;
		}

		return count;
	}

	@Override
	public Iterator<Item> iterator()
	{
		return new ItemIterator();
	}

	private class ItemIterator implements Iterator<Item>
	{
		private int index = 0;

		private Iterator<Item> backpackIterator = backpack.iterator();

		private Iterator<Item> miscIterator = miscSlots.iterator();

		private Item[] equipped = {weapon, armor};
		private int backpackIndex = equipped.length;

		@Override
		public boolean hasNext()
		{
			for(int i = index; i < backpackIndex; i++)
			{
				if(equipped[i] != null)
				{
					return true;
				}
			}

			if(miscIterator.hasNext())
				return true;

			return backpackIterator.hasNext();
		}

		@Override
		public Item next()
		{
			while(index < backpackIndex)
			{
				Item item = equipped[index++];

				if(item != null)
					return item;
			}

			if(miscIterator.hasNext())
				return miscIterator.next();

			return backpackIterator.next();
		}

		@Override
		public void remove()
		{
			switch(index)
			{
				case 0:
					equipped[0] = weapon = null;
					break;
				case 1:
					equipped[1] = armor = null;
					break;
				default:
					if(miscIterator.hasNext())
						miscIterator.remove();
					else
						backpackIterator.remove();
			}
		}
	}
}
