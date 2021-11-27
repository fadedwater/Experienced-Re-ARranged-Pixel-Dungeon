/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LanceGuardBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SpearGuardBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class LanceNShield extends MeleeWeapon {

    {
        image = ItemSpriteSheet.LANCE_N_SHIELD;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1.1f;

        tier = 6;
    }

    @Override
    public int max(int lvl) {
        return  3*(tier+1) +
                lvl*(tier+1);
    }

    @Override
    public int STRReq(int lvl) {
        return STRReq(tier-1, lvl); //18 base strength req, down from 20
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {

        // lvl 0 - 33%
        // lvl 1 - 50%
        // lvl 2 - 60%

        float procChance = (buffedLvl()+1f)/(buffedLvl()+3f);

        if (Random.Float() < procChance) {
            Buff.affect( attacker, LanceGuardBuff.class);
            return super.proc( attacker, defender, damage );
        } else {
            return super.proc( attacker, defender, damage );
        }
    }

    public String statsInfo(){
        if (isIdentified()){
            return Messages.get(this, "stats_desc", buffedLvl(), 6+3*buffedLvl());
        } else {
            return Messages.get(this, "typical_stats_desc", 0, 6);
        }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{Lance.class, ObsidianShield.class, ArcaneResin.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 30;
            if (Dungeon.hero.hasTalent(Talent.BLACKSMITH)) {
                cost -= 1f * Dungeon.hero.pointsInTalent(Talent.BLACKSMITH);
            }

            output = LanceNShield.class;
            outQuantity = 1;
        }

    }
}
