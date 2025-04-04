package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.utils.ModHelper;

public class Grizzly extends BaseMonster {
    public static final String ID = Grizzly.class.getSimpleName();
    
    public Grizzly(float x, float y) {
        super(ID, 0F, -15.0F, 384, 384, x, y);
        
        if (ModHelper.specialAscension(type)) {
            turnCount = 1;
        } else {
            turnCount = 0;
        }
        
        if (ModHelper.moreDamageAscension(type)) {
            setDamages(10, 6);
        }
        else {
            setDamages(9, 6);
        }
        
        addSlot(x - 300, AbstractDungeon.monsterRng.random(-15, 15));
        addSlot(x + 300, AbstractDungeon.monsterRng.random(-15, 15));
        monFunc = slot -> new Spider(slot.x, slot.y);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (specialAs) {
            spawnMonsters();
        }
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (nextMove) {
            case 1:
                spawnMonsters();
                break;
            case 2:
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case 3:
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[4], 1), 1));
                break;
            case 4:
                if (hasPower(ChargingPower.POWER_ID)) {
                    addToBot(new AnimateJumpAction(this));
                    addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                break;
        }
        
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        switch (turnCount % 4) {
            case 0:
                if (AbstractDungeon.getMonsters().monsters.stream().noneMatch(c -> c instanceof Spider && !c.isDead)) {
                    setMove(MOVES[0], (byte) 1, AbstractMonster.Intent.UNKNOWN);
                    break;
                }
                else {
                    turnCount++;
                }
            case 1:
                setMove(MOVES[1], (byte) 2, AbstractMonster.Intent.ATTACK, this.damage.get(0).base);
                break;
            case 2:
                setMove(MOVES[2], (byte) 3, AbstractMonster.Intent.UNKNOWN);
                break;
            case 3:
                setMove(MOVES[3], (byte) 4, AbstractMonster.Intent.ATTACK, this.damage.get(1).base, 2, true);
                break;
        }
        turnCount++;
    }

    @Override
    public void die() {
        super.die();
    }
}
