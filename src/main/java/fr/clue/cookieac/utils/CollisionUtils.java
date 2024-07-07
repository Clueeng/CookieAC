package fr.clue.cookieac.utils;

import com.viaversion.viaversion.api.Via;
import fr.clue.cookieac.CookieAC;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.Snow;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.util.Vector;

import java.util.concurrent.atomic.AtomicBoolean;

public class CollisionUtils {
    public static boolean accurateGround(Player player) {
        final double[] OFFSETS_X = { 0, 0.300, -0.300 };
        final double[] OFFSETS_Z = { 0, 0.300, -0.300 };

        Location playerLocation = player.getLocation().clone();
        Vector velocity = player.getVelocity().clone();
        playerLocation.add(velocity);
        for (double xOffset : OFFSETS_X) {
            for (double zOffset : OFFSETS_Z) { // 0.09800000190734863f
                Location checkLocation = playerLocation.clone().add(xOffset, -0.009800000190734863f, zOffset); //
                Block blockBelow = checkLocation.getBlock();
                //player.sendMessage(Component.text("shitty: solid:" + blockBelow.getType().isSolid() + " bbox: " + blockHasWeirdBoundingBox(blockBelow) + " boat: " + isPlayerOnTopOfBoat(player)));
                if ((blockBelow.getType().isSolid() || blockHasWeirdBoundingBox(blockBelow) || isPlayerOnTopOfBoat(player)) && !blockIsNotGroundBlackListed(blockBelow)) {
                    int ver = Via.getAPI().getPlayerVersion(player);
                    if(blockBelow.getBlockData() instanceof Snow){
                        // check for 1.17+
                        if(ver >= VersionUtil.versionMapping().get("1.17")){
                            player.sendMessage(Component.text("1.17+ : ver: " + ver + "solid:" + blockBelow.getType().isSolid() + " bbox: " + blockHasWeirdBoundingBox(blockBelow) + " boat: " + isPlayerOnTopOfBoat(player)));
                            return false;
                        }
                    }
                    player.sendMessage(Component.text("ver: " + ver + "solid:" + blockBelow.getType().isSolid() + " bbox: " + blockHasWeirdBoundingBox(blockBelow) + " boat: " + isPlayerOnTopOfBoat(player)));
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean blockIsNotGroundBlackListed(Block blockBelow) {
        return blockBelow.getBlockData() instanceof WallSign
                || blockBelow.getBlockData() instanceof Sign;
    }

    public static boolean isSolidBlockAbovePlayer(Player player, double offsetY, boolean fromHead) {
        final double[] OFFSETS_X = {0, 0.300, -0.300};
        final double[] OFFSETS_Z = {0, 0.300, -0.300};

        Location playerLocation = player.getLocation().clone();
        Vector velocity = player.getVelocity().clone();
        playerLocation.add(velocity);

        for (double xOffset : OFFSETS_X) {
            for (double zOffset : OFFSETS_Z) {
                Location checkLocation = playerLocation.clone().add(xOffset, fromHead ? offsetY + player.getHeight() : offsetY, zOffset);
                Block blockAbove = checkLocation.getBlock();
                //player.sendMessage(Component.text(String.valueOf(blockAbove)));
                if (blockAbove.getType().isSolid()) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean hasNoSolidBlocksAroundPlayer(Player player) {
        Location playerLocation = player.getLocation();
        int size = 5; // Size of the 5x5x5 area
        for (int x = -size / 2; x <= size / 2; x++) {
            for (int y = -size / 2; y <= size / 2; y++) {
                for (int z = -size / 2; z <= size / 2; z++) {
                    int yDiff = Math.abs(playerLocation.getBlockY() - (playerLocation.getWorld().getBlockAt(playerLocation.getBlockX() + x, playerLocation.getBlockY() + y, playerLocation.getBlockZ() + z).getY()));
                    if (yDiff <= 1) {
                        Block block = playerLocation.getWorld().getBlockAt(playerLocation.getBlockX() + x, playerLocation.getBlockY() + y, playerLocation.getBlockZ() + z);
                        if (!block.getType().isAir()) {
                            return false;
                        }
                    }
                }
            }
        }

        // If the loop completes without finding any solid blocks, return true
        return true;
    }

    public static Material groundMaterial(Player player) {
        final double[] OFFSETS_X = { 0, 0.300, -0.300 };
        final double[] OFFSETS_Z = { 0, 0.300, -0.300 };

        Location playerLocation = player.getLocation().clone();
        Vector velocity = player.getVelocity().clone();
        playerLocation.add(velocity);
        for (double xOffset : OFFSETS_X) {
            for (double zOffset : OFFSETS_Z) {
                Location checkLocation = playerLocation.clone().add(xOffset, player.getVelocity().getY() > 0 ? -0.09800000190734863f : player.getVelocity().getY(), zOffset); //
                Block blockBelow = checkLocation.getBlock();
                //player.sendMessage(Component.text("solid:" + blockBelow.getType().isSolid() + " bbox: " + blockHasWeirdBoundingBox(blockBelow) + " boat: " + isPlayerOnTopOfBoat(player)));
                if(blockBelow.getType().isSolid())return blockBelow.getType();

            }
        }
        return Material.AIR;
    }

    public static Material[] fences = {
            Material.BIRCH_FENCE,
            Material.BAMBOO_FENCE,
            Material.CHERRY_FENCE,
            Material.CRIMSON_FENCE,
            Material.MANGROVE_FENCE,
            Material.NETHER_BRICK_FENCE,
            Material.OAK_FENCE,
            Material.WARPED_FENCE,
            Material.CHERRY_FENCE_GATE,
            Material.BAMBOO_FENCE_GATE,
            Material.CRIMSON_FENCE_GATE,
            Material.MANGROVE_FENCE_GATE,
            Material.OAK_FENCE_GATE,
            Material.WARPED_FENCE_GATE,
            Material.ACACIA_FENCE,
            Material.DARK_OAK_FENCE,
            Material.JUNGLE_FENCE,
            Material. SPRUCE_FENCE,
            Material.ACACIA_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material. JUNGLE_FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            Material. DARK_OAK_FENCE_GATE,
            Material.ANDESITE_WALL,
            Material.BLACKSTONE_WALL,
            Material.BRICK_WALL,
            Material.COBBLED_DEEPSLATE_WALL,
            Material.COBBLESTONE_WALL,
            Material.ANDESITE_WALL
    };
    public static boolean standingOnFence(Player player) {
        final double[] OFFSETS_X = {0, 0.3, -0.3};
        final double[] OFFSETS_Z = {0, 0.3, -0.3};

        Location playerLocation = player.getLocation().clone();
        Vector velocity = player.getVelocity().clone();
        playerLocation.add(velocity);

        for (double xOffset : OFFSETS_X) {
            for (double zOffset : OFFSETS_Z) {
                Location checkLocation = playerLocation.clone().add(xOffset, -0.5, zOffset);
                Block blockBelow = checkLocation.getBlock();
                if (isFenceMaterial(blockBelow.getType(), player)) {
                    return true;
                }
            }
        }

        return false;
    }
    public static boolean onStairs(Player player) {
        final double[] OFFSETS_X = {0, 0.3, -0.3};
        final double[] OFFSETS_Z = {0, 0.3, -0.3};

        Location playerLocation = player.getLocation().clone();
        Vector velocity = player.getVelocity().clone();
        playerLocation.add(velocity);

        for (double xOffset : OFFSETS_X) {
            for (double zOffset : OFFSETS_Z) {
                Location checkLocation = playerLocation.clone().add(xOffset, -0.5, zOffset);
                Block blockBelow = checkLocation.getBlock();
                if (isStairsMaterial(blockBelow.getType())) {
                    return true;
                }
            }
        }

        return false;
    }


    public static boolean isStandingOnLilypad(Player player) {
        final double[] OFFSETS_X = {0, 0.300, -0.300};
        final double[] OFFSETS_Z = {0, 0.300, -0.300};
        final double LILYPAD_HEIGHT_OFFSET = 0.09375;

        Location playerLocation = player.getLocation().clone();
        Vector velocity = player.getVelocity().clone();
        //playerLocation.add(velocity);

        for (double xOffset : OFFSETS_X) {
            for (double zOffset : OFFSETS_Z) {
                Location checkLocation = playerLocation.clone().add(xOffset, -LILYPAD_HEIGHT_OFFSET, zOffset);
                Block blockBelow = checkLocation.getBlock();
                //player.sendMessage("y: " + checkLocation.getY() + " m: " + blockBelow.getType() + " py: " + playerLocation.getY());
                if (blockBelow.getType() == Material.LILY_PAD) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isFenceMaterial(Material material, Player player) {
        for (Material fence : fences) {
            if(material.equals(Material.AIR)){
                return false;
            }
            if (material.equals(fence)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isLayerBelowExempt(Player player) {
        Block blockBelow = player.getLocation().clone().subtract(0, 0.0625d, 0).getBlock().getRelative(0, 0, 0);
        Material blockType = blockBelow.getType();
        return exemptedMaterial(blockType) || blockHasWeirdBoundingBox(blockBelow);
    }

    public static boolean exemptedMaterial(Material blockType){
        return blockType == Material.COBWEB || blockType == Material.WATER || blockType == Material.LAVA;
    }

    private static boolean isStairsMaterial(Material type) {
        BlockData data = type.createBlockData();
        String materialName = data.getMaterial().name();
        return materialName.endsWith("stairs") || materialName.endsWith("STAIRS");
    }
    public static boolean blockHasWeirdBoundingBox(Block blockBelow){
        Material blockType = blockBelow.getType();

        // Check for blocks with irregular hitboxes
        switch (blockType) {
            default:
                return false;
            case CHEST:
            case LEGACY_CARPET:
            case BLACK_CARPET:
            case BLUE_CARPET:
            case BROWN_CARPET:
            case CYAN_CARPET:
            case GRAY_CARPET:
            case GREEN_CARPET:
            case CARVED_PUMPKIN:
            case LIGHT_BLUE_CARPET:
            case LIGHT_GRAY_CARPET:
            case LIME_CARPET:
            case MAGENTA_CARPET:
            case COCOA:
            case LADDER:
            case VINE:
            case TRAPPED_CHEST:
            case ENDER_CHEST:
            case LILY_PAD:
            case FLOWER_POT:
            case HOPPER:
            case BREWING_STAND:
            case BIRCH_FENCE:
            case BAMBOO_FENCE:
            case CHERRY_FENCE:
            case CRIMSON_FENCE:
            case MANGROVE_FENCE:
            case NETHER_BRICK_FENCE:
            case OAK_FENCE:
            case WARPED_FENCE:
            case CHERRY_FENCE_GATE:
            case BAMBOO_FENCE_GATE:
            case CRIMSON_FENCE_GATE:
            case MANGROVE_FENCE_GATE:
            case OAK_FENCE_GATE:
            case WARPED_FENCE_GATE:
            case ACACIA_FENCE:
            case DARK_OAK_FENCE:
            case JUNGLE_FENCE:
            case SPRUCE_FENCE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case ENCHANTING_TABLE:
            case DARK_OAK_DOOR:
            case ACACIA_DOOR:
            case BAMBOO_DOOR:
            case BIRCH_DOOR:
            case CHERRY_DOOR:
            case CRIMSON_DOOR:
            case JUNGLE_DOOR:
            case MANGROVE_DOOR:
            case OAK_DOOR:
            case SPRUCE_DOOR:
            case WARPED_DOOR:
            case IRON_DOOR:
            case ANVIL:
            case REPEATER:
            case COMPARATOR:
            case CAKE:
            case BLACK_BED:
            case BLUE_BED:
            case BROWN_BED:
            case CYAN_BED:
            case GRAY_BED:
            case GREEN_BED:
            case LIGHT_BLUE_BED:
            case LIGHT_GRAY_BED:
            case LIME_BED:
            case MAGENTA_BED:
            case ORANGE_BED:
            case PINK_BED:
            case PURPLE_BED:
            case RED_BED:
            case WHITE_BED:
            case YELLOW_BED:
            case ANDESITE_WALL:
            case BLACKSTONE_WALL:
            case BRICK_WALL:
            case COBBLESTONE_WALL:
            case COBBLED_DEEPSLATE_WALL:
            case DEEPSLATE_BRICK_WALL:
            case DIORITE_WALL:
            case DEEPSLATE_TILE_WALL:
            case GRANITE_WALL:
            case END_STONE_BRICK_WALL:
            case MOSSY_COBBLESTONE_WALL:
            case MUD_BRICK_WALL:
            case MOSSY_STONE_BRICK_WALL:
            case NETHER_BRICK_WALL:
            case POLISHED_BLACKSTONE_BRICK_WALL:
            case POLISHED_BLACKSTONE_WALL:
            case PRISMARINE_WALL:
            case POLISHED_DEEPSLATE_WALL:
            case RED_NETHER_BRICK_WALL:
            case SANDSTONE_WALL:
            case RED_SANDSTONE_WALL:
            case STONE_BRICK_WALL:
            case SKELETON_SKULL:
            case SKELETON_WALL_SKULL:
            case WITHER_SKELETON_SKULL:
            case WITHER_SKELETON_WALL_SKULL:
            case SNOW:
            case END_PORTAL:
            case END_PORTAL_FRAME:
            case SOUL_SAND:
                return true;
        }
    }
    public static boolean isPlayerOnTopOfBoat(Player player) {
        Location playerLocation = player.getLocation();
        double playerY = playerLocation.getY();
        AtomicBoolean d = new AtomicBoolean(false);

        Bukkit.getScheduler().runTask(CookieAC.getInstance(),r -> {
            for (Entity entity : player.getNearbyEntities(0.3, 1, 0.3)) {
                if (entity instanceof Vehicle && entity.getType().name().contains("BOAT")) {
                    Location boatLocation = entity.getLocation();
                    double boatY = boatLocation.getY();
                    double boatMaxY = boatY + entity.getHeight();
                    if (playerY >= boatMaxY - 0.0625 && playerY <= boatMaxY + 0.0625) {
                        d.set(true);
                    }
                }
            }
        });

        return d.get();
    }

    public static boolean nearGround(Player player) {
        return calculateDistanceToGround(player) < 0.2;
    }
    public static boolean nearCeiling(Player player) {
        return calculateDistanceToCeiling(player) < 0.2;
    }
    public static double calculateDistanceToCeiling(Player player) {
        final double[] OFFSETS_X = {0, 0.3, -0.3};
        final double[] OFFSETS_Z = {0, 0.3, -0.3};
        Location playerLocation = player.getLocation().clone();
        double startY = playerLocation.getY() + player.getHeight(); // Start from the player's head height
        double currentY = startY; // Begin checking just above the player's head
        double stepSize = 0.125; // Step size for each iteration
        double maxCheckDistance = 5.0; // Maximum distance to check (3 blocks)

        while (currentY < 256 && (currentY - startY) <= maxCheckDistance) {
            boolean ceilingFound = false;
            for (double xOffset : OFFSETS_X) {
                for (double zOffset : OFFSETS_Z) {
                    Location checkLocation = playerLocation.clone();
                    checkLocation.add(xOffset, 0, zOffset);
                    checkLocation.setY(currentY);
                    Block blockAbove = checkLocation.getBlock();
                    Material blockMaterial = blockAbove.getType();
                    if (blockMaterial.isSolid()) {
                        ceilingFound = true;
                        break;
                    }
                }
                if (ceilingFound) break; // Exit inner loop once ceiling is found
            }
            if (ceilingFound) {
                // Calculate the distance from the player's head to the ceiling
                double distance = currentY - startY;
                return distance;
            }

            // Move up another layer
            currentY += stepSize; // Increment by the step size
        }

        // If no ceiling was found within 3 blocks, return a special value or handle accordingly
        return Double.MAX_VALUE;
    }
    public static double calculateDistanceToGround(Player player) {
        final double[] OFFSETS_X = {0, 0.3, -0.3};
        final double[] OFFSETS_Z = {0, 0.3, -0.3};
        Location playerLocation = player.getLocation().clone();
        double startY = playerLocation.getY();
        double currentY = startY - 0.125; // Increased step size
        double maxCheckDistance = 3 * 1.0; // Maximum distance to check (5 blocks)

        while (currentY > -255 && (startY - currentY) <= maxCheckDistance) {
            boolean groundFound = false;
            for (double xOffset : OFFSETS_X) {
                for (double zOffset : OFFSETS_Z) {
                    Location checkLocation = playerLocation.clone();
                    checkLocation.add(xOffset, 0, zOffset);
                    checkLocation.setY(currentY);
                    Block blockBelow = checkLocation.getBlock();
                    Material blockMaterial = blockBelow.getType();
                    if (blockMaterial.isSolid()) {
                        groundFound = true;
                        break;
                    }
                }
                if (groundFound) break; // Exit inner loop once ground is found
            }
            if (groundFound) {
                // Calculate the distance from the player's starting Y position to the ground
                double distance = startY - currentY - 0.125; // Adjusted for larger step size
                return distance;
            }

            // Move down another layer
            currentY -= 0.125; // Increased step size
        }

        // If no ground was found within 5 blocks, return a special value or handle accordingly
        return Double.MAX_VALUE;
    }
    public static double calculateDistanceToSlimeBlock(Player player) {
        final double[] OFFSETS_X = {0, 0.3, -0.3};
        final double[] OFFSETS_Z = {0, 0.3, -0.3};
        Location playerLocation = player.getLocation().clone();
        double startY = playerLocation.getY();
        double currentY = startY - 0.125; // Increased step size
        double maxCheckDistance = 3 * 1.0; // Maximum distance to check (5 blocks)

        while (currentY > -255 && (startY - currentY) <= maxCheckDistance) {
            boolean groundFound = false;
            for (double xOffset : OFFSETS_X) {
                for (double zOffset : OFFSETS_Z) {
                    Location checkLocation = playerLocation.clone();
                    checkLocation.add(xOffset, 0, zOffset);
                    checkLocation.setY(currentY);
                    Block blockBelow = checkLocation.getBlock();
                    Material blockMaterial = blockBelow.getType();
                    if (blockMaterial == Material.SLIME_BLOCK) {
                        groundFound = true;
                        break;
                    }
                }
                if (groundFound) break; // Exit inner loop once ground is found
            }
            if (groundFound) {
                // Calculate the distance from the player's starting Y position to the ground
                double distance = startY - currentY - 0.125; // Adjusted for larger step size
                return distance;
            }

            // Move down another layer
            currentY -= 0.125; // Increased step size
        }

        // If no ground was found within 5 blocks, return a special value or handle accordingly
        return Double.MAX_VALUE;
    }
}
