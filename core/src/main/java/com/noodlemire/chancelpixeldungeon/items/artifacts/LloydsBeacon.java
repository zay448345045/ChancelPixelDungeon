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
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Invisibility;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.MagicMissile;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.CellSelector;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.scenes.InterlevelScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite.Glowing;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.ui.QuickSlotButton;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class LloydsBeacon extends Artifact
{
	private static final float TIME_TO_USE = 1;

	private static final String AC_ZAP = "ZAP",
			AC_SET = "SET",
			AC_RETURN = "RETURN";

	public int returnDepth = -1;
	private int returnPos;

	{
		image = ItemSpriteSheet.ARTIFACT_BEACON;

		levelCap = 3;

		charge = 0;
		chargeCap = 3 + level();

		defaultAction = AC_ZAP;
		usesTargeting = true;
	}

	private static final String DEPTH = "depth";
	private static final String POS = "pos";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(DEPTH, returnDepth);
		if(returnDepth != -1)
			bundle.put(POS, returnPos);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		returnDepth = bundle.getInt(DEPTH);
		returnPos = bundle.getInt(POS);
	}

	@Override
	public ArrayList<String> actions(Hero hero)
	{
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_ZAP);
		actions.add(AC_SET);
		if(returnDepth != -1)
			actions.add(AC_RETURN);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action)
	{
		super.execute(hero, action);

		if(action.equals(AC_SET) || action.equals(AC_RETURN))
		{
			if(Dungeon.bossLevel())
			{
				hero.spend(LloydsBeacon.TIME_TO_USE);
				GLog.w(Messages.get(this, "preventing"));
				return;
			}

			for(int i = 0; i < PathFinder.NEIGHBOURS8.length; i++)
			{
				Char ch = Actor.findChar(hero.pos + PathFinder.NEIGHBOURS8[i]);
				if(ch != null && ch.alignment == Char.Alignment.ENEMY)
				{
					GLog.w(Messages.get(this, "creatures"));
					return;
				}
			}
		}

		if(action.equals(AC_ZAP))
		{
			unBind();

			curUser = hero;

			if(!isEquipped(hero))
			{
				GLog.i(Messages.get(Artifact.class, "need_to_equip"));
				QuickSlotButton.cancel();
			}
			else if(charge < 1)
			{
				GLog.i(Messages.get(this, "no_charge"));
				QuickSlotButton.cancel();
			}
			else
				GameScene.selectCell(zapper);
		}
		else if(action.equals(AC_SET))
		{
			returnDepth = Dungeon.depth;
			returnPos = hero.pos;

			hero.spend(LloydsBeacon.TIME_TO_USE);
			hero.busy();

			hero.sprite.operate(hero.pos);
			Sample.INSTANCE.play(Assets.SND_BEACON);

			GLog.i(Messages.get(this, "return"));
		}
		else if(action.equals(AC_RETURN))
		{
			if(returnDepth == Dungeon.depth)
			{
				ScrollOfTeleportation.appear(hero, returnPos);
				Dungeon.level.press(returnPos, hero);
				Dungeon.observe();
				GameScene.updateFog();
			}
			else
			{
				Hero.detachTimeFreeze();

				InterlevelScene.mode = InterlevelScene.Mode.RETURN;
				InterlevelScene.returnDepth = returnDepth;
				InterlevelScene.returnPos = returnPos;
				Game.switchScene(InterlevelScene.class);
			}
		}
	}

	private final CellSelector.Listener zapper = new CellSelector.Listener()
	{
		@Override
		public void onSelect(Integer target)
		{
			if(target == null) return;

			Invisibility.dispel();
			charge -= Dungeon.depth > 20 ? 2 : 1;
			updateQuickslot();

			if(Actor.findChar(target) == curUser)
			{
				ScrollOfTeleportation.randomTeleport(curUser);
				curUser.spendAndNext(1f);
			}
			else
			{
				final Ballistica bolt = new Ballistica(curUser.pos, target, Ballistica.MAGIC_BOLT);
				final Char ch = Actor.findChar(bolt.collisionPos);

				if(ch == curUser)
				{
					ScrollOfTeleportation.randomTeleport(curUser);
					curUser.spendAndNext(1f);
				}
				else
				{
					Sample.INSTANCE.play(Assets.SND_ZAP);
					curUser.sprite.zap(bolt.collisionPos);
					curUser.busy();

					MagicMissile.boltFromChar(curUser.sprite.parent,
							MagicMissile.BEACON,
							curUser.sprite,
							bolt.collisionPos,
							new Callback()
							{
								@Override
								public void call()
								{
									ScrollOfTeleportation.randomTeleport(ch);
									curUser.spendAndNext(1f);
								}
							});
				}
			}
		}

		@Override
		public String prompt()
		{
			return Messages.get(LloydsBeacon.class, "prompt");
		}
	};

	@Override
	protected ArtifactBuff passiveBuff()
	{
		return new beaconRecharge();
	}

	@Override
	public void charge(Hero target, float amount)
	{
		super.charge(target, amount / (25f - (chargeCap - charge) * 10f));
	}

	@Override
	public Item upgrade()
	{
		if(level() == levelCap) return this;
		chargeCap++;
		GLog.p(Messages.get(this, "levelup"));
		return super.upgrade();
	}

	@Override
	public String desc()
	{
		String desc = super.desc();
		if(returnDepth != -1)
			desc += "\n\n" + Messages.get(this, "desc_set", returnDepth);
		return desc;
	}

	private static final Glowing WHITE = new Glowing(0xFFFFFF);

	@Override
	public Glowing glowing()
	{
		return returnDepth != -1 ? WHITE : null;
	}

	public class beaconRecharge extends ArtifactBuff
	{
		@Override
		public boolean act()
		{
			if(charge < chargeCap && (!cursed || isBound()))
			{
				partialCharge += 1 / (100f - (chargeCap - charge) * 10f);

				if(partialCharge >= 1)
				{
					partialCharge--;
					charge++;

					if(charge == chargeCap)
					{
						partialCharge = 0;
					}
				}
			}

			updateQuickslot();
			spend(TICK);
			return true;
		}
	}
}
