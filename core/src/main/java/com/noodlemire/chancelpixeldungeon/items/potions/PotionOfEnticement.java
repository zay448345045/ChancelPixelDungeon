package com.noodlemire.chancelpixeldungeon.items.potions;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.EnticementGas;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;

public class PotionOfEnticement extends Potion {
    {
        initials = 12;

        if(isIdentified()) defaultAction = AC_THROW;
    }

    @Override
    public void shatter( int cell ) {

        if (Dungeon.level.heroFOV[cell]) {
            setKnown();

            splash( cell );
            Sample.INSTANCE.play( Assets.SND_SHATTER );
        }

        GameScene.add( Blob.seed( cell, 1000, EnticementGas.class ) );
    }

    @Override
    public void setKnown()
    {
        defaultAction = AC_THROW;
        super.setKnown();
    }

    @Override
    public int price() {
        return isKnown() ? 30 * quantity : super.price();
    }
}
