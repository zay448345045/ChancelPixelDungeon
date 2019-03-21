package com.noodlemire.chancelpixeldungeon.items.potions;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.GasBlob;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.DurationBuff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Expulsion;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.artifacts.CloakOfShadows;
import com.noodlemire.chancelpixeldungeon.items.scrolls.EnvironmentScroll;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.utils.GLog;

public class PotionOfExpulsion extends Potion
{
	public static final int MIN_RANGE = 1,
			MAX_RANGE = 3;

	{
		initials = 17;
	}

	@Override
	public void apply(Hero hero)
	{
		GLog.i(Messages.get(this, "refreshed"));

		for(Buff buff : hero.buffs())
		{
			if(buff instanceof Expulsion)
			{
				Class<? extends Blob> blobclass;
				float blobamount;

				blobclass = ((Expulsion) buff).expulse();

				if(buff instanceof DurationBuff)
				{
					blobamount = ((DurationBuff) buff).left();
					buff.detach();
				}
				else if(buff instanceof CloakOfShadows.cloakStealth)
				{
					CloakOfShadows cloak = hero.belongings.misc1 instanceof CloakOfShadows
							? (CloakOfShadows) hero.belongings.misc1
							: (CloakOfShadows) hero.belongings.misc2;

					blobamount = cloak.charges() * 3;
					cloak.drain();
				}
				else
				{
					blobamount = buff.cooldown() + 1;
					buff.detach();
				}

				if(blobclass != null)
				{
					//Gasses tend to vanish quickly at low amount due to how they spread, so give them an amount boost.
					if(blobclass.getSuperclass() == GasBlob.class) blobamount *= 4;

					//Give a min range requirement so that spreading blobs give players a chance to escape
					//(Custom effects tend to be instant, so no min range is needed)
					boolean[] aoe = EnvironmentScroll.fovAt(curUser.pos, MAX_RANGE, false);
					boolean[] not = EnvironmentScroll.fovAt(curUser.pos, MIN_RANGE, false);

					for(int i = 0; i < aoe.length; i++)
						if(aoe[i] && !not[i])
							GameScene.add(Blob.seed(i, (int) Math.ceil(blobamount), blobclass));
				}
			}
		}
	}
}
