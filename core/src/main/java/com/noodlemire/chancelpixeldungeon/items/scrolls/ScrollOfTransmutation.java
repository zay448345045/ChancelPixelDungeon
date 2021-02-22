package com.noodlemire.chancelpixeldungeon.items.scrolls;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.effects.Enchanting;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.EquipableItem;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.Transmutable;
import com.noodlemire.chancelpixeldungeon.journal.Catalog;
import com.noodlemire.chancelpixeldungeon.levels.traps.DistortionTrap;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.noodlemire.chancelpixeldungeon.windows.WndBag;

public class ScrollOfTransmutation extends InventoryScroll
{
	private boolean empowered = false;

	{
		mode = WndBag.Mode.TRANSMUTABLE;
		icon = ItemSpriteSheet.Icons.SCROLL_TRANSMUTATION;
	}

	@Override
	protected void onItemSelected(Item item)
	{
		System.out.println("step 1");
		if(empowered)
		{
			Item result1 = ((Transmutable)item).transmute();
			Item result2 = ((Transmutable)item).transmute();

			handleTransmutation(item, result1.price() > result2.price() ? result1 : result2);
		}
		else
			handleTransmutation(item, ((Transmutable)item).transmute());
	}

	@Override
	public void empoweredRead()
	{
		//See onItemSelected. Higher cost items are preferred.
		empowered = true;
		doRead();
	}

	@Override
	public void doShout()
	{
		DistortionTrap.resetLevel();
	}

	private void handleTransmutation(Item item, Item result)
	{
		System.out.println("step 2");
		if(result == null)
		{
			GLog.i(Messages.get(this,"failed"));
			return;
		}

		System.out.println("step 3");
		//In case a never-seen item pops out
		if(result.isIdentified())
			Catalog.setSeen(result.getClass());

		if(item.isEquipped(curUser))
		{
			item.cursed = false;
			((EquipableItem)item).doUnequip(curUser, false);
			((EquipableItem)result).doEquip(curUser);
		}
		else
		{
			item.detach(curUser.belongings.backpack);
			if(!result.collect())
				Dungeon.level.drop(result, curUser.pos).sprite.drop();
		}
		System.out.println("step 4");

		Enchanting.show(curUser, result);
		curUser.sprite.emitter().burst(Speck.factory(Speck.CHANGE), 4);
	}

	@Override
	public int price()
	{
		return 40 * quantity;
	}
}
