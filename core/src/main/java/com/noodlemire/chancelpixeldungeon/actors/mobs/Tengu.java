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
import com.noodlemire.chancelpixeldungeon.actors.buffs.Poison;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.hero.HeroSubClass;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.TomeOfMastery;
import com.noodlemire.chancelpixeldungeon.items.artifacts.LloydsBeacon;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.PrisonBossLevel;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.levels.traps.GrippingTrap;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.TenguSprite;
import com.noodlemire.chancelpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Tengu extends Mob
{
	{
		spriteClass = TenguSprite.class;

		setHT(200, true);
		EXP = 20;

		setAttacksBeforeRest(2);

		HUNTING = new Hunting();

		flying = true; //doesn't literally fly, but he is fleet-of-foot enough to avoid hazards

		properties.add(Property.BOSS);
	}

	@Override
	protected void onAdd()
	{
		//when he's removed and re-added to the fight, his time is always set to now.
		spend(-cooldown());
		super.onAdd();
	}

	@Override
	public int damageRoll()
	{
		return 20 + 5 * restTimeNeeded(false);
	}

	@Override
	public int attackSkill(Char target)
	{
		return 20;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, 5);
	}

	@Override
	public int defenseSkill()
	{
		return 20;
	}

	@Override
	public void damage(int dmg, Object src)
	{
		int beforeHitHP = HP();
		super.damage(dmg, src);
		dmg = beforeHitHP - HP();

		//phase 2 of the fight is over
		if(HP() == 0 && beforeHitHP <= HT() / 2)
		{
			((PrisonBossLevel) Dungeon.level).progress();
			return;
		}

		int hpBracket = beforeHitHP > HT() / 2 ? 12 : 20;

		//phase 1 of the fight is over
		if(beforeHitHP > HT() / 2 && HP() <= HT() / 2)
		{
			heal((HT() / 2) - HP(), this);
			yell(Messages.get(this, "interesting"));
			((PrisonBossLevel) Dungeon.level).progress();
			BossHealthBar.bleed(true);

			//if tengu has lost a certain amount of hp, jump
		}
		else if(beforeHitHP / hpBracket != HP() / hpBracket)
		{
			jump();
		}
	}

	@Override
	public boolean isAlive()
	{
		return Dungeon.level.mobs.contains(this); //Tengu has special death rules, see prisonbosslevel.progress()
	}

	@Override
	public void die(Object cause)
	{
		if(Dungeon.hero.subClass == HeroSubClass.NONE)
			Dungeon.level.drop(new TomeOfMastery(), pos).sprite.drop();

		GameScene.bossSlain();
		super.die(cause);

		Badges.validateBossSlain();

		LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
		if(beacon != null)
		{
			beacon.upgrade();
		}

		yell(Messages.get(this, "defeated"));
	}

	@Override
	protected boolean canAttack(Char enemy)
	{
		return !restNeeded() && new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos;
	}

	//tengu's attack is always visible
	@Override
	protected boolean doAttack(Char enemy)
	{
		if(enemy == Dungeon.hero)
			((Hero) enemy).resting = false;
		sprite.attack(enemy.pos);
		spend(attackDelay());
		return true;
	}

	private void jump()
	{
		Level level = Dungeon.level;

		//incase tengu hasn't had a chance to act yet
		if(fieldOfView == null || fieldOfView.length != Dungeon.level.length())
		{
			fieldOfView = new boolean[Dungeon.level.length()];
			Dungeon.level.updateFieldOfView(this, fieldOfView);
		}

		if(enemy == null) enemy = chooseEnemy();
		if(enemy == null) return;

		int newPos;
		//if we're in phase 1, want to warp around within the room
		if(HP() > HT() / 2)
		{
			//place new traps
			for(int i = 0; i < 4; i++)
			{
				int trapPos;
				do
				{
					trapPos = Random.Int(level.length());
				}
				while(level.map[trapPos] != Terrain.INACTIVE_TRAP
				      && level.map[trapPos] != Terrain.TRAP);

				if(level.map[trapPos] == Terrain.INACTIVE_TRAP)
				{
					level.setTrap(new GrippingTrap().reveal(), trapPos);
					Level.set(trapPos, Terrain.TRAP);
					ScrollOfIdentify.discover(trapPos);
				}
			}

			int tries = 50;
			do
			{
				newPos = Random.IntRange(3, 7) + 32 * Random.IntRange(26, 30);
			}
			while((level.adjacent(newPos, enemy.pos) || Actor.findChar(newPos) != null)
			      && --tries > 0);
			if(tries <= 0) return;

			//otherwise go wherever, as long as it's a little bit away
		}
		else
		{
			do
			{
				newPos = Random.Int(level.length());
			}
			while(
					level.solid[newPos] ||
					level.distance(newPos, enemy.pos) < 8 ||
					Actor.findChar(newPos) != null);
		}

		if(level.heroFOV[pos]) CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);

		sprite.move(pos, newPos);
		move(newPos);

		if(level.heroFOV[newPos]) CellEmitter.get(newPos).burst(Speck.factory(Speck.WOOL), 6);
		Sample.INSTANCE.play(Assets.SND_PUFF);

		spend(1 / speed());
	}

	@Override
	public void notice()
	{
		super.notice();
		BossHealthBar.assignBoss(this);
		if(HP() <= HT() / 2) BossHealthBar.bleed(true);
		if(HP() == HT())
			yell(Messages.get(this, "notice_mine", Dungeon.hero.givenName()));
		else
			yell(Messages.get(this, "notice_face", Dungeon.hero.givenName()));
	}

	{
		resistances.add(ToxicGas.class);
		resistances.add(Poison.class);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
		if(HP() <= HT() / 2) BossHealthBar.bleed(true);
	}

	//tengu is always hunting
	private class Hunting extends Mob.Hunting
	{
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted)
		{
			if(restNeeded())
			{
				rest();
				return true;
			}

			lookForEnemy(enemyInFOV);
			if(enemyInFOV && !isCharmedBy(enemy) && canAttack(enemy))
			{
				return doAttack(enemy);
			}
			else
			{
				if(enemyInFOV)
				{
					target = enemy.pos;
				}
				else
				{
					chooseEnemy();
					if(enemy != null)
					{
						target = enemy.pos;
					}
				}

				spend(TICK);
				return true;
			}
		}
	}
}
