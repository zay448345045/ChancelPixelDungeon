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

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.actors.hero.HeroClass;
import com.watabou.noosa.Image;

public enum Icons
{
	SKULL,
	BUSY,
	COMPASS,
	INFO,
	PREFS,
	WARNING,
	TARGET,
	PATH_END,
	MASTERY,
	WATA,
	SHPX,
	NDMR,
	WARRIOR,
	MAGE,
	ROGUE,
	HUNTRESS,
	CLOSE,
	DEPTH,
	DYNAMIC,
	SLEEP,
	ALERT,
	LOST,
	BACKPACK,
	MISC_TAB,
	SEED_POUCH,
	SCROLL_HOLDER,
	POTION_BANDOLIER,
	WAND_HOLSTER,
	CHECKED,
	UNCHECKED,
	EXIT,
	NOTES,
	LANGS,
	CHALLENGE_OFF,
	CHALLENGE_ON,
	RESUME;

	public Image get()
	{
		return get(this);
	}

	public static Image get(Icons type)
	{
		Image icon = new Image(Assets.ICONS);
		switch(type)
		{
			case SKULL:
				icon.frame(icon.texture.uvRect(0, 0, 8, 8));
				break;
			case BUSY:
				icon.frame(icon.texture.uvRect(8, 0, 16, 8));
				break;
			case COMPASS:
				icon.frame(icon.texture.uvRect(0, 8, 7, 13));
				break;
			case INFO:
				icon.frame(icon.texture.uvRect(16, 0, 30, 14));
				break;
			case PREFS:
				icon.frame(icon.texture.uvRect(30, 0, 46, 16));
				break;
			case WARNING:
				icon.frame(icon.texture.uvRect(46, 0, 58, 12));
				break;
			case TARGET:
				icon.frame(icon.texture.uvRect(0, 13, 16, 29));
				icon.hardlight(0xFFD400);
				break;
			case PATH_END:
				icon.frame(icon.texture.uvRect(0, 13, 16, 29));
				icon.hardlight(0x888888);
				break;
			case MASTERY:
				icon.frame(icon.texture.uvRect(16, 14, 30, 28));
				break;
			case WATA:
				icon.frame(icon.texture.uvRect(30, 16, 45, 26));
				break;
			case SHPX:
				icon.frame(icon.texture.uvRect(64, 44, 80, 60));
				break;
			case NDMR:
				icon.frame(icon.texture.uvRect(108, 44, 123, 59));
				break;
			case WARRIOR:
				icon.frame(icon.texture.uvRect(0, 29, 16, 45));
				break;
			case MAGE:
				icon.frame(icon.texture.uvRect(16, 29, 32, 45));
				break;
			case ROGUE:
				icon.frame(icon.texture.uvRect(32, 29, 48, 45));
				break;
			case HUNTRESS:
				icon.frame(icon.texture.uvRect(48, 29, 64, 45));
				break;
			case CLOSE:
				icon.frame(icon.texture.uvRect(0, 45, 13, 58));
				break;
			case DEPTH:
				icon.frame(icon.texture.uvRect(38, 46, 53, 62));
				break;
			case DYNAMIC:
				icon.frame(icon.texture.uvRect(53, 48, 63, 62));
				break;
			case SLEEP:
				icon.frame(icon.texture.uvRect(13, 45, 22, 53));
				break;
			case ALERT:
				icon.frame(icon.texture.uvRect(22, 45, 30, 53));
				break;
			case LOST:
				icon.frame(icon.texture.uvRect(30, 45, 38, 53));
				break;
			case BACKPACK:
				icon.frame(icon.texture.uvRect(58, 0, 68, 10));
				break;
			case MISC_TAB:
				icon.frame(icon.texture.uvRect(106, 12, 116, 22));
				break;
			case SCROLL_HOLDER:
				icon.frame(icon.texture.uvRect(68, 0, 78, 10));
				break;
			case SEED_POUCH:
				icon.frame(icon.texture.uvRect(78, 0, 88, 10));
				break;
			case WAND_HOLSTER:
				icon.frame(icon.texture.uvRect(88, 0, 98, 10));
				break;
			case POTION_BANDOLIER:
				icon.frame(icon.texture.uvRect(98, 0, 108, 10));
				break;
			case CHECKED:
				icon.frame(icon.texture.uvRect(54, 12, 66, 24));
				break;
			case UNCHECKED:
				icon.frame(icon.texture.uvRect(66, 12, 78, 24));
				break;
			case EXIT:
				icon.frame(icon.texture.uvRect(110, 3, 123, 12));
				break;
			case NOTES:
				icon.frame(icon.texture.uvRect(79, 40, 94, 56));
				break;
			case LANGS:
				icon.frame(icon.texture.uvRect(95, 42, 107, 51));
				break;
			case CHALLENGE_OFF:
				icon.frame(icon.texture.uvRect(78, 12, 92, 24));
				break;
			case CHALLENGE_ON:
				icon.frame(icon.texture.uvRect(92, 12, 108, 24));
				break;
			case RESUME:
				icon.frame(icon.texture.uvRect(13, 53, 24, 64));
				break;
		}
		return icon;
	}

	public static Image get(HeroClass cl)
	{
		switch(cl)
		{
			case WARRIOR:
				return get(WARRIOR);
			case MAGE:
				return get(MAGE);
			case ROGUE:
				return get(ROGUE);
			case HUNTRESS:
				return get(HUNTRESS);
			default:
				return null;
		}
	}
}
