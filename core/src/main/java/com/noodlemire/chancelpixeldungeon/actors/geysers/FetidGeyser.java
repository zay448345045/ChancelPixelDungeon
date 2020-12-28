package com.noodlemire.chancelpixeldungeon.actors.geysers;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.StenchGas;

public class FetidGeyser extends ManualGeyser
{
	{
		spriteX = 2;
		spriteY = 0;
	}

	@Override
	public Class<? extends Blob> blobClass()
	{
		return StenchGas.class;
	}
}
