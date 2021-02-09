package com.noodlemire.chancelpixeldungeon.windows;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.hero.Belongings;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.scenes.PixelScene;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.ui.RenderedTextMultiline;
import com.noodlemire.chancelpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;

public class WndLevelUp extends Window
{
	private static final int BTN_SIZE = 31;
	private static final int BTN_GAP = 5;

	public WndLevelUp()
	{
		super();

		int WIDTH = BTN_SIZE * 3 + BTN_GAP * 2;

		RenderedText title = PixelScene.renderText(Messages.get(this, "title"), 9);
		title.x = 0;
		title.y = 0;
		title.hardlight(CharSprite.NEUTRAL);
		add(title);

		RenderedTextMultiline body = PixelScene.renderMultiline(Messages.get(this, "body"), 6);
		body.maxWidth(WIDTH);
		body.setPos(0, 18);
		add(body);

		ChoiceButton HT = new ChoiceButton(new Image(Assets.LEVEL_UP_CHOICES, 0, 0, BTN_SIZE, BTN_SIZE))
		{
			@Override
			protected void onClick()
			{
				Dungeon.hero.HT_lvl++;
				Dungeon.hero.updateHT(true);
				Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(WndLevelUp.class, "plus_ht"));
				hide();
			}
		};
		HT.setRect(0, body.bottom() + BTN_GAP, BTN_SIZE, BTN_SIZE);
		add(HT);

		ChoiceButton STR = new ChoiceButton(new Image(Assets.LEVEL_UP_CHOICES, 31, 0, BTN_SIZE, BTN_SIZE))
		{
			@Override
			protected void onClick()
			{
				Dungeon.hero.STR_lvl++;
				Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(WndLevelUp.class, "plus_str"));
				hide();
			}
		};
		STR.setRect(HT.right() + BTN_GAP, body.bottom() + BTN_GAP, BTN_SIZE, BTN_SIZE);
		add(STR);

		ChoiceButton SLOT = new ChoiceButton(new Image(Assets.LEVEL_UP_CHOICES, 62, 0, BTN_SIZE, BTN_SIZE))
		{
			@Override
			protected void onClick()
			{
				if(Dungeon.hero.slot_lvl < Belongings.MAX_MISC_AMOUNT)
				{
					Dungeon.hero.slot_lvl++;
					Dungeon.hero.belongings.miscSlots.size = Dungeon.hero.slot_lvl;
					Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(WndLevelUp.class, "plus_slot"));
					hide();
				}
				else
					GameScene.show(new WndMessage(Messages.get(WndLevelUp.class, "no_more_slots")));
			}
		};
		SLOT.setRect(STR.right() + BTN_GAP, body.bottom() + BTN_GAP, BTN_SIZE, BTN_SIZE);
		add(SLOT);

		resize((int)SLOT.right(), (int)SLOT.bottom());
	}

	@Override
	public void onBackPressed() {}

	private static class ChoiceButton extends Component
	{
		protected Image background;
		protected Button button;

		public ChoiceButton(Image bg)
		{
			background = bg;
			add(background);
		}

		@Override
		protected void createChildren()
		{
			super.createChildren();

			button = new Button()
			{
				@Override
				protected void onTouchDown()
				{
					background.brightness(1.2f);
					Sample.INSTANCE.play(Assets.SND_CLICK);
				}

				@Override
				protected void onTouchUp()
				{
					background.resetColor();
				}

				@Override
				protected void onClick()
				{
					ChoiceButton.this.onClick();
				}
			};
			add(button);
		}

		protected void onClick() {}

		@Override
		protected void layout()
		{
			super.layout();

			background.x = x;
			background.y = y;
			background.width = width;
			background.height = height;

			button.setRect(x + 2, y + 2, width - 4, height - 4);
		}
	}
}
