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
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ThunderCloud;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ToxicGas;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Paralysis;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Terror;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.effects.particles.ElmoParticle;
import com.noodlemire.chancelpixeldungeon.items.ArmorKit;
import com.noodlemire.chancelpixeldungeon.items.artifacts.LloydsBeacon;
import com.noodlemire.chancelpixeldungeon.items.keys.SkeletonKey;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfDecay;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.DM300Sprite;
import com.noodlemire.chancelpixeldungeon.ui.BossHealthBar;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DM300 extends Mob
{
	{
		spriteClass = DM300Sprite.class;

		setHT(300, true);
		EXP = 30;

		loot = new ArmorKit();
		lootChance = 1f;

		properties.add(Property.BOSS);
		properties.add(Property.INORGANIC);
		properties.add(Property.METALLIC);
	}

	@Override
	public int damageRoll()
	{
		return Random.NormalIntRange(20, 25);
	}

	@Override
	public int attackSkill(Char target)
	{
		return 28;
	}

	@Override
	public int drRoll()
	{
		return Random.NormalIntRange(0, 10);
	}

	@Override
	public int defenseSkill()
	{
		return 18;
	}

	@Override
	public boolean act()
	{

		GameScene.add(Blob.seed(pos, 30, ToxicGas.class));

		return super.act();
	}

	@Override
	public void move(int step)
	{
		super.move(step);

		if(Dungeon.level.map[step] == Terrain.INACTIVE_TRAP && HP() < HT())
		{
			heal(Random.Int(1, HT()));
			sprite.emitter().burst(ElmoParticle.FACTORY, 5);

			if(Dungeon.level.heroFOV[step] && Dungeon.hero.isAlive())
			{
				GLog.n(Messages.get(this, "repair"));
			}
		}

		int[] cells = {
				step - 1, step + 1, step - Dungeon.level.width(), step + Dungeon.level.width(),
				step - 1 - Dungeon.level.width(),
				step - 1 + Dungeon.level.width(),
				step + 1 - Dungeon.level.width(),
				step + 1 + Dungeon.level.width()
		};
		int cell = cells[Random.Int(cells.length)];

		if(Dungeon.level.heroFOV[cell])
		{
			CellEmitter.get(cell).start(Speck.factory(Speck.ROCK), 0.07f, 10);
			Camera.main.shake(3, 0.7f);
			Dungeon.playAt(Assets.SND_ROCKS, pos);

			if(Dungeon.level.water[cell])
			{
				GameScene.ripple(cell);
			}
			else if(Dungeon.level.map[cell] == Terrain.EMPTY)
			{
				Level.set(cell, Terrain.EMPTY_DECO);
				GameScene.updateMap(cell);
			}
		}

		Char ch = Actor.findChar(cell);
		if(ch != null && ch != this)
		{
			Buff.prolong(ch, Paralysis.class, 2);
		}
	}

	@Override
	public void die(Object cause)
	{

		super.die(cause);

		GameScene.bossSlain();
		Dungeon.level.drop(new SkeletonKey(Dungeon.depth), pos).sprite.drop();

		Badges.validateBossSlain();

		LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
		if(beacon != null)
		{
			beacon.upgrade();
		}

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
		resistances.add(ThunderCloud.class);

		immunities.add(ToxicGas.class);
		immunities.add(Terror.class);
		immunities.add(ScrollOfDecay.class);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
	}
}
