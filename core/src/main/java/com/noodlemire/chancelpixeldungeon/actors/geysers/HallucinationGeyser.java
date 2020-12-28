package com.noodlemire.chancelpixeldungeon.actors.geysers;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ConfusionGas;

public class HallucinationGeyser extends AutoGeyser
{
	{
		spriteX = 0;
		spriteY = 0;
	}

	@Override
	public Class<? extends Blob> blobClass()
	{
		return ConfusionGas.class;
	}
}
