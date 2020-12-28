package com.noodlemire.chancelpixeldungeon.actors.geysers;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Freezing;

public class SnowGeyser extends ManualGeyser
{
	{
		spriteX = 1;
		spriteY = 1;
	}

	@Override
	public Class<? extends Blob> blobClass()
	{
		return Freezing.class;
	}
}
