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

package com.noodlemire.chancelpixeldungeon.items.artifacts;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Darkness;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Expulsion;
import com.noodlemire.chancelpixeldungeon.actors.buffs.FadePercent;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Preparation;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.hero.HeroSubClass;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class CloakOfShadows extends Artifact
{
	{
		image = ItemSpriteSheet.ARTIFACT_CLOAK;

		exp = 0;
		levelCap = 10;

		charge = Math.min(level() + 3, 10);
		partialCharge = 0;
		chargeCap = Math.min(level() + 3, 10);

		defaultAction = AC_STEALTH;

		unique = true;
		bones = false;
	}

	private boolean stealthed = false;

	private static final String AC_STEALTH = "STEALTH";

	public int charges()
	{
		return charge;
	}

	public void drain()
	{
		charge = 0;
		stealthed = false;
		activeBuff.detach();
		activeBuff = null;
	}

	@Override
	public void charge(Hero target, float amount)
	{
		super.charge(target, 0.25f * amount);
	}

	@Override
	public ArrayList<String> actions(Hero hero)
	{
		ArrayList<String> actions = super.actions(hero);
		if(isEquipped(hero) && charge > 0)
			actions.add(AC_STEALTH);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action)
	{
		super.execute(hero, action);

		if(action.equals(AC_STEALTH))
		{
			if(!stealthed)
			{
				if(!isEquipped(hero)) GLog.i(Messages.get(Artifact.class, "need_to_equip"));
				else if(charge <= 0) GLog.i(Messages.get(this, "no_charge"));
				else
				{
					stealthed = true;
					hero.spend(1f);
					hero.busy();
					Sample.INSTANCE.play(Assets.SND_MELD);
					activeBuff = activeBuff();
					activeBuff.attachTo(hero);
					if(hero.sprite.parent != null)
					{
						hero.sprite.parent.add(new AlphaTweener(hero.sprite, 0.4f, 0.4f));
					}
					else
					{
						hero.sprite.alpha(0.4f);
					}
					hero.sprite.operate(hero.pos);
				}
			}
			else
			{
				stealthed = false;
				activeBuff.detach();
				activeBuff = null;
				hero.spend(1f);
				hero.sprite.operate(hero.pos);
			}

			unBind();
		}
	}

	@Override
	public void activate(Char ch)
	{
		super.activate(ch);
		if(stealthed)
		{
			activeBuff = activeBuff();
			activeBuff.attachTo(ch);
		}
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single)
	{
		if(super.doUnequip(hero, collect, single))
		{
			stealthed = false;
			return true;
		}
		else
			return false;
	}

	@Override
	protected ArtifactBuff passiveBuff()
	{
		return new cloakRecharge();
	}

	@Override
	protected ArtifactBuff activeBuff()
	{
		return new cloakStealth();
	}

	@Override
	public Item upgrade()
	{
		chargeCap = Math.min(chargeCap + 1, 10);
		return super.upgrade();
	}

	private static final String STEALTHED = "stealthed";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(STEALTHED, stealthed);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		stealthed = bundle.getBoolean(STEALTHED);
		// pre-0.6.2 saves
		if(bundle.contains("cooldown"))
		{
			exp = 0;
			level((int) Math.ceil(level() * 0.7f));
			charge = chargeCap = Math.min(3 + level(), 10);
		}
	}

	@Override
	public int price()
	{
		return 0;
	}

	public class cloakRecharge extends ArtifactBuff
	{
		@Override
		public boolean act()
		{
			if(charge < chargeCap)
			{
				if(!stealthed)
				{
					float turnsToCharge = (50 - (chargeCap - charge));
					if(level() > 7) turnsToCharge -= 10 * (level() - 7) / 3f;
					partialCharge += (1f / turnsToCharge);
				}

				if(partialCharge >= 1)
				{
					charge++;
					partialCharge -= 1;
					if(charge == chargeCap)
					{
						partialCharge = 0;
					}

				}
			}
			else
				partialCharge = 0;

			if(cooldown > 0)
				cooldown--;

			updateQuickslot();

			spend(TICK);

			return true;
		}

	}

	public class cloakStealth extends ArtifactBuff implements Expulsion, FadePercent
	{
		private static final int TURNS_PER_CHARGE = 5;

		int turnsToCost = 0;

		@Override
		public int icon()
		{
			return BuffIndicator.INVISIBLE;
		}

		@Override
		public boolean attachTo(Char target)
		{
			if(super.attachTo(target))
			{
				target.invisible++;
				if(target instanceof Hero && ((Hero) target).subClass == HeroSubClass.ASSASSIN)
				{
					Buff.affect(target, Preparation.class);
				}
				return true;
			}
			else
			{
				return false;
			}
		}

		@Override
		public boolean act()
		{
			turnsToCost--;

			if(turnsToCost <= 0)
			{
				charge--;
				if(charge < 0)
				{
					charge = 0;
					detach();
					GLog.w(Messages.get(this, "no_charge"));
					((Hero) target).interrupt();
				}
				else
				{
					//target hero level is 1 + 2*cloak level
					int lvlDiffFromTarget = ((Hero) target).lvl - (1 + level() * 2);
					//plus an extra one for each level after 6
					if(level() >= 7)
					{
						lvlDiffFromTarget -= level() - 6;
					}
					if(lvlDiffFromTarget >= 0)
					{
						exp += Math.round(10f * Math.pow(1.1f, lvlDiffFromTarget));
					}
					else
					{
						exp += Math.round(10f * Math.pow(0.75f, -lvlDiffFromTarget));
					}

					if(exp >= (level() + 1) * 50 && level() < levelCap)
					{
						upgrade();
						exp -= level() * 50;
						GLog.p(Messages.get(this, "levelup"));

					}
					turnsToCost = TURNS_PER_CHARGE;
				}
				updateQuickslot();
			}

			spend(TICK);

			return true;
		}

		@Override
		public Class<? extends Blob> expulse()
		{
			return Darkness.class;
		}

		@Override
		public void fx(boolean on)
		{
			if(on) target.sprite.add(CharSprite.State.INVISIBLE);
			else if(target.invisible == 0) target.sprite.remove(CharSprite.State.INVISIBLE);
		}

		@Override
		public String toString()
		{
			return Messages.get(this, "name");
		}

		@Override
		public String desc()
		{
			return Messages.get(this, "desc");
		}

		@Override
		public void detach()
		{
			if(target.invisible > 0)
				target.invisible--;
			stealthed = false;

			updateQuickslot();
			super.detach();
		}

		@Override
		public float fadePercent()
		{
			return 1 - (float)turnsToCost / TURNS_PER_CHARGE;
		}
	}
}
