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
import com.noodlemire.chancelpixeldungeon.actors.buffs.MagicImmunity;
import com.noodlemire.chancelpixeldungeon.effects.particles.SparkParticle;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ShamanSprite;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Shaman extends Mob implements Callback
{
	private static final float TIME_TO_ZAP = 1f;

	{
		spriteClass = ShamanSprite.class;

		EXP = Random.IntRange(7, 10);

		setHT(15 + EXP, true);

		loot = Generator.Category.SCROLL;
		lootChance = 0.33f;

		properties.add(Property.ELECTRIC);
	}

	@Override
	public int damageRoll()
	{
		return EXP + 1;
	}

	@Override
	public int attackSkill(Char target)
	{
		return EXP + 4;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, EXP - 3);
	}

	@Override
	public int defenseSkill()
	{
		return EXP + 1;
	}

	@Override
	protected boolean canAttack(Char enemy)
	{
		if(buff(MagicImmunity.class) != null)
			return super.canAttack(enemy);

		return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
	protected boolean doAttack(Char enemy)
	{
		if(Dungeon.level.distance(pos, enemy.pos) <= 1)
		{
			return super.doAttack(enemy);
		}
		else
		{
			boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
			if(visible)
				sprite.zap(enemy.pos);

			spend(TIME_TO_ZAP);

			if(hit(this, enemy, true))
			{
				int dmg = EXP * 2;
				if(Dungeon.level.water[enemy.pos] && !enemy.flying)
				{
					dmg *= 1.5f;
				}
				enemy.damage(dmg, this);

				enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
				enemy.sprite.flash();

				needRest(3);

				if(enemy == Dungeon.hero)
				{
					Camera.main.shake(2, 0.3f);

					if(!enemy.isAlive())
					{
						Dungeon.fail(getClass());
						GLog.n(Messages.get(this, "zap_kill"));
					}
				}
			}
			else
			{
				enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
			}

			return !visible;
		}
	}

	@Override
	public void call()
	{
		next();
	}
}
