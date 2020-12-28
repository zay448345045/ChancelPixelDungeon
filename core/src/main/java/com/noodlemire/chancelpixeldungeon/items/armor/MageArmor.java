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

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Magma;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Burning;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Roots;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.CellSelector;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;

public class MageArmor extends ClassArmor
{

	{
		image = ItemSpriteSheet.ARMOR_MAGE;
	}

	@Override
	public void doSpecial()
	{
		GameScene.selectCell(blinker);
	}

	protected CellSelector.Listener blinker = new CellSelector.Listener()
	{
		@Override
		public void onSelect(Integer target)
		{
			if (target != null)
			{
				if (!Dungeon.level.heroFOV[target] ||
						!(Dungeon.level.passable[target] || Dungeon.level.avoid[target]) ||
						Actor.findChar(target) != null)
				{
					GLog.w(Messages.get(MageArmor.class, "fov"));
					return;
				}

				curUser.dynamic(0, false);

				for (Mob mob : Dungeon.level.mobs)
				{
					if (Dungeon.level.heroFOV[mob.pos])
					{
						Buff.affect(mob, Burning.class).reignite();
						Buff.prolong(mob, Roots.class, 3);
						Blob.seed(mob.pos, 400, Magma.class);
					}
				}

				ScrollOfTeleportation.appear(curUser, target);
				Dungeon.observe();
				GameScene.updateFog();

				curUser.spendAndNext(Actor.TICK);
			}
		}

		@Override
		public String prompt()
		{
			return Messages.get(MageArmor.this, "prompt");
		}
	};
}