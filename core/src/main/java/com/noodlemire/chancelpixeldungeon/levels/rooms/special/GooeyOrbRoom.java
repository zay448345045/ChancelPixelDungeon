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

package com.noodlemire.chancelpixeldungeon.levels.rooms.special;

import com.noodlemire.chancelpixeldungeon.actors.geysers.GooeyOrb;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;

public class GooeyOrbRoom extends SpecialRoom
{
	public void paint(Level level)
	{
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.WATER);

		Point c = center();
		int cx = c.x;
		int cy = c.y;

		Door entrance = entrance();
		entrance.set(Door.Type.REGULAR);

		if(entrance.x == left)
			cx = right - 2;
		else if(entrance.x == right)
			cx = left + 2;
		else if(entrance.y == top)
			cy = bottom - 2;
		else if(entrance.y == bottom)
			cy = top + 2;

		GooeyOrb go = new GooeyOrb();
		go.pos = cx + cy * level.width();
		level.geysers.add(go);
	}
}
