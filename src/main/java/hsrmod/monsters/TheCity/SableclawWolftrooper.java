package hsrmod.monsters.TheCity;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.BleedingPower;
import hsrmod.powers.enemyOnly.MoonRagePower;

public class SableclawWolftrooper extends BaseMonster {
    public static final String ID = SableclawWolftrooper.class.getSimpleName();
    
    public SableclawWolftrooper(float x, float y) {
        super(ID, 120F, 169F, x, y);
        
        setDamages(2);
    }

    @Override
    public void takeTurn() {
        addToBot(new AnimateHopAction(this));
        addToBot(new VFXAction(new BiteEffect(p.hb.cX, p.hb.cY, Color.RED)));
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
        addToBot(new VFXAction(new BiteEffect(p.hb.cX, p.hb.cY, Color.RED)));
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
        if (hasPower(MoonRagePower.POWER_ID))
            addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new BleedingPower(AbstractDungeon.player, this, 1), 1));
    }

    @Override
    protected void getMove(int i) {
        setMove(MOVES[0], (byte) 0, AbstractMonster.Intent.ATTACK, this.damage.get(0).base, 2, true);
    }
}
