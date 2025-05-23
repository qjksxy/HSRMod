package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import hsrmod.effects.MoveToEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.BarrierPower;
import hsrmod.utils.ModHelper;

public class Beetle extends BaseMonster {
    public static final String ID = Beetle.class.getSimpleName();
    
    public Beetle(float x, float y) {
        super(ID, 0F, -15.0F, 165F, 197F, x, y);
        
        this.setDamagesWithAscension(6);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (ModHelper.specialAscension(type)) {
            addToBot(new ApplyPowerAction(this, this, new BarrierPower(this, 1), 1));
        }
    }

    @Override
    public void takeTurn() {
        addToBot(new VFXAction(new MoveToEffect(this, p.hb.cX + p.hb.width / 2 - hb.cX, p.hb.cY - hb.cY, true, 0.7f), 0.35f));
        addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot(new ApplyPowerAction(this, this, new BarrierPower(this, 1), 1));
    }

    @Override
    protected void getMove(int i) {
        setMove(MOVES[0], (byte) 0, Intent.ATTACK_BUFF, this.damage.get(0).base);
    }
}
