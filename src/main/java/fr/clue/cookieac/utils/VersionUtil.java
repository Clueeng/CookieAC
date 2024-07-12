package fr.clue.cookieac.utils;

import com.viaversion.viaversion.api.Via;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class VersionUtil {

    public static boolean playerAboveVersion(String checkingVersion, Player player){
        int viaVer = getViaVer(player);
        int cheVer = versionMapping().get(checkingVersion);
        return viaVer >= cheVer;
    }

    public static int getViaVer(Player player){
        return Via.getAPI().getPlayerVersion(player);
    }
    public static String formattedVersion(int ver){
        for(String s : versionMapping().keySet()){
            if(versionMapping().get(s).equals(ver)){
                return s;
            }
        }
        return "Unknown";
    }

    public static HashMap<String, Integer> versionMapping(){
        HashMap<String, Integer> h = new HashMap<>();
        h.put("1.21", 767);
        h.put("1.20.6", 766);
        h.put("1.20.5", 766);
        h.put("1.20.4", 765);
        h.put("1.20.3", 765);
        h.put("1.20.2", 764);
        h.put("1.20.1", 763);
        h.put("1.20", 763);
        h.put("1.19.4", 762);
        h.put("1.19.3", 761);
        h.put("1.19.2", 760);
        h.put("1.19.1", 760);
        h.put("1.19", 759);
        h.put("1.18.2", 758);
        h.put("1.18.1", 757);
        h.put("1.18", 757);
        h.put("1.17.1", 756);
        h.put("1.17", 755);
        h.put("1.16.5", 754);
        h.put("1.16.4", 754);
        h.put("1.16.3", 753);
        h.put("1.16.2", 752);
        h.put("1.16.1", 751);
        h.put("1.16", 750);
        h.put("1.15.2", 749);
        h.put("1.15.1", 748);
        h.put("1.15", 747);
        h.put("1.14.4", 746);
        h.put("1.14.3", 745);
        h.put("1.14.2", 744);
        h.put("1.14.1", 743);
        h.put("1.14", 742);
        h.put("1.13.2", 741);
        h.put("1.13.1", 740);
        h.put("1.13", 739);
        h.put("1.12.2", 738);
        h.put("1.12.1", 737);
        h.put("1.12", 736);
        h.put("1.11.2", 735);
        h.put("1.11.1", 735);
        h.put("1.11", 734);
        h.put("1.10", 733);
        h.put("1.9.4", 732);
        h.put("1.9.3", 731);
        h.put("1.9.2", 730);
        h.put("1.9.1", 729);
        h.put("1.9", 728);
        h.put("1.8", 727); // WYSI AHAHAHAH
        h.put("1.7.10", 726);
        return h;
    }
}
