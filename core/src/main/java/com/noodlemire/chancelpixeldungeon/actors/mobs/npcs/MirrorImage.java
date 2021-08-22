/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.noodlemire.chancelpixeldungeon.actors.mobs.npcs;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.CorrosiveGas;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ToxicGas;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Burning;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Corruption;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MirrorGuard;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.sprites.MirrorSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class MirrorImage extends NPC
{
	private boolean individual;

	{
		spriteClass = MirrorSprite.class;

		setHT(8, true);

		alignment = Alignment.ALLY;
		state = HUNTING;

		WANDERING = new Wandering();

		TIME_TO_REST = 2;

		//before other mobs
		actPriority = MOB_PRIO + 1;
	}

	private Hero hero;
	private int heroID;
	public int armTier;

	@Override
	protected boolean act()
	{
		if(hero == null)
		{
			hero = (Hero) Actor.findById(heroID);
			if(hero == null)
			{
				destroy();
				sprite.die();
				return true;
			}
		}

		if(hero.tier() != armTier)
		{
			armTier = hero.tier();
			((MirrorSprite) sprite).updateArmor(armTier);
		}

		return super.act();
	}

	private static final String HEROID = "hero_id";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(HEROID, heroID);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		heroID = bundle.getInt(HEROID);
	}

	public void duplicate(Hero hero, int HP)
	{
		this.hero = hero;
		heroID = this.hero.id();
		setHT(MirrorGuard.maxHP(hero), false);
		setHP(HP);
		EXP = hero.lvl;
	}

	public void duplicateIndividual(Hero hero)
	{
		duplicate(hero, hero.HT());
		individual = true;
	}

	public boolean isIndividual()
	{
		return individual;
	}

	@Override
	public int damageRoll()
	{
		return 2 + 2 * hero.lvl;
	}

	@Override
	public int attackSkill(Char target)
	{
		return hero.attackSkill(target);
	}

	@Override
	public int defenseSkill(Char enemy)
	{
		if(hero != null)
		{
			int baseEvasion = 4 + hero.lvl;
			int heroEvasion = hero.defenseSkill(enemy);

			//if the hero has more/less evasion, 50% of it is applied
			return super.defenseSkill(enemy) * (baseEvasion + heroEvasion) / 2;
		}
		else
			return 0;
	}

	@Override
	public int defenseSkill()
	{
		return 1;
	}

	@Override
	public int drRoll()
	{
		if(hero != null)
			return hero.drRoll();
		else
			return 0;
	}

	@Override
	public int defenseProc(Char enemy, int damage)
	{
		damage = super.defenseProc(enemy, damage);
		if(hero.belongings.armor != null)
			return hero.belongings.armor.proc(enemy, this, damage);
		else
			return damage;
	}

	@Override
	public float speed()
	{
		if(hero.belongings.armor != null)
			return hero.belongings.armor.speedFactor(this, super.speed());

		return super.speed();
	}

	@Override
	public int attackProc(Char enemy, int damage)
	{
		if(enemy instanceof Mob)
			((Mob) enemy).aggro(this);

		return super.attackProc(enemy, damage);
	}

	@Override
	public CharSprite sprite()
	{
		CharSprite s = super.sprite();

		hero = (Hero) Actor.findById(heroID);
		if(hero != null)
			armTier = hero.tier();

		((MirrorSprite)s).updateArmor(armTier);
		return s;
	}

	@Override
	public boolean interact()
	{
		if(!Dungeon.level.passable[pos])
			return true;

		int curPos = pos;

		moveSprite(pos, Dungeon.hero.pos);
		move(Dungeon.hero.pos);

		Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
		Dungeon.hero.move(curPos);

		Dungeon.hero.spend(1 / Dungeon.hero.speed());
		Dungeon.hero.busy();

		return true;
	}

	public void sendToBuff()
	{
		Buff.affect(hero, MirrorGuard.class).set(HP());
		destroy();
		CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
		sprite.die();
		Sample.INSTANCE.play(Assets.SND_TELEPORT);
	}

	{
		immunities.add(ToxicGas.class);
		immunities.add(CorrosiveGas.class);
		immunities.add(Burning.class);
		immunities.add(Corruption.class);
	}

	private class Wandering extends Mob.Wandering
	{
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted)
		{
			if(!individual && !enemyInFOV)
			{
				sendToBuff();
				return true;
			}
			else
				return super.act(enemyInFOV, justAlerted);
		}
	}
}