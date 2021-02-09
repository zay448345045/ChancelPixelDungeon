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
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.scenes.CellSelector;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.sprites.MissileSprite;
import com.watabou.utils.Callback;

import java.util.HashMap;

public class HuntressArmor extends ClassArmor
{
	{
		image = ItemSpriteSheet.ARMOR_HUNTRESS;
	}

	private final HashMap<Callback, Mob> targets = new HashMap<>();

	@Override
	public void doSpecial()
	{
		GameScene.selectCell( dasher );
	}

	protected CellSelector.Listener dasher = new CellSelector.Listener()
	{
		@Override
		public void onSelect(final Integer target )
		{
			if (target != null && target != curUser.pos)
			{
				Ballistica route = new Ballistica(curUser.pos, target, Ballistica.PROJECTILE);
				int cell = route.collisionPos;

				//can't occupy the same cell as another char, so move back one.
				if(Actor.findChar(cell) != null && cell != curUser.pos)
					cell = route.path.get(route.dist - 1);

				final int dest = cell;

				curUser.sprite.jump( curUser.pos, cell, new Callback()
				{
					@Override
					public void call()
					{
						curUser.move( dest );
						Dungeon.level.press( dest, curUser );
						Dungeon.observe();

						Item proto = new Arrow();

						for (Mob mob : Dungeon.level.mobs)
						{
							if (Dungeon.level.distance(curUser.pos, mob.pos) <= 12
									&& Dungeon.level.heroFOV[mob.pos])
							{
								Callback callback = new Callback()
								{
									@Override
									public void call()
									{
										float old_factor = curUser.dynamicFactor();
										curUser.attack(targets.get(this));
										targets.remove(this);

										if (targets.size() == 0)
										{
											curUser.dynamic(-curUser.dynamax());
											curUser.spendAndNext(curUser.attackDelay());
										} else
											curUser.dynamic(curUser.dynamax() * old_factor, false);
									}
								};

								((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
										reset(curUser.pos, mob.pos, proto, callback);

								targets.put(callback, mob);
							}
						}

						if (targets.size() == 0)
						{
							curUser.sprite.idle();
							curUser.dynamic(-curUser.dynamax());
							curUser.spendAndNext(curUser.attackDelay());
						}
						else
						{
							curUser.sprite.zap(curUser.pos);
							curUser.busy();
						}
					}
				} );
			}
		}

		@Override
		public String prompt() {
			return "Choose direction to dash";
		}
	};

	private static class Arrow extends MissileWeapon
	{
		public Arrow()
		{
			image = ItemSpriteSheet.ARROW;
		}
	}
}