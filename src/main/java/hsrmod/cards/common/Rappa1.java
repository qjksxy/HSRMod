package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.utils.ModHelper;
import hsrmod.signature.utils.SignatureHelper;

public class Rappa1 extends BaseCard {
    public static final String ID = Rappa1.class.getSimpleName();

    int count = 0;
    boolean canRepeat = false;

    public Rappa1() {
        super(ID);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);
        count = 0;
        execute();
    }

    void execute() {
        canRepeat = true;
        
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) return;
        
        count++;
        if (count >= 4) {
            SignatureHelper.unlock(cardID, true);
        }

        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BreakEffectPower(AbstractDungeon.player, magicNumber), magicNumber));
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL).setCallback((c) -> {
            if (c.didBreak) {
                addToBot(new BreakDamageAction(c.target, new DamageInfo(AbstractDungeon.player, tr)));
                if (canRepeat) {
                    canRepeat = false;
                    ModHelper.addToTopAbstract(this::execute);
                }
            }
        }));
    }
}
