package com.noodlemire.chancelpixeldungeon.items.artifacts;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.noodlemire.chancelpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class BraceletOfForce extends Artifact
{
	{
		image = ItemSpriteSheet.ARTIFACT_BRACELET;

		levelCap = 5;

		charge = 0;
		chargeCap = 100;

		defaultAction = AC_RESTORE;
	}

	private static final String AC_ABSORB = "ABSORB";
	private static final String AC_RESTORE = "RESTORE";

	private int absorbedWeapons = 0;

	@Override
	public ArrayList<String> actions(Hero hero)
	{
		ArrayList<String> actions = super.actions(hero);

		if(isEquipped(hero) && (!cursed || isBound()))
		{
			if(level() < levelCap)
				actions.add(AC_ABSORB);
			if(charge == chargeCap)
				actions.add(AC_RESTORE);
		}

		return actions;
	}

	@Override
	public void execute(Hero hero, String action)
	{
		super.execute(hero, action);

		if(action.equals(AC_ABSORB))
			GameScene.selectItem(itemSelector, WndBag.Mode.WEAPON, Messages.get(this, "prompt"));
		else if(action.equals(AC_RESTORE))
		{
			if(!isEquipped(hero))
				GLog.i(Messages.get(this, "need_to_equip"));
			else if(charge < chargeCap)
				GLog.i(Messages.get(this, "low_charge"));
			else
			{
				charge = 0;
				hero.dynamic(hero.dynamax());
				updateQuickslot();
				hero.spend(1f);
				hero.busy();
				hero.sprite.flash(1f, 0f, 0f);
				hero.sprite.operate(hero.pos);
			}
		}
	}

	public void gainCharge()
	{
		if(charge == chargeCap)
			return;

		charge += (int)Math.ceil(chargeCap / (8f - level()));

		if(charge >= chargeCap)
		{
			charge = chargeCap;
			GLog.p(Messages.get(this, "desc_ready"));
		}

		updateQuickslot();
	}

	public static boolean cursedHero(Hero hero)
	{
		if(hero.belongings.classMisc instanceof BraceletOfForce && hero.belongings.classMisc.cursed)
			return true;

		for(Item i : hero.belongings.miscSlots)
			if(i instanceof BraceletOfForce && i.cursed)
				return true;

		return false;
	}

	@Override
	public String desc()
	{
		String desc = Messages.get(this, "desc");

		if(isBound())
			desc += "\n\n" + Messages.get(this, "binding");

		if(isEquipped(Dungeon.hero))
		{
			desc += "\n\n";

			if(cursed)
				desc += Messages.get(this, "desc_cursed");
			else
				desc += Messages.get(this, "desc_hint", level()+1);

			if(charge == chargeCap)
				desc += "\n\n" + Messages.get(this, "desc_ready");
		}

		if(absorbedWeapons > 0)
			desc += "\n\n" + Messages.get(this, "desc_weps", absorbedWeapons);

		return desc;
	}

	private static final String ABSORBEDWEAPONS = "absorbedWeapons";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(ABSORBEDWEAPONS, absorbedWeapons);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		absorbedWeapons = bundle.getInt(ABSORBEDWEAPONS);

		//This can happen if an older save used a Ring of Force at a very high level
		if(level() > levelCap)
			level(levelCap);
	}

	protected WndBag.Listener itemSelector = new WndBag.Listener()
	{
		@Override
		public void onSelect(Item item)
		{
			if(item instanceof MeleeWeapon)
			{
				MeleeWeapon wep = (MeleeWeapon)item;

				if(wep.tier < (level() + 1))
					GLog.w(Messages.get(BraceletOfForce.class, "wep_too_weak"));
				else if(!wep.cursedKnown)
					GLog.w(Messages.get(BraceletOfForce.class, "wep_not_identified"));
				else if(wep.cursed || wep.hasCurseEnchant())
					GLog.w(Messages.get(BraceletOfForce.class, "wep_cursed"));
				else
				{
					absorbedWeapons++;

					Hero hero = Dungeon.hero;
					hero.sprite.operate(hero.pos);
					Sample.INSTANCE.play(Assets.SND_PLANT);
					hero.busy();
					hero.spend(2f);

					if(absorbedWeapons >= (level() + 1))
					{
						absorbedWeapons = 0;
						upgrade();
						GLog.p(Messages.get(BraceletOfForce.class, "levelup"));
					}
					else
						GLog.i(Messages.get(BraceletOfForce.class, "absorb_weapon"));

					item.detach(hero.belongings.backpack);
					if(hero.belongings.weapon == item)
						hero.belongings.weapon = null;
				}
			}
		}
	};
}
