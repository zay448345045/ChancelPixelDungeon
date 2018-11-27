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

import com.noodlemire.chancelpixeldungeon.items.potions.Potion;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ItemStatusHandler<T extends Item> {

	private Class<? extends T>[] items;
	private HashMap<Class<? extends T>, String> itemLabels;
	private HashMap<String, Integer> labelImages;
	private HashSet<Class<? extends T>> known;

	public ItemStatusHandler(Class<? extends T>[] items, Class<? extends T>[] constantItems,
							  HashMap<String, Integer> imageLabels, String defaultLabel, int defaultImage)
	{
		if(constantItems != null)
		{
			this.items = new Class[items.length + constantItems.length];

			for (int i = 0; i < this.items.length; i++)
				if (i < items.length)
					this.items[i] = items[i];
				else
					this.items[i] = constantItems[i - items.length];
		}
		else
			this.items = items;

		for(Class<? extends T> item : this.items)
			System.out.println("Next item is: " + item.getName());

		this.itemLabels = new HashMap<>();
		this.labelImages = new HashMap<>(imageLabels);
		known = new HashSet<Class<? extends T>>();

		ArrayList<String> labelsLeft = new ArrayList<String>(imageLabels.keySet());
		ArrayList<Class<? extends T>> itemsLeft = new ArrayList<>(Arrays.asList(items));

		if(constantItems != null)
			for(Class<? extends T> item : constantItems)
			{
				int index = Random.Int(labelsLeft.size());

				itemLabels.put(item, labelsLeft.get(index));
				labelsLeft.remove(index);
			}

		for(int i = 0; i < items.length; i++)
		{
			int randI = Random.Int(itemsLeft.size());

			Class<? extends T> item = itemsLeft.get(randI);
			itemsLeft.remove(randI);

			if(!labelsLeft.isEmpty())
			{
				int index = Random.Int(labelsLeft.size());

				itemLabels.put(item, labelsLeft.get(index));
				labelsLeft.remove(index);
			}
			else
			{
				labelImages.put(defaultLabel, defaultImage);
				itemLabels.put(item, defaultLabel);
			}
		}
	}

	public ItemStatusHandler( Class<? extends T>[] items, Class<? extends T>[] constantItems,
							  HashMap<String, Integer> labelImages, String defaultLabel, int defaultImage, Bundle bundle )
	{
		if(constantItems != null)
		{
			this.items = new Class[items.length + constantItems.length];

			for (int i = 0; i < this.items.length; i++)
				if (i < items.length)
					this.items[i] = items[i];
				else
					this.items[i] = constantItems[i - items.length];
		}
		else
			this.items = items;

		this.itemLabels = new HashMap<>();
		this.labelImages = new HashMap<>(labelImages);
		known = new HashSet<>();

		ArrayList<String> allLabels = new ArrayList<String>( labelImages.keySet() );

		if(labelImages.size() < this.items.length)
			for(int i = 0; i < this.items.length - labelImages.size(); i++)
			{
				this.labelImages.put(defaultLabel, defaultImage);
				labelImages.put(defaultLabel, defaultImage);
			}

		restore(bundle, allLabels, defaultLabel, defaultImage);
	}

	public ItemStatusHandler(Class<? extends T>[] items, HashMap<String, Integer> labelImages, Bundle bundle)
	{
		this(items, null, labelImages, "", 0, bundle);
	}

	public ItemStatusHandler(Class<? extends T>[] items, HashMap<String, Integer> labelImages)
	{
		this(items, null, labelImages, "", 0);
	}

	private static final String PFX_LABEL	= "_label";
	private static final String PFX_KNOWN	= "_known";
	
	public void save( Bundle bundle ) {
		for (int i=0; i < items.length; i++) {
			String itemName = items[i].toString();
			bundle.put( itemName + PFX_LABEL, itemLabels.get( items[i] ) );
			bundle.put( itemName + PFX_KNOWN, known.contains( items[i] ) );
		}
	}

	public void saveSelectively( Bundle bundle, ArrayList<Item> itemsToSave ){
		List<Class<? extends T>> items = Arrays.asList(this.items);
		for (Item item : itemsToSave){
			System.out.println("Saving " + item.name() + " with desc " + item.desc());
			if (items.contains(item.getClass())){
				Class<? extends T> cls = items.get(items.indexOf(item.getClass()));
				String itemName = cls.toString();
				bundle.put( itemName + PFX_LABEL, itemLabels.get( cls ) );
				bundle.put( itemName + PFX_KNOWN, known.contains( cls ) );
			}
		}
	}

	private void restore( Bundle bundle, ArrayList<String> labelsLeft, String defaultLabel, int defaultImage ) {

		ArrayList<Class<? extends T>> unlabelled = new ArrayList<>();

		for (int i=0; i < items.length; i++)
		{
			Class<? extends T> item = items[i];
			String itemName = item.toString();

			if (bundle.contains( itemName + PFX_LABEL ))
			{
				String label = bundle.getString( itemName + PFX_LABEL );
				System.out.println(itemName + ": " + label);
				itemLabels.put( item, label );
				labelsLeft.remove( label );

				if (bundle.getBoolean( itemName + PFX_KNOWN )) {
					known.add( item );
				}
			}
			else
			{
				unlabelled.add(items[i]);
			}
		}

		for (Class<? extends T> item : unlabelled){

			String itemName = item.toString();

			if(!labelsLeft.isEmpty())
			{
				int index = Random.Int(labelsLeft.size());

				itemLabels.put(item, labelsLeft.get(index));
				labelsLeft.remove(index);
			}
			else
			{
				labelImages.put(defaultLabel, defaultImage);
				itemLabels.put(item, defaultLabel);
			}

			if (bundle.contains( itemName + PFX_KNOWN ) && bundle.getBoolean( itemName + PFX_KNOWN )) {
				known.add( item );
			}
		}
	}
	
	public int image( T item ) {
		return labelImages.get(label(item));
	}
	
	public String label( T item ) {
		return itemLabels.get(item.getClass());
	}
	
	public boolean isKnown( T item ) {
		return known.contains( item.getClass() );
	}
	
	public void know( T item ) {
		known.add( (Class<? extends T>)item.getClass() );
	}
	
	public HashSet<Class<? extends T>> known() {
		return known;
	}
	
	public HashSet<Class<? extends T>> unknown() {
		HashSet<Class<? extends T>> result = new HashSet<Class<? extends T>>();
		for (Class<? extends T> i : items) {
			if (!known.contains( i )) {
				result.add( i );
			}
		}
		return result;
	}
}
