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

package com.noodlemire.chancelpixeldungeon.levels;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Bones;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Goo;
import com.noodlemire.chancelpixeldungeon.effects.Ripple;
import com.noodlemire.chancelpixeldungeon.items.Heap;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.levels.builders.Builder;
import com.noodlemire.chancelpixeldungeon.levels.builders.WebbedBuilder;
import com.noodlemire.chancelpixeldungeon.levels.rooms.Room;
import com.noodlemire.chancelpixeldungeon.levels.rooms.special.GooeyOrbRoom;
import com.noodlemire.chancelpixeldungeon.levels.rooms.special.RatKingRoom;
import com.noodlemire.chancelpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.noodlemire.chancelpixeldungeon.levels.rooms.standard.SewerBossEntranceRoom;
import com.noodlemire.chancelpixeldungeon.levels.rooms.standard.StandardRoom;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Bundle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SewerBossLevel extends SewerLevel
{
	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}

	private int stairs = 0;

	@Override
	protected ArrayList<Room> initRooms()
	{
		ArrayList<Room> initRooms = new ArrayList<>();
		initRooms.add(roomEntrance = roomExit = new SewerBossEntranceRoom());

		int standards = standardRooms();
		for(int i = 0; i < standards; i++)
			initRooms.add(new EmptyRoom());

		for(int i = 0; i < 3; i++)
			initRooms.add(new GooeyOrbRoom());

		initRooms.add(new RatKingRoom());
		return initRooms;
	}

	@Override
	protected int standardRooms()
	{
		//7 to 9, average 8
		return 6 + Random.chances(new float[]{1, 1, 1});
	}

	protected Builder builder()
	{
		return new WebbedBuilder()
				.setPathLength(50f, new float[]{1})
				.setTunnelLength(new float[]{3, 1}, new float[]{0, 0, 1, 2});
	}

	@Override
	protected float waterFill()
	{
		return 0.50f;
	}

	@Override
	protected int waterSmoothing()
	{
		return 5;
	}

	@Override
	protected float grassFill()
	{
		return 0.20f;
	}

	@Override
	protected int grassSmoothing()
	{
		return 4;
	}

	protected int nTraps()
	{
		return 0;
	}

	@Override
	protected void createMobs()
	{
		Goo boss = new Goo();
		Room room;
		do
		{
			room = randomRoom(StandardRoom.class);
		}
		while(room == roomEntrance);
		boss.pos = pointToCell(room.random());
		mobs.add(boss);
	}

	@Override
	protected void createGeysers()
	{
	}

	public Actor respawner()
	{
		return null;
	}

	@Override
	protected void createItems()
	{
		Item item = Bones.get();
		if(item != null)
		{
			int pos;
			do
			{
				pos = pointToCell(roomEntrance.random());
			}
			while(pos == entrance || solid[pos]);
			drop(item, pos).type = Heap.Type.REMAINS;
		}
	}

	@Override
	public int randomRespawnCell()
	{
		int pos;
		do
		{
			pos = pointToCell(roomEntrance.random());
		}
		while(pos == entrance || solid[pos]);
		return pos;
	}

	@Override
	public String tilesTex()
	{
		return Assets.TILES_SEWERS_BOSS;
	}

	@Override
	public String waterTex()
	{
		return Assets.WATER_SEWERS_BOSS;
	}

	public void seal()
	{
		if(entrance != 0)
		{
			super.seal();

			set(entrance, Terrain.WATER);
			GameScene.updateMap(entrance);
			GameScene.ripple(entrance);

			stairs = entrance;
			entrance = 0;
		}
	}

	public void unseal()
	{
		if(stairs != 0)
		{
			super.unseal();

			entrance = stairs;
			stairs = 0;

			set(entrance, Terrain.ENTRANCE);
			GameScene.updateMap(entrance);
		}
	}

	private static final String STAIRS = "stairs";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(STAIRS, stairs);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		stairs = bundle.getInt(STAIRS);
		roomExit = roomEntrance;
	}

	@Override
	public Group addVisuals()
	{
		super_addVisuals();
		addSewerBossVisuals(this, visuals);
		return visuals;
	}

	public static void addSewerBossVisuals(Level level, Group group)
	{
		for(int i = 0; i < level.length(); i++)
			if(level.map[i] == Terrain.WALL_DECO)
				group.add(new SewerBossLevel.Sink(i));
	}

	private static class Sink extends Emitter
	{
		private int pos;
		private float rippleDelay = 0;

		private static final Emitter.Factory factory = new Factory()
		{

			@Override
			public void emit(Emitter emitter, int index, float x, float y)
			{
				BlackWaterParticle p = (BlackWaterParticle) emitter.recycle(BlackWaterParticle.class);
				p.reset(x, y);
			}
		};

		public Sink(int pos)
		{
			super();

			this.pos = pos;

			PointF p = DungeonTilemap.tileCenterToWorld(pos);
			pos(p.x - 2, p.y + 3, 4, 0);

			pour(factory, 0.1f);
		}

		@Override
		public void update()
		{
			if(visible = (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos]))
			{

				super.update();

				if((rippleDelay -= Game.elapsed) <= 0)
				{
					Ripple ripple = GameScene.ripple(pos + Dungeon.level.width());
					if(ripple != null)
					{
						ripple.y -= DungeonTilemap.SIZE / 2;
						rippleDelay = Random.Float(0.4f, 0.6f);
					}
				}
			}
		}
	}

	public static final class BlackWaterParticle extends PixelParticle
	{
		public BlackWaterParticle()
		{
			super();

			acc.y = 50;
			am = 0.5f;

			color(ColorMath.random(0x000000, 0x3b3b3b));
			size(2);
		}

		public void reset(float x, float y)
		{
			revive();

			this.x = x;
			this.y = y;

			speed.set(Random.Float(-2, +2), 0);

			left = lifespan = 0.4f;
		}
	}
}
