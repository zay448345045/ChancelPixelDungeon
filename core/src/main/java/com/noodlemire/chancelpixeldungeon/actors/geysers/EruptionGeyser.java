package com.noodlemire.chancelpixeldungeon.actors.geysers;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Magma;

public class EruptionGeyser extends AutoGeyser
{
	{
		spriteX = 3;
		spriteY = 0;
	}

	@Override
	public Class<? extends Blob> blobClass()
	{
		return Magma.class;
	}
}
