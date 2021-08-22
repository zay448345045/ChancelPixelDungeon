package com.noodlemire.chancelpixeldungeon.actors.mobs;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Charm;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Drowsy;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Linkage;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MagicalSleep;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Terror;
import com.noodlemire.chancelpixeldungeon.actors.mobs.npcs.NPC;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.levels.features.Chasm;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.SkelecrabatSprite;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Skelecrabat extends Mob
{
	{
		spriteClass = SkelecrabatSprite.class;

		EXP = 18;

		setHT(40, true);
		baseSpeed = 2f;

		flying = true;

		properties.add(Property.MINIBOSS);
		properties.add(Property.UNDEAD);
		properties.add(Property.DEMONIC);

		immunities.add(Terror.class);
		immunities.add(Drowsy.class);
		immunities.add(MagicalSleep.class);
		immunities.add(Charm.class);
	}

	@Override
	public int damageRoll()
	{
		return Random.NormalIntRange(EXP / 2, EXP * 2 + 2);
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(EXP / 5, EXP);
	}

	@Override
	public int attackSkill(Char target)
	{
		return EXP * 3;
	}

	@Override
	public int defenseSkill()
	{
		return EXP * 2 - 2;
	}

	@Override
	public int attackProc(Char enemy, int damage)
	{
		damage = super.attackProc(enemy, damage);
		int reg = damage;

		if(reg > 0 && !enemy.properties().contains(Property.INORGANIC))
		{
			heal(reg, this);
			sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
		}

		return damage;
	}

	@Override
	protected Char chooseEnemy()
	{
		//Attack unpredictably. Don't stick to a single target. Only prefer closer targets.

		HashSet<Char> enemies = new HashSet<>();

		int min_dist = Integer.MAX_VALUE;
		if(enemy != null && enemy.isAlive() && fieldOfView[enemy.pos])
			min_dist = Dungeon.level.distance(pos, enemy.pos);

		for(Mob mob : Dungeon.level.mobs)
		{
			if(mob != this && mob.isAlive() && fieldOfView[mob.pos] && mob.invisible <= 0 && !(mob instanceof NPC))
			{
				int dist = Dungeon.level.distance(pos, mob.pos);

				if(dist < min_dist)
				{
					enemies.clear();
					min_dist = dist;
				}

				if(dist == min_dist)
					enemies.add(mob);
			}
		}

		if(Dungeon.hero.isAlive() && Dungeon.hero.invisible <= 0 && fieldOfView[Dungeon.hero.pos])
		{
			int dist = Dungeon.level.distance(pos, Dungeon.hero.pos);

			if(dist < min_dist)
			{
				enemies.clear();
				min_dist = dist;
			}

			if(dist == min_dist)
				enemies.add(Dungeon.hero);
		}

		if(enemies.isEmpty())
			return null;
		else
			return Random.element(enemies);
	}

	@Override
	public void die(Object cause)
	{
		if(cause == Chasm.class || cause instanceof Linkage)
		{
			super.die(cause);
			return;
		}

		boolean heroKilled = false;
		for(int i = 0; i < PathFinder.NEIGHBOURS8.length; i++)
		{
			Char ch = findChar(pos + PathFinder.NEIGHBOURS8[i]);
			if(ch != null && ch.isAlive())
			{
				int damage = Random.NormalIntRange(EXP / 2, EXP);
				damage = Math.max(0, damage - (ch.drRoll() + ch.drRoll()));
				damage = attackProc(ch, damage);
				ch.damage(damage, this);
				if(ch == Dungeon.hero && !ch.isAlive())
				{
					heroKilled = true;
				}
			}
		}

		Dungeon.playAt(Assets.SND_BONES, pos);

		if(heroKilled)
		{
			Dungeon.fail(getClass());
			GLog.n(Messages.get(Skeleton.class, "explo_kill"));
		}

		if(HP() <= 0)
			super.die(cause);
	}
}
