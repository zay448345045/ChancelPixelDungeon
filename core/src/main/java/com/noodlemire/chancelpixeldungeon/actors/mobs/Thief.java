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

package com.noodlemire.chancelpixeldungeon.actors.mobs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Corruption;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Terror;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Gold;
import com.noodlemire.chancelpixeldungeon.items.Honeypot;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ThiefSprite;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Thief extends Mob
{
	public Item item;

	{
		spriteClass = ThiefSprite.class;

		EXP = Random.IntRange(5, 8);

		TIME_TO_REST = 2;
		setAttacksBeforeRest(2);

		setHT(14 + EXP * 2, true);

		//see createloot
		loot = null;
		lootChance = 0.01f;

		WANDERING = new Wandering();
		FLEEING = new Fleeing();

		properties.add(Property.UNDEAD);
	}

	private static final String ITEM = "item";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(ITEM, item);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		item = (Item) bundle.get(ITEM);
	}

	@Override
	public float speed()
	{
		if(item != null) return (5 * super.speed()) / 6;
		else return super.speed();
	}

	@Override
	public int damageRoll()
	{
		return EXP + 1 + 4 * restTimeNeeded(false);
	}

	@Override
	protected float attackDelay()
	{
		return 0.5f;
	}

	@Override
	public void rollToDropLoot()
	{
		if(item != null)
		{
			Dungeon.level.drop(item, pos).sprite.drop();
			//updates position
			if(item instanceof Honeypot.ShatteredPot)
				((Honeypot.ShatteredPot) item).setHolder(this);
			item = null;
		}
		super.rollToDropLoot();
	}

	@Override
	protected Item createLoot()
	{
		if(Random.Int(2) == 0)
			return Generator.random(Generator.Category.ARTIFACT);
		else
			return Generator.random(Generator.Category.RING);
	}

	@Override
	public int attackSkill(Char target)
	{
		return EXP * 3;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, (EXP + 1) / 2);
	}

	@Override
	public int defenseSkill()
	{
		return EXP * 2;
	}

	@Override
	public int attackProc(Char enemy, int damage)
	{
		damage = super.attackProc(enemy, damage);

		if(item == null && enemy instanceof Hero && steal((Hero) enemy))
		{
			state = FLEEING;
		}

		return damage;
	}

	@Override
	public int defenseProc(Char enemy, int damage)
	{
		if(state == FLEEING)
		{
			Dungeon.level.drop(new Gold(), pos).sprite.drop();
		}

		return super.defenseProc(enemy, damage);
	}

	protected boolean steal(Hero hero)
	{
		Item item = hero.belongings.randomUnequipped();

		if(item != null && !item.unique && item.level() < 1)
		{

			GLog.w(Messages.get(Thief.class, "stole", item.name()));
			if(!item.stackable() || hero.belongings.getSimilar(item) == null)
			{
				Dungeon.quickslot.convertToPlaceholder(item);
			}
			item.updateQuickslot();

			if(item instanceof Honeypot)
			{
				this.item = ((Honeypot) item).shatter(this, this.pos);
				item.detach(hero.belongings.backpack);
			}
			else
			{
				this.item = item.detach(hero.belongings.backpack);
				if(item instanceof Honeypot.ShatteredPot)
					((Honeypot.ShatteredPot) item).setHolder(this);
			}

			return true;
		}
		else
			return false;
	}

	@Override
	public String description()
	{
		String desc = super.description();

		if(item != null)
		{
			desc += Messages.get(this, "carries", item.name());
		}

		return desc;
	}

	private class Wandering extends Mob.Wandering
	{
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted)
		{
			super.act(enemyInFOV, justAlerted);

			//if an enemy is just noticed and the thief posses an item, run, don't fight.
			if(state == HUNTING && item != null)
			{
				state = FLEEING;
			}

			return true;
		}
	}

	private class Fleeing extends Mob.Fleeing
	{
		@Override
		protected void nowhereToRun()
		{
			if(buff(Terror.class) == null && buff(Corruption.class) == null)
			{
				if(enemySeen())
				{
					sprite.showStatus(CharSprite.NEGATIVE, Messages.get(Mob.class, "rage"));
					state = HUNTING;
				}
				else if(item != null && !Dungeon.level.heroFOV[pos])
				{
					int count = 32;
					int newPos;
					do
					{
						newPos = Dungeon.level.randomRespawnCell();
						if(count-- <= 0)
						{
							break;
						}
					}
					while(newPos == -1 || Dungeon.level.heroFOV[newPos] || Dungeon.level.distance(newPos, pos) < (count / 3));

					if(newPos != -1)
					{

						if(Dungeon.level.heroFOV[pos])
							CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);
						pos = newPos;
						sprite.place(pos);
						sprite.visible = Dungeon.level.heroFOV[pos];
						if(Dungeon.level.heroFOV[pos])
							CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);

					}

					if(item != null) GLog.n(Messages.get(Thief.class, "escapes", item.name()));
					item = null;
					state = WANDERING;
				}
				else
				{
					state = WANDERING;
				}
			}
			else
			{
				super.nowhereToRun();
			}
		}
	}
}
