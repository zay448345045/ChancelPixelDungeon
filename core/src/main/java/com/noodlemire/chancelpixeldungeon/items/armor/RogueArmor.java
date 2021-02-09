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

package com.noodlemire.chancelpixeldungeon.items.armor;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Blindness;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.CellSelector;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class RogueArmor extends ClassArmor
{
	{
		image = ItemSpriteSheet.ARMOR_ROGUE;
	}

	@Override
	public void doSpecial()
	{
		GameScene.selectCell(teleporter);
	}

	private static CellSelector.Listener teleporter = new CellSelector.Listener()
	{
		@Override
		public void onSelect(Integer target)
		{
			if (target != null)
			{
				PathFinder.buildDistanceMap(curUser.pos, Dungeon.level.passable, 8);

				if (PathFinder.distance[target] == Integer.MAX_VALUE ||
						!Dungeon.level.heroFOV[target] ||
						!(Dungeon.level.passable[target] || Dungeon.level.avoid[target]) ||
						Actor.findChar(target) != null)
				{
					GLog.w(Messages.get(RogueArmor.class, "fov"));
					return;
				}

				curUser.dynamic(-curUser.dynamax());

				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[Dungeon.level.mobs.size()]))
				{
					if (Dungeon.level.heroFOV[mob.pos])
					{
						Buff.prolong(mob, Blindness.class, 2);
						if (mob.state == mob.HUNTING) mob.state = mob.WANDERING;
						mob.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 4);
					}
				}

				ScrollOfTeleportation.appear(curUser, target);
				CellEmitter.get(target).burst(Speck.factory(Speck.WOOL), 10);
				Sample.INSTANCE.play(Assets.SND_PUFF);
				Dungeon.level.press(target, curUser);
				Dungeon.observe();
				GameScene.updateFog();

				curUser.spendAndNext(Actor.TICK);
			}
		}

		@Override
		public String prompt()
		{
			return Messages.get(RogueArmor.class, "prompt");
		}
	};
}