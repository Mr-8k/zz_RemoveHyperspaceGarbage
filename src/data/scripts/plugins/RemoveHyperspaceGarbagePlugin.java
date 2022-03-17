package data.scripts.plugins;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignTerrainAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.impl.campaign.ghosts.SensorGhostManager;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.velfield.SlipstreamManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Objects;


public class RemoveHyperspaceGarbagePlugin extends BaseModPlugin {

    public static Logger log = Global.getLogger(RemoveHyperspaceGarbagePlugin.class);


    @Override
    public void onGameLoad(boolean newGame) {

        SectorAPI sector = Global.getSector();

        if (sector.hasScript(SlipstreamManager.class)) {
            sector.removeScriptsOfClass(SlipstreamManager.class);
            log.info("Removed slipstream manager.");
        }

        //boolean playerInHyperspace = Global.getSector().getPlayerFleet().isInHyperspace();

        //if (!playerInHyperspace) {
        if (sector.hasScript(SensorGhostManager.class)) {
            sector.removeScriptsOfClass(SensorGhostManager.class);
            Global.getSector().getMemoryWithoutUpdate().unset("$ghostManager");
            log.info("Removed ghost manager.");
        }
        //} else log.info("Ghost manager not removed, please load game outside of hyperspace.");

        LocationAPI hyperspace = Global.getSector().getHyperspace();
        List<CampaignTerrainAPI> allTerrain = Global.getSector().getHyperspace().getTerrainCopy();
        for (CampaignTerrainAPI terrain: allTerrain) {
            if (Objects.equals(terrain.getType(), Terrain.SLIPSTREAM)){
                hyperspace.removeEntity(terrain);
                log.info("Removed slipstream at: " + terrain.getLocationInHyperspace());
            }
        }

    }
}