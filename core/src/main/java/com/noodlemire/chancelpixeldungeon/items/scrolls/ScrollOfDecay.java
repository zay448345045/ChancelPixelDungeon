package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Invisibility;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Might;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Weakness;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRot extends EnvironmentScroll
{
	{
		initials = 12;

		bones = true;
	}

	@Override
	protected void onSelect(int cell)
	{
		decay(cell, false);
	}

	@Override
	public void doRead()
	{
		decay(curUser.pos, false);
	}

	@Override
	public void empoweredRead()
	{
		//Will target enemies through walls, but at reduced damage per wall in the way
		decay(curUser.pos, true);
	}

	private void decay(int pos, boolean empowered)
	{
		GameScene.flash(0xFF0000);

		Sample.INSTANCE.play(Assets.SND_BLAST);
		Invisibility.dispel();

		boolean[] aoe = EnvironmentScroll.fovAt(pos, curUser.viewDistance);

		for(int i = 0; i < aoe.length; i++)
		{
			if(aoe[i] || empowered)
			{
				Char ch = Actor.findChar(i);

				if(ch != null && ch.properties().contains(Char.Property.UNDEAD))
					Buff.affect(ch, Might.class, Weakness.DURATION);
				else if(ch != null && !ch.isImmune(getClass()))
				{
					Ballistica wallDetect = new Ballistica(curUser.pos, ch.pos, Ballistica.STOP_TARGET);
					int terrainPassed = 1;

					for(int p : wallDetect.subPath(1, wallDetect.dist))
						if(Dungeon.level.solid[p])
							terrainPassed++;

					ch.damage(Math.round(0.75f * ch.HP() / terrainPassed), this);
					Buff.affect(ch, Weakness.class, Weakness.DURATION);
				}

				if(Dungeon.level.flamable[i])
				{
					Level.set(i, Terrain.EMBERS);
					GameScene.updateMap(i);
				}
			}
		}

		if(curUser.isAlive())
			Dungeon.observe();

		setKnown();
		readAnimation();

		if(!curUser.isAlive())
		{
			Dungeon.fail(getClass());
			GLog.n(Messages.get(this, "ondeath"));
		}
	}
}
