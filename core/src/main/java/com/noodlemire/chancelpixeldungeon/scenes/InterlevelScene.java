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

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.GamesInProgress;
import com.noodlemire.chancelpixeldungeon.Statistics;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.items.artifacts.DriedRose;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.features.Chasm;
import com.noodlemire.chancelpixeldungeon.levels.rooms.special.SpecialRoom;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.ui.GameLog;
import com.noodlemire.chancelpixeldungeon.ui.RenderedTextMultiline;
import com.noodlemire.chancelpixeldungeon.windows.WndError;
import com.noodlemire.chancelpixeldungeon.windows.WndStory;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class InterlevelScene extends PixelScene
{
	//slow fade on entering a new region
	private static final float SLOW_FADE = 1f; //.33 in, 1.33 steady, .33 out, 2 seconds total
	//norm fade when loading, falling, returning, or descending to a new floor
	private static final float NORM_FADE = 0.67f; //.33 in, .67 steady, .33 out, 1.33 seconds total
	//fast fade when ascending, or descending to a floor you've been on
	private static final float FAST_FADE = 0.50f; //.33 in, .33 steady, .33 out, 1 second total

	private static final int NUM_TIPS = 52;

	private static float fadeTime;

	public enum Mode
	{
		DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL, RESET, NONE
	}

	public static Mode mode;

	public static int returnDepth;
	public static int returnPos;

	public static boolean noStory = false;

	public static boolean fallIntoPit;

	private enum Phase
	{
		FADE_IN, STATIC, FADE_OUT
	}

	private Phase phase;
	private float timeLeft;

	private RenderedText message;
	private RenderedText continueText;

	private static ArrayList<Integer> tipset;
	private RenderedTextMultiline tip;

	private static Thread thread;
	private static Exception error = null;
	private float waitingTime;
	private boolean touched = true;

	public static final HashSet<Mob> followingEnemies = new HashSet<>();

	private void newTipSet()
	{
		tipset = new ArrayList<>();
		for(int i = 1; i <= NUM_TIPS; i++)
			tipset.add(i);
	}

	@Override
	public void create()
	{
		super.create();

		String loadingAsset;
		int loadingDepth;
		final float scrollSpeed;
		fadeTime = NORM_FADE;
		switch(mode)
		{
			default:
				loadingDepth = Dungeon.depth;
				scrollSpeed = 0;
				break;
			case CONTINUE:
				loadingDepth = GamesInProgress.check(GamesInProgress.curSlot).depth;
				scrollSpeed = 5;
				break;
			case DESCEND:
				if(Dungeon.hero == null)
				{
					loadingDepth = 1;
					fadeTime = SLOW_FADE;
				}
				else
				{
					loadingDepth = Dungeon.depth + 1;
					if(!(Statistics.deepestFloor < loadingDepth))
					{
						fadeTime = FAST_FADE;
					}
					else if(loadingDepth == 6 || loadingDepth == 11
					        || loadingDepth == 16 || loadingDepth == 22)
					{
						fadeTime = SLOW_FADE;
					}
				}
				scrollSpeed = 5;
				break;
			case FALL:
				loadingDepth = Dungeon.depth + 1;
				scrollSpeed = 50;
				break;
			case ASCEND:
				fadeTime = FAST_FADE;
				loadingDepth = Dungeon.depth - 1;
				scrollSpeed = -5;
				break;
			case RETURN:
				loadingDepth = returnDepth;
				scrollSpeed = returnDepth > Dungeon.depth ? 15 : -15;
				break;
		}
		if(loadingDepth <= 5) loadingAsset = Assets.LOADING_SEWERS;
		else if(loadingDepth <= 10) loadingAsset = Assets.LOADING_PRISON;
		else if(loadingDepth <= 15) loadingAsset = Assets.LOADING_CAVES;
		else if(loadingDepth <= 21) loadingAsset = Assets.LOADING_CITY;
		else if(loadingDepth <= 25) loadingAsset = Assets.LOADING_HALLS;
		else loadingAsset = Assets.SHADOW;

		SkinnedBlock bg = new SkinnedBlock(Camera.main.width, Camera.main.height, loadingAsset)
		{
			@Override
			protected NoosaScript script()
			{
				return NoosaScriptNoLighting.get();
			}

			@Override
			public void draw()
			{
				Blending.disable();
				super.draw();
				Blending.enable();
			}

			@Override
			public void update()
			{
				super.update();
				offset(0, Game.elapsed * scrollSpeed);
			}
		};
		bg.scale(4, 4);
		add(bg);

		Image im = new Image(TextureCache.createGradient(0xAA000000, 0xBB000000, 0xCC000000, 0xDD000000, 0xFF000000))
		{
			@Override
			public void update()
			{
				super.update();
				if(phase == Phase.FADE_IN) aa = Math.max(0, (timeLeft - (fadeTime - 0.333f)));
				else if(phase == Phase.FADE_OUT) aa = Math.max(0, (0.333f - timeLeft));
				else aa = 0;
			}
		};
		im.angle = 90;
		im.x = Camera.main.width;
		im.scale.x = Camera.main.height / 5f;
		im.scale.y = Camera.main.width;
		add(im);

		String text = Messages.get(Mode.class, mode.name());

		message = PixelScene.renderText(text, 9);
		message.x = (Camera.main.width - message.width()) / 2;
		message.y = (Camera.main.height - message.height()) / 4;
		align(message);
		add(message);

		continueText = PixelScene.renderText(Messages.get(this, "continue"), 9);
		continueText.x = (Camera.main.width - continueText.width()) / 2;
		continueText.y = message.y + 10;
		align(continueText);
		add(continueText);

		if(tipset == null || tipset.isEmpty())
			newTipSet();

		int tip_i = tipset.remove(Random.Int(tipset.size()));

		tip = PixelScene.renderMultiline(Messages.get(this, "tip_" + tip_i), 9);
		tip.maxWidth((int)Math.round(Camera.main.width * 0.8));
		tip.setPos((Camera.main.width - tip.width()) / 2, (Camera.main.height - tip.height()) / 2);
		align(tip);
		add(tip);

		phase = Phase.FADE_IN;
		timeLeft = fadeTime;

		if(thread == null)
		{
			thread = new Thread()
			{
				@Override
				public void run()
				{
					try
					{
						switch(mode)
						{
							case DESCEND:
								descend();
								break;
							case ASCEND:
								ascend();
								break;
							case CONTINUE:
								restore();
								break;
							case RESURRECT:
								resurrect();
								break;
							case RETURN:
								returnTo();
								break;
							case FALL:
								fall();
								break;
							case RESET:
								reset();
								break;
						}

						if((Dungeon.depth % 5) == 0)
							Sample.INSTANCE.load(Assets.SND_BOSS);
					}
					catch(Exception e)
					{
						error = e;
					}

					if(phase == Phase.STATIC && error == null)
					{
						phase = Phase.FADE_OUT;
						timeLeft = fadeTime;
					}
				}
			};
			thread.start();
		}
		waitingTime = 0f;
		touched = true;
	}

	@Override
	public void update()
	{
		super.update();

		//In order to avoid unnecessary errors being thrown,
		//the game won't include time spent reading a tip as loading time.
		if(touched)
			waitingTime += Game.elapsed;

		float p = timeLeft / fadeTime;

		switch(phase)
		{
			case FADE_IN:
				message.alpha(1 - p);
				continueText.alpha(1 - p);
				tip.alpha(1 - p);

				if((timeLeft -= Game.elapsed) <= 0)
				{
					touched = false;
					phase = Phase.STATIC;

					if(!thread.isAlive() && error == null)
					{
						TouchArea continueArea = new TouchArea(0, 0, Camera.main.width, Camera.main.height)
						{
							@Override
							protected void onClick(Touchscreen.Touch touch)
							{
								touched = true;
								phase = Phase.FADE_OUT;
								timeLeft = fadeTime;
								this.destroy();
							}
						};
						add(continueArea);
					}
				}
				break;

			case FADE_OUT:
				message.alpha(p);
				continueText.alpha(p);
				tip.alpha(p);

				if((timeLeft -= Game.elapsed) <= 0)
				{
					Game.switchScene(GameScene.class);
					thread = null;
					error = null;
				}
				break;

			case STATIC:
				if(error != null)
				{
					String errorMsg;
					if(error instanceof FileNotFoundException)
						errorMsg = Messages.get(this, "file_not_found");
					else if(error instanceof IOException)
						errorMsg = Messages.get(this, "io_error");
					else if(error.getMessage() != null &&
					        error.getMessage().equals("old save"))
						errorMsg = Messages.get(this, "io_error");

					else
						throw new RuntimeException("fatal error occured while moving between floors", error);

					add(new WndError(errorMsg)
					{
						public void onBackPressed()
						{
							super.onBackPressed();
							Game.switchScene(StartScene.class);
						}
					});
					thread = null;
					error = null;
				}
				else if((int) waitingTime == 10)
				{
					waitingTime = 11f;
					String s = "";
					for(StackTraceElement t : thread.getStackTrace())
					{
						s += "\n";
						s += t.toString();
					}
					ChancelPixelDungeon.reportException(
							new RuntimeException("waited more than 10 seconds on levelgen. " +
							                     "Seed:" + Dungeon.seed + " depth:" + Dungeon.depth + " trace:" +
							                     s)
					);
				}
				break;
		}
	}

	private void descend() throws IOException
	{
		Actor.fixTime();

		if(Dungeon.hero == null)
		{
			DriedRose.clearHeldGhostHero();
			Dungeon.init();
			if(noStory)
			{
				Dungeon.chapters.add(WndStory.ID_SEWERS);
				noStory = false;
			}
			GameLog.wipe();
		}
		else
		{
			DriedRose.holdGhostHero(Dungeon.level);
			Dungeon.saveAll();
		}

		Level level;
		if(Dungeon.depth >= Statistics.deepestFloor)
		{
			level = Dungeon.newLevel();
		}
		else
		{
			Dungeon.depth++;
			level = Dungeon.loadLevel(GamesInProgress.curSlot);
		}
		Dungeon.switchLevel(level, level.entrance);
	}

	private void fall() throws IOException
	{
		Actor.fixTime();
		DriedRose.holdGhostHero(Dungeon.level);

		Buff.affect(Dungeon.hero, Chasm.Falling.class);
		Dungeon.saveAll();

		Level level;
		if(Dungeon.depth >= Statistics.deepestFloor)
		{
			level = Dungeon.newLevel();
		}
		else
		{
			Dungeon.depth++;
			level = Dungeon.loadLevel(GamesInProgress.curSlot);
		}
		Dungeon.switchLevel(level, level.fallCell(fallIntoPit));
	}

	private void ascend() throws IOException
	{
		Actor.fixTime();
		DriedRose.holdGhostHero(Dungeon.level);

		Dungeon.saveAll();
		Dungeon.depth--;
		Level level = Dungeon.loadLevel(GamesInProgress.curSlot);
		Dungeon.switchLevel(level, level.exit);
	}

	private void returnTo() throws IOException
	{
		Actor.fixTime();
		DriedRose.holdGhostHero(Dungeon.level);

		Dungeon.saveAll();
		Dungeon.depth = returnDepth;
		Level level = Dungeon.loadLevel(GamesInProgress.curSlot);
		Dungeon.switchLevel(level, returnPos);
	}

	private void restore() throws IOException
	{
		Actor.fixTime();
		DriedRose.clearHeldGhostHero();

		GameLog.wipe();

		Dungeon.loadGame(GamesInProgress.curSlot);
		if(Dungeon.depth == -1)
		{
			Dungeon.depth = Statistics.deepestFloor;
			Dungeon.switchLevel(Dungeon.loadLevel(GamesInProgress.curSlot), -1);
		}
		else
		{
			Level level = Dungeon.loadLevel(GamesInProgress.curSlot);
			Dungeon.switchLevel(level, Dungeon.hero.pos);
		}
	}

	private void resurrect()
	{
		Actor.fixTime();
		DriedRose.holdGhostHero(Dungeon.level);

		if(Dungeon.level.locked)
		{
			Dungeon.hero.resurrect(Dungeon.depth);
			Dungeon.depth--;
			Level level = Dungeon.newLevel();
			Dungeon.switchLevel(level, level.entrance);
		}
		else
		{
			Dungeon.hero.resurrect(-1);
			Dungeon.resetLevel();
		}
	}

	private void reset()
	{
		Actor.fixTime();
		DriedRose.holdGhostHero(Dungeon.level);

		SpecialRoom.resetPitRoom(Dungeon.depth + 1);

		Dungeon.depth--;
		Level level = Dungeon.newLevel();
		Dungeon.switchLevel(level, level.entrance);
	}

	@Override
	protected void onBackPressed()
	{
		//Do nothing
	}
}
