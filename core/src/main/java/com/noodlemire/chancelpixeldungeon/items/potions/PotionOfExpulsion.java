package com.noodlemire.chancelpixeldungeon.items.potions;

import com.noodlemire.chancelpixeldungeon.actors.buffs.Bleeding;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Blindness;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Burning;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Charm;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Corrosion;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Cripple;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Drowsy;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Frost;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Ooze;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Paralysis;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Poison;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Roots;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Sleep;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Slow;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Vertigo;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Weakness;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.armor.glyphs.Viscosity;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.utils.GLog;

public class PotionOfRefreshment extends Potion
{
    {
        initials = 17;
    }

    @Override
    public void apply(Hero hero)
    {
        setKnown();

        GLog.i(Messages.get(this, "refreshed"));
        Buff.detach(hero, Poison.class);
        Buff.detach(hero, Cripple.class);
        Buff.detach(hero, Weakness.class);
        Buff.detach(hero, Bleeding.class);
        Buff.detach(hero, Drowsy.class);
        Buff.detach(hero, Slow.class);
        Buff.detach(hero, Vertigo.class);
        Buff.detach(hero, Blindness.class);
        Buff.detach(hero, Burning.class);
        Buff.detach(hero, Charm.class);
        Buff.detach(hero, Corrosion.class);
        Buff.detach(hero, Frost.class);
        Buff.detach(hero, Ooze.class);
        Buff.detach(hero, Paralysis.class);
        Buff.detach(hero, Roots.class);
        Buff.detach(hero, Sleep.class);
        Buff.detach(hero, Viscosity.DeferedDamage.class);
    }
}
