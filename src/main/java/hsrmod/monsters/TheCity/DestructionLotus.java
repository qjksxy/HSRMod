package hsrmod.monsters.TheCity;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AngryPower;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.vfx.combat.BossCrystalImpactEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.DestructionLotusPower;
import hsrmod.powers.enemyOnly.SummonedPower;
import hsrmod.utils.PathDefine;

public class DestructionLotus extends BaseMonster {
    public static final String ID = DestructionLotus.class.getSimpleName();
    
    boolean awakened = false;
    
    public DestructionLotus(float x, float y, boolean awakened) {
        super(ID, PathDefine.MONSTER_PATH + ID + (awakened ? "_2" : "_1") + ".png", 0F, -15.0F, 140F, 150F, x, y);
        this.awakened = awakened;

        if (moreDamageAs) setDamages(4, 8);
        else setDamages(4, 7);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new SummonedPower(this)));
        addToBot(new ApplyPowerAction(this, this, new DestructionLotusPower(this)));
        if (specialAs)
            addToBot(new ApplyPowerAction(this, this, new AngryPower(this, 2)));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (nextMove) {
            case 1:
                addToBot(new AnimateSlowAttackAction(this));
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new VFXAction(new BossCrystalImpactEffect(p.hb.cX, p.hb.cY)));
                addToBot(new ApplyPowerAction(p, this, new DrawReductionPower(p, awakened ? 2 : 1), 1));
                break;
            case 2:
                addToBot(new AnimateFastAttackAction(this));
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                addToBot(new VFXAction(new BossCrystalImpactEffect(p.hb.cX, p.hb.cY)));
                addToBot(new ApplyPowerAction(p, this, new FrailPower(p, awakened ? 2 : 1, true), 1));
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        switch (turnCount % 2) {
            case 0:
                setMove(MOVES[0], (byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                break;
            case 1:
                setMove(MOVES[1], (byte) 2, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                break;
        }
        turnCount++;
    }

    @Override
    public void update() {
        super.update();
        this.animY = MathUtils.cosDeg((float) (System.currentTimeMillis() / 12L % 360L)) * 6.0F * Settings.scale;
    }
}
