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

import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Amok;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Awareness;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Challenged;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Light;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MindVision;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Bestiary;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Mob;
import com.noodlemire.chancelpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.noodlemire.chancelpixeldungeon.actors.mobs.npcs.Ghost;
import com.noodlemire.chancelpixeldungeon.actors.mobs.npcs.Imp;
import com.noodlemire.chancelpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.noodlemire.chancelpixeldungeon.items.Ankh;
import com.noodlemire.chancelpixeldungeon.items.Generator;
import com.noodlemire.chancelpixeldungeon.items.Heap;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.artifacts.DriedRose;
import com.noodlemire.chancelpixeldungeon.items.potions.Potion;
import com.noodlemire.chancelpixeldungeon.items.rings.Ring;
import com.noodlemire.chancelpixeldungeon.items.scrolls.Scroll;
import com.noodlemire.chancelpixeldungeon.journal.Notes;
import com.noodlemire.chancelpixeldungeon.levels.CavesBossLevel;
import com.noodlemire.chancelpixeldungeon.levels.CavesLevel;
import com.noodlemire.chancelpixeldungeon.levels.CityBossLevel;
import com.noodlemire.chancelpixeldungeon.levels.CityLevel;
import com.noodlemire.chancelpixeldungeon.levels.DeadEndLevel;
import com.noodlemire.chancelpixeldungeon.levels.HallsBossLevel;
import com.noodlemire.chancelpixeldungeon.levels.HallsLevel;
import com.noodlemire.chancelpixeldungeon.levels.LastLevel;
import com.noodlemire.chancelpixeldungeon.levels.LastShopLevel;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.PrisonBossLevel;
import com.noodlemire.chancelpixeldungeon.levels.PrisonLevel;
import com.noodlemire.chancelpixeldungeon.levels.SewerBossLevel;
import com.noodlemire.chancelpixeldungeon.levels.SewerLevel;
import com.noodlemire.chancelpixeldungeon.levels.rooms.secret.SecretRoom;
import com.noodlemire.chancelpixeldungeon.levels.rooms.special.SpecialRoom;
import com.noodlemire.chancelpixeldungeon.mechanics.ShadowCaster;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.scenes.InterlevelScene;
import com.noodlemire.chancelpixeldungeon.ui.QuickSlotButton;
import com.noodlemire.chancelpixeldungeon.utils.BArray;
import com.noodlemire.chancelpixeldungeon.utils.DungeonSeed;
import com.noodlemire.chancelpixeldungeon.windows.WndAlchemy;
import com.noodlemire.chancelpixeldungeon.windows.WndResurrect;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.FileUtils;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Dungeon
{
	//enum of items which have limited spawns, records how many have spawned
	//could all be their own separate numbers, but this allows iterating, much nicer for bundling/initializing.
	public enum LimitedDrops
	{
		//limited world drops
		UPGRADE_SCROLLS,
		BLESSING_SCROLLS,
		ARCANE_STYLI,

		//Health potion sources
		//enemies
		SWARM_HP,
		GUARD_HP,
		BAT_HP,
		WARLOCK_HP,
		SCORPIO_HP,
		//alchemy
		COOKING_HP,
		BLANDFRUIT_SEED,

		//Mystery meat
		CRAB_MEAT,
		SPINNER_MEAT,
		SCORPIO_MEAT,

		//doesn't use Generator, so we have to enforce one armband drop here
		THIEVES_ARMBAND,

		//containers
		DEW_VIAL,
		VELVET_POUCH,
		SCROLL_HOLDER,
		POTION_BANDOLIER,
		MAGICAL_HOLSTER,

		MISC_LEVELS,

		//One rare mob per chapter
		RARE_MOB;

		public int count = 0;

		//for items which can only be dropped once, should directly access count otherwise.
		public boolean dropped()
		{
			return count != 0;
		}

		public void drop()
		{
			count = 1;
		}

		public static void reset()
		{
			for(LimitedDrops lim : values())
				lim.count = 0;
		}

		public static void store(Bundle bundle)
		{
			for(LimitedDrops lim : values())
				bundle.put(lim.name(), lim.count);
		}

		public static void restore(Bundle bundle)
		{
			for(LimitedDrops lim : values())
				if(bundle.contains(lim.name()))
					lim.count = bundle.getInt(lim.name());
				else
					lim.count = 0;
		}
	}

	public static int challenges;

	public static Hero hero;
	public static Level level;

	public static QuickSlot quickslot = new QuickSlot();

	public static int depth;
	public static int gold;

	public static HashSet<Integer> chapters;

	public static SparseArray<ArrayList<Item>> droppedItems;

	public static int version;

	public static long seed;

	public static void init()
	{
		version = Game.versionCode;
		challenges = CPDSettings.challenges();

		seed = DungeonSeed.randomSeed();

		Actor.clear();
		Actor.resetNextID();

		Random.seed(seed);

		Scroll.initLabels();
		Potion.initColors();
		Ring.initGems();

		SpecialRoom.initForRun();
		SecretRoom.initForRun();

		Random.seed();

		Statistics.reset();
		Notes.reset();

		quickslot.reset();
		QuickSlotButton.reset();

		depth = 0;
		gold = 300;

		droppedItems = new SparseArray<>();

		LimitedDrops.reset();

		Bestiary.reset();

		chapters = new HashSet<>();

		Ghost.Quest.reset();
		Wandmaker.Quest.reset();
		Blacksmith.Quest.reset();
		Imp.Quest.reset();

		Generator.reset();
		Generator.initArtifacts();
		hero = new Hero();
		hero.live();

		Badges.reset();

		GamesInProgress.selectedClass.initHero(hero);
	}

	public static boolean isChallenged(int mask)
	{
		return (challenges & mask) != 0;
	}

	public static Level newLevel()
	{
		Dungeon.level = null;
		Actor.clear();

		depth++;
		if(depth > Statistics.deepestFloor)
		{
			Statistics.deepestFloor = depth;

			Statistics.completedWithNoKilling = Statistics.qualifiedForNoKilling;
		}

		Level level;
		switch(depth)
		{
			case 1:
			case 2:
			case 3:
			case 4:
				level = new SewerLevel();
				break;
			case 5:
				level = new SewerBossLevel();
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				level = new PrisonLevel();
				break;
			case 10:
				level = new PrisonBossLevel();
				break;
			case 11:
			case 12:
			case 13:
			case 14:
				level = new CavesLevel();
				break;
			case 15:
				level = new CavesBossLevel();
				break;
			case 16:
			case 17:
			case 18:
			case 19:
				level = new CityLevel();
				break;
			case 20:
				level = new CityBossLevel();
				break;
			case 21:
				level = new LastShopLevel();
				break;
			case 22:
			case 23:
			case 24:
				level = new HallsLevel();
				break;
			case 25:
				level = new HallsBossLevel();
				break;
			case 26:
				level = new LastLevel();
				break;
			default:
				level = new DeadEndLevel();
				Statistics.deepestFloor--;
		}

		level.create();

		Statistics.qualifiedForNoKilling = !bossLevel();

		return level;
	}

	public static void resetLevel()
	{
		Actor.clear();

		level.reset();
		switchLevel(level, level.entrance);
	}

	public static long seedCurDepth()
	{
		return seedForDepth(depth);
	}

	public static long seedForDepth(int depth)
	{
		Random.seed(seed);
		for(int i = 0; i < depth; i++)
			Random.Long(); //we don't care about these values, just need to go through them
		long result = Random.Long();
		Random.seed();
		return result;
	}

	public static boolean shopOnLevel()
	{
		return depth == 1 || depth == 6 || depth == 11 || depth == 16;
	}

	public static boolean bossLevel()
	{
		return bossLevel(depth);
	}

	public static boolean bossLevel(int depth)
	{
		return depth == 5 || depth == 10 || depth == 15 || depth == 20 || depth == 25;
	}

	private static Char checkAt(int pos)
	{
		if (hero.pos == pos)
			return hero;

		for (Mob mob : level.mobs)
			if (mob.pos == pos)
				return mob;

		return null;
	}

	private static void bounce(Char ch, int pos)
	{
		bounce(ch, pos, 2);
	}

	private static void bounce(final Char ch, int pos, final int i)
	{
		int mpos;

		do
		{
			mpos = pos + PathFinder.NEIGHBOURS8[Random.Int(PathFinder.NEIGHBOURS8.length)];
		}
		while (level.solid[mpos] || level.avoid[mpos]);

		final int fmpos = mpos;

		ch.sprite.jump(pos, fmpos, 10, 1f/i, new Callback()
		{
			@Override
			public void call()
			{
				Char other = checkAt(fmpos);

				if(other == null)
				{
					ch.pos = fmpos;
					level.press(fmpos, ch);
				}
				else
				{
					bounce(ch, fmpos, i+1);
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	public static void switchLevel(final Level level, int pos)
	{
		if(pos < 0 || pos >= level.length())
			pos = level.exit;

		PathFinder.setMapSize(level.width(), level.height());

		Dungeon.level = level;
		DriedRose.restoreGhostHero(level, pos);

		hero.pos = pos;

		for(Mob m : InterlevelScene.followingEnemies)
		{
			final Char prev = checkAt(pos);

			m.pos = pos;
			level.mobs.add(m);

			if(prev != null)
			{
				//bounce(prev, pos);
			}
		}

		Actor.init();

		Actor respawner = level.respawner();
		if(respawner != null)
		{
			Actor.addDelayed(respawner, level.respawnTime());
		}

		Light light = hero.buff(Light.class);
		hero.viewDistance = light == null ? level.viewDistance : Math.max(Light.DISTANCE, level.viewDistance);

		hero.curAction = hero.lastAction = null;

		observe();

		if(!InterlevelScene.followingEnemies.isEmpty())
		{
			Actor.add(new Actor()
			{
				@Override
				protected boolean act()
				{
					int stairpos = hero.pos;

					bounce(hero, stairpos);

					int i = 1; //This counter is used so that the last enemy in a stack won't be bounced away.
					for(Mob m : InterlevelScene.followingEnemies)
					{
						if(i < InterlevelScene.followingEnemies.size())
						{
							i++;

							bounce(m, stairpos);
						}
					}

					InterlevelScene.followingEnemies.clear();

					deactivate();
					return true;
				}
			});

			hero.spend(Actor.TICK);

			for(Mob m : InterlevelScene.followingEnemies)
				m.spend(Actor.TICK);
		}

		try
		{
			saveAll();
		}
		catch(IOException e)
		{
			ChancelPixelDungeon.reportException(e);
			/*This only catches IO errors. Yes, this means things can go wrong, and they can go wrong catastrophically.
			But when they do the user will get a nice 'report this issue' dialogue, and I can fix the bug.*/
		}
	}

	public static void dropToChasm(Item item)
	{
		int depth = Dungeon.depth + 1;
		ArrayList<Item> dropped = Dungeon.droppedItems.get(depth);
		if(dropped == null)
		{
			Dungeon.droppedItems.put(depth, dropped = new ArrayList<Item>());
		}
		dropped.add(item);
	}

	public static boolean souNeeded()
	{
		int souLeftThisSet;
		//3 SOU each chapter, 1.5 (rounded) on forbidden runes challenge
		if(isChallenged(Challenges.NO_SCROLLS))
			souLeftThisSet = Math.round(1.5f - (LimitedDrops.UPGRADE_SCROLLS.count - (depth / 5) * 1.5f));
		else
			souLeftThisSet = 3 - (LimitedDrops.UPGRADE_SCROLLS.count - (depth / 5) * 3);
		if(souLeftThisSet <= 0)
			return false;

		int floorThisSet = depth % 5;
		//chance is floors left / scrolls left
		return Random.Int(5 - floorThisSet) < souLeftThisSet;
	}

	public static boolean sobNeeded()
	{
		//1 SOB per chapter
		int sobLeftThisChapter = 1 - (LimitedDrops.BLESSING_SCROLLS.count - depth / 5);
		int floorThisChapter = depth % 5;
		//chance is floors left / scrolls left
		return Random.Int(5 - floorThisChapter) < sobLeftThisChapter;
	}

	public static boolean asNeeded()
	{
		//1 AS each chapter
		int asLeftThisSet = 1 - (LimitedDrops.ARCANE_STYLI.count - depth / 5);
		int floorThisSet = (depth % 5);
		//chance is floors left / stylic left
		return Random.Int(5 - floorThisSet) < asLeftThisSet;
	}

	private static final String VERSION = "version";
	private static final String SEED = "seed";
	private static final String CHALLENGES = "challenges";
	private static final String HERO = "hero";
	private static final String GOLD = "gold";
	private static final String DEPTH = "depth";
	private static final String DROPPED = "dropped%d";
	private static final String LEVEL = "level";
	private static final String LIMDROPS = "limited_drops";
	private static final String BESTIARY = "bestiary";
	private static final String CHAPTERS = "chapters";
	private static final String QUESTS = "quests";
	private static final String BADGES = "badges";

	private static void saveGame(int save)
	{
		try
		{
			Bundle bundle = new Bundle();

			version = Game.versionCode;
			bundle.put(VERSION, version);
			bundle.put(SEED, seed);
			bundle.put(CHALLENGES, challenges);
			bundle.put(HERO, hero);
			bundle.put(GOLD, gold);
			bundle.put(DEPTH, depth);

			for(int d : droppedItems.keyArray())
				bundle.put(Messages.format(DROPPED, d), droppedItems.get(d));

			quickslot.storePlaceholders(bundle);

			Bundle limDrops = new Bundle();
			LimitedDrops.store(limDrops);
			bundle.put(LIMDROPS, limDrops);

			Bundle bestiary = new Bundle();
			Bestiary.store(bestiary);
			bundle.put(BESTIARY, bestiary);

			int count = 0;
			int[] ids = new int[chapters.size()];
			for(Integer id : chapters)
				ids[count++] = id;
			bundle.put(CHAPTERS, ids);

			Bundle quests = new Bundle();
			Ghost.Quest.storeInBundle(quests);
			Wandmaker.Quest.storeInBundle(quests);
			Blacksmith.Quest.storeInBundle(quests);
			Imp.Quest.storeInBundle(quests);
			bundle.put(QUESTS, quests);

			SpecialRoom.storeRoomsInBundle(bundle);
			SecretRoom.storeRoomsInBundle(bundle);

			WndAlchemy.storeInBundle(bundle);

			Statistics.storeInBundle(bundle);
			Notes.storeInBundle(bundle);
			Generator.storeInBundle(bundle);

			Scroll.save(bundle);
			Potion.save(bundle);
			Ring.save(bundle);

			Actor.storeNextID(bundle);

			Bundle badges = new Bundle();
			Badges.saveLocal(badges);
			bundle.put(BADGES, badges);

			FileUtils.bundleToFile(GamesInProgress.gameFile(save), bundle);
		}
		catch(IOException e)
		{
			GamesInProgress.setUnknown(save);
			ChancelPixelDungeon.reportException(e);
		}
	}

	private static void saveLevel(int save) throws IOException
	{
		Bundle bundle = new Bundle();
		bundle.put(LEVEL, level);

		FileUtils.bundleToFile(GamesInProgress.depthFile(save, depth), bundle);
	}

	public static void saveAll() throws IOException
	{
		if(hero != null && hero.isAlive())
		{
			Actor.fixTime();
			saveGame(GamesInProgress.curSlot);
			saveLevel(GamesInProgress.curSlot);

			GamesInProgress.set(GamesInProgress.curSlot, depth, challenges, hero);
		}
		else if(WndResurrect.instance != null)
		{
			WndResurrect.instance.hide();
			Hero.reallyDie(WndResurrect.causeOfDeath);
		}
	}

	public static void loadGame(int save) throws IOException
	{
		loadGame(save, true);
	}

	private static void loadGame(int save, boolean fullLoad) throws IOException
	{
		Bundle bundle = FileUtils.bundleFromFile(GamesInProgress.gameFile(save));

		version = bundle.getInt(VERSION);

		seed = bundle.contains(SEED) ? bundle.getLong(SEED) : DungeonSeed.randomSeed();

		Actor.restoreNextID(bundle);

		quickslot.reset();
		QuickSlotButton.reset();

		Dungeon.challenges = bundle.getInt(CHALLENGES);

		Dungeon.level = null;
		Dungeon.depth = -1;

		Scroll.restore(bundle);
		Potion.restore(bundle);
		Ring.restore(bundle);

		quickslot.restorePlaceholders(bundle);

		if(fullLoad)
		{
			LimitedDrops.restore(bundle.getBundle(LIMDROPS));
			Bestiary.restore(bundle.getBundle(BESTIARY));

			chapters = new HashSet<Integer>();
			int[] ids = bundle.getIntArray(CHAPTERS);
			if(ids != null)
				for(int id : ids)
					chapters.add(id);

			Bundle quests = bundle.getBundle(QUESTS);
			if(!quests.isNull())
			{
				Ghost.Quest.restoreFromBundle(quests);
				Wandmaker.Quest.restoreFromBundle(quests);
				Blacksmith.Quest.restoreFromBundle(quests);
				Imp.Quest.restoreFromBundle(quests);
			}
			else
			{
				Ghost.Quest.reset();
				Wandmaker.Quest.reset();
				Blacksmith.Quest.reset();
				Imp.Quest.reset();
			}

			SpecialRoom.restoreRoomsFromBundle(bundle);
			SecretRoom.restoreRoomsFromBundle(bundle);
		}

		Bundle badges = bundle.getBundle(BADGES);
		if(!badges.isNull())
			Badges.loadLocal(badges);
		else
			Badges.reset();

		Notes.restoreFromBundle(bundle);

		hero = null;
		hero = (Hero) bundle.get(HERO);

		WndAlchemy.restoreFromBundle(bundle, hero);

		gold = bundle.getInt(GOLD);
		depth = bundle.getInt(DEPTH);

		Statistics.restoreFromBundle(bundle);
		Generator.restoreFromBundle(bundle);

		droppedItems = new SparseArray<>();
		for(int i = 2; i <= Statistics.deepestFloor + 1; i++)
		{
			ArrayList<Item> dropped = new ArrayList<Item>();
			if(bundle.contains(Messages.format(DROPPED, i)))
				for(Bundlable b : bundle.getCollection(Messages.format(DROPPED, i)))
					dropped.add((Item) b);
			if(!dropped.isEmpty())
				droppedItems.put(i, dropped);
		}
	}

	public static Level loadLevel(int save) throws IOException
	{
		Dungeon.level = null;
		Actor.clear();

		Bundle bundle = FileUtils.bundleFromFile(GamesInProgress.depthFile(save, depth));

		Level level = (Level) bundle.get(LEVEL);

		if(level == null)
			throw new IOException();
		else
			return level;
	}

	public static void deleteGame(int save, boolean deleteLevels)
	{
		FileUtils.deleteFile(GamesInProgress.gameFile(save));

		if(deleteLevels)
			FileUtils.deleteDir(GamesInProgress.gameFolder(save));

		GamesInProgress.delete(save);
	}

	public static void preview(GamesInProgress.Info info, Bundle bundle)
	{
		info.depth = bundle.getInt(DEPTH);
		info.version = bundle.getInt(VERSION);
		info.challenges = bundle.getInt(CHALLENGES);
		Hero.preview(info, bundle.getBundle(HERO));
		Statistics.preview(info, bundle);
	}

	public static void fail(Class cause)
	{
		if(hero.belongings.getItem(Ankh.class) == null)
			Rankings.INSTANCE.submit(false, cause);
	}

	public static void win(Class cause)
	{
		hero.belongings.identify();

		int chCount = 0;
		for(int ch : Challenges.MASKS)
			if((challenges & ch) != 0) chCount++;

		if(chCount != 0)
			Badges.validateChampion(chCount);

		Rankings.INSTANCE.submit(true, cause);
	}

	public static void observe()
	{
		observe(ShadowCaster.MAX_DISTANCE + 1);
	}

	public static void observe(int dist)
	{
		if(level == null)
			return;

		level.updateFieldOfView(hero, level.heroFOV);

		int x = hero.pos % level.width();
		int y = hero.pos / level.width();

		//left, right, top, bottom
		int l = Math.max(0, x - dist);
		int r = Math.min(x + dist, level.width() - 1);
		int t = Math.max(0, y - dist);
		int b = Math.min(y + dist, level.height() - 1);

		int width = r - l + 1;
		int height = b - t + 1;

		int pos = l + t * level.width();

		for(int i = t; i <= b; i++)
		{
			BArray.or(level.visited, level.heroFOV, pos, width, level.visited);
			pos += level.width();
		}

		GameScene.updateFog(l, t, width, height);

		if(hero.buff(MindVision.class) != null)
		{
			for(Mob m : level.mobs.toArray(new Mob[0]))
			{
				BArray.or(level.visited, level.heroFOV, m.pos - 1 - level.width(), 3, level.visited);
				BArray.or(level.visited, level.heroFOV, m.pos, 3, level.visited);
				BArray.or(level.visited, level.heroFOV, m.pos - 1 + level.width(), 3, level.visited);
				//updates adjacent cells too
				GameScene.updateFog(m.pos, 2);
			}
		}

		if(hero.buff(Awareness.class) != null)
		{
			for(Heap h : level.heaps.values())
			{
				BArray.or(level.visited, level.heroFOV, h.pos - 1 - level.width(), 3, level.visited);
				BArray.or(level.visited, level.heroFOV, h.pos - 1, 3, level.visited);
				BArray.or(level.visited, level.heroFOV, h.pos - 1 + level.width(), 3, level.visited);
				GameScene.updateFog(h.pos, 2);
			}
		}

		GameScene.afterObserve();
	}

	//we store this to avoid having to re-allocate the array with each pathfind
	private static boolean[] passable;

	private static void setupPassable()
	{
		if(passable == null || passable.length != Dungeon.level.length())
			passable = new boolean[Dungeon.level.length()];
		else
			BArray.setFalse(passable);
	}

	public static PathFinder.Path findPath(Char ch, int from, int to, boolean[] pass, boolean[] visible)
	{
		setupPassable();
		if (ch.flying || ch.buff(Amok.class) != null || ch.buff(Challenged.class) != null)
			BArray.or(pass, Dungeon.level.avoid, passable);
		else
			System.arraycopy(pass, 0, passable, 0, Dungeon.level.length());

		for (Char c : Actor.chars())
			if (visible[c.pos])
				passable[c.pos] = false;

		if ((!(ch instanceof Hero) || CPDSettings.avoid_blobs()) && ch.buff(Amok.class) == null && ch.buff(Challenged.class) == null)
			for (int i = 0; i < Dungeon.level.length(); i++)
				if (visible[i] && Blob.harmfulAt(ch.pos, i))
					passable[i] = false;

		return PathFinder.find(from, to, passable);
	}

	public static int findStep(Char ch, int from, int to, boolean[] pass, boolean[] visible)
	{
		if(Dungeon.level.adjacent(from, to))
			return Actor.findChar(to) == null && (pass[to] || Dungeon.level.avoid[to]) ? to : -1;

		setupPassable();
		if(ch.flying || ch.buff(Amok.class) != null)
			BArray.or(pass, Dungeon.level.avoid, passable);
		else
			System.arraycopy(pass, 0, passable, 0, Dungeon.level.length());

		for(Char c : Actor.chars())
			if(visible[c.pos])
				passable[c.pos] = false;

		return PathFinder.getStep(from, to, passable);
	}

	public static int flee(Char ch, int cur, int from, boolean[] pass, boolean[] visible)
	{
		setupPassable();
		if(ch.flying)
			BArray.or(pass, Dungeon.level.avoid, passable);
		else
			System.arraycopy(pass, 0, passable, 0, Dungeon.level.length());

		for(Char c : Actor.chars())
			if(visible[c.pos])
				passable[c.pos] = false;
		passable[cur] = true;

		return PathFinder.getStepBack(cur, from, passable);
	}

	public static void playAt(Object id, int pos)
	{
		if(level == null)
			return;

		float volume = (1 - level.trueDistance(pos, hero.pos) / 15);

		if(volume > 0)
			Sample.INSTANCE.play(id, volume);
	}
}
