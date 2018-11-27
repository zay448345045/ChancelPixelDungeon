package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class MagicShield extends Buff
{
    private int amount = 0;
    private float reduction = 0;

    private static final String AMOUNT = "amount";
    private static final String REDUCTION = "reduction";

    {
        type = buffType.POSITIVE;
    }

    @Override
    public void storeInBundle( Bundle bundle )
    {
        super.storeInBundle( bundle );
        bundle.put( AMOUNT, amount );
        bundle.put( REDUCTION, reduction );
    }

    @Override
    public void restoreFromBundle( Bundle bundle )
    {
        super.restoreFromBundle( bundle );
        amount = bundle.getInt(AMOUNT);
        reduction = bundle.getFloat(REDUCTION);
    }

    @Override
    public boolean act()
    {
        if(target.isAlive() && amount > 0 && target.SHLD > 0) 
		{
			//In case this debuff is applied to a mob, use the current depth since only the Hero has an actual level
			float lvl = target instanceof Hero ? ((Hero) target).lvl : Dungeon.depth;

            //Shield decays faster at high amounts and slower at low amounts
            reduction += lvl * amount / target.HT;

            //Because shielding is an integer, it must be reduced by integer amounts
            if (reduction >= 1) {
                target.SHLD -= (int)reduction;
                amount -= (int)reduction;
                //Casting a float to an int automatically removes decimal points, so the below leaves reduction only with its decimal value
                reduction -= (int)reduction;
            }

            //If the target lost shielding i.e. due to being hit, then subtract that shielding lost from amount
            amount = Math.min(amount, target.SHLD);
        }
        else detach();

        spend(TICK);

        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.ANTIMAGIC;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", amount);
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.FORCEFIELD);
        else target.sprite.remove(CharSprite.State.FORCEFIELD);
    }

    public void set(int amount) {
        this.amount += amount;
        target.SHLD += amount;
    }
}
