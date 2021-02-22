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

import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.Challenges;
import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.Statistics;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Amok;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Corruption;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Hunger;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Invisibility;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Might;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MindVision;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Preparation;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Sleep;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Slow;
import com.noodlemire.chancelpixeldungeon.actors.buffs.SoulMark;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Taunted;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Terror;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Weakness;
import com.noodlemire.chancelpixeldungeon.effects.Flare;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.effects.Surprise;
import com.noodlemire.chancelpixeldungeon.effects.Wound;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.noodlemire.chancelpixeldungeon.items.rings.Ring;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfWealth;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.WarHammer;
import com.noodlemire.chancelpixeldungeon.levels.features.Chasm;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Mob extends Char
{
	{
		name = Messages.get(this, "name");
		actPriority = MOB_PRIO;

		alignment = Alignment.ENEMY;
	}

	public static final int PATIENCE = 6;

	public AiState SLEEPING = new Sleeping();
	public AiState HUNTING = new Hunting();
	public AiState WANDERING = new Wandering();
	public AiState FLEEING = new Fleeing();
	public AiState PASSIVE = new Passive();
	public AiState state = SLEEPING;

	public Class<? extends CharSprite> spriteClass;

	protected int target = -1;

	public int EXP = 1;

	protected Char enemy;
	private int enemySeen;
	protected boolean alerted = false;

	protected static final float TIME_TO_WAKE_UP = 1f;

	public static final String STATE = "state";
	private static final String SEEN = "seen";
	private static final String TARGET = "target";
	private static final String EXP_KEY = "exp";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);

		if(state == SLEEPING)
		{
			bundle.put(STATE, Sleeping.TAG);
		}
		else if(state == WANDERING)
		{
			bundle.put(STATE, Wandering.TAG);
		}
		else if(state == HUNTING)
		{
			bundle.put(STATE, Hunting.TAG);
		}
		else if(state == FLEEING)
		{
			bundle.put(STATE, Fleeing.TAG);
		}
		else if(state == PASSIVE)
		{
			bundle.put(STATE, Passive.TAG);
		}
		bundle.put(SEEN, enemySeen);
		bundle.put(TARGET, target);
		bundle.put(EXP_KEY, EXP);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);

		String state = bundle.getString(STATE);
		if(state.equals(Sleeping.TAG))
		{
			this.state = SLEEPING;
		}
		else if(state.equals(Wandering.TAG))
		{
			this.state = WANDERING;
		}
		else if(state.equals(Hunting.TAG))
		{
			this.state = HUNTING;
		}
		else if(state.equals(Fleeing.TAG))
		{
			this.state = FLEEING;
		}
		else if(state.equals(Passive.TAG))
		{
			this.state = PASSIVE;
		}

		enemySeen = bundle.getInt(SEEN);

		target = bundle.getInt(TARGET);

		EXP = bundle.getInt(EXP_KEY);
	}

	@Override
	public CharSprite sprite()
	{
		CharSprite sprite = null;
		try
		{
			sprite = spriteClass.newInstance();
		}
		catch(Exception e)
		{
			ChancelPixelDungeon.reportException(e);
		}
		return sprite;
	}

	@Override
	protected boolean act()
	{
		super.act();

		boolean justAlerted = alerted;
		alerted = false;

		if(justAlerted)
		{
			sprite.showAlert();
		}
		else
		{
			sprite.hideAlert();
			sprite.hideLost();
		}

		if(paralysed > 0)
		{
			enemySeen = 0;
			spend(TICK);
			return true;
		}

		enemy = chooseEnemy();

		boolean enemyInFOV = enemy != null && enemy.isAlive() && (fieldOfView[enemy.pos] && enemy.invisible <= 0)
		                     || buff(MindVision.class) != null;

		return state.act(enemyInFOV, justAlerted);
	}

	public boolean lookForEnemy(boolean found)
	{
		if(found)
			enemySeen = PATIENCE;
		else
			enemySeen = Math.max(0, enemySeen - 1);

		return enemySeen > 0;
	}

	public void forgetEnemey()
	{
		enemySeen = 0;
	}

	public boolean enemySeen()
	{
		return enemySeen > 0;
	}

	protected Char chooseEnemy()
	{
		if(buff(Taunted.class) != null)
			return Dungeon.hero;

		Terror terror = buff(Terror.class);
		if(terror != null)
		{
			Char source = terror.getObject();
			if(source != null)
				return source;
			else if(terror.getPos() != -1)
				return null;
		}

		//find a new enemy if..
		boolean newEnemy = false;
		//we have no enemy, we are the enemy, or the current one is dead
		if(enemy == null || enemy == this || !enemy.isAlive() || state == WANDERING)
			newEnemy = true;
			//We are an ally, and current enemy is another ally.
		else if(alignment == Alignment.ALLY && enemy.alignment == Alignment.ALLY)
			newEnemy = true;
			//We are amoked and current enemy is the hero
		else if(buff(Amok.class) != null && enemy == Dungeon.hero)
			newEnemy = true;

		if(newEnemy)
		{
			HashSet<Char> enemies = new HashSet<>();

			//if the mob is amoked...
			if(buff(Amok.class) != null)
			{
				//try to find an enemy mob to attack first.
				for(Mob mob : Dungeon.level.mobs)
					if(mob.alignment == Alignment.ENEMY && mob != this && fieldOfView[mob.pos])
						enemies.add(mob);

				if(enemies.isEmpty())
				{
					//try to find ally mobs to attack second.
					for(Mob mob : Dungeon.level.mobs)
						if(mob.alignment == Alignment.ALLY && mob != this && fieldOfView[mob.pos])
							enemies.add(mob);

					if(enemies.isEmpty())
					{
						//try to find the hero third
						if(fieldOfView[Dungeon.hero.pos])
						{
							enemies.add(Dungeon.hero);
						}
					}
				}

				//if the mob is an ally...
			}
			else if(alignment == Alignment.ALLY)
			{
				//look for hostile mobs that are not passive to attack
				for(Mob mob : Dungeon.level.mobs)
					if(mob.alignment == Alignment.ENEMY
					   && fieldOfView[mob.pos]
					   && mob.state != mob.PASSIVE)
						enemies.add(mob);

				//if the mob is an enemy...
			}
			else if(alignment == Alignment.ENEMY)
			{
				//look for ally mobs to attack
				for(Mob mob : Dungeon.level.mobs)
					if(mob.alignment == Alignment.ALLY && fieldOfView[mob.pos])
						enemies.add(mob);

				//and look for the hero
				if(fieldOfView[Dungeon.hero.pos])
				{
					enemies.add(Dungeon.hero);
				}

			}

			//neutral character in particular do not choose enemies.
			if(enemies.isEmpty())
			{
				return null;
			}
			else
			{
				//go after the closest potential enemy, preferring the hero if two are equidistant
				Char closest = null;
				for(Char curr : enemies)
				{
					if(closest == null
					   || Dungeon.level.distance(pos, curr.pos) < Dungeon.level.distance(pos, closest.pos)
					   || Dungeon.level.distance(pos, curr.pos) == Dungeon.level.distance(pos, closest.pos) && curr == Dungeon.hero)
					{
						closest = curr;
					}
				}
				return closest;
			}
		}
		else
			return enemy;
	}

	protected boolean moveSprite(int from, int to)
	{
		if(sprite.isVisible() && (Dungeon.level.heroFOV[from] || Dungeon.level.heroFOV[to]))
		{
			sprite.move(from, to);
			return true;
		}
		else
		{
			sprite.turnTo(from, to);
			sprite.place(to);
			return true;
		}
	}

	@Override
	public void add(Buff buff)
	{
		super.add(buff);
		if(buff instanceof Amok || buff instanceof Corruption)
		{
			state = HUNTING;
		}
		else if(buff instanceof Terror)
		{
			state = FLEEING;
		}
		else if(buff instanceof Sleep)
		{
			state = SLEEPING;
			postpone(Sleep.SWS);
		}
	}

	@Override
	public void remove(Buff buff)
	{
		super.remove(buff);
		if(buff instanceof Terror)
		{
			sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "rage"));
			state = HUNTING;
		}
	}

	protected boolean canAttack(Char enemy)
	{
		return Dungeon.level.adjacent(pos, enemy.pos);
	}

	protected boolean getCloser(int target)
	{
		if(rooted || target == pos)
		{
			return false;
		}

		int step = -1;

		if(Dungeon.level.adjacent(pos, target))
		{
			path = null;

			if(Actor.findChar(target) == null && Dungeon.level.passable[target])
				step = target;
		}
		else
		{
			boolean newPath = false;
			//scrap the current path if it's empty, no longer connects to the current location
			//or if it's extremely inefficient and checking again may result in a much better path
			//or if they're starting to lose sight of their target
			if(path == null || path.isEmpty()
					|| !Dungeon.level.adjacent(pos, path.getFirst())
					|| path.size() > 2 * Dungeon.level.distance(pos, target)
					|| enemySeen <= PATIENCE / 3)
				newPath = true;
			else if(path.getLast() != target)
			{
				//if the new target is adjacent to the end of the path, adjust for that
				//rather than scrapping the whole path.
				if(Dungeon.level.adjacent(target, path.getLast()))
				{
					int last = path.removeLast();

					if(path.isEmpty())
					{
						//shorten for a closer one
						if(Dungeon.level.adjacent(target, pos))
						{
							path.add(target);
							//extend the path for a further target
						}
						else
						{
							path.add(last);
							path.add(target);
						}
					}
					else if(!path.isEmpty())
					{
						//if the new target is simply 1 earlier in the path shorten the path
						if(path.getLast() == target)
						{
							//if the new target is closer/same, need to modify end of path
						}
						else if(Dungeon.level.adjacent(target, path.getLast()))
						{
							path.add(target);

							//if the new target is further away, need to extend the path
						}
						else
						{
							path.add(last);
							path.add(target);
						}
					}
				}
				else
					newPath = true;
			}

			if(!newPath)
			{
				//looks ahead for path validity, up to length-1 or 4, but always at least 1.
				int lookAhead = (int) GameMath.gate(1, path.size() - 1, 4);
				for(int i = 0; i < lookAhead; i++)
				{
					int cell = path.get(i);
					if(!Dungeon.level.passable[cell] || (fieldOfView[cell] && (Actor.findChar(cell) != null || (!Dungeon.level.adjacent(pos, target) && Blob.harmfulAt(pos, cell)))))
					{
						newPath = true;
						break;
					}
				}
			}

			if(newPath)
			{
				path = Dungeon.findPath(this, pos, target,
						Dungeon.level.passable,
						fieldOfView);
			}

			//if hunting something, don't follow a path that is extremely inefficient
			//FIXME this is fairly brittle, primarily it assumes that hunting mobs can't see through
			// permanent terrain, such that if their path is inefficient it's always because
			// of a temporary blockage, and therefore waiting for it to clear is the best option.
			if(path == null ||
			   (state == HUNTING && path.size() > Math.max(9, 2 * Dungeon.level.distance(pos, target))))
			{
				return false;
			}

			step = path.removeFirst();
		}
		if(step != -1)
		{
			move(step);
			return true;
		}
		else
			return false;
	}

	protected boolean getFurther(int target)
	{
		int step = Dungeon.flee(this, pos, target,
				Dungeon.level.passable,
				fieldOfView);
		if(step != -1)
		{
			move(step);
			return true;
		}
		else
			return false;
	}

	@Override
	public void updateSpriteState()
	{
		super.updateSpriteState();
		if(Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class) != null ||
		   Dungeon.hero.buff(Slow.Freeze.class) != null)
			sprite.add(CharSprite.State.PARALYSED);
	}

	protected float attackDelay()
	{
		return 1f;
	}

	protected boolean doAttack(Char enemy)
	{
		boolean visible = Dungeon.level.heroFOV[pos];

		if(visible)
		{
			sprite.attack(enemy.pos);
		}
		else
		{
			attack(enemy);
		}

		Invisibility.dispel(this);

		spend(attackDelay());

		return !visible;
	}

	@Override
	public void onAttackComplete()
	{
		attack(enemy);
		super.onAttackComplete();
	}

	@Override
	public int attackProc(Char enemy, int damage)
	{
		damage = super.attackProc(enemy, damage);

		if(buff(Weakness.class) != null)
			damage *= 0.67f;
		if(buff(Might.class) != null)
			damage *= 1.33f;
		if(buff(Taunted.class) != null)
			damage *= 2;

		return damage;
	}

	public int defenseSkill()
	{
		return 0;
	}

	@Override
	public int defenseSkill(Char enemy)
	{
		boolean seen = (enemySeen == PATIENCE && enemy.invisible == 0);
		if(enemy == Dungeon.hero && !Dungeon.hero.canSurpriseAttack()) seen = true;
		if(seen
		   && paralysed == 0
		   && !(alignment == Alignment.ALLY && enemy == Dungeon.hero))
		{
			if(enemy == Dungeon.hero && Dungeon.hero.belongings.weapon instanceof WarHammer
					&& Dungeon.hero.dynamicFactor() >= 1
					&& Dungeon.hero.STR() >= ((WarHammer) Dungeon.hero.belongings.weapon).STRReq())
				return 0;

			return this.defenseSkill();
		}
		else
			return 0;
	}

	@Override
	public int defenseProc(Char enemy, int damage)
	{
		if((enemySeen < PATIENCE || enemy.invisible > 0)
		   && enemy == Dungeon.hero && Dungeon.hero.canSurpriseAttack())
		{
			if(enemy.buff(Preparation.class) != null)
				Wound.hit(this);
			else
				Surprise.hit(this);
		}

		//if attacked by something else than current target, and that thing is closer, switch targets
		if(this.enemy == null
		   || (enemy != this.enemy && (Dungeon.level.distance(pos, enemy.pos) < Dungeon.level.distance(pos, this.enemy.pos))))
		{
			aggro(enemy);
			target = enemy.pos;
		}

		if(buff(SoulMark.class) != null)
		{
			int restoration = Math.min(damage, HP());
			Dungeon.hero.buff(Hunger.class).satisfy(restoration * 0.5f);
			Dungeon.hero.heal((int) Math.ceil(restoration * 0.25f), buff(SoulMark.class));
			Dungeon.hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
		}

		return damage;
	}

	public boolean surprisedBy(Char enemy)
	{
		return enemySeen < PATIENCE && enemy == Dungeon.hero;
	}

	public void aggro(Char ch)
	{
		enemy = ch;
		if(state != PASSIVE)
		{
			state = HUNTING;
		}
	}

	@Override
	public void damage(int dmg, Object src)
	{
		Terror.recover(this);

		if(state == SLEEPING)
		{
			state = WANDERING;
		}
		if(state != HUNTING)
		{
			alerted = true;
		}

		super.damage(dmg, src);
	}


	@Override
	public void destroy()
	{
		super.destroy();

		Dungeon.level.mobs.remove(this);

		if(Dungeon.hero.isAlive())
		{
			if(alignment == Alignment.ENEMY)
			{
				Statistics.enemiesSlain++;
				Badges.validateMonstersSlain();
				Statistics.qualifiedForNoKilling = false;

				int exp = EXP;
				if(exp > 0)
					Dungeon.hero.earnExp(exp);
			}
		}
	}

	@Override
	public void die(Object cause)
	{
		if(buff(Taunted.class) != null)
			EXP *= 2;

		if(cause == Chasm.class)
		{
			//50% chance to round up, 50% to round down
			if(EXP % 2 == 1) EXP += Random.Int(2);
			EXP /= 2;
		}

		super.die(cause);

		if(alignment == Alignment.ENEMY)
			rollToDropLoot();

		if(Dungeon.hero.isAlive() && !Dungeon.level.heroFOV[pos])
			GLog.i(Messages.get(this, "died"));
	}

	public void rollToDropLoot()
	{
		float lootChance = this.lootChance;
		lootChance *= RingOfWealth.dropChanceMultiplier(Dungeon.hero);

		if(Random.Float() < lootChance)
		{
			Item loot = createLoot();
			if(loot != null)
			{
				Dungeon.level.drop(loot, pos).sprite.drop();
			}
		}

		//ring of wealth logic
		if(Ring.getBonus(Dungeon.hero, RingOfWealth.Wealth.class) > 0)
		{
			int rolls = 1;
			if(properties.contains(Property.BOSS)) rolls = 15;
			else if(properties.contains(Property.MINIBOSS)) rolls = 5;
			ArrayList<Item> bonus = RingOfWealth.tryRareDrop(Dungeon.hero, rolls);
			if(bonus != null)
			{
				for(Item b : bonus) Dungeon.level.drop(b, pos).sprite.drop();
				new Flare(8, 32).color(0xFFFF00, true).show(sprite, 2f);
			}
		}
	}

	protected Object loot = null;
	protected float lootChance = 0;

	@SuppressWarnings("unchecked")
	protected Item createLoot()
	{
		Item item;
		if(loot instanceof Generator.Category)
		{
			item = Generator.random((Generator.Category) loot);
		}
		else if(loot instanceof Class<?>)
		{
			item = Generator.random((Class<? extends Item>) loot);
		}
		else
		{
			item = (Item) loot;
		}
		return item;
	}

	public boolean reset()
	{
		return false;
	}

	public void beckon(int cell)
	{
		beckon(cell, true);
	}

	public void beckon(int cell, boolean noticed)
	{
		if(noticed) notice();

		if(state != HUNTING)
		{
			state = WANDERING;
		}
		target = cell;
	}

	public void notice()
	{
		sprite.showAlert();
	}

	public void yell(String str)
	{
		GLog.n("%s: \"%s\" ", name, str);
	}

	public interface AiState
	{
		boolean act(boolean enemyInFOV, boolean justAlerted);
	}

	protected class Sleeping implements AiState
	{
		public static final String TAG = "SLEEPING";

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted)
		{
			if(enemyInFOV && Random.Int(distance(enemy) + enemy.stealth() + (enemy.flying ? 2 : 0)) == 0)
			{
				enemySeen = PATIENCE;

				notice();
				state = HUNTING;
				target = enemy.pos;

				if(Dungeon.isChallenged(Challenges.SWARM_INTELLIGENCE))
					for(Mob mob : Dungeon.level.mobs)
						if(Dungeon.level.distance(pos, mob.pos) <= 8 && mob.state != mob.HUNTING)
							mob.beckon(target);

				spend(TIME_TO_WAKE_UP);
			}
			else
			{
				enemySeen = 0;

				spend(TICK);
			}
			return true;
		}
	}

	protected class Wandering implements AiState
	{
		public static final String TAG = "WANDERING";

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted)
		{
			if(enemyInFOV && (justAlerted || Random.Int(distance(enemy) / 2 + enemy.stealth()) == 0))
			{
				enemySeen = PATIENCE;

				notice();
				alerted = true;
				state = HUNTING;
				target = enemy.pos;

				if(Dungeon.isChallenged(Challenges.SWARM_INTELLIGENCE))
					for(Mob mob : Dungeon.level.mobs)
						if(Dungeon.level.distance(pos, mob.pos) <= 8 && mob.state != mob.HUNTING)
							mob.beckon(target);
			}
			else
			{
				enemySeen = 0;

				int oldPos = pos;
				if(target != -1 && getCloser(target))
				{
					spend(1 / speed());
					return moveSprite(oldPos, pos);
				}
				else
				{
					target = Dungeon.level.randomDestination();
					spend(TICK);
				}
			}

			return true;
		}
	}

	protected class Hunting implements AiState
	{
		public static final String TAG = "HUNTING";

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted)
		{
			enemyInFOV = lookForEnemy(enemyInFOV);
			if(enemyInFOV && !isCharmedBy(enemy) && canAttack(enemy))
				return doAttack(enemy);
			else
			{
				if(enemyInFOV)
					target = enemy.pos;
				else if(enemy == null)
				{
					state = WANDERING;
					target = Dungeon.level.randomDestination();
					return true;
				}

				int oldPos = pos;
				if(target != -1 && getCloser(target))
				{
					spend(1 / speed());
					return moveSprite(oldPos, pos);
				}
				else
				{
					spend(TICK);
					if(!enemyInFOV)
					{
						sprite.showLost();
						state = WANDERING;
						target = Dungeon.level.randomDestination();
					}
					return true;
				}
			}
		}
	}

	protected class Fleeing implements AiState
	{
		public static final String TAG = "FLEEING";

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted)
		{
			//Terror can now be focused on a specific location instead of a mob
			Terror terror = buff(Terror.class);
			enemyInFOV = lookForEnemy(enemyInFOV);

			if(terror != null && enemy == null && terror.getPos() != -1)
				target = terror.getPos();
				//loses target when 0-dist rolls a 6 or greater.
			else if(enemy == null || !enemyInFOV && 1 + Random.Int(Dungeon.level.distance(pos, target)) >= 6)
				target = -1;
				//if enemy isn't in FOV, keep running from their previous position.
			else if(enemyInFOV)
				target = enemy.pos;

			int oldPos = pos;
			if(target != -1 && getFurther(target))
			{
				spend(1 / speed());
				return moveSprite(oldPos, pos);
			}
			else
			{
				spend(TICK);
				nowhereToRun();

				return true;
			}
		}

		protected void nowhereToRun() {}
	}

	protected class Passive implements AiState
	{
		public static final String TAG = "PASSIVE";

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted)
		{
			enemySeen = 0;
			spend(TICK);
			return true;
		}
	}
}

