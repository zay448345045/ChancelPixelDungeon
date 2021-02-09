package com.noodlemire.chancelpixeldungeon.actors.blobs;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Paralysis;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Statue;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Thief;
import com.noodlemire.chancelpixeldungeon.effects.BlobEmitter;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Lightning;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.Heap;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.armor.MailArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.PlateArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.ScaleArmor;
import com.noodlemire.chancelpixeldungeon.items.armor.WarriorArmor;
import com.noodlemire.chancelpixeldungeon.items.bags.Bag;
import com.noodlemire.chancelpixeldungeon.items.rings.Ring;
import com.noodlemire.chancelpixeldungeon.items.wands.Wand;
import com.noodlemire.chancelpixeldungeon.items.weapon.Weapon;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.AssassinsBlade;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Gauntlet;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.MagesStaff;
import com.noodlemire.chancelpixeldungeon.items.weapon.melee.Whip;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.Bolas;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.Boomerang;
import com.noodlemire.chancelpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

public class ThunderCloud extends GasBlob implements Hero.Doom
{
	{
		harmful = true;
	}

	@Override
	void affect(Char ch, int cell)
	{
		Heap items = Dungeon.level.heaps.get(cell);

		if(((metallic(items) || metallic(ch)) && Random.Int(2) == 0) || Random.Int(100) == 0)
		{
			if(ch != null && ch.isAlive() && !ch.isImmune(this.getClass()))
			{
				int levelDamage = 5 + Dungeon.depth * 5;
				int damage = (ch.HT() + levelDamage) / 20;

				if(Random.Int(20) < (ch.HT() + levelDamage) % 20) damage++;

				Buff.prolong(ch, Paralysis.class, 1);
				ch.damage(damage, this);
			}

			if(items != null)
				for(Item item : items.items)
					if(item instanceof Wand)
						((Wand) item).gainCharge(1);
					else if(item instanceof MagesStaff)
						((MagesStaff) item).gainCharge(1);

			if(Dungeon.level.flamable[cell]) GameScene.add(Blob.seed(cell, 2, Fire.class));
			if(Dungeon.level.water[cell]) GameScene.ripple(cell);

			Emitter emitter = CellEmitter.get(cell);

			if(Dungeon.level.heroFOV[cell])
			{
				emitter.parent.add(new Lightning(cell + Dungeon.level.width(), cell - Dungeon.level.width(), null));
				emitter.parent.add(new Lightning(cell - 1, cell + 1, null));
			}

			Dungeon.playAt(Assets.SND_ROCKS, cell);
		}

		if(Random.Int(30) == 0 && (Dungeon.level.map[cell] == Terrain.EMPTY
		                           || Dungeon.level.map[cell] == Terrain.EMPTY_DECO))
		{
			Level.set(cell, Terrain.WATER);
			GameScene.updateMap(cell);
		}
	}

	@Override
	void affect(int cell)
	{
		affect(null, cell);
	}

	private static boolean metallic(Char ch)
	{
		return //Char exists and
				ch != null &&

				//Char is a statue holding a metal weapon
				((ch instanceof Statue && metallic(((Statue) ch).getWeapon()))

				//Char has the "metallic" property, such as DM-300 and Gnoll Brutes
				|| ch.properties().contains(Char.Property.METALLIC)

				//Char is a thief that has stolen a metallic item
				|| (ch instanceof Thief && metallic(((Thief) ch).item))

				 //Or char is a hero with something metal equipped
				 || ((ch instanceof Hero) && (metallic(((Hero) ch).belongings.weapon) || metallic(((Hero) ch).belongings.armor)
				                              || metallic(((Hero) ch).belongings.miscSlots))));
	}

	private static boolean metallic(Heap heap)
	{
		if(heap != null)
			for(Item item : heap.items)
				if(metallic(item)) return true;

		return false;
	}

	private static boolean metallic(Item item)
	{
		if(item instanceof Bag)
			for(Item i : ((Bag)item).items)
				if(metallic(i))
					return true;

		return  //Item is a metallic weapon
				((item instanceof Weapon && !(item instanceof Whip) && !(item instanceof AssassinsBlade) && !(item instanceof Gauntlet)
				  && !(item instanceof Boomerang) && !(item instanceof ThrowingStone) && !(item instanceof Bolas))

				 //Or item is a metallic armor
				 || item instanceof MailArmor || item instanceof ScaleArmor
				 || item instanceof PlateArmor || item instanceof WarriorArmor

				 //Even though most wands aren't metal, they still attract lightning due to being charged by electricity
				 || item instanceof Wand

				 //Or item is a ring
				 || item instanceof Ring);
	}

	@Override
	public void use(BlobEmitter emitter)
	{
		super.use(emitter);

		emitter.pour(Speck.factory(Speck.STORMCLOUD), 0.4f);
	}

	@Override
	public String tileDesc()
	{
		return Messages.get(this, "desc");
	}

	@Override
	public void onDeath()
	{
		Dungeon.fail(getClass());
		GLog.n(Messages.get(this, "ondeath"));
	}
}
