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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class AccuracyBuff extends Buff {
	
	private int count = 0;
	private int maxCount = 1+Dungeon.hero.belongings.weapon.buffedLvl();
	private boolean lastAttacked = false;

	@Override
	public int icon() {
		return BuffIndicator.MARK;
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (maxCount - count)/ maxCount);
	}

	public int getCount() {
		return count;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}
	
	public void attack( Char enemy ) {
		if (count < maxCount) {
			count++;
		} else {
			count = maxCount;
		}
		lastAttacked = true;
		BuffIndicator.refreshHero(); //refresh the buff visually on-hit
	}

	@Override
	public void detach() {
		super.detach();
	}

	@Override
	public boolean act() {
		if (!lastAttacked) {
			count-=TICK;
		} else {
			lastAttacked = false;
		}

		spend(TICK);
		if (count <= 0) {
			detach();
		}
		return true;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", count, 5*count, maxCount);
	}

	private static final String COUNT = "count";
	private static final String MAXCOUNT = "maxCount";
	private static final String LASTATTACKED = "lastAttacked";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(COUNT, count);
		bundle.put(MAXCOUNT, maxCount);
		bundle.put(LASTATTACKED, lastAttacked);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		count = bundle.getInt( COUNT );
		maxCount = bundle.getInt( MAXCOUNT );
		lastAttacked = bundle.getBoolean( LASTATTACKED );
	}

}
