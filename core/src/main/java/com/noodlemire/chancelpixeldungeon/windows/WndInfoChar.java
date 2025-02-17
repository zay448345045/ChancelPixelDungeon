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

package com.noodlemire.chancelpixeldungeon.windows;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.PixelScene;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.noodlemire.chancelpixeldungeon.ui.HealthBar;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

public class WndInfoChar extends WndTitledMessage
{
	public WndInfoChar(Char ch)
	{
		super(new CharTitle(ch), ch.description());
	}

	private static class CharTitle extends Component
	{
		private static final int GAP = 2;

		private final CharSprite image;
		private final RenderedText name;
		private final HealthBar health;
		private final BuffIndicator buffs;

		public CharTitle(Char ch)
		{
			String chName = ch.name;

			if(ch instanceof Mob)
				chName = chName + " " + Messages.get(ch, "lv", ((Mob) ch).EXP);

			name = PixelScene.renderText(Messages.titleCase(chName), 9);
			name.hardlight(TITLE_COLOR);
			add(name);

			image = ch.sprite();
			add(image);

			health = new HealthBar();
			health.level(ch);
			add(health);

			buffs = new BuffIndicator(ch);
			add(buffs);
		}

		@Override
		protected void layout()
		{

			image.x = 0;
			image.y = Math.max(0, name.height() + health.height() - image.height);

			name.x = image.width + GAP;
			name.y = Math.max(0, image.height - health.height() - name.height());

			float w = width - image.width - GAP;

			health.setRect(image.width + GAP, name.y + name.height(), w, health.height());

			buffs.setPos(
					name.x + name.width() + GAP - 1,
					name.y + name.baseLine() - BuffIndicator.SIZE - 2);

			height = health.bottom();
		}
	}
}
