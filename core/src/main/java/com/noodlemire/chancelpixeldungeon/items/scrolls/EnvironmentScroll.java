package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MindVision;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.mechanics.ShadowCaster;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.CellSelector;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

public abstract class EnvironmentScroll extends Scroll
{
	private boolean shouted = false;
	private String prompt = Messages.get(this, "shout_prompt");
	private static boolean identifiedByUse = false;

	protected abstract void onSelect(int cell);

	int mode = Ballistica.SCROLL_SPELL;

	//doRead and doShout both separately call activate so that either can be overridden safely
	@Override
	public void doRead()
	{
		activate();
	}

	@Override
	public void doShout()
	{
		shouted = true;
		activate();
	}

	private void activate()
	{
		if(!isKnown())
		{
			setKnown();
			identifiedByUse = true;
		}
		else
			identifiedByUse = false;

		GameScene.selectCell(caster(prompt));
	}

	//helper method used for placing areas of effect at a specific position with a given range
	public static boolean[] fovAt(int pos, int range, boolean mindvision)
	{
		boolean[] fov = new boolean[Dungeon.level.length()];
		int x = pos % Dungeon.level.width();
		int y = pos / Dungeon.level.width();

		ShadowCaster.castShadow(x, y, fov, Dungeon.level.losBlocking, range);

		//For extra, more intentional mind vision synergy
		if(mindvision && curUser.buff(MindVision.class) != null)
			for(int i = 0; i < fov.length; i++)
				if(!fov[i] && Actor.findChar(i) != null)
					fov[i] = true;

		return fov;
	}

	static boolean[] fovAt(int pos, int range)
	{
		return fovAt(pos, range, true);
	}

	private void confirmCancelation()
	{
		GameScene.show(new WndOptions(Messages.titleCase(name()), Messages.get(this, "warning"),
				Messages.get(this, "yes"), Messages.get(this, "no"))
		{
			@Override
			protected void onSelect(int index)
			{
				switch(index)
				{
					case 0:
						curUser.spendAndNext(TIME_TO_READ);
						identifiedByUse = false;
						break;
					case 1:
						GameScene.selectCell(caster(prompt));
						break;
				}
			}

			public void onBackPressed() {}
		});
	}

	private CellSelector.Listener caster(final String prompt)
	{
		return new CellSelector.Listener()
		{
			@Override
			public void onSelect(Integer target)
			{
				//FIXME this safety check shouldn't be necessary
				//it would be better to eliminate the curItem static variable.
				if(!(curItem instanceof EnvironmentScroll))
					return;

				if(target != null)
				{
					((EnvironmentScroll) curItem)
							.onSelect(new Ballistica(curUser.pos, target, mode).collisionPos);
					((Scroll) curItem).readAnimation();

					if(shouted)
						Sample.INSTANCE.play(Assets.SND_CHALLENGE);
					else
						Sample.INSTANCE.play(Assets.SND_READ);
				}
				else if(identifiedByUse && !((Scroll) curItem).ownedByBook)
					((EnvironmentScroll) curItem).confirmCancelation();
				else if(!((Scroll) curItem).ownedByBook)
					curItem.collect(curUser.belongings.backpack);
			}

			@Override
			public String prompt()
			{
				return prompt;
			}
		};
	}
}
