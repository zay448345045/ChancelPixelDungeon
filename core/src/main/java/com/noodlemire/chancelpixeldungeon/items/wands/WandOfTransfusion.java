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
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Charm;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MagicShield;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.effects.Beam;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.effects.particles.BloodParticle;
import com.noodlemire.chancelpixeldungeon.effects.particles.ShadowParticle;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.MagesStaff;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.tiles.DungeonTilemap;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfTransfusion extends Wand
{
	{
		image = ItemSpriteSheet.WAND_TRANSFUSION;

		collisionProperties = Ballistica.PROJECTILE;
	}

	private boolean freeCharge = false;

	@Override
	protected void onZap(Ballistica beam)
	{
		for(int c : beam.subPath(0, beam.dist))
			CellEmitter.center(c).burst(BloodParticle.BURST, 1);

		int cell = beam.collisionPos;

		Char ch = Actor.findChar(cell);

		if(ch instanceof Mob)
		{
			// 10% of max hp
			int selfDmg = (int) Math.ceil(curUser.HT() * 0.10f);
			boolean selfShield = true;

			processSoulMark(ch, chargesPerCast());

			//this wand does different things depending on the target.

			//heals/shields an ally, or a charmed enemy
			if(ch.alignment == Char.Alignment.ALLY || ch.buff(Charm.class) != null)
			{
				int healing = selfDmg + 2 * level();
				int shielding = (ch.HP() + healing) - ch.HT();

				if(shielding > 0)
				{
					healing -= shielding;
					Buff.affect(ch, MagicShield.class).set(shielding);
				}

				ch.heal(healing, this);
				ch.sprite.emitter().burst(Speck.factory(Speck.HEALING), 2 + level() / 2);

				selfShield = false;

				//harms the undead
			}
			else if(ch.properties().contains(Char.Property.UNDEAD))
			{
				int damage = selfDmg + 2 * level();
				ch.damage(damage, this);
				ch.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10 + level());
				Sample.INSTANCE.play(Assets.SND_BURNING);

				//charms an enemy
			}
			else
			{
				Buff.affect(ch, Charm.class, 4 + level()).object = curUser.id();

				ch.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
			}

			if(selfShield)
				Buff.affect(curUser, MagicShield.class).set(selfDmg);
			else
			{
				if(!freeCharge)
					damageHero(selfDmg);
				else
					freeCharge = false;
			}
		}
	}

	//this wand costs health too
	private void damageHero(int damage)
	{
		curUser.damage(damage, this);

		if(!curUser.isAlive())
		{
			Dungeon.fail(getClass());
			GLog.n(Messages.get(this, "ondeath"));
		}
	}

	@Override
	protected int initialCharges()
	{
		return 1;
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage)
	{
		// lvl 0 - 10%
		// lvl 1 - 18%
		// lvl 2 - 25%
		if(Random.Int(level() + 10) >= 9)
		{
			//grants a free use of the staff
			freeCharge = true;
			GLog.p(Messages.get(this, "charged"));
			attacker.sprite.emitter().burst(BloodParticle.BURST, 20);
		}
	}

	@Override
	protected void fx(Ballistica beam, Callback callback)
	{
		curUser.sprite.parent.add(
				new Beam.HealthRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));
		callback.call();
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle)
	{
		particle.color(0xCC0000);
		particle.am = 0.6f;
		particle.setLifespan(1f);
		particle.speed.polar(Random.Float(PointF.PI2), 2f);
		particle.setSize(1f, 2f);
		particle.radiateXY(0.5f);
	}

	@Override
	public String statsDesc() {
		int selfDMG = Math.round(Dungeon.hero.HT()*0.10f);
		if (levelKnown)
			return Messages.get(this, "stats_desc", selfDMG, selfDMG + 3*level(), 5+2*level(), 3+level()/2, 6+level());
		else
			return Messages.get(this, "stats_desc", selfDMG, selfDMG, 5, 3, 6);
	}

	private static final String FREECHARGE = "freecharge";

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		freeCharge = bundle.getBoolean(FREECHARGE);
	}

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(FREECHARGE, freeCharge);
	}
}
