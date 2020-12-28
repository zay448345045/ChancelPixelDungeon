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

package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

public class MirrorGuard extends Buff
{
	{
		type = buffType.POSITIVE;
	}

	private float HP;

	@Override
	public boolean act()
	{
		Hero hero = (Hero) target;

		Mob closest = null;
		int v = hero.visibleDangers();
		for(int i = 0; i < v; i++)
		{
			Char ch = hero.visibleDanger(i);
			if(!(ch instanceof Mob))
				continue;

			Mob mob = (Mob)ch;

			if(mob.isAlive() && mob.state != mob.PASSIVE && !hero.mindVisionEnemies.contains(mob)
			   && (closest == null || Dungeon.level.distance(hero.pos, mob.pos) < Dungeon.level.distance(hero.pos, closest.pos)))
			{
				closest = mob;
			}
		}

		if(closest != null && Dungeon.level.distance(hero.pos, closest.pos) < 5)
		{
			//spawn guardian
			int bestPos = -1;
			for(int i = 0; i < PathFinder.NEIGHBOURS8.length; i++)
			{
				int p = hero.pos + PathFinder.NEIGHBOURS8[i];
				if(Actor.findChar(p) == null && Dungeon.level.passable[p])
					if(bestPos == -1 || Dungeon.level.trueDistance(p, closest.pos) < Dungeon.level.trueDistance(bestPos, closest.pos))
						bestPos = p;
			}
			if(bestPos != -1)
				summonAt(hero, bestPos);
			else
				spend(TICK);
		}
		else
			spend(TICK);

		return true;
	}

	public void summonAt(Hero hero, int pos)
	{
		MirrorImage img = new MirrorImage();
		img.duplicate(hero, (int) Math.floor(HP));
		img.state = img.HUNTING;
		GameScene.add(img, 1);
		ScrollOfTeleportation.appear(img, pos);

		detach();
	}

	public void set(int HP)
	{
		this.HP = HP;
	}

	private int maxHP()
	{
		return maxHP((Hero) target);
	}

	public static int maxHP(Hero hero)
	{
		return 8 + (int) Math.floor(hero.lvl * 2.5f);
	}

	@Override
	public int icon()
	{
		return BuffIndicator.ARMOR;
	}

	@Override
	public void tintIcon(Image icon)
	{
		icon.tint(0.5f, 0.5f, 1, 0.5f);
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc", (int) HP, maxHP());
	}

	private static final String HEALTH = "hp";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(HEALTH, HP);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		HP = bundle.getFloat(HEALTH);
	}
}