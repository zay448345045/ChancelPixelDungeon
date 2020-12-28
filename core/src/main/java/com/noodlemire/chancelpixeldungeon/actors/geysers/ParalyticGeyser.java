package com.noodlemire.chancelpixeldungeon.actors.geysers;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ParalyticGas;

public class ParalyticGeyser extends ManualGeyser
{
	{
		spriteX = 0;
		spriteY = 2;
	}

	@Override
	public Class<? extends Blob> blobClass()
	{
		return ParalyticGas.class;
	}
}
