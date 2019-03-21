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

package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Might;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MirrorGuard;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.sprites.HeroSprite;
import com.noodlemire.chancelpixeldungeon.sprites.MirrorSprite;
import com.noodlemire.chancelpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;

public class ScrollOfReflection extends EnvironmentScroll
{
	{
		initials = 4;
		mode = Ballistica.PROJECTILE;
	}

	@Override
	public void doRead()
	{
		reflect(false);
	}

	@Override
	protected void onSelect(int cell)
	{
		for(Mob m : Dungeon.level.mobs.toArray(new Mob[0]))
			if(m instanceof MirrorImage && !((MirrorImage)m).isIndividual())
				((MirrorImage)m).sendToBuff();

		reflect(false);

		new ReflectionCharge().cast(curUser, cell);
	}

	@Override
	public void empoweredRead()
	{
		reflect(true);
	}

	private void reflect(boolean empowered)
	{
		boolean found = false;
		for(Mob m : Dungeon.level.mobs.toArray(new Mob[0]))
		{
			if(m instanceof MirrorImage && !((MirrorImage)m).isIndividual())
			{
				found = true;
				m.heal(m.HT());
				m.sprite.emitter().burst(Speck.factory(Speck.HEALING), 4);

				if(empowered)
					Buff.affect(m, Might.class, Might.DURATION);
			}
		}

		if(!found)
			Buff.affect(curUser, MirrorGuard.class).set(MirrorGuard.maxHP(curUser));

		Sample.INSTANCE.play(Assets.SND_READ);

		readAnimation();
	}

	public static class ReflectionCharge extends MissileWeapon
	{
		@Override
		public void cast(final Hero user, final int dst)
		{
			final int cell = throwPos(user, dst);

			((ReflectionChargeSprite)user.sprite.parent.recycle(ReflectionChargeSprite.class))
					.reset(user.pos, cell,
					new Callback()
					{
						@Override
						public void call()
						{
							MirrorGuard mirror = curUser.buff(MirrorGuard.class);

							int bestPos = -1;

							if(Actor.findChar(cell) == null)
								bestPos = cell;
							else
							{
								for(int i = 0; i < PathFinder.NEIGHBOURS8.length; i++)
								{
									int p = cell + PathFinder.NEIGHBOURS8[i];
									if(Actor.findChar(p) == null && Dungeon.level.passable[p])
									{
										bestPos = p;
										break;
									}
								}
							}

							if(mirror != null && bestPos != -1)
								mirror.summonAt(curUser, bestPos);
						}
					});
		}
	}

	public static class ReflectionChargeSprite extends MirrorSprite
	{
		private static final float SPEED = 240f;

		private Animation fly;

		private Callback callback;

		public void reset(int from, int to, Callback listener)
		{
			reset(DungeonTilemap.tileToWorld(from), DungeonTilemap.tileToWorld(to), listener);
		}

		public void reset(PointF from, PointF to, Callback listener)
		{
			revive();
			updateArmor(Dungeon.hero.tier());

			setup(from, to, listener);
		}

		private void setup(PointF from, PointF to, Callback listener)
		{
			origin.set(width / 2, height / 2);

			this.callback = listener;

			point(from);

			PointF d = PointF.diff(to, from);
			speed.set(d).normalize().scale(SPEED);

			angle = 180 - (float)(Math.atan2(d.x, d.y) / 3.1415926 * 180);

			if(d.x >= 0)
			{
				flipVertical = false;
				updateFrame();
			}
			else
			{
				angle += 180;
				flipVertical = true;
				updateFrame();
			}

			PosTweener tweener = new PosTweener(this, to, d.length() / SPEED);
			tweener.listener = this;
			parent.add(tweener);

			play(fly);
		}

		@Override
		public void updateArmor(int tier)
		{
			super.updateArmor(tier);

			TextureFilm film = new TextureFilm(HeroSprite.tiers(), tier, FRAME_WIDTH, FRAME_HEIGHT);

			fly = new Animation(1, true);
			fly.frames(film, 18);
		}

		@Override
		public void onComplete(Tweener tweener)
		{
			kill();
			if(callback != null)
				callback.call();
		}
	}
}
