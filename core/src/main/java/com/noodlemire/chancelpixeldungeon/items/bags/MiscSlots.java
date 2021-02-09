package com.noodlemire.chancelpixeldungeon.items.bags;

import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.KindofMisc;
import com.watabou.utils.Bundle;

public class MiscSlots extends Bag
{
	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);

		for(Item i : items)
			if(i instanceof KindofMisc)
				((KindofMisc)i).activate(owner);
	}
}