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

package com.noodlemire.chancelpixeldungeon.actors;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Electricity;
import com.noodlemire.chancelpixeldungeon.actors.blobs.ToxicGas;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Bleeding;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Burning;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Charm;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Chill;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Corrosion;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Corruption;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Cripple;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Doom;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Frost;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Haste;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Hunger;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Linkage;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MagicShield;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MagicalSleep;
import com.noodlemire.chancelpixeldungeon.actors.buffs.MeleeProc;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Ooze;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Paralysis;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Poison;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Preparation;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Repulsion;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Slow;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Speed;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Vertigo;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.hero.HeroSubClass;
import com.noodlemire.chancelpixeldungeon.items.armor.glyphs.Potential;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfElements;
import com.noodlemire.chancelpixeldungeon.items.scrolls.ScrollOfSupernova;
import com.noodlemire.chancelpixeldungeon.items.wands.DamageWand;
import com.noodlemire.chancelpixeldungeon.items.wands.Wand;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfBlastWave;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfFireblast;
import com.noodlemire.chancelpixeldungeon.items.wands.WandOfLightning;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Blazing;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Grim;
import com.noodlemire.chancelpixeldungeon.items.weapon.enchantments.Shocking;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.Shuriken;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.darts.ShockingDart;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.levels.features.Chasm;
import com.noodlemire.chancelpixeldungeon.levels.features.Door;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Arrays;
import java.util.HashSet;

public abstract class Char extends Actor
{
	public int pos = 0;

	public CharSprite sprite;

	public String name = "mob";

	private int HT;
	private int HP;
	private int SHLD;

	protected float baseSpeed = 1;
	public PathFinder.Path path;

	public int paralysed = 0;
	public boolean rooted = false;
	public boolean flying = false;
	public int invisible = 0;

	//these are relative to the hero
	public enum Alignment
	{
		ENEMY,
		NEUTRAL,
		ALLY
	}

	public Alignment alignment;

	public int viewDistance = 8;

	protected boolean[] fieldOfView = null;

	private final HashSet<Buff> buffs = new HashSet<>();

	@Override
	protected boolean act()
	{
		observe();
		return false;
	}

	protected void observe()
	{
		if(fieldOfView == null || fieldOfView.length != Dungeon.level.length())
		{
			fieldOfView = new boolean[Dungeon.level.length()];
		}
		Dungeon.level.updateFieldOfView(this, fieldOfView);
	}

	protected static final String POS = "pos";
	protected static final String TAG_HP = "HP";
	protected static final String TAG_HT = "HT";
	protected static final String TAG_SHLD = "SHLD";
	protected static final String BUFFS = "buffs";

	@Override
	public void storeInBundle(Bundle bundle)
	{

		super.storeInBundle(bundle);

		bundle.put(POS, pos);
		bundle.put(TAG_HP, HP);
		bundle.put(TAG_HT, HT);
		bundle.put(TAG_SHLD, SHLD);
		bundle.put(BUFFS, buffs);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);

		pos = bundle.getInt(POS);
		HP = bundle.getInt(TAG_HP);
		HT = bundle.getInt(TAG_HT);
		SHLD = bundle.getInt(TAG_SHLD);

		for(Bundlable b : bundle.getCollection(BUFFS))
		{
			if(b != null)
			{
				((Buff) b).attachTo(this);
			}
		}
	}

	public String description()
	{
		return Messages.get(this, "desc");
	}

	public int HP()
	{
		return HP;
	}

	protected void setHP(int to)
	{
		HP = Math.min(to, HT);
		HP = Math.max(HP, 1);
	}

	public void heal(int by, Object src)
	{
		if(!(src instanceof Linkage))
		{
			Linkage link = buff(Linkage.class);
			if (link != null)
			{
				Actor a = Actor.findById(link.object);

				if (a instanceof Char)
				{
					Char ch = (Char) a;
					if (ch.isAlive())
					{
						by /= 2;
						ch.heal(by, link);
					}
				}
			}
		}

		if(HP < HT && by > 0)
		{
			int amt = Math.min(HT - HP, by);
			HP += amt;
			sprite.showStatus(CharSprite.POSITIVE, "+" + amt);
		}
	}

	public int HT()
	{
		return HT;
	}

	protected void setHT(int to, boolean fullHealth)
	{
		if(to == HT)
			return;

		int oldHT = HT;
		HT = Math.max(to, 1);

		if(fullHealth)
			HP = HT;
		else
			HP = (int)GameMath.gate(1,HP + Math.max(0, HT - oldHT), HT);
	}

	public int SHLD()
	{
		return SHLD;
	}

	public void SHLD(int change)
	{
		SHLD = Math.max(0, SHLD + change);
	}

	public boolean attack(Char enemy)
	{
		if(enemy == null || !enemy.isAlive()) return false;

		boolean visibleFight = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];

		if(hit(this, enemy, false))
		{
			onAttack(enemy, visibleFight, true);

			return true;
		}
		else
		{
			if(visibleFight)
				enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());

			Dungeon.playAt(Assets.SND_MISS, enemy.pos);

			return false;
		}
	}

	public int onAttack(Char enemy, boolean visibleFight, boolean blood)
	{
		boolean repulsed = enemy.buff(Repulsion.class) != null;
		boolean selfHarm = false;

		int dmg;
		Preparation prep = buff(Preparation.class);
		if(prep != null)
		{
			dmg = prep.damageRoll(this, enemy);
		}
		else
		{
			dmg = damageRoll();
		}

		int dr = enemy.drRoll();

		if(this instanceof Hero)
		{
			Hero h = (Hero) this;
			if((h.belongings.weapon instanceof MissileWeapon && h.subClass == HeroSubClass.SNIPER)
					|| (h.belongings.weapon instanceof Shuriken && h.critBoost((Weapon)h.belongings.weapon)))
				dr = 0;
		}

		int effectiveDamage = enemy.defenseProc(this, dmg);
		effectiveDamage = Math.max(effectiveDamage - dr, 0);
		effectiveDamage = attackProc(enemy, effectiveDamage);

		Dungeon.playAt(Assets.SND_HIT, enemy.pos);

		// If the enemy is already dead, interrupt the attack.
		// This matters as defence procs can sometimes inflict self-damage, such as armor glyphs.
		if(!enemy.isAlive())
			return effectiveDamage;

		//TODO: consider revisiting this and shaking in more cases.
		float shake = 0f;
		if(enemy == Dungeon.hero)
			shake = effectiveDamage / (enemy.HT / 4);

		if(shake > 1f)
			Camera.main.shake(GameMath.gate(1, shake, 5), 0.3f);

		if(repulsed)
		{
			effectiveDamage /= 2;
			damage(effectiveDamage, this);
		}

		enemy.damage(effectiveDamage, this);

		if(blood)
		{
			enemy.sprite.bloodBurstA(sprite.center(), effectiveDamage);
			enemy.sprite.flash();
		}

		for(Buff buff : buffs)
			if(buff instanceof MeleeProc)
				((MeleeProc)buff).proc(enemy);

		if(repulsed)
		{
			sprite.bloodBurstA(sprite.center(), effectiveDamage);
			sprite.flash();

			WandOfBlastWave.throwChar(this, new Ballistica(pos, pos + (pos - enemy.pos), Ballistica.MAGIC_BOLT), 1);

			selfHarm = true;
		}

		if(!enemy.isAlive() && visibleFight)
		{
			if(enemy == Dungeon.hero && this != Dungeon.hero)
			{
				Dungeon.fail(getClass());
				GLog.n(Messages.capitalize(Messages.get(Char.class, "kill", name)));
			}
			else if(this == Dungeon.hero)
			{
				GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)));
			}
		}

		//Even though suicide is an option, it is never the option.
		if(this == Dungeon.hero && !isAlive() && (selfHarm || enemy == Dungeon.hero))
		{
			Dungeon.fail(getClass());
			GLog.n(Messages.capitalize(Messages.get(Hero.class, "suicide")));
		}

		return effectiveDamage;
	}

	public static boolean hit(Char attacker, Char defender, boolean magic)
	{
		float acuRoll = Random.Float(attacker.attackSkill(defender));
		float defRoll = Random.Float(defender.defenseSkill(attacker));
		return (magic ? acuRoll * 2 : acuRoll) >= defRoll;
	}

	public int attackSkill(Char target)
	{
		return 0;
	}

	public int defenseSkill(Char enemy)
	{
		return 0;
	}

	public String defenseVerb()
	{
		return Messages.get(this, "def_verb");
	}

	public int drRoll()
	{
		return 0;
	}

	public int damageRoll()
	{
		return 1;
	}

	public int attackProc(Char enemy, int damage)
	{
		return damage;
	}

	public int defenseProc(Char enemy, int damage)
	{
		return damage;
	}

	public float speed()
	{
		float speed = baseSpeed;

		if(buff(Cripple.class) != null) speed *= 0.5;
		if(buff(Haste.class) != null) speed *= 3.0;

		return speed;
	}

	public void damage(int dmg, Object src)
	{
		if(!isAlive() || dmg < 0)
			return;

		if(buff(Frost.class) != null)
			Buff.detach(this, Frost.class);

		if(buff(MagicalSleep.class) != null)
			Buff.detach(this, MagicalSleep.class);

		if(buff(Doom.class) != null)
			dmg *= 2;

		if(src instanceof DamageWand && ((Wand) src).ownedByStaff && Dungeon.hero.critBoost(null))
			dmg = (int)Math.round(dmg * 1.25);

		Class<?> srcClass = src.getClass();
		if(isImmune(srcClass))
		{
			dmg = 0;
		}
		else
		{
			dmg = Math.round(dmg * resist(srcClass));
		}

		if(!(src instanceof Linkage))
		{
			Linkage link = buff(Linkage.class);
			if (link != null)
			{
				Actor a = Actor.findById(link.object);

				if (a instanceof Char)
				{
					Char ch = (Char) a;
					if (ch.isAlive())
					{
						dmg /= 2;
						ch.damage(dmg, link);
					}
				}
			}
		}

		if(buff(Paralysis.class) != null)
		{
			buff(Paralysis.class).processDamage(dmg);
		}

		MagicShield shield = buff(MagicShield.class);

		//FIXME: when I add proper damage properties, should add an IGNORES_SHIELDS property to use here.
		if(src instanceof Hunger || SHLD == 0)
		{
			HP -= dmg;
		}
		else if(SHLD >= dmg)
		{
			if(shield != null)
				shield.shorten(dmg);

			SHLD(-dmg);
		}
		else if(SHLD > 0)
		{
			if(shield != null)
				shield.shorten(SHLD);

			HP -= (dmg - SHLD);
			SHLD = 0;
		}

		if(shield != null)
			System.out.println("MS: " + shield.left() + ", SHLD: " + SHLD);

		sprite.showStatus(HP > HT / 2 ?
						CharSprite.WARNING :
						CharSprite.NEGATIVE,
				Integer.toString(dmg));

		if(HP < 0) HP = 0;

		if(!isAlive())
		{
			die(src);
		}
	}

	public void destroy()
	{
		HP = 0;
		Actor.remove(this);
	}

	public void die(Object src)
	{
		destroy();
		if(src != Chasm.class && sprite != null) sprite.die();
	}

	public boolean isAlive()
	{
		return HP > 0;
	}

	@Override
	protected void spend(float time)
	{
		float timeScale = 1f;
		if(buff(Slow.class) != null)
		{
			timeScale *= 0.5f;
			//slowed and chilled do not stack
		}
		else if(buff(Chill.class) != null)
		{
			timeScale *= buff(Chill.class).speedFactor();
		}
		if(buff(Speed.class) != null)
		{
			timeScale *= 2.0f;
		}

		super.spend(time / timeScale);
	}

	public synchronized HashSet<Buff> buffs()
	{
		return new HashSet<>(buffs);
	}

	@SuppressWarnings("unchecked")
	public synchronized <T extends Buff> HashSet<T> buffs(Class<T> c)
	{
		HashSet<T> filtered = new HashSet<>();
		for(Buff b : buffs)
		{
			if(c.isInstance(b))
			{
				filtered.add((T) b);
			}
		}
		return filtered;
	}

	@SuppressWarnings("unchecked")
	public synchronized <T extends Buff> T buff(Class<T> c)
	{
		for(Buff b : buffs)
		{
			if(c.isInstance(b))
			{
				return (T) b;
			}
		}
		return null;
	}

	public synchronized boolean isCharmedBy(Char ch)
	{
		int chID = ch.id();
		for(Buff b : buffs)
		{
			if(b instanceof Charm && ((Charm) b).object == chID)
			{
				return true;
			}
		}
		return false;
	}

	public synchronized void add(Buff buff)
	{
		buffs.add(buff);
		Actor.add(buff);

		if(sprite != null)
			switch(buff.type)
			{
				case POSITIVE:
					sprite.showStatus(CharSprite.POSITIVE, buff.toString());
					break;
				case NEGATIVE:
					sprite.showStatus(CharSprite.NEGATIVE, buff.toString());
					break;
				case NEUTRAL:
					sprite.showStatus(CharSprite.NEUTRAL, buff.toString());
					break;
				case SILENT:
				default:
					break; //show nothing
			}
	}

	public synchronized void remove(Buff buff)
	{
		buffs.remove(buff);
		Actor.remove(buff);
	}

	public synchronized void remove(Class<? extends Buff> buffClass)
	{
		for(Buff buff : buffs(buffClass))
		{
			remove(buff);
		}
	}

	@Override
	protected synchronized void onRemove()
	{
		for(Buff buff : buffs.toArray(new Buff[0]))
			buff.detach();
	}

	public synchronized void updateSpriteState()
	{
		for(Buff buff : buffs)
			buff.fx(true);
	}

	public CharSprite sprite()
	{
		try
		{
			return sprite.getClass().newInstance();
		}
		catch(Exception e)
		{
			ChancelPixelDungeon.reportException(e);
			return null;
		}
	}

	public int stealth()
	{
		return 0;
	}

	public void move(int step)
	{
		if(Dungeon.level.adjacent(step, pos) && buff(Vertigo.class) != null)
		{
			sprite.interruptMotion();
			int newPos = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
			if(!(Dungeon.level.passable[newPos] || Dungeon.level.avoid[newPos]) || Actor.findChar(newPos) != null)
				return;
			else
			{
				sprite.move(pos, newPos);
				step = newPos;
			}
		}

		if(Dungeon.level.map[pos] == Terrain.OPEN_DOOR)
		{
			Door.leave(pos);
		}

		pos = step;

		if(flying && Dungeon.level.map[pos] == Terrain.DOOR)
		{
			Door.enter(pos);
		}

		if(this != Dungeon.hero)
		{
			sprite.visible = Dungeon.level.heroFOV[pos];
		}

		if(!flying)
		{
			Dungeon.level.press(pos, this);
		}
	}

	public int distance(Char other)
	{
		return Dungeon.level.distance(pos, other.pos);
	}

	public void onMotionComplete()
	{
		//Does nothing by default
		//The main actor thread already accounts for motion,
		// so calling next() here isn't necessary (see Actor.process)
	}

	public void onAttackComplete()
	{
		next();
	}

	public void onOperateComplete()
	{
		next();
	}

	protected final HashSet<Class> resistances = new HashSet<>();

	//returns percent effectiveness after resistances
	//TODO currently resistances reduce effectiveness by a static 50%, and do not stack.
	public float resist(Class effect)
	{
		HashSet<Class> resists = new HashSet<>(resistances);
		for(Property p : properties())
		{
			resists.addAll(p.resistances());
		}
		for(Buff b : buffs())
		{
			resists.addAll(b.resistances());
		}

		float result = 1f;
		for(Class c : resists)
		{
			if(c.isAssignableFrom(effect))
			{
				result *= 0.5f;
			}
		}
		return result * RingOfElements.resist(this, effect);
	}

	protected final HashSet<Class> immunities = new HashSet<>();

	public boolean isImmune(Class effect)
	{
		HashSet<Class> immunes = new HashSet<>(immunities);
		for(Property p : properties())
		{
			immunes.addAll(p.immunities());
		}
		for(Buff b : buffs())
		{
			immunes.addAll(b.immunities());
		}

		for(Class c : immunes)
		{
			if(c.isAssignableFrom(effect))
			{
				return true;
			}
		}
		return false;
	}

	protected HashSet<Property> properties = new HashSet<>();

	public HashSet<Property> properties()
	{
		return new HashSet<>(properties);
	}

	public enum Property
	{
		BOSS(new HashSet<Class>(Arrays.asList(Grim.class, ScrollOfSupernova.class)),
				new HashSet<Class>(Arrays.asList(Corruption.class))),
		MINIBOSS(new HashSet<Class>(),
				new HashSet<Class>(Arrays.asList(Corruption.class))),
		UNDEAD,
		DEMONIC,
		INORGANIC(new HashSet<Class>(),
				new HashSet<Class>(Arrays.asList(Bleeding.class, ToxicGas.class, Poison.class))),
		BLOB_IMMUNE(new HashSet<Class>(),
				new HashSet<Class>(Arrays.asList(Blob.class))),
		FIERY(new HashSet<Class>(Arrays.asList(WandOfFireblast.class)),
				new HashSet<Class>(Arrays.asList(Burning.class, Blazing.class))),
		ACIDIC(new HashSet<Class>(Arrays.asList(ToxicGas.class, Corrosion.class)),
				new HashSet<Class>(Arrays.asList(Ooze.class))),
		ELECTRIC(new HashSet<Class>(Arrays.asList(WandOfLightning.class, Shocking.class, Potential.class, Electricity.class, ShockingDart.class)),
				new HashSet<Class>()),
		IMMOVABLE,
		METALLIC;

		private final HashSet<Class> resistances;
		private final HashSet<Class> immunities;

		Property()
		{
			this(new HashSet<Class>(), new HashSet<Class>());
		}

		Property(HashSet<Class> resistances, HashSet<Class> immunities)
		{
			this.resistances = resistances;
			this.immunities = immunities;
		}

		public HashSet<Class> resistances()
		{
			return new HashSet<>(resistances);
		}

		public HashSet<Class> immunities()
		{
			return new HashSet<>(immunities);
		}
	}
}
