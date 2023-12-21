package fi.johvu.motiwarp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Utils {


    public static Location stringToLocation(String string) {
        String world = string.split("/" )[0];
        String x = string.split("/" )[1];
        String y = string.split("/" )[2];
        String z = string.split("/" )[3];
        String pitch = string.split("/" )[4];
        String yaw = string.split("/" )[5];
        World world1 = Bukkit.getWorld(world);
        if (world1 !=  null)   {
            Location location = new Location(world1, Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));
            location.setPitch(Float.valueOf(pitch));
            location.setYaw(Float.valueOf(yaw));
            return location;
        }
        return null;
    }


    public static String locationToString(Location location) {
        String world = location.getWorld().getName();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float pitch = location.getPitch();
        float yaw = location.getYaw();
        return world + "/" + x + "/" + y + "/" + z + "/" + pitch + "/" + yaw;
    }

}
