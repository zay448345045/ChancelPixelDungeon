package com.noodlemire.chancelpixeldungeon.items.stones;

import com.noodlemire.chancelpixeldungeon.effects.Enchanting;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.armor.Armor;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.ui.AttackIndicator;
import com.noodlemire.chancelpixeldungeon.windows.WndBag;

public class StoneOfBinding extends InventoryStone
{
	{
		image = ItemSpriteSheet.STONE_RAIDO;
		mode = WndBag.Mode.UNIDED_OR_CURSED_OR_ENCHANTED;
	}

	@Override
	protected void onItemSelected(Item item)
	{
		Enchanting.show(curUser, item);

		if(item.cursed ||
		   (item instanceof Weapon && ((Weapon)item).enchantment != null) ||
		   (item instanceof Armor && ((Armor)item).glyph != null))
			item.bind();

		item.cursedKnown = true;

		item.updateQuickslot();

		AttackIndicator.updateState();
	}
}
