package com.shatteredpixel.shatteredpixeldungeon.items.seals;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

public class BerserkerSeal extends Item {
    {
        image = ItemSpriteSheet.USA_SEAL;
        levelKnown = true;
        bones = false;
        unique = true;
    }

    public boolean curseInfusionBonus = false;
    private static final String CURSE_INFUSION_BONUS = "curse_infusion_bonus";
    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( CURSE_INFUSION_BONUS, curseInfusionBonus );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        curseInfusionBonus = bundle.getBoolean( CURSE_INFUSION_BONUS );
    }
    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public int level() {
        int level = Dungeon.hero == null ? 1 : 1+Dungeon.hero.lvl/10;
        if (curseInfusionBonus) level += 1 + level/6;
        level = Math.min(level,10);
        return level;
    }
    @Override
    public String info() {
        String info = Messages.get(this, "desc");
        info += Messages.get(this,"stat_desc",(int)(100*(RegenFactor()-1)));
        return info;
    }
    @Override
    public int buffedLvl() {
        //level isn't affected by buffs/debuffs
        return level();
    }

    public float RegenFactor() {
        return 1f + (level)/10f;
    }

}
