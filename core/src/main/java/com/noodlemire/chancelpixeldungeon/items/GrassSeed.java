package com.noodlemire.chancelpixeldungeon.items;

import com.noodlemire.chancelpixeldungeon.Challenges;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Roots;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class GrassSeed extends Item
{
	private static final float TIME_TO_PLANT = 1;

	private static final String AC_PLANT = "plant";

	{
		image = ItemSpriteSheet.GRASS_SEED;
		defaultAction = AC_THROW;
	}

	@Override
	public ArrayList<String> actions(Hero hero)
	{
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_PLANT);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action)
	{
		super.execute(hero, action);

		if(action.equals(AC_PLANT))
		{
			hero.spend(TIME_TO_PLANT);
			hero.busy();
			hero.sprite.operate(hero.pos);

			detach(hero.belongings.backpack);

			onThrow(hero.pos);
		}
	}

	@Override
	public boolean isIdentified()
	{
		return true;
	}

	@Override
	protected void onThrow(int cell)
	{
		Char ch = Actor.findChar(cell);
		if(ch != null && !ch.isImmune(Roots.class) && !ch.flying)
			Buff.affect(ch, Roots.class, 2);
		else if(Dungeon.level.map[cell] == Terrain.ALCHEMY ||
		        Dungeon.level.map[cell] == Terrain.ENTRANCE ||
		        Dungeon.level.map[cell] == Terrain.EXIT ||
		        Dungeon.level.map[cell] == Terrain.UNLOCKED_EXIT ||
		        Dungeon.level.map[cell] == Terrain.WELL ||
		        Dungeon.level.map[cell] == Terrain.PEDESTAL ||
		        Dungeon.level.pit[cell] ||
		        Dungeon.level.traps.get(cell) != null ||
		        Dungeon.isChallenged(Challenges.NO_HERBALISM))
			super.onThrow(cell);
		else
		{
			Level.set(cell, Terrain.HIGH_GRASS);
			GameScene.updateMap(cell);
		}
	}

	@Override
	public boolean stackable()
	{
		return true;
	}
}
