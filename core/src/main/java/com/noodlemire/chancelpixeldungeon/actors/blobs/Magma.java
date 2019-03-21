package com.noodlemire.chancelpixeldungeon.actors.blobs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.effects.BlobEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.Dewdrop;
import com.noodlemire.chancelpixeldungeon.items.Heap;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;

public class Magma extends GasBlob
{
	@Override
	void affect(Char ch, int cell)
	{
		affect(cell);
	}

	@Override
	void affect(int cell)
	{
		Heap h = Dungeon.level.heaps.get(cell);
		if(h != null && h.type == Heap.Type.HEAP)
		{
			if(Dungeon.level.temperature[cell] > 4)
			{
				for(Item i : h.items)
				{
					if(i instanceof Dewdrop)
					{
						h.items.remove(i);
						Heap.evaporateFX(cell);

						if(h.isEmpty())
							h.destroy();
						else if(h.sprite != null)
							h.sprite.view(h.peek());

						if(!Dungeon.level.water[cell])
							Dungeon.level.temperature[cell] = 0;

						break;
					}
				}
			}
			else
				Dungeon.level.temperature[cell]++;
		}
		else if(Dungeon.level.water[cell])
		{
			if(Dungeon.level.temperature[cell] > 8)
			{
				Level.set(cell, Terrain.EMPTY);
				Heap.evaporateFX(cell);
				GameScene.updateMap(cell);
				Dungeon.level.temperature[cell] = 0;
			}
			else
				Dungeon.level.temperature[cell]++;
		}
	}

	@Override
	public void use(BlobEmitter emitter)
	{
		super.use(emitter);

		emitter.pour(Speck.factory(Speck.MAGMA), 0.4f);
	}

	@Override
	public String tileDesc()
	{
		return Messages.get(this, "desc");
	}
}
