package data.scripts.plugins;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignEventListener;
import com.fs.starfarer.api.campaign.CampaignTerrainAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.impl.campaign.ghosts.SensorGhostManager;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.velfield.SlipstreamManager;
import org.apache.log4j.Logger;
import scripts.RHSG_Hostility_Remover;

import java.util.List;
import java.util.Objects;


public class RemoveHyperspaceGarbagePlugin extends BaseModPlugin {

    public static Logger log = Global.getLogger(RemoveHyperspaceGarbagePlugin.class);


    @Override
    public void onGameLoad(boolean newGame) {

        SectorAPI sector = Global.getSector();

        if(Global.getSettings().getBoolean("RHSG_removeSlipstreams")) {
            if (sector.hasScript(SlipstreamManager.class)) {
                sector.removeScriptsOfClass(SlipstreamManager.class);
                log.info("Removed slipstream manager.");
            }
            LocationAPI hyperspace = Global.getSector().getHyperspace();
            List<CampaignTerrainAPI> allTerrain = Global.getSector().getHyperspace().getTerrainCopy();
            for (CampaignTerrainAPI terrain : allTerrain) {
                if (Objects.equals(terrain.getType(), Terrain.SLIPSTREAM)) {
                    hyperspace.removeEntity(terrain);
                    log.info("Removed slipstream at: " + terrain.getLocationInHyperspace());
                }
            }
        }

        //boolean playerInHyperspace = Global.getSector().getPlayerFleet().isInHyperspace();

        //if (!playerInHyperspace) {
        if(Global.getSettings().getBoolean("RHSG_removeGhosts"))
            if (sector.hasScript(SensorGhostManager.class)) {
                sector.removeScriptsOfClass(SensorGhostManager.class);
                Global.getSector().getMemoryWithoutUpdate().unset("$ghostManager");
                log.info("Removed ghost manager.");
            }
        //} else log.info("Ghost manager not removed, please load game outside of hyperspace.");

/*        if(Global.getSettings().getBoolean("RHSG_removeHyperspaceTerrain")) {
            LocationAPI hyperspace = Global.getSector().getHyperspace();
            List<CampaignTerrainAPI> allTerrain = Global.getSector().getHyperspace().getTerrainCopy();
            for (CampaignTerrainAPI terrain : allTerrain) {
                if (Objects.equals(terrain.getType(), Terrain.HYPERSPACE)) {
                    hyperspace.removeEntity(terrain);
                    log.info("Removed hyperspace at: " + terrain.getLocationInHyperspace());
                }
            }
        }*/

        if(Global.getSettings().getBoolean("RHSG_removeHostileActivityProgress")) {
            log.info("RHSG_removeHostileActivityProgress is true");
            sector = Global.getSector();
            if (!sector.getListenerManager().hasListenerOfClass(RHSG_Hostility_Remover.class)) {
                sector.getListenerManager().addListener(new RHSG_Hostility_Remover());
                log.info("Hostile activity suppression listener added.");
            } else {
                log.info("Hostile activity suppression listener is present.");
                //sector.getListenerManager().removeListenerOfClass(RHSG_Hostility_Remover.class);
                //sector.getListenerManager().addListener(new RHSG_Hostility_Remover());
            }
        } else log.info("RHSG_removeHostileActivityProgress is false");

    }
}