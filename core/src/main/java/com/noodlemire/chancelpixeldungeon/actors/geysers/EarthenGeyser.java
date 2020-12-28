package com.noodlemire.chancelpixeldungeon.actors.geysers;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.RootCloud;

public class EarthenGeyser extends ManualGeyser
{
	{
		spriteX = 1;
		spriteY = 0;
	}

	@Override
	public Class<? extends Blob> blobClass()
	{
		return RootCloud.class;
	}
}
