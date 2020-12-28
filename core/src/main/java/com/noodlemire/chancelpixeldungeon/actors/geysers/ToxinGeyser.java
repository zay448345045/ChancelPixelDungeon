package com.noodlemire.chancelpixeldungeon.actors.geysers;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ToxicGas;

public class ToxinGeyser extends ManualGeyser
{
	{
		spriteX = 2;
		spriteY = 1;
	}

	@Override
	public Class<? extends Blob> blobClass()
	{
		return ToxicGas.class;
	}
}
