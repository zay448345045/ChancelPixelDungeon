package com.noodlemire.chancelpixeldungeon.items.potions;

import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.windows.WndBag;

public class PotionOfPlacebo extends InventoryPotion
{
    {
        initials = 19;
        mode = WndBag.Mode.IDED_POTION;
    }

    @Override
    protected void onItemSelected( Item item )
    {
        ((Potion)item).apply(curUser);
    }

    @Override public void apply(Hero hero) {} //There's a reason why it's considered a scam to so many...

    @Override
    public int price() {
        return isKnown() ? 15 * quantity : super.price();
    }
}
