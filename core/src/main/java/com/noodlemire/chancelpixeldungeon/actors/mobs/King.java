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

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ToxicGas;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Blindness;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Paralysis;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Vertigo;
import com.noodlemire.chancelpixeldungeon.effects.Flare;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.artifacts.LloydsBeacon;
import com.noodlemire.chancelpixeldungeon.items.keys.SkeletonKey;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfDisintegration;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Grim;
import com.noodlemire.chancelpixeldungeon.levels.CityBossLevel;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.KingSprite;
import com.noodlemire.chancelpixeldungeon.sprites.UndeadSprite;
import com.noodlemire.chancelpixeldungeon.ui.BossHealthBar;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class King extends Mob
{

	private static final int MAX_ARMY_SIZE = 5;

	{
		spriteClass = KingSprite.class;

		setHT(400, true);
		EXP = 40;

		TIME_TO_REST = 2;

		Undead.count = 0;

		properties.add(Property.BOSS);
		properties.add(Property.UNDEAD);
	}

	private boolean nextPedestal = true;

	private static final String PEDESTAL = "pedestal";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(PEDESTAL, nextPedestal);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		nextPedestal = bundle.getBoolean(PEDESTAL);
		BossHealthBar.assignBoss(this);
	}

	@Override
	public int damageRoll()
	{
		return 40;
	}

	@Override
	public int attackSkill(Char target)
	{
		return 32;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, 14);
	}

	@Override
	public int defenseSkill()
	{
		return 25;
	}

	@Override
	protected boolean getCloser(int target)
	{
		if (canTryToSummon() && super.getCloser(((CityBossLevel) Dungeon.level).pedestal(nextPedestal)))
			return true;

		return super.getCloser(target);
	}

	@Override
	protected boolean canAttack(Char enemy)
	{
		return canTryToSummon() ?
				pos == ((CityBossLevel) Dungeon.level).pedestal(nextPedestal) :
				Dungeon.level.adjacent(pos, enemy.pos);
	}

	private boolean canTryToSummon()
	{
		if(Undead.count < maxArmySize())
		{
			Char ch = Actor.findChar(((CityBossLevel) Dungeon.level).pedestal(nextPedestal));
			return ch == this || ch == null;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean attack(Char enemy)
	{
		if(canTryToSummon() && pos == ((CityBossLevel) Dungeon.level).pedestal(nextPedestal))
		{
			summon();
			return true;
		}
		else
		{
			if(Actor.findChar(((CityBossLevel) Dungeon.level).pedestal(nextPedestal)) == enemy)
			{
				nextPedestal = !nextPedestal;
			}
			return super.attack(enemy);
		}
	}

	@Override
	public void die(Object cause)
	{

		GameScene.bossSlain();
		Dungeon.level.drop(new SkeletonKey(Dungeon.depth), pos).sprite.drop();

		super.die(cause);

		Badges.validateBossSlain();

		LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
		if(beacon != null)
		{
			beacon.upgrade();
		}

		yell(Messages.get(this, "defeated", Dungeon.hero.givenName()));
	}

	@Override
	public void aggro(Char ch)
	{
		super.aggro(ch);
		for(Mob mob : Dungeon.level.mobs)
		{
			if(mob instanceof Undead)
			{
				mob.aggro(ch);
			}
		}
	}

	private int maxArmySize()
	{
		return 1 + MAX_ARMY_SIZE * (HT() - HP()) / HT();
	}

	private void summon()
	{
		nextPedestal = !nextPedestal;

		sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
		Dungeon.playAt(Assets.SND_CHALLENGE, pos);

		boolean[] passable = Dungeon.level.passable.clone();
		for(Char c : Actor.chars())
		{
			passable[c.pos] = false;
		}

		int undeadsToSummon = maxArmySize() - Undead.count;

		PathFinder.buildDistanceMap(pos, passable, undeadsToSummon);
		PathFinder.distance[pos] = Integer.MAX_VALUE;
		int dist = 1;

		undeadLabel:
		for(int i = 0; i < undeadsToSummon; i++)
		{
			do
			{
				for(int j = 0; j < Dungeon.level.length(); j++)
				{
					if(PathFinder.distance[j] == dist)
					{

						Undead undead = new Undead();
						undead.pos = j;
						GameScene.add(undead);

						ScrollOfTeleportation.appear(undead, j);
						new Flare(3, 32).color(0x000000, false).show(undead.sprite, 2f);

						PathFinder.distance[j] = Integer.MAX_VALUE;

						continue undeadLabel;
					}
				}
				dist++;
			}
			while(dist < undeadsToSummon);
		}

		yell(Messages.get(this, "arise"));
	}

	@Override
	public void notice()
	{
		super.notice();
		BossHealthBar.assignBoss(this);
		yell(Messages.get(this, "notice"));
	}

	{
		resistances.add(WandOfDisintegration.class);
	}

	{
		immunities.add(Paralysis.class);
		immunities.add(Vertigo.class);
		immunities.add(Blindness.class);
	}

	public static class Undead extends Mob
	{
		public static int count = 0;

		{
			spriteClass = UndeadSprite.class;

			setHT(45, true);

			EXP = 0;

			setAttacksBeforeRest(2);

			state = WANDERING;

			properties.add(Property.UNDEAD);
			properties.add(Property.INORGANIC);
		}

		@Override
		protected void onAdd()
		{
			count++;
			super.onAdd();
		}

		@Override
		protected void onRemove()
		{
			count--;
			super.onRemove();
		}

		@Override
		public int damageRoll()
		{
			return 15 + 10 * restTimeNeeded(false);
		}

		@Override
		public int attackSkill(Char target)
		{
			return 16;
		}

		@Override
		public int attackProc(Char enemy, int damage)
		{
			damage = super.attackProc(enemy, damage);
			if(Random.Int(MAX_ARMY_SIZE) == 0)
			{
				Buff.prolong(enemy, Paralysis.class, 1);
			}

			return damage;
		}

		@Override
		public void damage(int dmg, Object src)
		{
			super.damage(dmg, src);
			if(src instanceof ToxicGas)
			{
				((ToxicGas) src).clear(pos);
			}
		}

		@Override
		public void die(Object cause)
		{
			super.die(cause);

			Dungeon.playAt(Assets.SND_BONES, pos);
		}

		@Override
		public int drRoll()
		{
			return Random.NormalIntRange(0, 5);
		}

		@Override
		public int defenseSkill()
		{
			return 15;
		}

		{
			immunities.add(Grim.class);
			immunities.add(Paralysis.class);
		}
	}
}
