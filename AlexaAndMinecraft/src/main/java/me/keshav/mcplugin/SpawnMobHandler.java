package me.keshav.mcplugin;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnMobHandler {

    public Main plugin;

    public SpawnMobHandler(Main plugin) {
        this.plugin = plugin;
    }

    public void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (!Bukkit.getOnlinePlayers().isEmpty()) {
                Player player = Bukkit.getOnlinePlayers().stream().skip((int)
                        (Bukkit.getOnlinePlayers().size() * Math.random())).findFirst().orElse(null);
                execute(player);
            }
        }, 40, (20 * 15));
    }

    public void execute(Player player) {
        Table table = getDBTable();
        int count = getScanRequest().getCount();

        if (count > 0) {
            World world = player.getWorld();
            Location loc = player.getLocation();

            double radius = Math.random() * 10;
            double radius2 = radius * 2;
            double offset = (Math.random() * radius2) - radius;
            loc.add(offset, 0, offset);

            EntityType[] mobs = {EntityType.ZOMBIE, EntityType.CREEPER, EntityType.SKELETON, EntityType.SPIDER, EntityType.ENDERMAN};

            ScanResult mobRequest = getScanItem();
            String mob = mobRequest.getItems().get(0).get("SignalId").getS();
            Entity s =  null;
            switch (mob) {
                case "zombie":
                    s = world.spawnEntity(loc, mobs[0]);
                    break;
                case "creeper":
                    s = world.spawnEntity(loc, mobs[1]);
                    break;
                case "skeleton":
                    s = world.spawnEntity(loc, mobs[2]);
                    break;
                case "spider":
                    s = world.spawnEntity(loc, mobs[3]);
                    break;
                case "enderman":
                    s = world.spawnEntity(loc, mobs[4]);
                    break;
                default:
                    s = world.spawnEntity(loc, mobs[(int) (Math.random() * 4)]);
            }

            player.sendMessage(mob + " Spawned - Alexa request");
            table.deleteItem("SignalId", mob);
        }
    }

    public Table getDBTable() {
        AmazonDynamoDB client = getClient();
        DynamoDB dynamoDB = new DynamoDB(client);

        return dynamoDB.getTable("Minecraft_Signal");
    }

    public ScanResult getScanRequest() {
        AmazonDynamoDB client = getClient();
        ScanRequest scanRequest = new ScanRequest()
                .withTableName("Minecraft_Signal")
                .withConsistentRead(true);

        return client.scan(scanRequest);
    }

    public ScanResult getScanItem() {
        AmazonDynamoDB client = getClient();
        ScanRequest scanRequest = new ScanRequest()
                .withTableName("Minecraft_Signal")
                .withConsistentRead(true)
                .withLimit(1);

        return client.scan(scanRequest);
    }

    public AmazonDynamoDB getClient() {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.AP_SOUTHEAST_2)
                .build();
    }
}
