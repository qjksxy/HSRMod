package hsrmod.relics.common;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

public class VileMechanicalSatellite900 extends BaseRelic {
    public static final String ID = VileMechanicalSatellite900.class.getSimpleName();
    public static final int DISCOUNT = 25; // %

    public VileMechanicalSatellite900() {
        super(ID);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        if (room instanceof ShopRoom) {
            beginLongPulse();
            ModHelper.addEffectAbstract(()->{
                AbstractDungeon.shopScreen.applyDiscount(1 - DISCOUNT/100f, false);
            });
        } else {
            stopPulse();
        }
    }
}
