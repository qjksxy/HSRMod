package hsrmod.cardsV2.Erudition;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.WallopEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.utils.ModHelper;

import java.util.Comparator;

public class TheHerta1 extends BaseCard {
    public static final String ID = TheHerta1.class.getSimpleName();

    public int totalDmg = 0;

    public TheHerta1() {
        super(ID);
        setBaseEnergyCost(220);
        tags.add(CustomEnums.ENERGY_COSTING);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);

        totalDmg = 0;
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
                .setCallback(ci -> {
                    totalDmg += ci.info.output;
                    addToTop(new VFXAction(new WallopEffect(ci.info.base, ci.target.hb.cX, ci.target.hb.cY)));
                }));
        int useCount = 0;
        if (p.hand.group.stream().anyMatch(c -> c.hasTag(CustomEnums.ENERGY_COSTING) && c != this)) useCount++;
        if (upgraded && p.hand.group.stream().anyMatch(c -> (c.target == CardTarget.ALL_ENEMY || c.target == CardTarget.ALL) && c != this))
            useCount++;

        AbstractMonster monsterWithMostHealth = AbstractDungeon.getMonsters().monsters.stream()
                .filter(mo -> !mo.isDeadOrEscaped() && mo.currentHealth > 0 && !mo.halfDead)
                .max(Comparator.comparingInt(m2 -> m2.currentHealth))
                .orElse(null);
        if (monsterWithMostHealth != null) {
            for (int i = 0; i < useCount; i++) {
                addToBot(new ElementalDamageAction(monsterWithMostHealth, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_VERTICAL)
                        .setCallback(ci -> {
                            totalDmg += ci.info.output;
                            addToTop(new VFXAction(new WallopEffect(ci.info.base, ci.target.hb.cX, ci.target.hb.cY)));
                        }));
            }
        }

        if (!SignatureHelper.isUnlocked(cardID))
            ModHelper.addToBotAbstract(() -> {
                if (totalDmg == 42) {
                    SignatureHelper.unlock(cardID, true);
                }
            });
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        int val = 0;
        if (AbstractDungeon.player.hand.group.stream().anyMatch(c -> c.hasTag(CustomEnums.ENERGY_COSTING) && c != this)) {
            val++;
        }
        if (upgraded && AbstractDungeon.player.hand.group.stream().anyMatch(c -> (c.target == CardTarget.ALL_ENEMY || c.target == CardTarget.ALL) && c != this)) {
            val++;
        }
        if (val == 0) {
            glowColor = BLUE_BORDER_GLOW_COLOR;
        } else if (val == 1) {
            glowColor = GREEN_BORDER_GLOW_COLOR;
        } else {
            glowColor = GOLD_BORDER_GLOW_COLOR;
        }
    }
}
