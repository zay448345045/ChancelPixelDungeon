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
import com.noodlemire.chancelpixeldungeon.actors.buffs.Light;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.particles.PurpleParticle;
import com.noodlemire.chancelpixeldungeon.items.Dewdrop;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfDisintegration;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Grim;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.sprites.EyeSprite;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Eye extends Mob
{
	{
		spriteClass = EyeSprite.class;

		EXP = Random.IntRange(20, 24);

		setHT(EXP * 6, true);
		viewDistance = Light.DISTANCE;

		TIME_TO_REST = 1;

		flying = true;

		HUNTING = new Hunting();

		loot = new Dewdrop();
		lootChance = 0.5f;

		properties.add(Property.DEMONIC);
	}

	@Override
	public int damageRoll()
	{
		return EXP + 10;
	}

	@Override
	public int attackSkill(Char target)
	{
		return EXP * 2 - 10;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, EXP / 2);
	}

	@Override
	public int defenseSkill()
	{
		return EXP;
	}

	private Ballistica beam;
	private int beamTarget = -1;
	private int beamCooldown;
	public boolean beamCharged;

	@Override
	protected boolean canAttack(Char enemy)
	{
		if(beamCooldown == 0)
		{
			Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.STOP_TERRAIN);

			if(enemy.invisible == 0 && !isCharmedBy(enemy) && fieldOfView[enemy.pos] && aim.subPath(1, aim.dist).contains(enemy.pos))
			{
				beam = aim;
				beamTarget = aim.collisionPos;
				return true;
			}
			else
				//if the beam is charged, it has to attack, will aim at previous location of target.
				return beamCharged;
		}
		else
			return super.canAttack(enemy);
	}

	@Override
	protected boolean act()
	{
		if(beamCharged && state != HUNTING)
		{
			beamCharged = false;
		}
		if(beam == null && beamTarget != -1)
		{
			beam = new Ballistica(pos, beamTarget, Ballistica.STOP_TERRAIN);
			sprite.turnTo(pos, beamTarget);
		}
		if(beamCooldown > 0)
			beamCooldown--;
		return super.act();
	}

	@Override
	protected boolean doAttack(Char enemy)
	{
		if(beamCooldown > 0)
		{
			return super.doAttack(enemy);
		}
		else if(!beamCharged)
		{
			((EyeSprite) sprite).charge(enemy.pos);
			spend(attackDelay() * 2f);
			beamCharged = true;
			return true;
		}
		else
		{
			spend(attackDelay());

			needRest(3);

			beam = new Ballistica(pos, beamTarget, Ballistica.STOP_TERRAIN);
			if(Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[beam.collisionPos])
			{
				sprite.zap(beam.collisionPos);
				return false;
			}
			else
			{
				deathGaze();
				return true;
			}
		}
	}

	@Override
	public void damage(int dmg, Object src)
	{
		if(beamCharged) dmg /= 4;
		super.damage(dmg, src);
	}

	public void deathGaze()
	{
		if(!beamCharged || beamCooldown > 0 || beam == null)
			return;

		beamCharged = false;
		beamCooldown = Random.IntRange(3, 6);

		boolean terrainAffected = false;

		for(int pos : beam.subPath(1, beam.dist))
		{
			if(Dungeon.level.flamable[pos])
			{
				Dungeon.level.destroy(pos);
				GameScene.updateMap(pos);
				terrainAffected = true;
			}

			Char ch = Actor.findChar(pos);
			if(ch == null)
				continue;

			if(hit(this, ch, true))
			{
				ch.damage(EXP * 5 - 40, this);

				if(Dungeon.level.heroFOV[pos])
				{
					ch.sprite.flash();
					CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
				}

				if(!ch.isAlive() && ch == Dungeon.hero)
				{
					Dungeon.fail(getClass());
					GLog.n(Messages.get(this, "deathgaze_kill"));
				}
			}
			else
			{
				ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
			}
		}

		if(terrainAffected)
		{
			Dungeon.observe();
		}

		beam = null;
		beamTarget = -1;
	}

	private static final String BEAM_TARGET = "beamTarget";
	private static final String BEAM_COOLDOWN = "beamCooldown";
	private static final String BEAM_CHARGED = "beamCharged";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(BEAM_TARGET, beamTarget);
		bundle.put(BEAM_COOLDOWN, beamCooldown);
		bundle.put(BEAM_CHARGED, beamCharged);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		if(bundle.contains(BEAM_TARGET))
			beamTarget = bundle.getInt(BEAM_TARGET);
		beamCooldown = bundle.getInt(BEAM_COOLDOWN);
		beamCharged = bundle.getBoolean(BEAM_CHARGED);
	}

	{
		resistances.add(WandOfDisintegration.class);
		resistances.add(Grim.class);
	}

	private class Hunting extends Mob.Hunting
	{
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted)
		{
			//even if enemy isn't seen, attack them if the beam is charged
			if(beamCharged && enemy != null && canAttack(enemy))
			{
				lookForEnemy(enemyInFOV);
				return doAttack(enemy);
			}
			return super.act(enemyInFOV, justAlerted);
		}
	}
}
