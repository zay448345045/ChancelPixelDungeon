package com.noodlemire.chancelpixeldungeon.effects;

import com.noodlemire.chancelpixeldungeon.CPDSettings;
import com.noodlemire.chancelpixeldungeon.tiles.DungeonTilemap;
import com.noodlemire.chancelpixeldungeon.ui.Icons;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class PathIndicator extends Image
{
	public static PathIndicator INSTANCE;

	private Integer target;

	private final ArrayList<PathNode> nodes = new ArrayList<>();

	public PathIndicator(int t)
	{
		super(Icons.PATH_END.get());
		alpha(0.25f);

		unset();
		INSTANCE = this;
		set(t);
	}

	public static void set(int t)
	{
		if(INSTANCE == null)
			return;

		INSTANCE.target = t;
		INSTANCE.visible = CPDSettings.show_destination();

		INSTANCE.point(DungeonTilemap.tileToWorld(INSTANCE.target));
	}

	public static void unset()
	{
		if(INSTANCE == null)
			return;

		for(PathNode node : INSTANCE.nodes)
		{
			node.visible = false;
			node.killAndErase();
		}

		INSTANCE.nodes.clear();
		INSTANCE.visible = false;
		INSTANCE.killAndErase();
		INSTANCE = null;
	}

	public static void createNodePath(Group parent, PathFinder.Path path)
	{
		if(INSTANCE == null || path == null)
			return;

		for(int i = 0; i < path.size() - 1; i++)
		{
			PathNode node = new PathNode(path.get(i));

			parent.addToBack(node);
			INSTANCE.nodes.add(node);

			node.visible = CPDSettings.show_paths();
		}
	}

	public static void follow()
	{
		if(INSTANCE == null)
			return;

		PathNode node = INSTANCE.nodes.remove(0);
		node.visible = false;
		node.killAndErase();

		if(INSTANCE.nodes.isEmpty())
			unset();
	}

	private static class PathNode extends Image
	{
		public PathNode(int pos)
		{
			super(TextureCache.createSolid(0x33FFFFFF));

			origin.set(0.5f);

			point(DungeonTilemap.tileToWorld(pos).offset(
					DungeonTilemap.SIZE / 2,
					DungeonTilemap.SIZE / 2));

			scale.set(DungeonTilemap.SIZE * 0.2f);
		}
	}
}
