package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;

//For more "tangible" debuffs which could be removed and converted into an environmental/aoe effect
public interface Expulsion
{
	//Can either give a blob class to let potion of expulsion spread it, or return null and give a custom effect
	public Class<? extends Blob> expulse();
}
