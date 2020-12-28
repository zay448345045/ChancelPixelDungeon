package com.noodlemire.chancelpixeldungeon.items.weapon;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.Splash;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfFuror;
import com.noodlemire.chancelpixeldungeon.items.rings.RingOfSharpshooting;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.CellSelector;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.sprites.MissileSprite;
import com.noodlemire.chancelpixeldungeon.ui.QuickSlotButton;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class Bow extends Weapon
{
	private static final String AC_SHOOT = "SHOOT";

	public boolean sniperSpecial = false;
	private int targetPos;

	{
		image = ItemSpriteSheet.BOW_UNLOADED;

		defaultAction = AC_SHOOT;
		usesTargeting = true;

		unique = true;
		bones = false;
		cursedKnown = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero)
	{
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_SHOOT);
		actions.remove(AC_EQUIP);
		return actions;
	}

	@Override
	public int damageRoll(Char owner)
	{
		int damage = super.damageRoll(owner);

		if(sniperSpecial)
		{
			switch(augment)
			{
				case NONE:
					damage = Math.round(damage * 2f / 3f);
					break;
				case SPEED:
					damage = Math.round(damage / 2f);
					break;
				case DAMAGE:
					damage = Math.round(damage * (1 + 0.1f * (Dungeon.level.distance(owner.pos, targetPos) - 1)));
					break;
			}
		}

		return damage;
	}

	@Override
	public float speedFactor(Char owner)
	{
		if (sniperSpecial)
		{
			switch (augment)
			{
				case NONE:
					return 0f;
				case SPEED:
					return 1f * RingOfFuror.modifyAttackDelay(owner);
				case DAMAGE:
					return 2f * RingOfFuror.modifyAttackDelay(owner);
			}
		}

		return super.speedFactor(owner);
	}

	@Override
	public void execute(Hero hero, String action)
	{
		super.execute(hero, action);

		if(action.equals(AC_SHOOT))
		{
			curUser = hero;
			curItem = this;
			image = ItemSpriteSheet.BOW_LOADED;
			updateQuickslot();
			GameScene.selectCell(shooter);
		}
	}

	private int dispMin()
	{
		return augment.damageFactor(min());
	}

	private int dispMax()
	{
		return augment.damageFactor(max());
	}

	@Override
	public String info()
	{
		StringBuilder info = new StringBuilder(desc());

		info.append("\n\n")
				.append(Messages.get(getClass(), "stats", dispMin(), dispMax(), STRReq()));

		if(STRReq() > Dungeon.hero.STR())
			info.append(Messages.get(Weapon.class, "too_heavy"));

		if(augment == Augment.SPEED)
			info.append("\n\n").append(Messages.get(Weapon.class, "faster"));
		else if(augment == Augment.DAMAGE)
			info.append("\n\n").append(Messages.get(Weapon.class, "stronger"));

		if(enchantment != null)
			info.append("\n\n").append(Messages.get(Weapon.class, "enchanted", enchantment.name()))
					.append(" ").append(Messages.get(enchantment, "desc"));

		if(cursed)
			info.append("\n\n").append(Messages.get(Weapon.class, "cursed"));

		return info.append("\n\n").append(Messages.get(MissileWeapon.class, "distance")).toString();
	}

	@Override
	public boolean isIdentified()
	{
		return true;
	}

	@Override
	public boolean isUpgradable()
	{
		return false;
	}

	//Internally, bow's level is half as what's shown in-game to reduce enchantment proc rates.
	@Override
	public int level()
	{
		return Dungeon.hero.lvl / 6;
	}

	@Override
	public int min(int lvl)
	{
		return 1 + level() + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) / 2;
	}

	@Override
	public int max(int lvl)
	{
		return 6 + visiblyUpgraded() + RingOfSharpshooting.levelDamageBonus(Dungeon.hero);
	}

	@Override
	public int STRReq(int lvl)
	{
		return 10 - (int) (Math.sqrt(8 * visiblyUpgraded() + 1) - 1) / 2;
	}

	@Override
	public int visiblyUpgraded()
	{
		return Dungeon.hero.lvl / 3;
	}

	public Arrow knockArrow()
	{
		return new Arrow();
	}

	private CellSelector.Listener shooter = new CellSelector.Listener()
	{
		@Override
		public void onSelect(Integer tar)
		{
			if(tar != null)
				new Arrow().cast(curUser, tar);
			image = ItemSpriteSheet.BOW_UNLOADED;
			updateQuickslot();
		}

		@Override
		public String prompt()
		{
			return Messages.get(Bow.this.getClass(), "prompt");
		}
	};

	public class Arrow extends MissileWeapon
	{
		private static final int COLOR = 0x6600FFFC;

		int flurryCount = -1;

		public Arrow()
		{
			image = ItemSpriteSheet.ARROW;
		}

		@Override
		public void cast(final Hero user, final int dst)
		{
			final int cell = throwPos(user, dst);
			Bow.this.targetPos = cell;
			if(sniperSpecial && Bow.this.augment == Augment.SPEED)
			{
				if(flurryCount == -1)
					flurryCount = 3;

				final Char enemy = Actor.findChar(cell);

				if(enemy == null)
				{
					user.spendAndNext(castDelay(user, dst));
					sniperSpecial = false;
					flurryCount = -1;
					return;
				}

				QuickSlotButton.target(enemy);

				final boolean last = flurryCount == 1;

				user.busy();

				Sample.INSTANCE.play(Assets.SND_MISS, 0.6f, 0.6f, 1.5f);

				((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).reset(user.sprite, cell, this,
						new Callback()
						{
							@Override
							public void call()
							{
								if(enemy.isAlive())
								{
									curUser = user;
									onThrow(cell);
								}

								if(last)
								{
									user.spendAndNext(castDelay(user, dst));
									sniperSpecial = false;
									flurryCount = -1;
								}
							}
						});

				user.sprite.zap(cell, new Callback()
				{
					@Override
					public void call()
					{
						flurryCount--;
						if(flurryCount > 0)
							cast(user, dst);
					}
				});
			}
			else
				super.cast(user, dst);
		}

		@Override
		public boolean hasEnchant(Class<? extends Enchantment> type)
		{
			enchantment = Bow.this.enchantment;
			return Bow.this.hasEnchant(type);
		}

		@Override
		public int min(int lvl)
		{
			return Bow.this.min(lvl);
		}

		@Override
		public int max(int lvl)
		{
			return Bow.this.max(lvl);
		}

		@Override
		public float speedFactor(Char user) {
			return Bow.this.speedFactor(user);
		}

		@Override
		public float accuracyFactor(Char owner) {
			if (sniperSpecial && Bow.this.augment == Augment.DAMAGE){
				return Float.POSITIVE_INFINITY;
			} else {
				return super.accuracyFactor(owner);
			}
		}

		@Override
		public void onThrow(int cell)
		{
			Char enemy = Actor.findChar(cell);
			if(enemy == null || enemy == curUser)
			{
				parent = null;
				Splash.at(cell, COLOR, 3);
			}
			else
			{
				if(!curUser.shoot(enemy, this))
					Splash.at(cell, COLOR, 3);
				if(sniperSpecial && Bow.this.augment != Augment.SPEED)
					sniperSpecial = false;
			}
		}

		@Override
		public int proc(Char attacker, Char defender, int damage)
		{
			return Bow.this.proc(attacker, defender, damage);
		}

		@Override
		public int STRReq(int lvl)
		{
			return Bow.this.STRReq(lvl);
		}
	}
}
