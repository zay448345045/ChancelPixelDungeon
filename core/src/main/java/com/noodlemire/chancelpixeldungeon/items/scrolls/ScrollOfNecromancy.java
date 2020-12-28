package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Corruption;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Wraith;
import com.noodlemire.chancelpixeldungeon.items.Heap;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.quest.CorpseDust;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ScrollOfNecromancy extends EnvironmentScroll
{
	{
		initials = 15;

		if(isIdentified()) defaultAction = AC_SHOUT;
	}

	@Override
	public void doRead()
	{
		summonWraiths(false, curUser.pos);
		readAnimation();
		Sample.INSTANCE.play(Assets.SND_READ);
	}

	@Override
	protected void onSelect(int cell)
	{
		summonWraiths(false, cell);
	}

	@Override
	public void empoweredRead()
	{
		summonWraiths(true, curUser.pos);
		readAnimation();
		Sample.INSTANCE.play(Assets.SND_READ);
	}

	private void summonWraiths(boolean empower, int center)
	{
		ArrayList<Integer> emptyPos = new ArrayList<>();
		int wraithRate = empower ? 2 : 1;
		int wraithCount = 1 + wraithRate;

		Char centerChar = Actor.findChar(center);
		if(center != curUser.pos && centerChar != null && centerChar.buff(Corruption.class) == null
				&& !centerChar.properties().contains(Char.Property.BOSS) && !centerChar.properties().contains(Char.Property.MINIBOSS))
			Buff.affect(centerChar, Corruption.class);

		for(int p : PathFinder.NEIGHBOURS9)
		{
			int pos = p + center;
			Heap heap = Dungeon.level.heaps.get(pos);

			if(heap != null)
				if(heap.items.contains(new CorpseDust()))
					wraithCount += wraithRate * 4;
				else if(heap.type == Heap.Type.TOMB)
					wraithCount += wraithRate * 2;
				else if((heap.type == Heap.Type.REMAINS || heap.type == Heap.Type.SKELETON) && heapCursed(heap))
					wraithCount += wraithRate;

			if(pos > 0 && Dungeon.level.passable[pos] && Actor.findChar(pos) == null)
				emptyPos.add(pos);
		}

		if(curUser.belongings.backpack.contains(CorpseDust.class))
			wraithCount += wraithRate * 4;

		if(wraithCount >= emptyPos.size())
			for(int e : emptyPos)
				Buff.affect(Wraith.spawnAt(e), Corruption.class);
		else
			for(int i = 0; i < wraithCount; i++)
			{
				Integer e = Random.element(emptyPos);
				Buff.affect(Wraith.spawnAt(e), Corruption.class);
				emptyPos.remove(e);
			}
	}

	private boolean heapCursed(Heap heap)
	{
		for(Item item : heap.items)
			if(item.cursed)
				return true;

		return false;
	}

	@Override
	public void setKnown()
	{
		super.setKnown();
		if(isIdentified()) defaultAction = AC_SHOUT;
	}
}
