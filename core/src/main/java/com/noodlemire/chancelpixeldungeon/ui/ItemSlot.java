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

package com.noodlemire.chancelpixeldungeon.ui;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.armor.Armor;
import com.noodlemire.chancelpixeldungeon.items.keys.Key;
import com.noodlemire.chancelpixeldungeon.items.keys.SkeletonKey;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.PixelScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;

public class ItemSlot extends Button
{
	public static final int DEGRADED = 0xFF4444;
	public static final int UPGRADED = 0x44FF44;
	public static final int WARNING = 0xFF8800;
	public static final int BOOSTED = 0x44FFFF;

	private static final float ENABLED = 1.0f;
	private static final float DISABLED = 0.3f;

	protected ItemSprite icon;
	protected Item item;
	protected BitmapText topLeft;
	protected BitmapText topRight;
	protected BitmapText bottomRight;
	protected Image itemIcon;
	protected boolean iconVisible = true;

	private static final String TXT_STRENGTH = ":%d";
	private static final String TXT_TYPICAL_STR = "%d?";
	private static final String TXT_KEY_DEPTH = "\u007F%d";

	private static final String TXT_LEVEL = "%+d";

	// Special "virtual items"
	public static final Item CHEST = new Item()
	{
		public int image()
		{
			return ItemSpriteSheet.CHEST;
		}
	};
	public static final Item LOCKED_CHEST = new Item()
	{
		public int image()
		{
			return ItemSpriteSheet.LOCKED_CHEST;
		}
	};
	public static final Item CRYSTAL_CHEST = new Item()
	{
		public int image()
		{
			return ItemSpriteSheet.CRYSTAL_CHEST;
		}
	};
	public static final Item EBONY_CHEST = new Item()
	{
		public int image()
		{
			return ItemSpriteSheet.EBONY_CHEST;
		}
	};
	public static final Item TOMB = new Item()
	{
		public int image()
		{
			return ItemSpriteSheet.TOMB;
		}
	};
	public static final Item SKELETON = new Item()
	{
		public int image()
		{
			return ItemSpriteSheet.BONES;
		}
	};
	public static final Item REMAINS = new Item()
	{
		public int image()
		{
			return ItemSpriteSheet.REMAINS;
		}
	};

	public ItemSlot()
	{
		super();
		icon.visible(false);
		enable(false);
	}

	public ItemSlot(Item item)
	{
		this();
		item(item);
	}

	@Override
	protected void createChildren()
	{
		super.createChildren();

		icon = new ItemSprite();
		add(icon);

		topLeft = new BitmapText(PixelScene.pixelFont);
		add(topLeft);

		topRight = new BitmapText(PixelScene.pixelFont);
		add(topRight);

		bottomRight = new BitmapText(PixelScene.pixelFont);
		add(bottomRight);
	}

	@Override
	protected void layout()
	{
		super.layout();

		icon.x = x + (width - icon.width) / 2f;
		icon.y = y + (height - icon.height) / 2f;
		PixelScene.align(icon);

		if(topLeft != null)
		{
			topLeft.measure();
			if(topLeft.width > width)
			{
				topLeft.scale.set(PixelScene.align(0.8f));
			}
			else
			{
				topLeft.scale.set(1f);
			}
			topLeft.x = x;
			topLeft.y = y;
			PixelScene.align(topLeft);
		}

		if(topRight != null)
		{
			topRight.x = x + (width - topRight.width());
			topRight.y = y;
			PixelScene.align(topRight);
		}

		if(bottomRight != null)
		{
			bottomRight.x = x + (width - bottomRight.width());
			bottomRight.y = y + (height - bottomRight.height());
			PixelScene.align(bottomRight);
		}

		if (itemIcon != null)
		{
			itemIcon.x = x + width - (ItemSpriteSheet.Icons.SIZE + itemIcon.width())/2f;
			itemIcon.y = y + (ItemSpriteSheet.Icons.SIZE - itemIcon.height())/2f;
			PixelScene.align(itemIcon);
		}
	}

	public void item(Item item)
	{
		if(this.item == item)
		{
			if(item != null)
			{
				icon.frame(item.image());
				icon.glow(item.glowing());
			}
			updateText();
			return;
		}

		this.item = item;

		if(item == null)
		{
			enable(false);
			icon.visible(false);
		}
		else
		{
			enable(true);
			icon.visible(true);

			icon.view(item);
		}

		updateText();
	}

	private void updateText()
	{
		if (itemIcon != null){
			remove(itemIcon);
			itemIcon = null;
		}

		if(item == null)
		{
			topLeft.visible = topRight.visible = bottomRight.visible = false;
			return;
		}
		else
		{
			topLeft.visible = topRight.visible = bottomRight.visible = true;
		}

		topLeft.text(item.status());

		boolean isArmor = item instanceof Armor;
		boolean isWeapon = item instanceof Weapon;

		if (item.icon() != null)
		{
			topRight.text(null);

			itemIcon = new Image(Assets.ITEM_ICONS);
			itemIcon.frame(ItemSpriteSheet.Icons.film.get(item.icon()));
			add(itemIcon);
		}
		else if(isArmor || isWeapon)
		{
			if(item.levelKnown || (isWeapon && !(item instanceof MeleeWeapon)))
			{
				int str = isArmor ? ((Armor) item).STRReq() : ((Weapon) item).STRReq();
				topRight.text(Messages.format(TXT_STRENGTH, str));
				if(str > Dungeon.hero.STR())
					topRight.hardlight(DEGRADED);
				else
					topRight.resetColor();
			}
			else
			{
				topRight.text(Messages.format(TXT_TYPICAL_STR, isArmor ?
						((Armor) item).STRReq(0) :
						((Weapon) item).STRReq(0)));
				topRight.hardlight(WARNING);
			}
			topRight.measure();
		}
		else if(item instanceof Key && !(item instanceof SkeletonKey))
		{
			topRight.text(Messages.format(TXT_KEY_DEPTH, ((Key) item).depth));
			topRight.measure();
		}
		else
			topRight.text(null);

		int level = item.visiblyUpgraded();

		if(level != 0)
		{
			bottomRight.text(Messages.format(TXT_LEVEL, level));
			bottomRight.measure();

			if(item.levelKnown && item.level() > item.rawLevel())
				bottomRight.hardlight(BOOSTED);
			else
				bottomRight.hardlight(UPGRADED);
		}
		else
			bottomRight.text(null);

		layout();
	}

	public void enable(boolean value)
	{
		active = value;

		float alpha = value ? ENABLED : DISABLED;
		icon.alpha(alpha);
		topLeft.alpha(alpha);
		topRight.alpha(alpha);
		bottomRight.alpha(alpha);
		if (itemIcon != null) itemIcon.alpha( alpha );
	}

	public void showParams(boolean TL, boolean TR, boolean BR)
	{
		if(TL) add(topLeft);
		else remove(topLeft);

		if(TR) add(topRight);
		else remove(topRight);

		if(BR) add(bottomRight);
		else remove(bottomRight);
		iconVisible = BR;
	}
}
