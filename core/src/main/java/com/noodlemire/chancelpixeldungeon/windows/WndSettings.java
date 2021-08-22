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

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.CPDSettings;
import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.scenes.PixelScene;
import com.noodlemire.chancelpixeldungeon.ui.CheckBox;
import com.noodlemire.chancelpixeldungeon.ui.OptionSlider;
import com.noodlemire.chancelpixeldungeon.ui.RedButton;
import com.noodlemire.chancelpixeldungeon.ui.Toolbar;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.DeviceCompat;

public class WndSettings extends WndTabbed
{
	private static final int WIDTH = 112;
	private static final int HEIGHT = 138;
	private static final int SLIDER_HEIGHT = 24;
	private static final int BTN_HEIGHT = 18;
	private static final int GAP_TINY = 2;
	private static final int GAP_SML = 6;
	private static final int GAP_LRG = 18;

	private final DisplayTab display;
	private final UITab ui;
	private final AudioTab audio;
	private final ControlsTab controls;

	private static int last_index = 0;

	public WndSettings()
	{
		super();

		display = new DisplayTab();
		add(display);

		ui = new UITab();
		add(ui);

		audio = new AudioTab();
		add(audio);

		controls = new ControlsTab();
		add(controls);

		add(new LabeledTab(Messages.get(this, "display"))
		{
			@Override
			protected void select(boolean value)
			{
				super.select(value);
				display.visible = display.active = value;
				if(value) last_index = 0;
			}
		});

		add(new LabeledTab(Messages.get(this, "ui"))
		{
			@Override
			protected void select(boolean value)
			{
				super.select(value);
				ui.visible = ui.active = value;
				if(value) last_index = 1;
			}
		});

		add(new LabeledTab(Messages.get(this, "audio"))
		{
			@Override
			protected void select(boolean value)
			{
				super.select(value);
				audio.visible = audio.active = value;
				if(value) last_index = 2;
			}
		});

		add(new LabeledTab(Messages.get(this, "controls"))
		{
			@Override
			protected void select(boolean value)
			{
				super.select(value);
				controls.visible = controls.active = value;
				if(value) last_index = 3;
			}
		});

		resize(WIDTH, HEIGHT);

		layoutTabs();

		select(last_index);
	}

	private class DisplayTab extends Group
	{
		public DisplayTab()
		{
			super();

			OptionSlider scale = new OptionSlider(Messages.get(this, "scale"),
					(int) Math.ceil(2 * Game.density) + "X",
					PixelScene.maxDefaultZoom + "X",
					(int) Math.ceil(2 * Game.density),
					PixelScene.maxDefaultZoom)
			{
				@Override
				protected void onChange()
				{
					if(getSelectedValue() != CPDSettings.scale())
					{
						CPDSettings.scale(getSelectedValue());
						ChancelPixelDungeon.switchNoFade((Class<? extends PixelScene>) ChancelPixelDungeon.scene().getClass(), new Game.SceneChangeCallback()
						{
							@Override
							public void beforeCreate()
							{
								//do nothing
							}

							@Override
							public void afterCreate()
							{
								Game.scene().add(new WndSettings());
							}
						});
					}
				}
			};
			if((int) Math.ceil(2 * Game.density) < PixelScene.maxDefaultZoom)
			{
				scale.setSelectedValue(PixelScene.defaultZoom);
				scale.setRect(0, 0, WIDTH, SLIDER_HEIGHT);
				add(scale);
			}

			CheckBox chkSaver = new CheckBox(Messages.get(this, "saver"))
			{
				@Override
				protected void onClick()
				{
					super.onClick();
					if(checked())
					{
						checked(!checked());
						ChancelPixelDungeon.scene().add(new WndOptions(
								Messages.get(DisplayTab.class, "saver"),
								Messages.get(DisplayTab.class, "saver_desc"),
								Messages.get(DisplayTab.class, "okay"),
								Messages.get(DisplayTab.class, "cancel"))
						{
							@Override
							protected void onSelect(int index)
							{
								if(index == 0)
								{
									checked(!checked());
									CPDSettings.powerSaver(checked());
								}
							}
						});
					}
					else
					{
						CPDSettings.powerSaver(checked());
					}
				}
			};
			if(PixelScene.maxScreenZoom >= 2)
			{
				chkSaver.setRect(0, scale.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
				chkSaver.checked(CPDSettings.powerSaver());
				add(chkSaver);
			}

			RedButton btnOrientation = new RedButton(CPDSettings.landscape() ?
					Messages.get(this, "portrait")
					: Messages.get(this, "landscape"))
			{
				@Override
				protected void onClick()
				{
					CPDSettings.landscape(!CPDSettings.landscape());
				}
			};
			btnOrientation.setRect(0, chkSaver.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			add(btnOrientation);


			OptionSlider brightness = new OptionSlider(Messages.get(this, "brightness"),
					Messages.get(this, "dark"), Messages.get(this, "bright"), -2, 2)
			{
				@Override
				protected void onChange()
				{
					CPDSettings.brightness(getSelectedValue());
				}
			};
			brightness.setSelectedValue(CPDSettings.brightness());
			brightness.setRect(0, btnOrientation.bottom() + GAP_LRG, WIDTH, SLIDER_HEIGHT);
			add(brightness);

			OptionSlider tileGrid = new OptionSlider(Messages.get(this, "visual_grid"),
					Messages.get(this, "off"), Messages.get(this, "high"), -1, 3)
			{
				@Override
				protected void onChange()
				{
					CPDSettings.visualGrid(getSelectedValue());
				}
			};
			tileGrid.setSelectedValue(CPDSettings.visualGrid());
			tileGrid.setRect(0, brightness.bottom() + GAP_TINY, WIDTH, SLIDER_HEIGHT);
			add(tileGrid);


		}
	}

	private class UITab extends Group
	{

		public UITab()
		{
			super();

			RenderedText barDesc = PixelScene.renderText(Messages.get(this, "mode"), 9);
			barDesc.x = (WIDTH - barDesc.width()) / 2;
			PixelScene.align(barDesc);
			add(barDesc);

			RedButton btnSplit = new RedButton(Messages.get(this, "split"))
			{
				@Override
				protected void onClick()
				{
					CPDSettings.toolbarMode(Toolbar.Mode.SPLIT.name());
					Toolbar.updateLayout();
				}
			};
			btnSplit.setRect(0, barDesc.y + barDesc.baseLine() + GAP_TINY, 36, 16);
			add(btnSplit);

			RedButton btnGrouped = new RedButton(Messages.get(this, "group"))
			{
				@Override
				protected void onClick()
				{
					CPDSettings.toolbarMode(Toolbar.Mode.GROUP.name());
					Toolbar.updateLayout();
				}
			};
			btnGrouped.setRect(btnSplit.right() + GAP_TINY, barDesc.y + barDesc.baseLine() + GAP_TINY, 36, 16);
			add(btnGrouped);

			RedButton btnCentered = new RedButton(Messages.get(this, "center"))
			{
				@Override
				protected void onClick()
				{
					CPDSettings.toolbarMode(Toolbar.Mode.CENTER.name());
					Toolbar.updateLayout();
				}
			};
			btnCentered.setRect(btnGrouped.right() + GAP_TINY, barDesc.y + barDesc.baseLine() + GAP_TINY, 36, 16);
			add(btnCentered);

			CheckBox chkFlipToolbar = new CheckBox(Messages.get(this, "flip_toolbar"))
			{
				@Override
				protected void onClick()
				{
					super.onClick();
					CPDSettings.flipToolbar(checked());
					Toolbar.updateLayout();
				}
			};
			chkFlipToolbar.setRect(0, btnGrouped.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			chkFlipToolbar.checked(CPDSettings.flipToolbar());
			add(chkFlipToolbar);

			final CheckBox chkFlipTags = new CheckBox(Messages.get(this, "flip_indicators"))
			{
				@Override
				protected void onClick()
				{
					super.onClick();
					CPDSettings.flipTags(checked());
					GameScene.layoutTags();
				}
			};
			chkFlipTags.setRect(0, chkFlipToolbar.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			chkFlipTags.checked(CPDSettings.flipTags());
			add(chkFlipTags);

			OptionSlider slots = new OptionSlider(Messages.get(this, "quickslots"), "0", "4", 0, 4)
			{
				@Override
				protected void onChange()
				{
					CPDSettings.quickSlots(getSelectedValue());
					Toolbar.updateLayout();
				}
			};
			slots.setSelectedValue(CPDSettings.quickSlots());
			slots.setRect(0, chkFlipTags.bottom() + GAP_TINY, WIDTH, SLIDER_HEIGHT);
			add(slots);

			CheckBox chkImmersive = new CheckBox(Messages.get(this, "nav_bar"))
			{
				@Override
				protected void onClick()
				{
					super.onClick();
					CPDSettings.fullscreen(checked());
				}
			};
			chkImmersive.setRect(0, slots.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
			chkImmersive.checked(CPDSettings.fullscreen());
			chkImmersive.enable(DeviceCompat.supportsFullScreen());
			add(chkImmersive);

			CheckBox chkFont = new CheckBox(Messages.get(this, "system_font"))
			{
				@Override
				protected void onClick()
				{
					super.onClick();
					ChancelPixelDungeon.switchNoFade((Class<? extends PixelScene>) ChancelPixelDungeon.scene().getClass(), new Game.SceneChangeCallback()
					{
						@Override
						public void beforeCreate()
						{
							CPDSettings.systemFont(checked());
						}

						@Override
						public void afterCreate()
						{
							Game.scene().add(new WndSettings());
						}
					});
				}
			};
			chkFont.setRect(0, chkImmersive.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			chkFont.checked(CPDSettings.systemFont());
			add(chkFont);
		}

	}

	private class AudioTab extends Group
	{
		public AudioTab()
		{
			OptionSlider musicVol = new OptionSlider(Messages.get(this, "music_vol"), "0", "10", 0, 10)
			{
				@Override
				protected void onChange()
				{
					CPDSettings.musicVol(getSelectedValue());
				}
			};
			musicVol.setSelectedValue(CPDSettings.musicVol());
			musicVol.setRect(0, 0, WIDTH, SLIDER_HEIGHT);
			add(musicVol);

			CheckBox musicMute = new CheckBox(Messages.get(this, "music_mute"))
			{
				@Override
				protected void onClick()
				{
					super.onClick();
					CPDSettings.music(!checked());
				}
			};
			musicMute.setRect(0, musicVol.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			musicMute.checked(!CPDSettings.music());
			add(musicMute);


			OptionSlider SFXVol = new OptionSlider(Messages.get(this, "sfx_vol"), "0", "10", 0, 10)
			{
				@Override
				protected void onChange()
				{
					CPDSettings.SFXVol(getSelectedValue());
				}
			};
			SFXVol.setSelectedValue(CPDSettings.SFXVol());
			SFXVol.setRect(0, musicMute.bottom() + GAP_LRG, WIDTH, SLIDER_HEIGHT);
			add(SFXVol);

			CheckBox btnSound = new CheckBox(Messages.get(this, "sfx_mute"))
			{
				@Override
				protected void onClick()
				{
					super.onClick();
					CPDSettings.soundFx(!checked());
					Sample.INSTANCE.play(Assets.SND_CLICK);
				}
			};
			btnSound.setRect(0, SFXVol.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			btnSound.checked(!CPDSettings.soundFx());
			add(btnSound);

			resize(WIDTH, (int) btnSound.bottom());
		}
	}

	private class ControlsTab extends Group
	{
		public ControlsTab()
		{
			CheckBox avoidBlobs = new CheckBox(Messages.get(this, "avoid_blobs"))
			{
				@Override
				protected void onClick()
				{
					super.onClick();
					CPDSettings.avoid_blobs(checked());
				}
			};
			avoidBlobs.setRect(0, 0, WIDTH, BTN_HEIGHT);
			avoidBlobs.checked(CPDSettings.avoid_blobs());
			add(avoidBlobs);

			CheckBox showPaths = new CheckBox(Messages.get(this, "show_paths"))
			{
				@Override
				protected void onClick()
				{
					super.onClick();
					CPDSettings.show_paths(checked());
				}
			};
			showPaths.setRect(0, avoidBlobs.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			showPaths.checked(CPDSettings.show_paths());
			add(showPaths);

			CheckBox showDest = new CheckBox(Messages.get(this, "show_dest"))
			{
				@Override
				protected void onClick()
				{
					super.onClick();
					CPDSettings.show_destination(checked());
				}
			};
			showDest.setRect(0, showPaths.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			showDest.checked(CPDSettings.show_destination());
			add(showDest);
		}
	}
}
