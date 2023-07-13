package scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.impl.campaign.intel.events.HostileActivityEventIntel;
import org.apache.log4j.Logger;

public class RHSG_Hostility_Remover implements EconomyTickListener {
    public static Logger log = Global.getLogger(RHSG_Hostility_Remover.class);
    @Override
    public void reportEconomyTick(int iterIndex) {
        if(Global.getSettings().getBoolean("RHSG_removeHostileActivityProgress")) {
            if (HostileActivityEventIntel.get() != null) {
                HostileActivityEventIntel.get().setProgress(0);
                log.info("Hostile activity suppressed");
            } else log.info("Hostile activity inactive");
        }
    }

    @Override
    public void reportEconomyMonthEnd() {

    }
}
