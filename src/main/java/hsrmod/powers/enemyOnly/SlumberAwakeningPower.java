package hsrmod.powers.enemyOnly;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.TheBeyond.SomethingUntoDeath;
import hsrmod.powers.BuffPower;
import hsrmod.powers.StatePower;
import hsrmod.utils.ModHelper;

import java.util.List;

public class SlumberAwakeningPower extends StatePower {
    public static final String POWER_ID = HSRMod.makePath(SlumberAwakeningPower.class.getSimpleName());
    
    List<AbstractCard> cards;

    public SlumberAwakeningPower(AbstractCreature owner, List<AbstractCard> cards) {
        super(POWER_ID, owner);
        this.cards = cards;
        amount = cards.size();
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], cards.toString());
    }

    public void onDeath() {
        if (AbstractDungeon.getMonsters().monsters.stream()
                .anyMatch(m -> m instanceof SomethingUntoDeath && ModHelper.check(m))) {
            int handSize = BaseMod.MAX_HAND_SIZE;
            for (AbstractCard card : cards) {
                if (AbstractDungeon.player.hand.size() < handSize) {
                    this.addToBot(new MakeTempCardInHandAction(card, false, true));
                } else {
                    this.addToBot(new MakeTempCardInDiscardAction(card, true));
                }
                handSize--;
            }
        } else {
            for (AbstractCard card : cards) {
                CardCrawlGame.sound.play("CARD_EXHAUST");
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, (Settings.WIDTH * MathUtils.random(0.2f, 0.8f)), (Settings.HEIGHT * MathUtils.random(0.2f, 0.8f))));
                AbstractDungeon.player.masterDeck.removeCard(card.cardID);
            }
        }
    }
}
