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

package com.noodlemire.chancelpixeldungeon.ui;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Invisibility;
import com.noodlemire.chancelpixeldungeon.actors.buffs.PinCushion;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.CellSelector;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.scenes.PixelScene;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.PathFinder;

public class PinCushionIndicator extends Tag
{
	private static final float TIME_TO_REMOVE = 1f;

	Image icon;

	public static PinCushionIndicator instance;

	private static final int DEFAULT_MISSILE_SHOWN = ItemSpriteSheet.DART;
	private static int missileShown;

	public PinCushionIndicator()
	{
		super(0xFFFF4C);

		instance = this;

		setSize(24, 24);
		visible = false;
		missileShown = DEFAULT_MISSILE_SHOWN;
	}

	private boolean checkMobs()
	{
		for(int n : PathFinder.NEIGHBOURS8)
		{
			int pos = Dungeon.hero.pos + n;
			Char ch = Actor.findChar(pos);

			if(ch != null && ch.buff(PinCushion.class) != null)
			{
				missileShown = ch.buff(PinCushion.class).getMostRecent().image;
				return true;
			}
		}

		return false;
	}

	@Override
	public void destroy()
	{
		super.destroy();
		instance = null;
	}

	@Override
	protected synchronized void layout()
	{
		super.layout();

		if(icon != null)
		{
			icon.x = x + (width - icon.width()) / 2;
			icon.y = y + (height - icon.height()) / 2;
			PixelScene.align(icon);
			if(!members.contains(icon))
				add(icon);
		}
	}

	private boolean needsLayout = false;

	@Override
	public synchronized void update()
	{
		super.update();

		if(!Dungeon.hero.ready)
		{
			if(icon != null) icon.alpha(0.5f);
		}
		else
		{
			if(icon != null) icon.alpha(1f);
		}

		if(!visible && checkMobs())
		{
			visible = true;
			updateIcon();
			flash();
		}
		else
		{
			visible = checkMobs();
		}

		if(needsLayout)
		{
			layout();
			needsLayout = false;
		}
	}

	@Override
	protected void onClick()
	{
		GameScene.selectCell(mobSelector);
	}

	public static void updateIcon()
	{
		if(instance != null)
		{
			synchronized(instance)
			{
				if(instance.icon != null)
				{
					instance.icon.killAndErase();
					instance.icon = null;
				}
				if(instance.visible)
				{
					instance.icon = new ItemSprite(missileShown, null);
					instance.needsLayout = true;
				}
			}
		}
	}

	private static CellSelector.Listener mobSelector = new CellSelector.Listener()
	{
		@Override
		public void onSelect(Integer target)
		{
			if(target == null)
				return;

			Char ch = Actor.findChar(target);

			if(ch == null)
				return;

			if(Dungeon.level.distance(Dungeon.hero.pos, target) > 1)
			{
				GLog.i(Messages.get(PinCushionIndicator.class, "too_far"));
				return;
			}

			PinCushion pc = ch.buff(PinCushion.class);

			if(pc == null)
			{
				GLog.i(Messages.get(PinCushionIndicator.class, "empty_mob"));
				return;
			}

			if(Char.hit(Dungeon.hero, ch, false))
			{
				MissileWeapon mw = pc.removeMostRecent();
				int damage = mw.damageRoll(Dungeon.hero);

				mw.rangedHit(ch, target, true);
				damage = Dungeon.hero.attackProc(ch, damage);
				damage = ch.defenseProc(Dungeon.hero, damage);
				ch.damage(damage, Dungeon.hero);

				ch.sprite.flash();
				ch.sprite.bloodBurstB(ch.sprite.origin, damage);

				if(!ch.isAlive())
					AttackIndicator.updateState();
			}
			else
				ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());

			Dungeon.hero.spendAndNext(TIME_TO_REMOVE);
			Invisibility.dispel();
		}

		@Override
		public String prompt()
		{
			return Messages.get(PinCushionIndicator.class, "prompt");
		}
	};
}