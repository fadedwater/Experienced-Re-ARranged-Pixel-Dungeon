/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MedicKit;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.seals.BerserkerSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ChaoticCenser;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.SaltCube;

public class Regeneration extends Buff {
	
	{
		//unlike other buffs, this one acts after the hero and takes priority against other effects
		//healing is much more useful if you get some of it off before taking damage
		actPriority = HERO_PRIO - 1;
	}
	
	private static final float REGENERATION_DELAY = 10;
	
	@Override
	public boolean act() {
		if (target.isAlive()) {

			//if other trinkets ever get buffs like this should probably make the buff attaching
			// behaviour more like wands/rings/artifacts
			if (ChaoticCenser.averageTurnsUntilGas() != -1){
				Buff.affect(Dungeon.hero, ChaoticCenser.CenserGasTracker.class);
			}

			//cancel regenning entirely in thie case
			if (SaltCube.healthRegenMultiplier() == 0){
				spend(REGENERATION_DELAY);
				return true;
			}

			if (target.HP < regencap() && !((Hero)target).isStarving()) {
				if (regenOn()) {
					target.HP += 1;
					if (target.HP == regencap()) {
						((Hero) target).resting = false;
					}
					MedicKit.MedicKitBuff medicKitBuff = Dungeon.hero.buff( MedicKit.MedicKitBuff.class );
					if (medicKitBuff != null) {
						medicKitBuff.charge((Hero)target, 1);
					}
				}
			}

			ChaliceOfBlood.chaliceRegen regenBuff = Dungeon.hero.buff( ChaliceOfBlood.chaliceRegen.class);

			float delay = REGENERATION_DELAY;
			if (regenBuff != null && target.buff(MagicImmune.class) == null) {
				if (regenBuff.isCursed()) {
					delay *= 1.5f;
				} else {
					//15% boost at +0, scaling to a 500% boost at +10
					delay -= 1.33f + regenBuff.itemLevel()*0.667f;
					delay /= RingOfEnergy.artifactChargeMultiplier(target);
				}
			}

			if (((Hero)target).hasTalent(Talent.STRONG_HEALPOWER)) {
				delay /= 1f+0.1f*((Hero)target).pointsInTalent(Talent.STRONG_HEALPOWER);
			}
			if (((Hero)target).hasTalent(Talent.ACCUMULATION) && ((Hero) target).heroClass != HeroClass.DUELIST) {
				delay /= 1f+0.1f*((Hero)target).pointsInTalent(Talent.ACCUMULATION);
			}
			if (Dungeon.hero.belongings.getItem(BerserkerSeal.class) != null){
				BerserkerSeal berseal = Dungeon.hero.belongings.getItem(BerserkerSeal.class);
				delay /= berseal.RegenFactor();
			}

			delay /= SaltCube.healthRegenMultiplier();
			spend( delay );
			
		} else {
			
			diactivate();
			
		}
		
		return true;
	}
	
	public int regencap(){
		return target.HT;
	}

	public static boolean regenOn(){
		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null && !lock.regenOn()){
			return false;
		}
		return true;
	}
}
