package com.noodlemire.chancelpixeldungeon.actors.geysers;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.EnticementGas;

public class EnticingGeyser extends ManualGeyser
{
	{
		spriteX = 2;
		spriteY = 2;
	}

	@Override
	public Class<? extends Blob> blobClass()
	{
		return EnticementGas.class;
	}
}
