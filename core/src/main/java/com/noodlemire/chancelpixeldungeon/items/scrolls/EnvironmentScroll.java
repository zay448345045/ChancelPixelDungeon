package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.CellSelector;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;

public abstract class EnvShoutScroll extends Scroll
{
	@Override
	public void doRead()
	{
		GameScene.selectCell();
	}

	static CellSelector.Listener thrower = new CellSelector.Listener()
	{
		@Override
		public void onSelect(Integer target)
		{
			if (target != null)
			{
				curItem.cast(curUser, target);
			}
		}

		@Override
		public String prompt()
		{
			return Messages.get(Item.class, "prompt");
		}
	};
}
