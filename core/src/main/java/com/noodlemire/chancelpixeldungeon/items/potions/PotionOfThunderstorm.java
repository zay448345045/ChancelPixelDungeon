package com.noodlemire.chancelpixeldungeon.items.potions;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ThunderCloud;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;

public class PotionOfThunderstorm extends Potion {
    {
        initials = 16;

        if(isIdentified()) defaultAction = AC_THROW;
    }

    @Override
    public void shatter( int cell ) {

        if (Dungeon.level.heroFOV[cell]) {
            setKnown();

            splash( cell );
            Sample.INSTANCE.play( Assets.SND_SHATTER );
        }

        GameScene.add( Blob.seed( cell, 1000, ThunderCloud.class ) );
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
