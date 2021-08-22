/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.noodlemire.chancelpixeldungeon;

import com.watabou.glwrap.Quad;
import com.watabou.glwrap.Vertexbuffer;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.NoosaScript;

import java.util.ArrayList;

public class Chrome
{
	public enum Type
	{
		TOAST,
		TOAST_TR,
		WINDOW,
		BUTTON,
		TAG,
		GEM,
		SCROLL,
		TAB_SET,
		TAB_SELECTED,
		TAB_UNSELECTED
	}

	public static NinePatch get(Type type)
	{
		String Asset = Assets.CHROME;
		switch(type)
		{
			case WINDOW:
				return new NinePatchRepeating(Asset, 0, 0, 20, 20, 7);
			case TOAST:
				return new NinePatchRepeating(Asset, 22, 0, 18, 18, 6);
			case TOAST_TR:
				return new NinePatchRepeating(Asset, 40, 0, 18, 18, 6);
			case BUTTON:
				return new NinePatch(Asset, 58, 0, 6, 6, 2);
			case TAG:
				return new NinePatch(Asset, 22, 18, 16, 14, 3);
			case GEM:
				return new NinePatch(Asset, 0, 32, 32, 32, 13);
			case SCROLL:
				return new NinePatch(Asset, 32, 32, 32, 32, 5, 11, 5, 11);
			case TAB_SET:
				return new NinePatchRepeating(Asset, 64, 0, 20, 20, 7);
			case TAB_SELECTED:
				return new NinePatchRepeating(Asset, 64, 23, 18, 16, 6, 5, 6, 5);
			case TAB_UNSELECTED:
				return new NinePatchRepeating(Asset, 82, 23, 18, 19, 6, 8, 6, 5);
			default:
				return null;
		}
	}

	static class NinePatchRepeating extends NinePatch
	{
		private static final int REPEAT_OFFSET = 6;

		ArrayList<Float> quadsList;

		NinePatchRepeating(Object tx, int x, int y, int w, int h, int margin)
		{
			super(tx, x, y, w, h, margin, margin, margin, margin);
		}

		NinePatchRepeating(Object tx, int x, int y, int w, int h, int left, int top, int right, int bottom)
		{
			super(tx, x, y, w, h, left, top, right, bottom);
		}

		@Override
		protected void updateVertices()
		{
			quadsList = new ArrayList<>();

			quads.position(0);

			float right = width - marginRight;
			float bottom = height - marginBottom;

			float outleft = flipHorizontal ? outterF.right : outterF.left;
			float outright = flipHorizontal ? outterF.left : outterF.right;
			float outtop = flipVertical ? outterF.bottom : outterF.top;
			float outbottom = flipVertical ? outterF.top : outterF.bottom;

			float inleft = flipHorizontal ? innerF.right : innerF.left;
			float inright = flipHorizontal ? innerF.left : innerF.right;
			float intop = flipVertical ? innerF.bottom : innerF.top;
			float inbottom = flipVertical ? innerF.top : innerF.bottom;

			Quad.fill(vertices, //upper left corner
					0, marginLeft, 0, marginTop, outleft, inleft, outtop, intop);
			fillFloats(quadsList, vertices);
			for(int i = marginLeft; i < right; i += REPEAT_OFFSET) //top
			{
				Quad.fill(vertices,
						i, i + REPEAT_OFFSET, 0, marginTop, inleft, inright, outtop, intop);
				fillFloats(quadsList, vertices);
			}
			Quad.fill(vertices, //upper right corner
					right, width, 0, marginTop, inright, outright, outtop, intop);
			fillFloats(quadsList, vertices);

			for(int i = marginTop; i < bottom; i += REPEAT_OFFSET) //left
			{
				Quad.fill(vertices,
						0, marginLeft, i, i + REPEAT_OFFSET, outleft, inleft, intop, inbottom);
				fillFloats(quadsList, vertices);
			}
			Quad.fill(vertices, //center
					marginLeft, right, marginTop, bottom, inleft, inright, intop, inbottom);
			fillFloats(quadsList, vertices);
			for(int i = marginTop; i < bottom; i += REPEAT_OFFSET) //right
			{
				Quad.fill(vertices,
						right, width, i, i + REPEAT_OFFSET, inright, outright, intop, inbottom);
				fillFloats(quadsList, vertices);
			}

			Quad.fill(vertices, //bottom left corner
					0, marginLeft, bottom, height, outleft, inleft, inbottom, outbottom);
			fillFloats(quadsList, vertices);
			for(int i = marginLeft; i < right; i += REPEAT_OFFSET) //bottom
			{
				Quad.fill(vertices,
						i, i + REPEAT_OFFSET, bottom, height, inleft, inright, inbottom, outbottom);
				fillFloats(quadsList, vertices);
			}
			Quad.fill(vertices, //bottom right corner
					right, width, bottom, height, inright, outright, inbottom, outbottom);
			fillFloats(quadsList, vertices);

			quads = Quad.createSet(quadsList.size());
			for(Float f : quadsList)
				quads.put(f);

			dirty = true;
		}

		@Override
		public void draw()
		{
			super.draw();

			if(dirty)
			{
				if(buffer == null)
					buffer = new Vertexbuffer(quads);
				else
					buffer.updateVertices(quads);
				dirty = false;
			}

			NoosaScript script = NoosaScript.get();

			texture.bind();

			script.camera(camera());

			script.uModel.valueM4(matrix);
			script.lighting(
					rm, gm, bm, am,
					ra, ga, ba, aa);

			script.drawQuadSet(buffer, quadsList.size(), 0);
		}

		private void fillFloats(ArrayList<Float> al, float[] floats)
		{
			for(float f : floats)
				al.add(f);
		}
	}
}
