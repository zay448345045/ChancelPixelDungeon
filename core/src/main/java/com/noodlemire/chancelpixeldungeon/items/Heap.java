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

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Burning;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Frost;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mimic;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Wraith;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Flare;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.effects.particles.ElmoParticle;
import com.noodlemire.chancelpixeldungeon.effects.particles.FlameParticle;
import com.noodlemire.chancelpixeldungeon.effects.particles.ShadowParticle;
import com.noodlemire.chancelpixeldungeon.items.artifacts.Artifact;
import com.noodlemire.chancelpixeldungeon.items.artifacts.DriedRose;
import com.noodlemire.chancelpixeldungeon.items.food.ChargrilledMeat;
import com.noodlemire.chancelpixeldungeon.items.food.FrozenCarpaccio;
import com.noodlemire.chancelpixeldungeon.items.food.MysteryMeat;
import com.noodlemire.chancelpixeldungeon.items.journal.DocumentPage;
import com.noodlemire.chancelpixeldungeon.items.potions.Potion;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfWealth;
import com.noodlemire.chancelpixeldungeon.items.scrolls.Scroll;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfBlessing;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.noodlemire.chancelpixeldungeon.items.wands.Wand;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class Heap implements Bundlable, Hero.Doom
{
	private static final int SEEDS_TO_POTION = 3;

	public enum Type
	{
		HEAP,
		FOR_SALE,
		CHEST,
		LOCKED_CHEST,
		CRYSTAL_CHEST,
		EBONY_CHEST,
		TOMB,
		SKELETON,
		REMAINS,
		MIMIC,
		AMPHORA
	}

	public Type type = Type.HEAP;

	public int pos = 0;

	public ItemSprite sprite;
	public boolean seen = false;

	public LinkedList<Item> items = new LinkedList<Item>();

	public int image()
	{
		switch(type)
		{
			case HEAP:
			case FOR_SALE:
				return size() > 0 ? items.peek().image() : 0;
			case CHEST:
			case MIMIC:
				return ItemSpriteSheet.CHEST;
			case LOCKED_CHEST:
				return ItemSpriteSheet.LOCKED_CHEST;
			case CRYSTAL_CHEST:
				return ItemSpriteSheet.CRYSTAL_CHEST;
			case EBONY_CHEST:
				return ItemSpriteSheet.EBONY_CHEST;
			case TOMB:
				return ItemSpriteSheet.TOMB;
			case SKELETON:
				return ItemSpriteSheet.BONES;
			case REMAINS:
				return ItemSpriteSheet.REMAINS;
			default:
				return 0;
		}
	}

	public ItemSprite.Glowing glowing()
	{
		return (type == Type.HEAP || type == Type.FOR_SALE) && items.size() > 0 ? items.peek().glowing() : null;
	}

	public void open(Hero hero)
	{
		switch(type)
		{
			case MIMIC:
				if(Mimic.spawnAt(pos, items) != null)
				{
					destroy();
				}
				else
				{
					type = Type.CHEST;
				}
				break;
			case EBONY_CHEST:
				CellEmitter.get(pos).burst(ShadowParticle.CURSE, 6);
				Sample.INSTANCE.play(Assets.SND_CURSED);

				for(int n : PathFinder.NEIGHBOURS9)
				{
					Char ch = Actor.findChar(pos + n);

					if(ch != null && ch.isAlive())
					{
						int damage = Random.NormalIntRange(Dungeon.depth, Dungeon.depth * 3);
						damage -= ch.drRoll();

						ch.damage(damage, this);
					}
				}

				break;
			case TOMB:
				Wraith.spawnAround(hero.pos);
				break;
			case REMAINS:
			case SKELETON:
				CellEmitter.center(pos).start(Speck.factory(Speck.RATTLE), 0.1f, 3);
				for(Item item : items)
				{
					if(item.cursed)
					{
						if(Wraith.spawnAt(pos) == null)
						{
							hero.sprite.emitter().burst(ShadowParticle.CURSE, 6);
							hero.damage(hero.HP() / 2, this);
						}
						Sample.INSTANCE.play(Assets.SND_CURSED);
						break;
					}
				}
				break;
			default:
		}

		if(type != Type.MIMIC)
		{
			type = Type.HEAP;
			ArrayList<Item> bonus = RingOfWealth.tryRareDrop(hero, 1);
			if(bonus != null)
			{
				items.addAll(0, bonus);
				new Flare(8, 32).color(0xFFFF00, true).show(sprite, 2f);
			}
			sprite.link();
			sprite.drop();
		}
	}

	public int size()
	{
		return items.size();
	}

	public Item pickUp()
	{
		if(items.isEmpty())
		{
			destroy();
			return null;
		}
		Item item = items.removeFirst();

		if(items.isEmpty())
			destroy();
		else if(sprite != null)
		{
			sprite.view(image(), glowing());
			sprite.place(pos);
		}

		return item;
	}

	public Item peek()
	{
		return items.peek();
	}

	public void drop(Item item)
	{
		if(item.stackable() && type != Type.FOR_SALE)
		{
			for(Item i : items)
			{
				if(i.isSimilar(item))
				{
					item = i.merge(item);
					break;
				}
			}
			items.remove(item);
		}

		if((item instanceof Dewdrop || item instanceof DriedRose.Petal) && type != Type.FOR_SALE)
		{
			items.add(item);
		}
		else
		{
			items.addFirst(item);
		}

		if(sprite != null)
		{
			if(type == Type.HEAP || type == Type.FOR_SALE)
				sprite.view(items.peek());
			else
				sprite.view(image(), glowing());
			sprite.place(pos);
		}
	}

	public void replace(Item a, Item b)
	{
		int index = items.indexOf(a);
		if(index != -1)
		{
			items.remove(index);
			items.add(index, b);
		}
	}

	public void burn()
	{
		if(type == Type.MIMIC)
		{
			Mimic m = Mimic.spawnAt(pos, items);
			if(m != null)
			{
				Buff.affect(m, Burning.class).reignite();
				m.sprite.emitter().burst(FlameParticle.FACTORY, 5);
				destroy();
			}
		}

		if(type != Type.HEAP)
			return;

		boolean burnt = false;
		boolean evaporated = false;

		for(Item item : items.toArray(new Item[0]))
		{
			if(item instanceof Scroll
			   && !(item instanceof ScrollOfUpgrade || item instanceof ScrollOfBlessing))
			{
				items.remove(item);
				burnt = true;
			}
			else if(item instanceof Dewdrop)
			{
				items.remove(item);
				evaporated = true;
			}
			else if(item instanceof MysteryMeat)
			{
				replace(item, ChargrilledMeat.cook((MysteryMeat) item));
				burnt = true;
			}
			else if(item instanceof Bomb)
			{
				items.remove(item);
				((Bomb) item).explode(pos);
				//stop processing the burning, it will be replaced by the explosion.
				return;
			}
		}

		if(burnt || evaporated)
		{
			if(Dungeon.level.heroFOV[pos])
				if(burnt)
					burnFX(pos);
				else
					evaporateFX(pos);

			if(isEmpty())
				destroy();
			else if(sprite != null)
				sprite.view(items.peek());
		}
	}

	//Note: should not be called to initiate an explosion, but rather by an explosion that is happening.
	public void explode()
	{
		//breaks open most standard containers, mimics die.
		if(type == Type.MIMIC || type == Type.CHEST || type == Type.EBONY_CHEST || type == Type.SKELETON)
		{
			type = Type.HEAP;
			sprite.link();
			sprite.drop();
			return;
		}

		if(type != Type.HEAP)
			return;
		else
		{
			for(Item item : items.toArray(new Item[0]))
			{
				if(item instanceof Potion)
				{
					items.remove(item);
					((Potion) item).shatter(pos);
				}
				else if(item instanceof Bomb)
				{
					items.remove(item);
					((Bomb) item).explode(pos);
					//stop processing current explosion, it will be replaced by the new one.
					return;

					//unique and upgraded items can endure the blast
				}
				else if(!(item.level() > 0 || item.unique))
					items.remove(item);
			}

			if(isEmpty())
				destroy();
			else if(sprite != null)
				sprite.view(items.peek());
		}
	}

	public void freeze()
	{

		if(type == Type.MIMIC)
		{
			Mimic m = Mimic.spawnAt(pos, items);
			if(m != null)
			{
				Buff.prolong(m, Frost.class, Frost.duration(m) * Random.Float(1.0f, 1.5f));
				destroy();
			}
		}

		if(type != Type.HEAP)
		{
			return;
		}

		boolean frozen = false;
		for(Item item : items.toArray(new Item[0]))
		{
			if(item instanceof MysteryMeat)
			{
				replace(item, FrozenCarpaccio.cook((MysteryMeat) item));
				frozen = true;
			}
			else if(item instanceof Potion)
			{
				items.remove(item);
				((Potion) item).shatter(pos);
				frozen = true;
			}
			else if(item instanceof Bomb)
			{
				((Bomb) item).fuse = null;
				frozen = true;
			}
		}

		if(frozen)
		{
			if(isEmpty())
			{
				destroy();
			}
			else if(sprite != null)
			{
				sprite.view(items.peek());
			}
		}
	}

	public static void burnFX(int pos)
	{
		CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
		Dungeon.playAt(Assets.SND_BURNING, pos);
	}

	public static void evaporateFX(int pos)
	{
		CellEmitter.get(pos).burst(Speck.factory(Speck.STEAM), 5);
	}

	public boolean isEmpty()
	{
		return items == null || items.size() == 0;
	}

	public void destroy()
	{
		Dungeon.level.heaps.remove(this.pos);
		if(sprite != null)
		{
			sprite.kill();
		}
		items.clear();
	}

	@Override
	public String toString()
	{
		switch(type)
		{
			case CHEST:
			case MIMIC:
				return Messages.get(this, "chest");
			case LOCKED_CHEST:
				return Messages.get(this, "locked_chest");
			case CRYSTAL_CHEST:
				return Messages.get(this, "crystal_chest");
			case EBONY_CHEST:
				return Messages.get(this, "ebony_chest");
			case TOMB:
				return Messages.get(this, "tomb");
			case SKELETON:
				return Messages.get(this, "skeleton");
			case REMAINS:
				return Messages.get(this, "remains");
			default:
				return peek().toString();
		}
	}

	public String info()
	{
		switch(type)
		{
			case CHEST:
			case MIMIC:
				return Messages.get(this, "chest_desc");
			case LOCKED_CHEST:
				return Messages.get(this, "locked_chest_desc");
			case CRYSTAL_CHEST:
				if(peek() instanceof Artifact)
					return Messages.get(this, "crystal_chest_desc", Messages.get(this, "artifact"));
				else if(peek() instanceof Wand)
					return Messages.get(this, "crystal_chest_desc", Messages.get(this, "wand"));
				else
					return Messages.get(this, "crystal_chest_desc", Messages.get(this, "ring"));
			case EBONY_CHEST:
				return Messages.get(this, "ebony_chest_desc");
			case TOMB:
				return Messages.get(this, "tomb_desc");
			case SKELETON:
				return Messages.get(this, "skeleton_desc");
			case REMAINS:
				return Messages.get(this, "remains_desc");
			default:
				return peek().info();
		}
	}

	private static final String POS = "pos";
	private static final String SEEN = "seen";
	private static final String TYPE = "type";
	private static final String ITEMS = "items";

	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		pos = bundle.getInt(POS);
		seen = bundle.getBoolean(SEEN);
		type = Type.valueOf(bundle.getString(TYPE));

		items = new LinkedList<Item>((Collection<Item>) ((Collection<?>) bundle.getCollection(ITEMS)));
		items.removeAll(Collections.singleton(null));

		//remove any document pages that either don't exist anymore or that the player already has
		for(Item item : items.toArray(new Item[0]))
		{
			if(item instanceof DocumentPage
			   && (!((DocumentPage) item).document().pages().contains(((DocumentPage) item).page())
			       || ((DocumentPage) item).document().hasPage(((DocumentPage) item).page())))
			{
				items.remove(item);
			}
		}

	}

	@Override
	public void storeInBundle(Bundle bundle)
	{
		bundle.put(POS, pos);
		bundle.put(SEEN, seen);
		bundle.put(TYPE, type.toString());
		bundle.put(ITEMS, items);
	}

	@Override
	public void onDeath()
	{
		Dungeon.fail(getClass());
		GLog.n(Messages.get(this, "ondeath"));
	}
}
