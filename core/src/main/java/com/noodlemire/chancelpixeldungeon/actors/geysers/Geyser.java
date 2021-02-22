package com.noodlemire.chancelpixeldungeon.actors.geysers;

import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.GasBlob;
import com.noodlemire.chancelpixeldungeon.actors.buffs.FlavourBuff;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.sprites.GeyserSprite;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public abstract class Geyser extends Char
{
	protected int spriteX, spriteY;

	{
		name = Messages.get(this, "name");
		actPriority = MOB_PRIO;

		alignment = Alignment.NEUTRAL;

		setHT((int)Math.round(40 * Math.pow(2, (Dungeon.depth-1)/5)), true);

		properties.add(Property.IMMOVABLE);
		properties.add(Property.BLOB_IMMUNE);
		properties.add(Property.INORGANIC);

		sprite = sprite();
	}

	@Override
	public CharSprite sprite()
	{
		return new GeyserSprite(spriteX, spriteY, false);
	}

	@Override
	protected boolean act()
	{
		spend(TICK);
		return true;
	}

	@Override
	public void destroy()
	{
		super.destroy();

		Dungeon.level.geysers.remove(this);
	}

	public abstract Class<? extends Blob> blobClass();

	protected void spew()
	{
		if(buff(Disabled.class) != null)
			return;

		Class<? extends Blob> b = blobClass();

		if(GasBlob.class.isAssignableFrom(b))
			GameScene.add(Blob.seed(pos, 40, b));
		else
			for(int p : PathFinder.NEIGHBOURS9)
				if(!Dungeon.level.solid[pos + p])
					GameScene.add(Blob.seed(pos + p, 2, b));
	}

	public static Geyser createGeyser()
	{
		int selection = Math.min((Dungeon.depth-1) / 5,
				Random.chances(new float[]{1, 2, 4, 8, 16}));
		Class<? extends Geyser> result;

		switch(selection)
		{
			case 0:
				result = Random.oneOf(MirageGeyser.class, EarthenGeyser.class, FetidGeyser.class, MagmaGeyser.class);
				break;
			case 1:
				result = Random.oneOf(FlameGeyser.class, SnowGeyser.class, ToxinGeyser.class, ThunderGeyser.class);
				break;
			case 2:
				result = Random.oneOf(ParalyticGeyser.class, CorrosionGeyser.class, EnticingGeyser.class, SparkGeyser.class);
				break;
			case 3:
				result = Random.oneOf(HallucinationGeyser.class, InfernalGeyser.class, PollutantGeyser.class, EruptionGeyser.class);
				break;
			case 4:
				result = Random.oneOf(BlizzardGeyser.class, LightningGeyser.class, ElectricGeyser.class, MeltdownGeyser.class);
				break;
			default:
				result = MirageGeyser.class;
		}

		try
		{
			return result.newInstance();
		}
		catch(Exception e)
		{
			ChancelPixelDungeon.reportException(e);
			return null;
		}
	}

	public static class Disabled extends FlavourBuff
	{
		{
			geyserCompatable = true;
		}

		public static final float DURATION = 100f;

		@Override
		public int icon()
		{
			return BuffIndicator.SLOW;
		}

		@Override
		public String toString()
		{
			return Messages.get(this, "name");
		}

		@Override
		public String desc()
		{
			return Messages.get(this, "desc", dispTurns());
		}
	}
}
