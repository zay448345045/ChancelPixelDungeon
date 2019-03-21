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

package com.noodlemire.chancelpixeldungeon.actors.blobs;

import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.BlobEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.armor.Armor;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfBlessing;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.journal.Notes.Landmark;
import com.noodlemire.chancelpixeldungeon.messages.Messages;

import static com.noodlemire.chancelpixeldungeon.actors.hero.HeroSubClass.ASSASSIN;
import static com.noodlemire.chancelpixeldungeon.actors.hero.HeroSubClass.BATTLEMAGE;
import static com.noodlemire.chancelpixeldungeon.actors.hero.HeroSubClass.BERSERKER;
import static com.noodlemire.chancelpixeldungeon.actors.hero.HeroSubClass.FREERUNNER;
import static com.noodlemire.chancelpixeldungeon.actors.hero.HeroSubClass.GLADIATOR;
import static com.noodlemire.chancelpixeldungeon.actors.hero.HeroSubClass.SNIPER;
import static com.noodlemire.chancelpixeldungeon.actors.hero.HeroSubClass.WARDEN;
import static com.noodlemire.chancelpixeldungeon.actors.hero.HeroSubClass.WARLOCK;

public class WaterOfExchange extends WellWater
{
	@Override
	protected Item affectItem(Item item)
	{
		if(item instanceof ScrollOfUpgrade)
			item = new ScrollOfBlessing();
		else if(item instanceof ScrollOfBlessing)
			item = new ScrollOfUpgrade();
		else if(item instanceof Weapon && ((Weapon) item).augment != Weapon.Augment.NONE)
			if(((Weapon) item).augment == Weapon.Augment.DAMAGE)
				((Weapon) item).augment = Weapon.Augment.SPEED;
			else
				((Weapon) item).augment = Weapon.Augment.DAMAGE;
		else if(item instanceof Armor && ((Armor) item).augment != Armor.Augment.NONE)
			if(((Armor) item).augment == Armor.Augment.DEFENSE)
				((Armor) item).augment = Armor.Augment.EVASION;
			else
				((Armor) item).augment = Armor.Augment.DEFENSE;
		else
			return null;

		return item;
	}

	@Override
	protected boolean affectHero(Hero hero)
	{
		switch(hero.subClass)
		{
			case BERSERKER:
				hero.subClass = GLADIATOR;
				break;
			case GLADIATOR:
				hero.subClass = BERSERKER;
				break;
			case BATTLEMAGE:
				hero.subClass = WARLOCK;
				break;
			case WARLOCK:
				hero.subClass = BATTLEMAGE;
				break;
			case ASSASSIN:
				hero.subClass = FREERUNNER;
				break;
			case FREERUNNER:
				hero.subClass = ASSASSIN;
				break;
			case SNIPER:
				hero.subClass = WARDEN;
				break;
			case WARDEN:
				hero.subClass = SNIPER;
				break;
			default:
				return false;
		}
		return true;
	}

	@Override
	public void use(BlobEmitter emitter)
	{
		super.use(emitter);
		emitter.start(Speck.factory(Speck.CHANGE), 0.2f, 0);
	}

	@Override
	protected Landmark record()
	{
		return Landmark.WELL_OF_EXCHANGE;
	}

	@Override
	public String tileDesc()
	{
		return Messages.get(this, "desc");
	}
}
