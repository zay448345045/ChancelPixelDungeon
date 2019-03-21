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

import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ItemExistenceHandler<T extends Item>
{
	private Class<? extends T>[] items, constantItems;
	private HashMap<Class<? extends T>, String> itemLabels;
	private HashMap<String, Integer> labelImages;
	private HashSet<Class<? extends T>> known;

	public ItemExistenceHandler(Class<? extends T>[] items, Class<? extends T>[] constantItems,
	                         HashMap<String, Integer> imageLabels, String defaultLabel, int defaultImage)
	{
		this.items = items;
		this.constantItems = constantItems;

		imageLabels.remove(defaultLabel);

		itemLabels = new HashMap<>();
		labelImages = new HashMap<>(imageLabels);
		known = new HashSet<>();

		ArrayList<String> labelsLeft = new ArrayList<>(imageLabels.keySet());
		ArrayList<Class<? extends T>> constantsLeft = new ArrayList<>(Arrays.asList(constantItems));
		ArrayList<Class<? extends T>> itemsLeft = new ArrayList<>(Arrays.asList(items));

		System.out.print("Initializing constants in this list: ");
		for(Class<? extends T> thing : constantItems)
			System.out.print(thing.getSimpleName() + " ");

		System.out.println("\n");

		for(int i = 0; i < constantItems.length; i++)
		{
			int index = Random.Int(constantsLeft.size());
			System.out.println("Selected random index: " + index);

			Class<? extends T> item = constantsLeft.get(index);
			System.out.println("It is a " + item.getSimpleName());
			constantsLeft.remove(index);

			int randI = Random.Int(labelsLeft.size());
			System.out.println("Selected randI: " + randI);

			itemLabels.put(item, labelsLeft.get(randI));
			System.out.println("It is " + labelsLeft.get(randI));
			labelsLeft.remove(randI);

			System.out.print("Constants left in this list: ");
			for(Class<? extends T> thing : constantsLeft)
				System.out.print(thing.getSimpleName() + " ");

			System.out.println("\n");
		}

		labelImages.put(defaultLabel, defaultImage);

		for(int i = 0; i < items.length; i++)
		{
			int index = Random.Int(itemsLeft.size());

			Class<? extends T> item = itemsLeft.get(index);
			itemsLeft.remove(index);

			if(labelsLeft.isEmpty())
				itemLabels.put(item, defaultLabel);
			else
			{
				int randi = Random.Int(labelsLeft.size());

				itemLabels.put(item, labelsLeft.get(randi));
				labelsLeft.remove(randi);
			}
		}

		for(Class<?> cl : constantItems)
		{
			try
			{
				T newinstance = (T) cl.newInstance();
				if(newinstance.image == ItemSpriteSheet.POTION_UNSTABLE ||
				   newinstance.image == ItemSpriteSheet.SCROLL_MYSTERY)
				{
					System.out.println(cl.getSimpleName() + " was found to not exist!");
					System.out.println("After all, it is a " + newinstance.name());
					for(; ; ) ;
				}
			}
			catch(Exception e)
			{
				ChancelPixelDungeon.reportException(e);
			}
		}
	}

	public ItemExistenceHandler(Class<? extends T>[] items, Class<? extends T>[] constantItems,
	                         HashMap<String, Integer> labelImages, String defaultLabel, int defaultImage, Bundle bundle)
	{
		this.items = items;
		this.constantItems = constantItems;

		itemLabels = new HashMap<>();
		this.labelImages = new HashMap<>(labelImages);
		known = new HashSet<>();

		ArrayList<String> allLabels = new ArrayList<>(labelImages.keySet());

		if(labelImages.size() < this.items.length)
			for(int i = 0; i < this.items.length - labelImages.size(); i++)
			{
				this.labelImages.put(defaultLabel, defaultImage);
				labelImages.put(defaultLabel, defaultImage);
			}

		restore(bundle, allLabels, defaultLabel, defaultImage);
	}

	private static final String PFX_LABEL = "_label";
	private static final String PFX_KNOWN = "_known";

	public void save(Bundle bundle)
	{
		for(Class<? extends T> item : items)
		{
			String itemName = item.toString();
			bundle.put(itemName + PFX_LABEL, itemLabels.get(item));
			bundle.put(itemName + PFX_KNOWN, known.contains(item));
		}
		for(Class<? extends T> item : constantItems)
		{
			String itemName = item.toString();
			bundle.put(itemName + PFX_LABEL, itemLabels.get(item));
			bundle.put(itemName + PFX_KNOWN, known.contains(item));
		}
	}

	public void saveSelectively(Bundle bundle, ArrayList<Item> itemsToSave)
	{
		List<Class<? extends T>> items = Arrays.asList(this.items);
		List<Class<? extends T>> constants = Arrays.asList(this.constantItems);

		for(Item item : itemsToSave)
		{
			if(items.contains(item.getClass()))
				doSelectiveSave(bundle, items, item);
			else if(constants.contains(items.getClass()))
				doSelectiveSave(bundle, constants, item);
		}
	}

	private void doSelectiveSave(Bundle bundle, List<Class<? extends T>> items, Item item)
	{
		Class<? extends T> cls = items.get(items.indexOf(item.getClass()));
		String itemName = cls.toString();
		bundle.put(itemName + PFX_LABEL, itemLabels.get(cls));
		bundle.put(itemName + PFX_KNOWN, known.contains(cls));
	}

	private void restore(Bundle bundle, ArrayList<String> labelsLeft, String defaultLabel, int defaultImage)
	{
		ArrayList<Class<? extends T>> unlabelled = new ArrayList<>();

		for(Class<? extends T> item : items)
			getKnownOrUnknown(bundle, item, labelsLeft, unlabelled);

		for(Class<? extends T> item : constantItems)
			getKnownOrUnknown(bundle, item, labelsLeft, unlabelled);

		for(Class<? extends T> item : unlabelled)
		{
			String itemName = item.toString();

			if(labelsLeft.isEmpty())
			{
				labelImages.put(defaultLabel, defaultImage);
				itemLabels.put(item, defaultLabel);
			}
			else
			{
				int index = Random.Int(labelsLeft.size());

				itemLabels.put(item, labelsLeft.get(index));
				labelsLeft.remove(index);
			}

			if(bundle.contains(itemName + PFX_KNOWN) && bundle.getBoolean(itemName + PFX_KNOWN))
				known.add(item);
		}
	}

	private void getKnownOrUnknown(Bundle bundle, Class<? extends T> item, ArrayList<String> labelsLeft,
	                               ArrayList<Class<? extends T>> unlabelled)
	{
		String itemName = item.toString();

		if(bundle.contains(itemName + PFX_LABEL))
		{
			String label = bundle.getString(itemName + PFX_LABEL);
			itemLabels.put(item, label);
			labelsLeft.remove(label);

			if(bundle.getBoolean(itemName + PFX_KNOWN))
				known.add(item);
		}
		else
			unlabelled.add(item);
	}

	public int image(T item)
	{
		return labelImages.get(label(item));
	}

	public String label(T item)
	{
		return itemLabels.get(item.getClass());
	}

	public boolean isKnown(T item)
	{
		return known.contains(item.getClass());
	}

	public void know(T item)
	{
		known.add((Class<? extends T>) item.getClass());
	}

	public HashSet<Class<? extends T>> known()
	{
		return known;
	}

	public HashSet<Class<? extends T>> unknown()
	{
		HashSet<Class<? extends T>> result = new HashSet<Class<? extends T>>();
		for(Class<? extends T> i : items)
			if(!known.contains(i))
				result.add(i);
		for(Class<? extends T> i : constantItems)
			if(!known.contains(i))
				result.add(i);

		return result;
	}
}
