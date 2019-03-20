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

import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ItemExistenceHandler<T extends Item>
{
	private Class<? extends T>[] items, constantItems, allItems;
	private HashMap<Class<? extends T>, String> itemLabels;
	private HashMap<String, Integer> labelImages;
	private HashSet<Class<? extends T>> known;

	public ItemExistenceHandler(Class<? extends T>[] items, Class<? extends T>[] constantItems,
	                         HashMap<String, Integer> imageLabels, String defaultLabel, int defaultImage)
	{
		this.items = items;
		this.constantItems = constantItems;
		allItems = System.arrayCopy(items);
		for(Class<? extends T> c : constantItems)
			allItems.add(c);

		itemLabels = new HashMap<>();
		labelImages = new HashMap<>(imageLabels);
		known = new HashSet<>();

		ArrayList<String> labelsLeft = new ArrayList<>(imageLabels.keySet());
		ArrayList<Class<? extends T>> itemsLeft = new ArrayList<>(Arrays.asList(items));

		for(Class<? extends T> item : constantItems)
		{
			System.out.println("Constant considered: " + item.getSimpleName());
			int index = Random.Int(labelsLeft.size());

			itemLabels.put(item, labelsLeft.get(index));
			labelsLeft.remove(index);
		}

		for(Class<? extends T> thing : itemLabels.keySet())
			System.out.println("Constant put: " + thing.getSimpleName());

		for(int i = 0; i < items.length; i++)
		{
			int index = Random.Int(itemsLeft.size());

			Class<? extends T> item = itemsLeft.get(index);
			itemsLeft.remove(index);

			if(labelsLeft.isEmpty())
			{
				labelImages.put(defaultLabel, defaultImage);
				itemLabels.put(item, defaultLabel);
			}
			else
			{
				int randi = Random.Int(labelsLeft.size());

				itemLabels.put(item, labelsLeft.get(randi));
				labelsLeft.remove(randi);
			}
		}
	}

	public ItemExistenceHandler(Class<? extends T>[] items, Class<? extends T>[] constantItems,
	                         HashMap<String, Integer> labelImages, String defaultLabel, int defaultImage, Bundle bundle)
	{
		this.items = items;
		this.constantItems = constantItems;
		allItems = System.arrayCopy(items);
		for(Class<? extends T> thing : constantItems)
			allItems.add(thing);

		itemLabels = new HashMap<>();
		this.labelImages = new HashMap<>(labelImages);
		known = new HashSet<>();

		ArrayList<String> allLabels = new ArrayList<String>(labelImages.keySet());

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
		for(Class<? extends T> item : allItems)
		{
			String itemName = item.toString();
			bundle.put(itemName + PFX_LABEL, itemLabels.get(item));
			bundle.put(itemName + PFX_KNOWN, known.contains(item));
		}
	}

	public void saveSelectively(Bundle bundle, ArrayList<Item> itemsToSave)
	{
		List<Class<? extends T>> items = Arrays.asList(allItems);

		for(Item item : itemsToSave)
		{
			if(items.contains(item.getClass()))
			{
				Class<? extends T> cls = items.get(items.indexOf(item.getClass()));
				String itemName = cls.toString();
				bundle.put(itemName + PFX_LABEL, itemLabels.get(cls));
				bundle.put(itemName + PFX_KNOWN, known.contains(cls));
			}
		}
	}

	private void restore(Bundle bundle, ArrayList<String> labelsLeft, String defaultLabel, int defaultImage)
	{
		ArrayList<Class<? extends T>> unlabelled = new ArrayList<>();

		for(Class<? extends T> item : allItems)
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
		for(Class<? extends T> i : allItems)
			if(!known.contains(i))
				result.add(i);

		return result;
	}
}
