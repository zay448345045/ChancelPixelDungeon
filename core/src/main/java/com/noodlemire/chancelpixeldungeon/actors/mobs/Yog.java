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
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Fire;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ThunderCloud;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ToxicGas;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Amok;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Burning;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Charm;
import com.noodlemire.chancelpixeldungeon.actors.buffs.LockedFloor;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Ooze;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Paralysis;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Poison;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Sleep;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Terror;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Vertigo;
import com.noodlemire.chancelpixeldungeon.effects.Pushing;
import com.noodlemire.chancelpixeldungeon.effects.particles.ShadowParticle;
import com.noodlemire.chancelpixeldungeon.items.keys.SkeletonKey;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfDecay;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfSupernova;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Grim;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.BurningFistSprite;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.sprites.LarvaSprite;
import com.noodlemire.chancelpixeldungeon.sprites.RottingFistSprite;
import com.noodlemire.chancelpixeldungeon.sprites.YogSprite;
import com.noodlemire.chancelpixeldungeon.ui.BossHealthBar;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class Yog extends Mob
{
	{
		spriteClass = YogSprite.class;

		setHT(500, true);

		EXP = 50;

		state = PASSIVE;

		properties.add(Property.BOSS);
		properties.add(Property.IMMOVABLE);
		properties.add(Property.DEMONIC);
	}

	public Yog()
	{
		super();
	}

	public void spawnFists()
	{
		RottingFist fist1 = new RottingFist();
		BurningFist fist2 = new BurningFist();

		do
		{
			fist1.pos = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
			fist2.pos = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
		}
		while(!Dungeon.level.passable[fist1.pos] || !Dungeon.level.passable[fist2.pos] || fist1.pos == fist2.pos);

		GameScene.add(fist1);
		GameScene.add(fist2);

		notice();
	}

	@Override
	protected boolean act()
	{
		heal(1);

		return super.act();
	}

	@Override
	public void damage(int dmg, Object src)
	{

		HashSet<Mob> fists = new HashSet<>();

		for(Mob mob : Dungeon.level.mobs)
			if(mob instanceof RottingFist || mob instanceof BurningFist)
				fists.add(mob);

		dmg >>= fists.size();

		super.damage(dmg, src);

		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if(lock != null) lock.addTime(dmg * 0.5f);

	}

	@Override
	public int defenseProc(Char enemy, int damage)
	{

		ArrayList<Integer> spawnPoints = new ArrayList<>();

		for(int i = 0; i < PathFinder.NEIGHBOURS8.length; i++)
		{
			int p = pos + PathFinder.NEIGHBOURS8[i];
			if(Actor.findChar(p) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p]))
			{
				spawnPoints.add(p);
			}
		}

		if(spawnPoints.size() > 0)
		{
			Larva larva = new Larva();
			larva.pos = Random.element(spawnPoints);

			GameScene.add(larva);
			Actor.addDelayed(new Pushing(larva, pos, larva.pos), -1);
		}

		for(Mob mob : Dungeon.level.mobs)
		{
			if(mob instanceof BurningFist || mob instanceof RottingFist || mob instanceof Larva)
			{
				mob.aggro(enemy);
			}
		}

		return super.defenseProc(enemy, damage);
	}

	@Override
	public void beckon(int cell)
	{
	}

	@SuppressWarnings("unchecked")
	@Override
	public void die(Object cause)
	{
		for(Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone())
			if(mob instanceof BurningFist || mob instanceof RottingFist)
				mob.die(cause);

		GameScene.bossSlain();
		Dungeon.level.drop(new SkeletonKey(Dungeon.depth), pos).sprite.drop();
		super.die(cause);

		yell(Messages.get(this, "defeated"));
	}

	@Override
	public void notice()
	{
		super.notice();
		BossHealthBar.assignBoss(this);
		yell(Messages.get(this, "notice"));
	}

	{
		immunities.add(Grim.class);
		immunities.add(Terror.class);
		immunities.add(Amok.class);
		immunities.add(Charm.class);
		immunities.add(Sleep.class);
		immunities.add(Burning.class);
		immunities.add(ToxicGas.class);
		immunities.add(ScrollOfSupernova.class);
		immunities.add(Vertigo.class);
		immunities.add(ScrollOfDecay.class);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
	}

	public static class RottingFist extends Mob
	{
		private static final int REGENERATION = 4;

		{
			spriteClass = RottingFistSprite.class;

			setHT(400, true);
			defenseSkill = 25;

			EXP = 0;

			state = WANDERING;

			properties.add(Property.BOSS);
			properties.add(Property.DEMONIC);
			properties.add(Property.ACIDIC);
		}

		@Override
		public int attackSkill(Char target)
		{
			return 36;
		}

		@Override
		public int damageRoll()
		{
			return Random.NormalIntRange(20, 50);
		}

		@Override
		public int drRoll()
		{
			return Random.NormalIntRange(0, 15);
		}

		@Override
		public int attackProc(Char enemy, int damage)
		{
			damage = super.attackProc(enemy, damage);

			if(Random.Int(3) == 0)
			{
				Buff.affect(enemy, Ooze.class);
				enemy.sprite.burst(0xFF000000, 5);
			}

			return damage;
		}

		@Override
		public boolean act()
		{
			if(Dungeon.level.water[pos] && HP() < HT())
			{
				sprite.emitter().burst(ShadowParticle.UP, 2);
				heal(REGENERATION);
			}

			return super.act();
		}

		@Override
		public void damage(int dmg, Object src)
		{
			super.damage(dmg, src);
			LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
			if(lock != null) lock.addTime(dmg * 0.5f);
		}

		{
			immunities.add(Paralysis.class);
			immunities.add(Amok.class);
			immunities.add(Sleep.class);
			immunities.add(Terror.class);
			immunities.add(Poison.class);
			immunities.add(Vertigo.class);
		}
	}

	public static class BurningFist extends Mob
	{
		{
			spriteClass = BurningFistSprite.class;

			setHT(300, true);
			defenseSkill = 25;

			EXP = 0;

			state = WANDERING;

			properties.add(Property.BOSS);
			properties.add(Property.DEMONIC);
			properties.add(Property.FIERY);
		}

		@Override
		public int attackSkill(Char target)
		{
			return 36;
		}

		@Override
		public int damageRoll()
		{
			return Random.NormalIntRange(26, 32);
		}

		@Override
		public int drRoll()
		{
			return Random.NormalIntRange(0, 15);
		}

		@Override
		protected boolean canAttack(Char enemy)
		{
			return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
		}

		@Override
		public boolean attack(Char enemy)
		{

			if(!Dungeon.level.adjacent(pos, enemy.pos))
			{
				spend(attackDelay());

				if(hit(this, enemy, true))
				{

					int dmg = damageRoll();
					enemy.damage(dmg, this);

					enemy.sprite.bloodBurstA(sprite.center(), dmg);
					enemy.sprite.flash();

					if(!enemy.isAlive() && enemy == Dungeon.hero)
					{
						Dungeon.fail(getClass());
						GLog.n(Messages.get(Char.class, "kill", name));
					}
					return true;
				}
				else
				{
					enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
					return false;
				}
			}
			else
				return super.attack(enemy);
		}

		@Override
		public boolean act()
		{
			for(int i = 0; i < PathFinder.NEIGHBOURS9.length; i++)
				GameScene.add(Blob.seed(pos + PathFinder.NEIGHBOURS9[i], 2, Fire.class));

			return super.act();
		}

		@Override
		public void damage(int dmg, Object src)
		{
			super.damage(dmg, src);
			LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
			if(lock != null) lock.addTime(dmg * 0.5f);
		}

		{
			resistances.add(ToxicGas.class);
		}

		{
			immunities.add(Amok.class);
			immunities.add(Sleep.class);
			immunities.add(Terror.class);
			immunities.add(Vertigo.class);
		}
	}

	public static class Larva extends Mob
	{
		{
			spriteClass = LarvaSprite.class;

			setHT(50, true);
			defenseSkill = 20;

			EXP = 0;

			state = HUNTING;

			properties.add(Property.DEMONIC);
		}

		@Override
		public int attackSkill(Char target)
		{
			return 30;
		}

		@Override
		public int damageRoll()
		{
			return Random.NormalIntRange(22, 30);
		}

		@Override
		public int drRoll()
		{
			return Random.NormalIntRange(0, 8);
		}

	}
}
