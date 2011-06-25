package com.FriedTaco.taco.MorePhysics;


	import java.io.File;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.util.logging.Level;
	import java.util.logging.Logger;
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.Properties;
	import org.bukkit.entity.Boat;
	import org.bukkit.entity.Player;
	import org.bukkit.event.Event.Priority;
	import org.bukkit.event.Event;
	import org.bukkit.event.player.PlayerLoginEvent;
	import org.bukkit.plugin.PluginDescriptionFile;
	import org.bukkit.plugin.java.JavaPlugin;
	import org.bukkit.plugin.PluginManager;
	import com.nijiko.permissions.PermissionHandler;
	import com.nijikokun.bukkit.Permissions.Permissions;
	import org.bukkit.plugin.Plugin;
	import org.yaml.snakeyaml.Yaml;
	import org.yaml.snakeyaml.constructor.SafeConstructor;




	public class MorePhysics extends JavaPlugin {
		public static final Logger log = Logger.getLogger("Minecraft");
		private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();  
	    private final MorePhysicsPlayerListener playerListener  = new MorePhysicsPlayerListener(this);
	    private final MorePhysicsVehicleListener VehicleListener = new MorePhysicsVehicleListener(this);
		public static ArrayList<Boat> sinking = new ArrayList<Boat>();
		@SuppressWarnings("unused")
		private static Yaml yaml = new Yaml(new SafeConstructor());
		public static PermissionHandler Permissions;
		public boolean movement,swimming,boats;		
		static String mainDirectory = "plugins/MorePhysics";
		static File config = new File(mainDirectory + File.separator + "config.dat");
		static Properties properties = new Properties(); 

	   
		 private void setupPermissions() {
		      Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
		      if (MorePhysics.Permissions == null) 
		      {
		          if (test != null) {
		              MorePhysics.Permissions = ((Permissions)test).getHandler();
		              System.out.println("[MorePhysics] Permissions detected. Now using permissions.");
		          } else {
		             System.out.println("[MorePhysics] Permissions NOT detected. Giving permission to ops.");
		          }
		      }
		  }
		 
		 public void loadSettings() throws Exception
		 {
			 if (!this.getDataFolder().exists())
			 {
				 	this.getDataFolder().mkdirs();
			 }
			 final String dir = "plugins/MorePhysics";
		        if (!new File(dir + File.separator + "MorePhysics.properties").exists()) {
		            FileWriter writer = null;
		            try {
		                writer = new FileWriter(dir + File.separator + "MorePhysics.properties");
		                writer.write("MorePhysics v 1.0 configuration\r\n\n");
		                writer.write("#Allow boats to sink\r\n");
		                writer.write("BoatsSink=true \r\n\n");
		                writer.write("#Allow armour to affect movement on land\r\n");
		                writer.write("MovementAffected=true\r\n");
		                writer.write("#Allow armour to affect movement in water\r\n");
		                writer.write("SwimmingAffected=true\r\n\n");
		                
		                } catch (Exception e) {
		                log.log(Level.SEVERE,
		                        "Exception while creating MorePhysics.properties", e);
		                try {
		                    if (writer != null)
		                        writer.close();
		                } catch (IOException ex) {
		                    log
		                            .log(
		                                    Level.SEVERE,
		                                    "Exception while closing writer for MorePhysics.properties",
		                                    ex);
		                }
		            } finally {
		                try {
		                    if (writer != null)
		                        writer.close();
		                } catch (IOException e) {
		                    log
		                            .log(
		                                    Level.SEVERE,
		                                    "Exception while closing writer for MorePhysics.properties",
		                                    e);
		                }
		            }
		        }
		        PropertiesFile properties = new PropertiesFile(dir + File.separator + "MorePhysics.properties");
		        try {
		          boats = properties.getBoolean("BoatsSink", true);
		          swimming = properties.getBoolean("MovementAffected", true);
		          movement = properties.getBoolean("SwimmingAffected", true);
		        } catch (Exception e) {
		            log.log(Level.SEVERE,
		                    "Exception while reading from MorePhysics.properties", e);
		        }

		 }
	    public void onDisable() {
	    }
	    @Override
	    public void onEnable() {
	    	try {
				loadSettings();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        PluginManager pm = getServer().getPluginManager();
	        pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.VEHICLE_DAMAGE, VehicleListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.VEHICLE_DESTROY, VehicleListener, Priority.Normal, this);
	        pm.registerEvent(Event.Type.VEHICLE_MOVE, VehicleListener, Priority.Normal, this);
	        PluginDescriptionFile pdfFile = this.getDescription();
	        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
	        setupPermissions();
	    }

	    public boolean isDebugging(final Player player) {
	        if (debugees.containsKey(player)) {
	            return debugees.get(player);
	        } else {
	            return false;
	        }
	    }

	    public void setDebugging(final Player player, final boolean value) {
	        debugees.put(player, value);
	    }

		public void recordEvent(PlayerLoginEvent event) {
			// TODO Auto-generated method stub
			
		}
	}



