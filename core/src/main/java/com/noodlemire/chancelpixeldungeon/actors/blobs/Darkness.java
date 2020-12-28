package com.noodlemire.chancelpixeldungeon.actors.blobs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Blindness;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Light;
import com.noodlemire.chancelpixeldungeon.effects.BlobEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.messages.Messages;

public class Darkness extends GasBlob
{
	{
		harmful = true;
	}

	@Override
	void affect(Char ch, int cell)
	{
		if(ch.buff(Light.class) == null)
			Buff.affect(ch, Blindness.class, 1);

		affect(cell);
	}

	@Override
	void affect(int cell)
	{
		Dungeon.level.losBlocking[cell] =
				off[cell] > 0 || (Terrain.flags[Dungeon.level.map[cell]] & Terrain.LOS_BLOCKING) != 0;
	}

	@Override
	public void use(BlobEmitter emitter)
	{
		super.use(emitter);
		emitter.pour(Speck.factory(Speck.DARKNESS), 0.1f);
	}

	@Override
	public void clear(int cell)
	{
		super.clear(cell);
		Level l = Dungeon.level;
		l.losBlocking[cell] = cur[cell] > 0 || (Terrain.flags[l.map[cell]] & Terrain.LOS_BLOCKING) != 0;
	}

	@Override
	public void fullyClear()
	{
		super.fullyClear();
		Dungeon.level.buildFlagMaps();
	}

	@Override
	public String tileDesc()
	{
		return Messages.get(this, "desc");
	}
}
