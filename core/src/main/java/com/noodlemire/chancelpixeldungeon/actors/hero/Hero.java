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

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.Bones;
import com.noodlemire.chancelpixeldungeon.CPDSettings;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.GamesInProgress;
import com.noodlemire.chancelpixeldungeon.Statistics;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Amok;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Awareness;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Barkskin;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Berserk;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Combo;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Corruption;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Drowsy;
import com.noodlemire.chancelpixeldungeon.actors.buffs.DynamicRecovery;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Fury;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Haste;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Hunger;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Invisibility;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Might;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MindVision;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Momentum;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Paralysis;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Regeneration;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Slow;
import com.noodlemire.chancelpixeldungeon.actors.buffs.SnipersMark;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Vertigo;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Weakness;
import com.noodlemire.chancelpixeldungeon.actors.geysers.Geyser;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.actors.mobs.npcs.NPC;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.CheckedCell;
import com.noodlemire.chancelpixeldungeon.effects.Flare;
import com.noodlemire.chancelpixeldungeon.effects.PathIndicator;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.Amulet;
import com.noodlemire.chancelpixeldungeon.items.Ankh;
import com.noodlemire.chancelpixeldungeon.items.Dewdrop;
import com.noodlemire.chancelpixeldungeon.items.Heap;
import com.noodlemire.chancelpixeldungeon.items.Heap.Type;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.KindOfWeapon;
import com.noodlemire.chancelpixeldungeon.items.armor.glyphs.AntiMagic;
import com.noodlemire.chancelpixeldungeon.items.armor.glyphs.Viscosity;
import com.noodlemire.chancelpixeldungeon.items.artifacts.BraceletOfForce;
import com.noodlemire.chancelpixeldungeon.items.artifacts.CapeOfThorns;
import com.noodlemire.chancelpixeldungeon.items.artifacts.DriedRose;
import com.noodlemire.chancelpixeldungeon.items.artifacts.EtherealChains;
import com.noodlemire.chancelpixeldungeon.items.artifacts.HornOfPlenty;
import com.noodlemire.chancelpixeldungeon.items.artifacts.TalismanOfForesight;
import com.noodlemire.chancelpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.noodlemire.chancelpixeldungeon.items.keys.CrystalKey;
import com.noodlemire.chancelpixeldungeon.items.keys.GoldenKey;
import com.noodlemire.chancelpixeldungeon.items.keys.IronKey;
import com.noodlemire.chancelpixeldungeon.items.keys.Key;
import com.noodlemire.chancelpixeldungeon.items.keys.SkeletonKey;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfAccuracy;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfAptitude;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfEvasion;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfFuror;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfHaste;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfMight;
import com.noodlemire.chancelpixeldungeon.items.scrolls.Scroll;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfBlessing;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.noodlemire.chancelpixeldungeon.items.weapon.Bow;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Flail;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.noodlemire.chancelpixeldungeon.journal.Notes;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.levels.features.Chasm;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.plants.Earthroot;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.scenes.InterlevelScene;
import com.noodlemire.chancelpixeldungeon.scenes.SurfaceScene;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.sprites.HeroSprite;
import com.noodlemire.chancelpixeldungeon.ui.AttackIndicator;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.noodlemire.chancelpixeldungeon.ui.QuickSlotButton;
import com.noodlemire.chancelpixeldungeon.utils.BArray;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.noodlemire.chancelpixeldungeon.windows.WndAlchemy;
import com.noodlemire.chancelpixeldungeon.windows.WndLevelUp;
import com.noodlemire.chancelpixeldungeon.windows.WndMessage;
import com.noodlemire.chancelpixeldungeon.windows.WndResurrect;
import com.noodlemire.chancelpixeldungeon.windows.WndTradeItem;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

public class Hero extends Char
{
	{
		actPriority = HERO_PRIO;

		alignment = Alignment.ALLY;
	}

	public boolean rankings = false;

	private static final int STARTING_STR = 10;

	private static final float TIME_TO_REST = 1f;
	private static final float TIME_TO_SEARCH = 2f;
	private static final float HUNGER_FOR_SEARCH = 6f;

	public static final float[] DYNAMIC_MILESTONES = {0.25f, 0.5f, 0.75f, 1f, 1.25f};

	public HeroClass heroClass = HeroClass.ROGUE;
	public HeroSubClass subClass = HeroSubClass.NONE;

	private int attackSkill = 10;
	private int defenseSkill = 5;

	public boolean ready = false;
	private boolean damageInterrupt = true;
	public HeroAction curAction = null;
	public HeroAction lastAction = null;

	private Char enemy;

	public boolean resting = false;

	public Belongings belongings;

	public int lvl = 1;
	public int exp = 0;

	public int HTBoost = 0;

	private float dynamic = dynamax();
	public boolean critBoost = false;
	public boolean attacked = false;
	public boolean moved = false;

	private ArrayList<Char> visibleDangers;

	public int HT_lvl = 0;
	public int STR_lvl = 0;
	public int slot_lvl = 0;

	//This list is maintained so that some logic checks can be skipped
	// for enemies we know we aren't seeing normally, resulting in better performance
	public ArrayList<Mob> mindVisionEnemies = new ArrayList<>();

	public Hero()
	{
		super();
		name = Messages.get(this, "name");

		setHT(20, true);

		belongings = new Belongings(this);

		visibleDangers = new ArrayList<>();
	}

	public void updateHT(boolean boostHP)
	{
		int curHT = HT();

		int newHT = 20 + 5 * HT_lvl + HTBoost;
		float multiplier = RingOfMight.HTMultiplier(this);
		newHT = Math.round(multiplier * newHT);

		if(boostHP)
		{
			heal(Math.max(newHT - curHT, 0), null);
		}
		setHT(newHT, false);
	}

	public int STR(boolean includeBuffs)
	{
		int STR = STARTING_STR + STR_lvl;

		if(includeBuffs)
		{
			STR += RingOfMight.strengthBonus(this);

			if(buff(Weakness.class) != null) STR -= 2;
			if(buff(Might.class) != null) STR += 1;
		}

		return STR;
	}

	public int STR()
	{
		return STR(true);
	}

	public float dynamax()
	{
		return 7f + lvl * 2.1f;
	}

	public float dynamicFactor()
	{
		Combo combo = buff(Combo.class);
		float max = dynamax();

		if(combo != null)
			dynamic(max * combo.count() / 10f, false);

		return dynamic / max;
	}

	public int dynamicRoll(int min, int max)
	{
		return dynamicRoll(min, max, 1);
	}

	public int dynamicRoll(int min, int max, float multiplier)
	{
		int diff = max - min;
		int dmg = min + Math.round(diff * dynamicFactor());

		dmg = Math.min(max, dmg);

		//Because of the variety of times where being crit-boosed matters,
		//You will only lose a crit boost the moment of your first non-crit attack.
		critBoost = dynamic == dynamax();

		if(critBoost)
		{
			for(Item item : belongings.miscSlots)
			{
				if(item instanceof BraceletOfForce)
				{
					((BraceletOfForce) item).gainCharge();
					break;
				}
			}
		}

		if(buff(Combo.class) == null)
			dynamic(-dmg * multiplier);

		attacked = true;

		return dmg;
	}

	public boolean critBoost(Weapon wep)
	{
		if(wep == null || wep.STRReq() <= STR())
			return critBoost;

		return false;
	}

	public void dynamic(float change)
	{
		dynamic(change, true);
	}

	public void dynamic(float change, boolean add)
	{
		dynamic = add ? dynamic + change : change;
		float max = dynamax() * (BraceletOfForce.cursedHero(this) ? 0.75f : 1f);

		if(dynamic < 0)
			dynamic = 0;
		else if(dynamic > max)
			dynamic = max;

		dynamic = GameMath.gate(0, dynamic, max);
	}

	private static final String ATTACK = "attackSkill";
	private static final String DEFENSE = "defenseSkill";
	private static final String LEVEL = "lvl";
	private static final String EXPERIENCE = "exp";
	private static final String HTBOOST = "htboost";
	private static final String DYNAMIC = "dynamic";
	private static final String HT_LVL = "ht_lvl";
	private static final String STR_LVL = "str_lvl";
	private static final String SLOT_LVL = "slot_lvl";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);

		heroClass.storeInBundle(bundle);
		subClass.storeInBundle(bundle);

		bundle.put(ATTACK, attackSkill);
		bundle.put(DEFENSE, defenseSkill);

		bundle.put(LEVEL, lvl);
		bundle.put(EXPERIENCE, exp);

		bundle.put(HTBOOST, HTBoost);

		bundle.put(DYNAMIC, dynamic);

		bundle.put(HT_LVL, HT_lvl);
		bundle.put(STR_LVL, STR_lvl);
		bundle.put(SLOT_LVL, slot_lvl);

		belongings.storeInBundle(bundle);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);

		heroClass = HeroClass.restoreInBundle(bundle);
		subClass = HeroSubClass.restoreInBundle(bundle);

		attackSkill = bundle.getInt(ATTACK);
		defenseSkill = bundle.getInt(DEFENSE);

		lvl = bundle.getInt(LEVEL);
		exp = bundle.getInt(EXPERIENCE);

		HTBoost = bundle.getInt(HTBOOST);

		dynamic = bundle.getFloat(DYNAMIC);

		HT_lvl = bundle.getInt(HT_LVL);
		STR_lvl = bundle.getInt(STR_LVL);
		slot_lvl = bundle.getInt(SLOT_LVL);

		belongings.restoreFromBundle(bundle);
	}

	public static void preview(GamesInProgress.Info info, Bundle bundle)
	{
		info.level = bundle.getInt(LEVEL);
		info.str = 10 + bundle.getInt(STR_LVL);
		info.exp = bundle.getInt(EXPERIENCE);
		info.hp = bundle.getInt(Char.TAG_HP);
		info.ht = bundle.getInt(Char.TAG_HT);
		info.shld = bundle.getInt(Char.TAG_SHLD);
		info.heroClass = HeroClass.restoreInBundle(bundle);
		info.subClass = HeroSubClass.restoreInBundle(bundle);
		Belongings.preview(info, bundle);
	}

	public String className()
	{
		return subClass == null || subClass == HeroSubClass.NONE ? heroClass.title() : subClass.title();
	}

	public String givenName()
	{
		return name.equals(Messages.get(this, "name")) ? className() : name;
	}

	public void live()
	{
		Buff.affect(this, Regeneration.class);
		Buff.affect(this, Hunger.class);
		Buff.affect(this, DynamicRecovery.class);
	}

	public int tier()
	{
		return belongings.armor == null ? 0 : belongings.armor.tier;
	}

	//this variable is only needed because of the boomerang, remove if/when it is no longer equippable
	private boolean rangedAttack = false;

	public boolean shoot(Char enemy, MissileWeapon wep)
	{
		//temporarily set the hero's weapon to the missile weapon being used
		KindOfWeapon equipped = belongings.weapon;
		belongings.weapon = wep;
		rangedAttack = true;
		boolean result = attack(enemy);
		Invisibility.dispel();
		belongings.weapon = equipped;
		rangedAttack = false;

		return result;
	}

	@Override
	public int attackSkill(Char target)
	{
		KindOfWeapon wep = belongings.weapon;

		float accuracy = RingOfAccuracy.accuracyMultiplier(this);
		if(wep instanceof MissileWeapon && rangedAttack
				&& Dungeon.level.distance(pos, target.pos) == 1)
			accuracy *= 0.5f;

		if(wep != null)
			return (int) (attackSkill * accuracy * wep.accuracyFactor(this));
		else
			return (int) (attackSkill * accuracy);
	}

	@Override
	public int defenseSkill(Char enemy)
	{
		float evasion = defenseSkill;

		evasion *= RingOfEvasion.evasionMultiplier(this);

		if(paralysed > 0)
			evasion /= 2;

		if(belongings.armor != null)
			evasion = belongings.armor.evasionFactor(this, evasion);

		return Math.round(evasion);
	}

	@Override
	public int drRoll()
	{
		int dr = 0;
		Barkskin bark = buff(Barkskin.class);

		if(belongings.armor != null)
		{
			int armDr = Random.NormalIntRange(belongings.armor.DRMin(), belongings.armor.DRMax());
			if(STR() < belongings.armor.STRReq())
				armDr -= 2 * (belongings.armor.STRReq() - STR());

			if(armDr > 0) dr += armDr;
		}
		if(belongings.weapon != null)
		{
			int wepDr = Random.NormalIntRange(0, belongings.weapon.defenseFactor(this));
			if(STR() < ((Weapon) belongings.weapon).STRReq())
				wepDr -= 2 * (((Weapon) belongings.weapon).STRReq() - STR());

			if(wepDr > 0) dr += wepDr;
		}
		if(bark != null)
			dr += Random.NormalIntRange(0, (int) bark.left());

		return dr;
	}

	@Override
	public int damageRoll()
	{
		KindOfWeapon wep = belongings.weapon;
		int dmg;

		if(wep != null)
			dmg = wep.damageRoll(this);
		else
			dmg = dynamicRoll(1, Math.max(STR() - 8, 1));

		if(dmg < 0) dmg = 0;

		Berserk berserk = buff(Berserk.class);
		if(berserk != null)
			dmg = berserk.damageFactor(dmg);

		if(buff(Fury.class) != null)
			dmg = Math.round(dmg * 1.5f);

		return dmg;
	}

	@Override
	public float speed()
	{
		float speed = super.speed();

		speed *= RingOfHaste.speedMultiplier(this);

		if(belongings.armor != null)
			speed = belongings.armor.speedFactor(this, speed);

		Momentum momentum = buff(Momentum.class);
		if(momentum != null)
		{
			((HeroSprite) sprite).sprint(1f + 0.05f * momentum.stacks());
			speed *= momentum.speedMultiplier();
		}

		return speed;
	}

	public boolean canSurpriseAttack()
	{
		if(!(belongings.weapon instanceof Weapon)) return true;
		if(STR() < ((Weapon) belongings.weapon).STRReq()) return false;
		return !(belongings.weapon instanceof Flail);
	}

	public boolean canAttack(Char enemy)
	{
		if(enemy == null || pos == enemy.pos)
			return false;

		//can always attack adjacent enemies
		if(Dungeon.level.adjacent(pos, enemy.pos))
			return true;

		KindOfWeapon wep = Dungeon.hero.belongings.weapon;

		if(wep != null && Dungeon.level.distance(pos, enemy.pos) <= wep.reachFactor(this))
		{
			boolean[] passable = BArray.not(Dungeon.level.solid, null);
			for(Mob m : Dungeon.level.mobs)
				passable[m.pos] = false;
			for(Geyser g : Dungeon.level.geysers)
				passable[g.pos] = false;
			for(Char ch : Dungeon.level.others)
				passable[ch.pos] = false;

			PathFinder.buildDistanceMap(enemy.pos, passable, wep.reachFactor(this));

			return PathFinder.distance[pos] <= wep.reachFactor(this);
		}
		else
			return false;
	}

	public float attackDelay()
	{
		if(belongings.weapon != null)
			return belongings.weapon.speedFactor(this);
		else
		{
			//Normally putting furor speed on unarmed attacks would be unnecessary
			//But there's going to be that one guy who gets a furor+force ring combo
			//This is for that one guy, you shall get your fists of fury!
			return RingOfFuror.modifyAttackDelay(this);
		}
	}

	@Override
	public void spend(float time)
	{
		justMoved = false;

		TimekeepersHourglass.timeFreeze timebuff = buff(TimekeepersHourglass.timeFreeze.class);
		Slow.Freeze slowbuff = buff(Slow.Freeze.class);
		if(timebuff != null)
			timebuff.processTime(time);
		else if(slowbuff != null)
			slowbuff.shorten(time);
		else
			super.spend(time);
	}

	public void spendAndNext(float time)
	{
		busy();
		spend(time);
		next();
	}

	@Override
	public boolean act()
	{
		//calls to dungeon.observe will also update hero's local FOV.
		fieldOfView = Dungeon.level.heroFOV;

		if(!ready)
			//do a full observe (including fog update) if not resting.
			if(!resting || buff(MindVision.class) != null || buff(Awareness.class) != null || heroClass == HeroClass.HUNTRESS)
				Dungeon.observe();
			else
				//otherwise just directly re-calculate FOV
				Dungeon.level.updateFieldOfView(this, fieldOfView);

		checkVisibleDangers();
		BuffIndicator.refreshHero();

		if(paralysed > 0)
		{
			curAction = null;

			spendAndNext(TICK);
			return false;
		}

		if(curAction == null)
		{
			if(resting)
			{
				spend(TIME_TO_REST);
				next();
				return false;
			}

			ready();
			return false;
		}
		else
		{
			resting = false;

			ready = false;

			if(curAction instanceof HeroAction.Move)
				return actMove((HeroAction.Move) curAction);
			else if(curAction instanceof HeroAction.Interact)
				return actInteract((HeroAction.Interact) curAction);
			else if(curAction instanceof HeroAction.Buy)
				return actBuy((HeroAction.Buy) curAction);
			else if(curAction instanceof HeroAction.PickUp)
				return actPickUp((HeroAction.PickUp) curAction);
			else if(curAction instanceof HeroAction.OpenChest)
				return actOpenChest((HeroAction.OpenChest) curAction);
			else if(curAction instanceof HeroAction.Unlock)
				return actUnlock((HeroAction.Unlock) curAction);
			else if(curAction instanceof HeroAction.Descend)
				return actDescend((HeroAction.Descend) curAction);
			else if(curAction instanceof HeroAction.Ascend)
				return actAscend((HeroAction.Ascend) curAction);
			else if(curAction instanceof HeroAction.Attack)
				return actAttack((HeroAction.Attack) curAction);
			else if(curAction instanceof HeroAction.Alchemy)
				return actAlchemy((HeroAction.Alchemy) curAction);
		}

		return false;
	}

	public void busy()
	{
		ready = false;
	}

	private void ready()
	{
		if(sprite.looping()) sprite.idle();
		curAction = null;
		damageInterrupt = true;
		ready = true;

		AttackIndicator.updateState();

		GameScene.ready();
	}

	public void interrupt()
	{
		if(isAlive() && curAction != null &&
				((curAction.dst != pos) ||
						(curAction instanceof HeroAction.Ascend || curAction instanceof HeroAction.Descend)))
		{
			lastAction = curAction;
		}
		curAction = null;
	}

	public void resume()
	{
		curAction = lastAction;
		lastAction = null;
		damageInterrupt = false;
		next();
	}

	//FIXME this is a fairly crude way to track this, really it would be nice to have a short
	//history of hero actions
	public boolean justMoved = false;

	private boolean actMove(HeroAction.Move action)
	{
		if(getCloser(action.dst))
		{
			justMoved = true;
			moved = true;
			return true;
		}
		else
		{
			ready();
			return false;
		}
	}

	private boolean actInteract(HeroAction.Interact action)
	{
		Char ch = action.ch;

		if(Dungeon.level.adjacent(pos, ch.pos))
		{
			ready();
			sprite.turnTo(pos, ch.pos);
			if(ch instanceof NPC)
				return ((NPC) ch).interact();
			else if(Dungeon.level.passable[pos] || Dungeon.hero.flying)
			{
				int curPos = ch.pos;

				ch.sprite.move(ch.pos, pos);
				ch.move(pos);

				sprite.move(pos, curPos);
				move(curPos);

				spend(1 / Dungeon.hero.speed());
				busy();
				return true;
			}
			else
				return false;
		}
		else
		{
			if(fieldOfView[ch.pos] && getCloser(ch.pos))
				return true;
			else
			{
				ready();
				return false;
			}
		}
	}

	private boolean actBuy(HeroAction.Buy action)
	{
		int dst = action.dst;
		if(pos == dst || Dungeon.level.adjacent(pos, dst))
		{
			ready();

			Heap heap = Dungeon.level.heaps.get(dst);
			if(heap != null && heap.type == Type.FOR_SALE && heap.size() == 1)
			{
				GameScene.show(new WndTradeItem(heap, true));
			}

			return false;
		}
		else if(getCloser(dst))
			return true;
		else
		{
			ready();
			return false;
		}
	}

	private boolean actAlchemy(HeroAction.Alchemy action)
	{
		int dst = action.dst;
		if(Dungeon.level.distance(dst, pos) <= 1)
		{
			ready();
			GameScene.show(new WndAlchemy());
			return false;
		}
		else if(getCloser(dst))
			return true;
		else
		{
			ready();
			return false;
		}
	}

	private boolean actPickUp(HeroAction.PickUp action)
	{
		int dst = action.dst;
		if(pos == dst)
		{
			Heap heap = Dungeon.level.heaps.get(pos);
			if(heap != null)
			{
				Item item = heap.peek();
				if(item.doPickUp(this))
				{
					heap.pickUp();

					if(item instanceof Dewdrop
							|| item instanceof TimekeepersHourglass.sandBag
							|| item instanceof DriedRose.Petal
							|| item instanceof Key)
					{
						//Do Nothing
					}
					else
					{
						boolean important =
								((item instanceof ScrollOfUpgrade || item instanceof ScrollOfBlessing) && ((Scroll) item).isKnown());
						if(important)
							GLog.p(Messages.get(this, "you_now_have", item.name()));
						else
							GLog.i(Messages.get(this, "you_now_have", item.name()));
					}

					curAction = null;
				}
				else
				{
					heap.sprite.drop();
					ready();
				}
			}
			else
				ready();

			return false;

		}
		else if(getCloser(dst))
			return true;
		else
		{
			ready();
			return false;
		}
	}

	private boolean actOpenChest(HeroAction.OpenChest action)
	{
		int dst = action.dst;
		if(Dungeon.level.adjacent(pos, dst) || pos == dst)
		{
			Heap heap = Dungeon.level.heaps.get(dst);
			if(heap != null && (heap.type != Type.HEAP && heap.type != Type.FOR_SALE))
			{
				if((heap.type == Type.LOCKED_CHEST && Notes.keyCount(new GoldenKey(Dungeon.depth)) < 1)
						|| (heap.type == Type.CRYSTAL_CHEST && Notes.keyCount(new CrystalKey(Dungeon.depth)) < 1))
				{

					GLog.w(Messages.get(this, "locked_chest"));
					ready();
					return false;

				}

				switch(heap.type)
				{
					case TOMB:
						Sample.INSTANCE.play(Assets.SND_TOMB);
						Camera.main.shake(1, 0.5f);
						break;
					case SKELETON:
					case REMAINS:
						break;
					default:
						Sample.INSTANCE.play(Assets.SND_UNLOCK);
				}

				sprite.operate(dst);
			}
			else
				ready();

			return false;

		}
		else if(getCloser(dst))
			return true;
		else
		{
			ready();
			return false;
		}
	}

	private boolean actUnlock(HeroAction.Unlock action)
	{
		int doorCell = action.dst;
		if(Dungeon.level.adjacent(pos, doorCell))
		{
			boolean hasKey = false;
			int door = Dungeon.level.map[doorCell];

			if(door == Terrain.LOCKED_DOOR
					&& Notes.keyCount(new IronKey(Dungeon.depth)) > 0)
				hasKey = true;
			else if(door == Terrain.LOCKED_EXIT
					&& Notes.keyCount(new SkeletonKey(Dungeon.depth)) > 0)
				hasKey = true;

			if(hasKey)
			{
				sprite.operate(doorCell);

				Sample.INSTANCE.play(Assets.SND_UNLOCK);
			}
			else
			{
				GLog.w(Messages.get(this, "locked_door"));
				ready();
			}

			return false;
		}
		else if(getCloser(doorCell))
			return true;
		else
		{
			ready();
			return false;
		}
	}

	public static void detachTimeFreeze()
	{
		Buff timebuff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
		if(timebuff != null) timebuff.detach();
		Buff slowbuff = Dungeon.hero.buff(Slow.Freeze.class);
		if(slowbuff != null) slowbuff.detach();
	}

	public void checkFollowers()
	{
		if(buff(TimekeepersHourglass.timeFreeze.class) == null && buff(Slow.Freeze.class) == null)
		{
			for(Mob m : Dungeon.level.mobs)
				if(Dungeon.level.adjacent(pos, m.pos) && m.getEnemy() == this
						&& !m.properties().contains(Property.BOSS)
						&& !m.properties().contains(Property.MINIBOSS)
						&& !m.properties().contains(Property.IMMOVABLE))
					InterlevelScene.followingEnemies.add(m);

			for(Mob m : InterlevelScene.followingEnemies)
			{
				Dungeon.level.mobs.remove(m);
			}
		}
	}

	private boolean actDescend(HeroAction.Descend action)
	{
		int stairs = action.dst;
		if(pos == stairs && pos == Dungeon.level.exit)
		{
			curAction = null;

			checkFollowers();

			detachTimeFreeze();

			InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
			Game.switchScene(InterlevelScene.class);

			return false;
		}
		else if(getCloser(stairs))
			return true;
		else
		{
			ready();
			return false;
		}
	}

	private boolean actAscend(HeroAction.Ascend action)
	{
		int stairs = action.dst;
		if(pos == stairs && pos == Dungeon.level.entrance)
		{
			if(Dungeon.depth == 1)
			{
				if(belongings.getItem(Amulet.class) == null)
				{
					GameScene.show(new WndMessage(Messages.get(this, "leave")));
					ready();
				}
				else
				{
					Badges.silentValidateHappyEnd();
					Dungeon.win(Amulet.class);
					Dungeon.deleteGame(GamesInProgress.curSlot, true);
					Game.switchScene(SurfaceScene.class);
				}
			}
			else
			{
				curAction = null;

				checkFollowers();

				detachTimeFreeze();

				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene(InterlevelScene.class);
			}

			return false;
		}
		else if(getCloser(stairs))
			return true;
		else
		{
			ready();
			return false;
		}
	}

	private boolean actAttack(HeroAction.Attack action)
	{
		enemy = action.target;

		if(enemy.invisible > 0 && buff(MindVision.class) == null)
		{
			ready();
			GLog.w(Messages.get(this, "no_attack_invis"));
			return true;
		}

		if(enemy.isAlive() && canAttack(enemy) && !isCharmedBy(enemy))
		{
			sprite.attack(enemy.pos);

			return false;
		}
		else
		{
			if(fieldOfView[enemy.pos] && getCloser(enemy.pos))
				return true;
			else
			{
				ready();
				return false;
			}
		}
	}

	public Char enemy()
	{
		return enemy;
	}

	public void rest(boolean fullRest)
	{
		spendAndNext(TIME_TO_REST);
		if(!fullRest)
			sprite.showStatus(CharSprite.DEFAULT, Messages.get(this, "wait"));
		resting = fullRest;
	}

	@Override
	public int attackProc(final Char enemy, int damage)
	{
		return attackProc(enemy, damage, belongings.weapon);
	}

	public int attackProc(final Char enemy, int damage, KindOfWeapon wep)
	{
		if(wep != null) damage = wep.proc(this, enemy, damage);

		switch(subClass)
		{
			case SNIPER:
				if(wep instanceof MissileWeapon && !(wep instanceof Bow.Arrow))
				{
					final float delay = attackDelay();
					Actor.add(new Actor()
					{
						{
							actPriority = VFX_PRIO;
						}

						@Override
						protected boolean act()
						{
							if(enemy.isAlive())
								Buff.prolong(Hero.this, SnipersMark.class, delay).object = enemy.id();
							Actor.remove(this);
							return true;
						}
					});
				}
				break;
			default:
		}

		return damage;
	}

	@Override
	public int defenseProc(Char enemy, int damage)
	{
		if(damage > 0 && subClass == HeroSubClass.BERSERKER)
		{
			Berserk berserk = Buff.affect(this, Berserk.class);
			berserk.damage(damage);
		}

		if(belongings.armor != null)
			damage = belongings.armor.proc(enemy, this, damage);

		Earthroot.Armor armor = buff(Earthroot.Armor.class);
		if(armor != null)
			damage = armor.absorb(damage);

		return damage;
	}

	@Override
	public void damage(int dmg, Object src)
	{
		if(buff(TimekeepersHourglass.timeStasis.class) != null ||
				buff(Haste.Stasis.class) != null)
			return;

		if(!(src instanceof Hunger || src instanceof Viscosity.DeferedDamage) && damageInterrupt)
		{
			interrupt();
			resting = false;
		}

		if(this.buff(Drowsy.class) != null)
		{
			Buff.detach(this, Drowsy.class);
			GLog.w(Messages.get(this, "pain_resist"));
		}

		CapeOfThorns.Thorns thorns = buff(CapeOfThorns.Thorns.class);
		if(thorns != null)
			dmg = thorns.proc(dmg, (src instanceof Char ? (Char) src : null), this);

		//TODO improve this when I have proper damage source logic
		if(belongings.armor != null && belongings.armor.hasGlyph(AntiMagic.class)
				&& AntiMagic.RESISTS.contains(src.getClass()))
			dmg -= Random.NormalIntRange(belongings.armor.DRMin(), belongings.armor.DRMax()) / 3;

		super.damage(dmg, src);
	}

	public void checkVisibleDangers()
	{
		ArrayList<Char> visible = new ArrayList<>();
		boolean seenMob = false;

		boolean newChar = false;

		Char target = null;
		for(Mob m : Dungeon.level.mobs.toArray(new Mob[0]))
		{
			if(fieldOfView[m.pos] && m.alignment == Alignment.ENEMY)
			{
				visible.add(m);
				seenMob = true;

				if(!visibleDangers.contains(m))
					newChar = true;

				if(!mindVisionEnemies.contains(m) && QuickSlotButton.autoAim(m) != -1)
					if(target == null)
						target = m;
					else if(distance(target) > distance(m))
						target = m;
			}
		}

		DynamicRecovery dr = buff(DynamicRecovery.class);
		if(dr != null && seenMob)
			dr.resetTimer();

		for(Geyser g : Dungeon.level.geysers.toArray(new Geyser[0]))
		{
			if(fieldOfView[g.pos])
			{
				visible.add(g);
				if(!visibleDangers.contains(g))
					newChar = true;
			}
		}

		if(target != null && (QuickSlotButton.lastTarget == null ||
				!QuickSlotButton.lastTarget.isAlive() ||
				!fieldOfView[QuickSlotButton.lastTarget.pos]))
			QuickSlotButton.target(target);

		if(newChar)
		{
			interrupt();
			resting = false;
		}

		visibleDangers = visible;
	}

	public int visibleDangers()
	{
		return visibleDangers.size();
	}

	public Char visibleDanger(int index)
	{
		return visibleDangers.get(index % visibleDangers.size());
	}

	private boolean getCloser(final int target)
	{
		if(target == pos)
		{
			PathIndicator.unset();
			return false;
		}

		if(rooted)
		{
			Camera.main.shake(1, 1f);
			PathIndicator.unset();
			return false;
		}

		int step = -1;

		if(Dungeon.level.adjacent(pos, target))
		{
			path = null;

			if(Actor.findChar(target) == null)
			{
				if(Dungeon.level.pit[target] && !flying && !Dungeon.level.solid[target])
				{
					if(!Chasm.jumpConfirmed)
					{
						Chasm.heroJump(this);
						interrupt();
					}
					else
						Chasm.heroFall(target);

					PathIndicator.unset();
					return false;
				}
				if(Dungeon.level.passable[target] || Dungeon.level.avoid[target])
					step = target;
			}
		}
		else
		{
			boolean newPath = false;
			if(path == null || path.isEmpty() || !Dungeon.level.adjacent(pos, path.getFirst()))
				newPath = true;
			else if(path.getLast() != target)
				newPath = true;
			else
			{
				//looks ahead for path validity, up to length-1 or 2.
				//Note that this is shorter than for mobs, so that mobs usually yield to the hero
				int lookAhead = (int) GameMath.gate(0, path.size() - 1, 2);
				for(int i = 0; i < lookAhead; i++)
				{
					int cell = path.get(i);
					if(!Dungeon.level.passable[cell] || (fieldOfView[cell] && (Actor.findChar(cell) != null || Blob.harmfulAt(pos, cell))))
					{
						newPath = true;
						break;
					}
				}
			}

			if(newPath)
			{
				int len = Dungeon.level.length();
				boolean[] p = Dungeon.level.passable;
				boolean[] v = Dungeon.level.visited;
				boolean[] m = Dungeon.level.mapped;
				boolean[] passable = new boolean[len];
				for(int i = 0; i < len; i++)
					passable[i] = p[i] && (v[i] || m[i]);

				path = Dungeon.findPath(this, pos, target, passable, fieldOfView);

				if(path != null && path.size() > 1)
				{
					int visTarget = target;
					if(!Dungeon.level.passable[target])
						visTarget = path.get(path.size() - 2);

					if(CPDSettings.show_destination() || CPDSettings.show_paths())
					{
						sprite.parent.addToBack(new PathIndicator(visTarget));
						PathIndicator.createNodePath(sprite.parent, path);
					}
				}
			}

			if(path == null)
			{
				PathIndicator.unset();
				return false;
			}
			step = path.removeFirst();
		}

		if(step != -1)
		{
			float speed = speed();

			sprite.move(pos, step);
			move(step);

			spend(1 / speed);

			search(false);

			if(subClass == HeroSubClass.FREERUNNER)
				Buff.affect(this, Momentum.class).gainStack();

			//FIXME this is a fairly sloppy fix for a crash involving pitfall traps.
			//really there should be a way for traps to specify whether action should continue or
			//not when they are pressed.
			if(InterlevelScene.mode != InterlevelScene.Mode.FALL)
			{
				PathIndicator.follow();
				return true;
			}
			else
			{
				PathIndicator.unset();
				return false;
			}
		}
		else
		{
			PathIndicator.unset();
			return false;
		}
	}

	public boolean handle(int cell)
	{
		if(cell == -1)
			return false;

		Char ch;
		Heap heap;

		if(Dungeon.level.map[cell] == Terrain.ALCHEMY && cell != pos)
			curAction = new HeroAction.Alchemy(cell);
		else if(fieldOfView[cell] && (ch = Actor.findChar(cell)) != null && ch != this)
			if(ch instanceof NPC || (ch.buff(Corruption.class) != null && ch.buff(Amok.class) == null))
				curAction = new HeroAction.Interact(ch);
			else
				curAction = new HeroAction.Attack(ch);
		else if((heap = Dungeon.level.heaps.get(cell)) != null
				//moving to an item doesn't auto-pickup when enemies are near...
				&& (visibleDangers.size() == 0 || cell == pos ||
				//...but only for standard heaps; chests and similar open as normal.
				(heap.type != Type.HEAP && heap.type != Type.FOR_SALE)))
		{
			switch(heap.type)
			{
				case HEAP:
					curAction = new HeroAction.PickUp(cell);
					break;
				case FOR_SALE:
					curAction = heap.size() == 1 && heap.peek().price() > 0 ?
							new HeroAction.Buy(cell) :
							new HeroAction.PickUp(cell);
					break;
				default:
					curAction = new HeroAction.OpenChest(cell);
			}
		}
		else if(Dungeon.level.map[cell] == Terrain.LOCKED_DOOR || Dungeon.level.map[cell] == Terrain.LOCKED_EXIT)
			curAction = new HeroAction.Unlock(cell);
		else if(cell == Dungeon.level.exit && Dungeon.depth < 26)
			curAction = new HeroAction.Descend(cell);
		else if(cell == Dungeon.level.entrance)
			curAction = new HeroAction.Ascend(cell);
		else
		{
			curAction = new HeroAction.Move(cell);
			lastAction = null;
		}

		return true;
	}

	public void earnExp(int exp)
	{
		exp = Math.round(exp * RingOfAptitude.experienceMultiplier(this));
		if(exp == 0)
			return;

		sprite.showStatus(CharSprite.POSITIVE, Messages.get(Mob.class, "exp", exp));

		this.exp += exp;
		float percent = exp / (float) maxExp();

		EtherealChains.chainsRecharge chains = buff(EtherealChains.chainsRecharge.class);
		if(chains != null) chains.gainExp(percent);

		HornOfPlenty.hornRecharge horn = buff(HornOfPlenty.hornRecharge.class);
		if(horn != null) horn.gainCharge(percent);

		Berserk berserk = buff(Berserk.class);
		if(berserk != null) berserk.recover(percent);

		boolean levelUp = false;
		while(this.exp >= maxExp())
		{
			this.exp -= maxExp();
			lvl++;
			levelUp = true;

			attackSkill++;
			defenseSkill++;

			GameScene.show(new WndLevelUp());
		}

		if(levelUp)
		{
			Badges.validateLevelReached();
			Badges.validateStrengthAttained();

			Item bow = null;

			for(Item item : belongings.backpack.items)
			{
				if(item instanceof Bow)
				{
					bow = item;
					break;
				}
			}

			if(heroClass == HeroClass.HUNTRESS && bow != null)
			{
				Badges.validateItemLevelAquired(bow);
				bow.updateQuickslot();
			}

			Sample.INSTANCE.play(Assets.SND_LEVELUP);
		}
	}

	public int maxExp()
	{
		return maxExp(lvl);
	}

	public static int maxExp(int lvl)
	{
		return 4 + lvl * 4;
	}

	public boolean isStarving()
	{
		return buff(Hunger.class) != null && buff(Hunger.class).isStarving();
	}

	@Override
	public void add(Buff buff)
	{
		if(buff(TimekeepersHourglass.timeStasis.class) != null ||
				buff(Haste.Stasis.class) != null)
			return;

		super.add(buff);

		if(sprite != null)
		{
			String msg = buff.heroMessage();
			if(msg != null)
				GLog.w(msg);

			if(buff instanceof Paralysis || buff instanceof Vertigo)
				interrupt();
		}

		BuffIndicator.refreshHero();
	}

	@Override
	public void remove(Buff buff)
	{
		super.remove(buff);

		BuffIndicator.refreshHero();
	}

	@Override
	public int stealth()
	{
		int stealth = super.stealth();

		if(belongings.armor != null)
			stealth = Math.round(belongings.armor.stealthFactor(this, stealth));

		return stealth;
	}

	@Override
	public void die(Object cause)
	{
		curAction = null;

		Ankh ankh = null;

		//look for ankhs in player inventory, prioritize ones which are blessed.
		for(Item item : belongings)
		{
			if(item instanceof Ankh)
			{
				ankh = (Ankh) item;
				if(ankh.isBlessed())
					break;
			}
		}

		if(ankh != null && ankh.isBlessed())
		{
			heal(HT() / 4, ankh);

			//ensures that you'll get to act first in almost any case, to prevent reviving and then instantly dieing again.
			Buff.detach(this, Paralysis.class);
			spend(-cooldown());

			new Flare(8, 32).color(0xFFFF66, true).show(sprite, 2f);
			CellEmitter.get(this.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);

			ankh.detach(belongings.backpack);

			Sample.INSTANCE.play(Assets.SND_TELEPORT);
			GLog.w(Messages.get(this, "revive"));
			Statistics.ankhsUsed++;

			return;
		}

		Actor.fixTime();
		super.die(cause);

		if(ankh == null)
			reallyDie(cause);
		else
		{
			Dungeon.deleteGame(GamesInProgress.curSlot, false);
			GameScene.show(new WndResurrect(ankh, cause));
		}
	}

	public static void reallyDie(Object cause)
	{
		int length = Dungeon.level.length();
		int[] map = Dungeon.level.map;
		boolean[] visited = Dungeon.level.visited;
		boolean[] discoverable = Dungeon.level.discoverable;

		for(int i = 0; i < length; i++)
		{
			int terr = map[i];

			if(discoverable[i])
			{
				visited[i] = true;
				if((Terrain.flags[terr] & Terrain.SECRET) != 0)
				{
					Dungeon.level.discover(i);
				}
			}
		}

		Bones.leave();

		Dungeon.observe();
		GameScene.updateFog();

		Dungeon.hero.belongings.identify();

		int pos = Dungeon.hero.pos;

		ArrayList<Integer> passable = new ArrayList<>();
		for(Integer ofs : PathFinder.NEIGHBOURS8)
		{
			int cell = pos + ofs;
			if((Dungeon.level.passable[cell] || Dungeon.level.avoid[cell]) && Dungeon.level.heaps.get(cell) == null)
				passable.add(cell);
		}
		Collections.shuffle(passable);

		ArrayList<Item> items = new ArrayList<>(Dungeon.hero.belongings.backpack.items);
		for(Integer cell : passable)
		{
			if(items.isEmpty())
				break;

			Item item = Random.element(items);
			Dungeon.level.drop(item, cell).sprite.drop(pos);
			items.remove(item);
		}

		GameScene.gameOver();

		if(cause instanceof Hero.Doom)
			((Hero.Doom) cause).onDeath();

		Dungeon.deleteGame(GamesInProgress.curSlot, true);
	}

	//effectively cache this buff to prevent having to call buff(Berserk.class) a bunch.
	//This is relevant because we call isAlive during drawing, which has both performance
	//and concurrent modification implications if that method calls buff(Berserk.class)
	private Berserk berserk;

	@Override
	public boolean isAlive()
	{
		if(HP() <= 0)
		{
			if(berserk == null) berserk = buff(Berserk.class);
			return berserk != null && berserk.berserking();
		}
		else
		{
			berserk = null;
			return super.isAlive();
		}
	}

	@Override
	public void move(int step)
	{
		super.move(step);

		moved = true;

		if(!flying)
			if(Dungeon.level.water[pos])
				Sample.INSTANCE.play(Assets.SND_WATER, 1, 1, Random.Float(0.8f, 1.25f));
			else
				Sample.INSTANCE.play(Assets.SND_STEP);
	}

	@Override
	public void onAttackComplete()
	{
		AttackIndicator.target(enemy);

		boolean hit = attack(enemy);

		if(subClass == HeroSubClass.GLADIATOR)
		{
			if(hit)
				Buff.affect(this, Combo.class).hit();
			else
			{
				Combo combo = buff(Combo.class);
				if(combo != null) combo.miss();
			}
		}

		//Misses still count as attacks, so dynamic won't be increased this hero turn.
		attacked = true;

		Invisibility.dispel();
		spend(attackDelay());

		curAction = null;

		super.onAttackComplete();
	}

	@Override
	public void onOperateComplete()
	{
		if(curAction instanceof HeroAction.Unlock)
		{
			int doorCell = curAction.dst;
			int door = Dungeon.level.map[doorCell];

			if(door == Terrain.LOCKED_DOOR)
			{
				Notes.remove(new IronKey(Dungeon.depth));
				Level.set(doorCell, Terrain.DOOR);
			}
			else
			{
				Notes.remove(new SkeletonKey(Dungeon.depth));
				Level.set(doorCell, Terrain.UNLOCKED_EXIT);
			}
			GameScene.updateKeyDisplay();

			Level.set(doorCell, door == Terrain.LOCKED_DOOR ? Terrain.DOOR : Terrain.UNLOCKED_EXIT);
			GameScene.updateMap(doorCell);
			spend(Key.TIME_TO_UNLOCK);
		}
		else if(curAction instanceof HeroAction.OpenChest)
		{
			Heap heap = Dungeon.level.heaps.get(curAction.dst);
			if(heap.type == Type.SKELETON || heap.type == Type.REMAINS)
				Sample.INSTANCE.play(Assets.SND_BONES);
			else if(heap.type == Type.LOCKED_CHEST)
				Notes.remove(new GoldenKey(Dungeon.depth));
			else if(heap.type == Type.CRYSTAL_CHEST)
				Notes.remove(new CrystalKey(Dungeon.depth));

			GameScene.updateKeyDisplay();
			heap.open(this);
			spend(Key.TIME_TO_UNLOCK);
		}
		curAction = null;

		super.onOperateComplete();
	}

	public boolean search(boolean intentional)
	{
		if(!isAlive()) return false;

		boolean smthFound = false;

		int distance = heroClass == HeroClass.ROGUE ? 2 : 1;

		int cx = pos % Dungeon.level.width();
		int cy = pos / Dungeon.level.width();
		int ax = cx - distance;
		if(ax < 0)
			ax = 0;
		int bx = cx + distance;
		if(bx >= Dungeon.level.width())
			bx = Dungeon.level.width() - 1;
		int ay = cy - distance;
		if(ay < 0)
			ay = 0;
		int by = cy + distance;
		if(by >= Dungeon.level.height())
			by = Dungeon.level.height() - 1;

		TalismanOfForesight.Foresight foresight = buff(TalismanOfForesight.Foresight.class);
		boolean cursed = foresight != null && foresight.isCursed();

		for(int y = ay; y <= by; y++)
		{
			for(int x = ax, p = ax + y * Dungeon.level.width(); x <= bx; x++, p++)
			{
				if(fieldOfView[p] && p != pos)
				{
					if(intentional)
						sprite.parent.addToBack(new CheckedCell(p));

					if(Dungeon.level.secret[p])
					{
						float chance;
						//intentional searches always succeed
						if(intentional)
							chance = 1f;
							//unintentional searches always fail with a cursed talisman
						else if(cursed)
							chance = 0f;
							//unintentional trap detection scales from 40% at floor 0 to 30% at floor 25
						else if(Dungeon.level.map[p] == Terrain.SECRET_TRAP)
							chance = 0.4f - (Dungeon.depth / 250f);
							//unintentional door detection scales from 20% at floor 0 to 0% at floor 20
						else
							chance = 0.2f - (Dungeon.depth / 100f);

						if(Random.Float() < chance)
						{
							int oldValue = Dungeon.level.map[p];

							GameScene.discoverTile(p, oldValue);

							Dungeon.level.discover(p);

							ScrollOfIdentify.discover(p);

							smthFound = true;

							if(foresight != null && !foresight.isCursed())
								foresight.charge();
						}
					}
				}
			}
		}

		if(intentional)
		{
			sprite.showStatus(CharSprite.DEFAULT, Messages.get(this, "search"));
			sprite.operate(pos);
			if(cursed)
			{
				GLog.n(Messages.get(this, "search_distracted"));
				buff(Hunger.class).reduceHunger(TIME_TO_SEARCH - 2 * HUNGER_FOR_SEARCH);
			}
			else
				buff(Hunger.class).reduceHunger(TIME_TO_SEARCH - HUNGER_FOR_SEARCH);
			spendAndNext(TIME_TO_SEARCH);
		}

		if(smthFound)
		{
			GLog.w(Messages.get(this, "noticed_smth"));
			Sample.INSTANCE.play(Assets.SND_SECRET);
			interrupt();
		}

		return smthFound;
	}

	public void resurrect(int resetLevel)
	{
		heal(HT(), null);
		Dungeon.gold = 0;
		exp = 0;

		belongings.resurrect(resetLevel);

		live();
	}

	@Override
	public void next()
	{
		if(isAlive())
			super.next();
	}

	public interface Doom
	{
		void onDeath();
	}
}
