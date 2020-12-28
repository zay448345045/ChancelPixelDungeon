package com.noodlemire.chancelpixeldungeon.actors.geysers;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Magma;

public class MagmaGeyser extends ManualGeyser
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
