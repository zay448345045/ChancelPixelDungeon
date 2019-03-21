package com.noodlemire.chancelpixeldungeon.items.stones;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.mobs.npcs.Sheep;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class StoneOfFlock extends Runestone
{
	{
		image = ItemSpriteSheet.STONE_DAGAZ;
	}

	@Override
	protected void activate(int cell)
	{
		for(int i : PathFinder.NEIGHBOURS9)
		{
			if(!Dungeon.level.solid[cell + i]
			   && !Dungeon.level.pit[cell + i]
			   && Actor.findChar(cell + i) == null)
			{

				Sheep sheep = new Sheep();
				sheep.lifespan = Random.IntRange(5, 8);
				sheep.pos = cell + i;
				GameScene.add(sheep);
				Dungeon.level.press(sheep.pos, sheep);

				CellEmitter.get(sheep.pos).burst(Speck.factory(Speck.WOOL), 4);
			}
		}
		CellEmitter.get(cell).burst(Speck.factory(Speck.WOOL), 4);
		Sample.INSTANCE.play(Assets.SND_PUFF);
	}
}
