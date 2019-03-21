package com.noodlemire.chancelpixeldungeon.actors.blobs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.effects.BlobEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class EnticementGas extends GasBlob
{

	@Override
	protected void evolve()
	{
		super.evolve();

		for(Mob mob : Dungeon.level.mobs)
		{
			Point cell = (Point) Random.element(area.getPoints().toArray());
			mob.beckon(cell.x + cell.y * Dungeon.level.width());
		}
	}

	@Override
	void affect(Char ch, int cell)
	{
		//Just a visual effect
		if(Dungeon.level.heroFOV[cell])
			ch.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 2);
	}

	@Override
	void affect(int cell)
	{
	}

	@Override
	public void use(BlobEmitter emitter)
	{
		super.use(emitter);

		emitter.pour(Speck.factory(Speck.ENTICE), 0.4f);
	}

	@Override
	public String tileDesc()
	{
		return Messages.get(this, "desc");
	}
}
