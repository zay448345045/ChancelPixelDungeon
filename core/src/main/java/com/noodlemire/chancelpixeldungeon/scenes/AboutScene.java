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

package com.noodlemire.chancelpixeldungeon.scenes;

import android.content.Intent;
import android.net.Uri;

import com.noodlemire.chancelpixeldungeon.CPDSettings;
import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.effects.Flare;
import com.noodlemire.chancelpixeldungeon.ui.Archs;
import com.noodlemire.chancelpixeldungeon.ui.ExitButton;
import com.noodlemire.chancelpixeldungeon.ui.Icons;
import com.noodlemire.chancelpixeldungeon.ui.RenderedTextMultiline;
import com.noodlemire.chancelpixeldungeon.ui.Window;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.TouchArea;

public class AboutScene extends PixelScene
{
	private static final String TTL_NDMR = "Chancel Pixel Dungeon",
			TXT_NDMR = "Design, Code, & Graphics: Noodlemire",
			LNK_NDMR = "https://discord.gg/RuFGsTHTBH",

	TTL_SHPX = "Shattered Pixel Dungeon",
			TXT_SHPX = "Design, Code, & Graphics: Evan",
			LNK_SHPX = "ShatteredPixel.com",

	TTL_WATA = "Pixel Dungeon",
			TXT_WATA = "Code & Graphics: Watabou\n" +
			           "Music: Cube_Code",
			LNK_WATA = "pixeldungeon.watabou.ru";

	@Override
	public void create()
	{
		super.create();

		final float colWidth = Camera.main.width / (CPDSettings.landscape() ? 3 : 1);
		final float colTop = (Camera.main.height / 3) - (CPDSettings.landscape() ? 30 : 90);
		final float shpxOffset = CPDSettings.landscape() ? colWidth : 0;
		final float wataOffset = CPDSettings.landscape() ? 2 * colWidth : 0;

		Image ndmr = Icons.NDMR.get();
		ndmr.x = (colWidth - ndmr.width()) / 2;
		ndmr.y = CPDSettings.landscape() ? colTop : colTop + 15;
		align(ndmr);
		add(ndmr);

		new Flare(7, 64).color(0x999911, true).show(ndmr, 0).angularSpeed = +20;

		RenderedText ndmrtitle = renderText(TTL_NDMR, 8);
		ndmrtitle.hardlight(Window.TITLE_COLOR);
		add(ndmrtitle);

		ndmrtitle.x = (colWidth - ndmrtitle.width()) / 2;
		ndmrtitle.y = ndmr.y + ndmr.height + 5;
		align(ndmrtitle);

		RenderedTextMultiline ndmrtext = renderMultiline(TXT_NDMR, 8);
		ndmrtext.maxWidth((int) Math.min(colWidth, 120));
		add(ndmrtext);

		ndmrtext.setPos((colWidth - ndmrtext.width()) / 2, ndmrtitle.y + ndmrtitle.height() + 12);
		align(ndmrtext);

		RenderedTextMultiline ndmrlink = renderMultiline(LNK_NDMR, 8);
		ndmrlink.maxWidth(ndmrtext.maxWidth());
		ndmrlink.hardlight(Window.TITLE_COLOR);
		add(ndmrlink);

		ndmrlink.setPos((colWidth - ndmrlink.width()) / 2, ndmrtext.bottom() + 6);
		align(ndmrlink);

		TouchArea ndmrhotArea = new TouchArea(ndmrlink.left(), ndmrlink.top(), ndmrlink.width(), ndmrlink.height())
		{
			@Override
			protected void onClick(Touch touch)
			{
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(LNK_NDMR));
				Game.instance.startActivity(intent);
			}
		};
		add(ndmrhotArea);

		Image shpx = Icons.SHPX.get();
		shpx.x = shpxOffset + (colWidth - shpx.width()) / 2;
		shpx.y = CPDSettings.landscape() ?
				colTop :
				ndmrlink.top() + shpx.height + 5;
		align(shpx);
		add(shpx);

		new Flare(7, 64).color(0x225511, true).show(shpx, 0).angularSpeed = +20;

		RenderedText shpxtitle = renderText(TTL_SHPX, 8);
		shpxtitle.hardlight(Window.SHPX_COLOR);
		add(shpxtitle);

		shpxtitle.x = shpxOffset + (colWidth - shpxtitle.width()) / 2;
		shpxtitle.y = shpx.y + shpx.height + 5;
		align(shpxtitle);

		RenderedTextMultiline shpxtext = renderMultiline(TXT_SHPX, 8);
		shpxtext.maxWidth((int) Math.min(colWidth, 120));
		add(shpxtext);

		shpxtext.setPos(shpxOffset + (colWidth - shpxtext.width()) / 2, shpxtitle.y + shpxtitle.height() + 12);
		align(shpxtext);

		RenderedTextMultiline shpxlink = renderMultiline(LNK_SHPX, 8);
		shpxlink.maxWidth(shpxtext.maxWidth());
		shpxlink.hardlight(Window.SHPX_COLOR);
		add(shpxlink);

		shpxlink.setPos(shpxOffset + (colWidth - shpxlink.width()) / 2, shpxtext.bottom() + 6);
		align(shpxlink);

		TouchArea shpxhotArea = new TouchArea(shpxlink.left(), shpxlink.top(), shpxlink.width(), shpxlink.height())
		{
			@Override
			protected void onClick(Touch touch)
			{
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + LNK_SHPX));
				Game.instance.startActivity(intent);
			}
		};
		add(shpxhotArea);

		Image wata = Icons.WATA.get();
		wata.x = wataOffset + (colWidth - wata.width()) / 2;
		wata.y = CPDSettings.landscape() ?
				colTop :
				shpxlink.top() + wata.height + 5;
		align(wata);
		add(wata);

		new Flare(7, 64).color(0x112233, true).show(wata, 0).angularSpeed = +20;

		RenderedText wataTitle = renderText(TTL_WATA, 8);
		wataTitle.hardlight(Window.TITLE_COLOR);
		add(wataTitle);

		wataTitle.x = wataOffset + (colWidth - wataTitle.width()) / 2;
		wataTitle.y = wata.y + wata.height + 11;
		align(wataTitle);

		RenderedTextMultiline wataText = renderMultiline(TXT_WATA, 8);
		wataText.maxWidth((int) Math.min(colWidth, 120));
		add(wataText);

		wataText.setPos(wataOffset + (colWidth - wataText.width()) / 2, wataTitle.y + wataTitle.height() + 12);
		align(wataText);

		RenderedTextMultiline wataLink = renderMultiline(LNK_WATA, 8);
		wataLink.maxWidth((int) Math.min(colWidth, 120));
		wataLink.hardlight(Window.TITLE_COLOR);
		add(wataLink);

		wataLink.setPos(wataOffset + (colWidth - wataLink.width()) / 2, wataText.bottom() + 6);
		align(wataLink);

		TouchArea hotArea = new TouchArea(wataLink.left(), wataLink.top(), wataLink.width(), wataLink.height())
		{
			@Override
			protected void onClick(Touch touch)
			{
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + LNK_WATA));
				Game.instance.startActivity(intent);
			}
		};
		add(hotArea);


		Archs archs = new Archs();
		archs.setSize(Camera.main.width, Camera.main.height);
		addToBack(archs);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos(Camera.main.width - btnExit.width(), 0);
		add(btnExit);

		fadeIn();
	}

	@Override
	protected void onBackPressed()
	{
		ChancelPixelDungeon.switchNoFade(TitleScene.class);
	}
}
