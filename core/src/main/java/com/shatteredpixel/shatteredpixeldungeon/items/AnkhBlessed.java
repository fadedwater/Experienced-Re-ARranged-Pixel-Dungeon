/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class AnkhBlessed extends Ankh {
	{
		image = ItemSpriteSheet.ANKH;
		stackable = true;

		//You tell the ankh no, don't revive me, and then it comes back to revive you again in another run.
		//I'm not sure if that's enthusiasm or passive-aggression.
		bones = true;
	}

	private boolean blessed = true;

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions(hero);
		return actions;
	}
	@Override
	public String desc() {
			return Messages.get(this, "desc_blessed");
	}

	public boolean isBlessed(){
		return true;
	}

	private static final Glowing WHITE = new Glowing( 0xFFFFCC );

	@Override
	public Glowing glowing() {
		return WHITE;
	}

	private static final String BLESSED = "blessed";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( BLESSED, blessed );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		blessed	= bundle.getBoolean( BLESSED );
	}

	@Override
	public int value() {
		return 100 * quantity;
	}
}
