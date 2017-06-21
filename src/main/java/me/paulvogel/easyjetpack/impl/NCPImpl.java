package main.java.me.paulvogel.easyjetpack.impl;

import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import main.java.me.paulvogel.easyjetpack.CheatPluginAdapter;
import fr.neatmonster.nocheatplus.checks.CheckType;

public class NCPImpl extends CheatPluginAdapter {

	@Override
	public void exemptPlayer(Player player, Type type) {
        switch (type) {
        case FLY:
            NCPExemptionManager.exemptPermanently(player, CheckType.MOVING_SURVIVALFLY);
            break;
        case NOCLIP:
            NCPExemptionManager.exemptPermanently(player, CheckType.MOVING_NOFALL);
            break;
    }
	}

	@Override
	public void unexemptPlayer(Player player, Type type) {
		switch (type) {
		case FLY:
			NCPExemptionManager.unexempt(player, CheckType.MOVING_SURVIVALFLY);
			break;
		case NOCLIP:
			NCPExemptionManager.unexempt(player, CheckType.MOVING_NOFALL);
			
		}

	}

	@Override
	public boolean isExempted(Player player, Type type) {
		switch (type) {
		case FLY:
			NCPExemptionManager.isExempted(player, CheckType.MOVING_SURVIVALFLY);
			break;
		case NOCLIP:
			NCPExemptionManager.isExempted(player, CheckType.MOVING_NOFALL);
			break;
		}
		return false;
	}

	@Override
	public boolean isPluginEnabled() {
        return Bukkit.getServer().getPluginManager().getPlugin("NoCheatPlus") != null;
	}

}
