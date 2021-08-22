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

package com.noodlemire.chancelpixeldungeon.items.wands;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.Statistics;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Amok;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Bleeding;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Blindness;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Burning;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Charm;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Chill;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Corrosion;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Corruption;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Cripple;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Doom;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Drowsy;
import com.noodlemire.chancelpixeldungeon.actors.buffs.FlavourBuff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Following;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Frost;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MagicalSleep;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Ooze;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Paralysis;
import com.noodlemire.chancelpixeldungeon.actors.buffs.PinCushion;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Poison;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Roots;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Slow;
import com.noodlemire.chancelpixeldungeon.actors.buffs.SoulMark;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Terror;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Vertigo;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Weakness;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Bee;
import com.noodlemire.chancelpixeldungeon.actors.mobs.King;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mimic;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Piranha;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Statue;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Wraith;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Yog;
import com.noodlemire.chancelpixeldungeon.effects.MagicMissile;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.MagesStaff;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashMap;

//TODO final balancing decisions here
public class WandOfCorruption extends Wand
{
	{
		image = ItemSpriteSheet.WAND_CORRUPTION;
	}

	//Note that some debuffs here have a 0% chance to be applied.
	// This is because the wand of corruption considers them to be a certain level of harmful
	// for the purposes of reducing resistance, but does not actually apply them itself

	private static final float MINOR_DEBUFF_WEAKEN = 4 / 5f;
	private static final HashMap<Class<? extends Buff>, Float> MINOR_DEBUFFS = new HashMap<>();

	static
	{
		MINOR_DEBUFFS.put(Weakness.class, 2f);
		MINOR_DEBUFFS.put(Cripple.class, 1f);
		MINOR_DEBUFFS.put(Blindness.class, 1f);
		MINOR_DEBUFFS.put(Terror.class, 1f);

		MINOR_DEBUFFS.put(Chill.class, 0f);
		MINOR_DEBUFFS.put(Ooze.class, 0f);
		MINOR_DEBUFFS.put(Roots.class, 0f);
		MINOR_DEBUFFS.put(Vertigo.class, 0f);
		MINOR_DEBUFFS.put(Drowsy.class, 0f);
		MINOR_DEBUFFS.put(Bleeding.class, 0f);
		MINOR_DEBUFFS.put(Burning.class, 0f);
		MINOR_DEBUFFS.put(Poison.class, 0f);
	}

	private static final float MAJOR_DEBUFF_WEAKEN = 2 / 3f;
	private static final HashMap<Class<? extends Buff>, Float> MAJOR_DEBUFFS = new HashMap<>();

	static
	{
		MAJOR_DEBUFFS.put(Amok.class, 3f);
		MAJOR_DEBUFFS.put(Slow.class, 2f);
		MAJOR_DEBUFFS.put(Paralysis.class, 1f);

		MAJOR_DEBUFFS.put(Charm.class, 0f);
		MAJOR_DEBUFFS.put(MagicalSleep.class, 0f);
		MAJOR_DEBUFFS.put(SoulMark.class, 0f);
		MAJOR_DEBUFFS.put(Corrosion.class, 0f);
		MAJOR_DEBUFFS.put(Frost.class, 0f);
		MAJOR_DEBUFFS.put(Doom.class, 0f);
	}

	@Override
	protected void onZap(Ballistica bolt)
	{
		Char ch = Actor.findChar(bolt.collisionPos);

		if(ch != null)
		{
			if(!(ch instanceof Mob))
				return;

			Mob enemy = (Mob) ch;

			if(enemy.buff(Corruption.class) != null && enemy.buff(Following.class) == null)
			{
				for(Mob m : Dungeon.level.mobs)
					if(Dungeon.level.heroFOV[m.pos] && m.buff(Corruption.class) != null)
						Buff.affect(m, Following.class);

				return;
			}

			float corruptingPower = 2 + level();

			//base enemy resistance is usually based on their exp, but in special cases it is based on other criteria
			float enemyResist = 1 + enemy.EXP * 0.625f;
			if(ch instanceof Mimic || ch instanceof Statue)
			{
				enemyResist = 1 + Dungeon.depth;
			}
			else if(ch instanceof Piranha || ch instanceof Bee)
			{
				enemyResist = 1 + Dungeon.depth / 2f;
			}
			else if(ch instanceof Wraith)
			{
				//this is so low because wraiths are always at max hp
				enemyResist = 0.5f + Dungeon.depth / 8f;
			}
			else if(ch instanceof Yog.BurningFist || ch instanceof Yog.RottingFist)
			{
				enemyResist = 1 + 30;
			}
			else if(ch instanceof Yog.Larva || ch instanceof King.Undead)
			{
				enemyResist = 1 + 5;
			}

			//100% health: 3x resist   75%: 2.1x resist   50%: 1.5x resist   25%: 1.1x resist
			enemyResist *= 1 + 2 * Math.pow(enemy.HP() / (float) enemy.HT(), 2);

			//debuffs placed on the enemy reduce their resistance
			for(Buff buff : enemy.buffs())
			{
				if(MAJOR_DEBUFFS.containsKey(buff.getClass())) enemyResist *= MAJOR_DEBUFF_WEAKEN;
				else if(MINOR_DEBUFFS.containsKey(buff.getClass()))
					enemyResist *= MINOR_DEBUFF_WEAKEN;
				else if(buff.type == Buff.buffType.NEGATIVE) enemyResist *= MINOR_DEBUFF_WEAKEN;
			}

			//cannot re-corrupt or doom an enemy, so give them a major debuff instead
			if(enemy.buff(Corruption.class) != null || enemy.buff(Doom.class) != null)
				enemyResist = corruptingPower * .99f;

			if(corruptingPower > enemyResist)
				corruptEnemy(enemy);
			else
			{
				if(enemy.buff(Corruption.class) == null)
				{
					for(Mob m : Dungeon.level.mobs)
					{
						if(m.buff(Corruption.class) != null && m.buff(Following.class) != null)
						{
							Buff.detach(m, Following.class);
							m.aggro(enemy);
						}
					}
				}

				float debuffChance = corruptingPower / enemyResist;
				if(Random.Float() < debuffChance)
					debuffEnemy(enemy, MAJOR_DEBUFFS);
				else
					debuffEnemy(enemy, MINOR_DEBUFFS);
			}

			processSoulMark(ch, chargesPerCast());
		}
		else
			Dungeon.level.press(bolt.collisionPos, null, true);
	}

	private void debuffEnemy(Mob enemy, HashMap<Class<? extends Buff>, Float> category)
	{
		//do not consider buffs which are already assigned, or that the enemy is immune to.
		HashMap<Class<? extends Buff>, Float> debuffs = new HashMap<>(category);
		for(Buff existing : enemy.buffs())
		{
			if(debuffs.containsKey(existing.getClass()))
			{
				debuffs.put(existing.getClass(), 0f);
			}
		}
		for(Class<? extends Buff> toAssign : debuffs.keySet())
		{
			if(debuffs.get(toAssign) > 0 && enemy.isImmune(toAssign))
			{
				debuffs.put(toAssign, 0f);
			}
		}

		//all buffs with a > 0 chance are flavor buffs
		Class<? extends FlavourBuff> debuffCls = (Class<? extends FlavourBuff>) Random.chances(debuffs);

		if(debuffCls != null)
		{
			Buff.append(enemy, debuffCls, 6 + level() * 3);
		}
		else
		{
			//if no debuff can be applied (all are present), then go up one tier
			if(category == MINOR_DEBUFFS) debuffEnemy(enemy, MAJOR_DEBUFFS);
			else if(category == MAJOR_DEBUFFS) corruptEnemy(enemy);
		}
	}

	private static void corruptEnemy(Mob enemy)
	{
		//cannot re-corrupt or doom an enemy, so give them a major debuff instead
		if(enemy.buff(Corruption.class) != null || enemy.buff(Doom.class) != null)
		{
			GLog.w(Messages.get(WandOfCorruption.class, "already_corrupted"));
			return;
		}

		if(!enemy.isImmune(Corruption.class))
		{
			enemy.heal(enemy.HT(), curItem);
			for(Buff buff : enemy.buffs())
			{
				if(buff.type == Buff.buffType.NEGATIVE
						&& !(buff instanceof SoulMark))
				{
					buff.detach();
				}
				else if(buff instanceof PinCushion)
				{
					buff.detach();
				}
			}
			Buff.affect(enemy, Corruption.class);

			Statistics.enemiesSlain++;
			Badges.validateMonstersSlain();
			Statistics.qualifiedForNoKilling = false;
			if(enemy.EXP > 0)
			{
				curUser.earnExp(enemy.EXP);
			}
			enemy.rollToDropLoot();
		}
		else
			Buff.affect(enemy, Doom.class);
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage)
	{
		if(defender instanceof Mob)
		{
			for(Mob m : Dungeon.level.mobs)
			{
				if(m.buff(Following.class) != null)
				{
					Buff.detach(m, Following.class);
					m.aggro(defender);
				}
			}
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback)
	{
		MagicMissile.boltFromChar(curUser.sprite.parent,
				MagicMissile.SHADOW,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle)
	{
		particle.color(0);
		particle.am = 0.6f;
		particle.setLifespan(2f);
		particle.speed.set(0, 5);
		particle.setSize(0.5f, 2f);
		particle.shuffleXY(1f);
	}
}
