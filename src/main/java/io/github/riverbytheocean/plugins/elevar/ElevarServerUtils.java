package io.github.riverbytheocean.plugins.elevar;

import io.github.riverbytheocean.plugins.elevar.game.GamePeriod;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.io.*;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

public class ElevarServerUtils {

    /*
    Code here are from other projects, mainly DiffuseHyperion's GameMaster library! https://github.com/DiffuseHyperion/GameMaster
     */

    public static boolean hasGameStarted() {
        return !(Elevar.getPeriod().equals(GamePeriod.RESET) || Elevar.getPeriod().equals(GamePeriod.LOBBY));
    }

    /**
     * Check and edits common server properties.
     * @param disableSpawnProtection Disable spawn protection?
     * @param enableFlight Disable minecraft's anticheat? (it sucks)
     * @return If a change was required.
     */
    public static boolean checkForServerProperties(boolean disableSpawnProtection, boolean enableFlight) throws IOException {
        boolean neededChange = false;
        if (disableSpawnProtection) {
            neededChange = checkAndEditServerProperties("spawn-protection", "0", "spawn-protection=\\d+", "spawn-protection=0");
        }
        if (enableFlight) {
            neededChange = checkAndEditServerProperties("allow-flight", "true", "allow-flight=[a-zA-Z]+", "allow-flight=true");
        }
        return neededChange;
    }

    /**
     * reads and returns a file's contents as a string!
     * @param file the file that will be read.
     */
    public static String readFile(File file) {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) {
                builder.append(line).append(System.lineSeparator());
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return builder.toString();
    }

    /**
     * Read a property in server.properties!
     * @param propertyName the property's name
     * @return The property's value
     */
    public static String readServerProperties(String propertyName) throws IOException {
        File propertiesFile = new File(Bukkit.getWorldContainer(), "server.properties");
        FileInputStream stream = new FileInputStream(propertiesFile);
        Properties properties = new Properties();
        properties.load(stream);
        return properties.getProperty(propertyName);
    }

    /**
     * Check and edit a property in the server's server.properties.
     * @param propertyToCheck The property in the file to check.
     * @param correctConfig What should propertyToCheck be.
     * @param oldContent What an old line could look like. This should be a regex. (Example: spawn-protection=\\d+)
     * @param newContent What should the correct line look like.
     * @return If a change was required.
     */
    public static boolean checkAndEditServerProperties(String propertyToCheck, String correctConfig, String oldContent, String newContent) throws IOException {
        if (!Objects.equals(readServerProperties(propertyToCheck), correctConfig)) {
            File propertiesFile = new File(Bukkit.getWorldContainer(), "server.properties");
            String newcontent = readFile(propertiesFile).replaceAll(oldContent, newContent);
            writeFile(newcontent, propertiesFile);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Creates a lobby world that players are transported to while the world is deleted and created.
     *
     * Use PlayerJoinEvent to teleport people into the world, using p.teleport(World.getSpawnLocation());
     *
     * @return Returns the created world, or if a world already exists with the provided name, the existing world.
     */
    public static World createLobbyWorld() {
        WorldCreator worldcreator = new WorldCreator("elevar_lobby");
        worldcreator.seed(0);
        worldcreator.environment(World.Environment.NORMAL);
        worldcreator.type(WorldType.FLAT);
        worldcreator.generatorSettings("{\"layers\":[{\"block\":\"minecraft:barrier\",\"height\":99}],\"biome\":\"minecraft:the_void\"}");
        return worldcreator.createWorld();
    }

    /**
     * Creates a world.
     * <p>
     * The world should not exist, or this method will return that world.
     * <p>
     * Use PlayerJoinEvent to teleport people into the world, using p.teleport(World.getSpawnLocation());
     * @param worldName The name of the created world.
     * @param seed The seed for the world.
     * @return Returns the created world, or if a world already exists with the provided name, the existing world.
     */
    public static World createWorld(String worldName, Long seed, World.Environment env, WorldType type) {
        WorldCreator worldcreator = new WorldCreator(worldName);
        worldcreator.seed(seed);
        worldcreator.environment(env);
        worldcreator.type(type);
        return worldcreator.createWorld();
    }

    /**
     * Creates a world. The world will be a normal overworld.
     * <p>
     * The world should not exist, or this method will return that world.
     * <p>
     * Use PlayerJoinEvent to teleport people into the world, using p.teleport(World.getSpawnLocation());
     * @see #createWorld(String, Long, World.Environment, WorldType)
     * @param worldName The name of the created world.
     * @return Returns the created world, or if a world already exists with the provided name, the existing world.
     */
    public static World createWorld(String worldName, Long seed) {
        return createWorld(worldName, seed, World.Environment.NORMAL, WorldType.NORMAL);
    }

    /**
     * Creates a world. It will use a random seed.
     * <p>
     * The world should not exist, or this method will return that world.
     * <p>
     * Use PlayerJoinEvent to teleport people into the world, using p.teleport(World.getSpawnLocation());
     * @see #createWorld(String, Long, World.Environment, WorldType)
     * @param worldName The name of the created world.
     * @return Returns the created world, or if a world already exists with the provided name, the existing world.
     */
    public static World createWorld(String worldName) {
        return createWorld(worldName, new Random().nextLong());
    }

    /**
     * Creates a world. It's name will be the one specified under `level-name` in server.properties. It will use a random seed.
     * <p>
     * The world should not exist, or this method will return that world.
     * <p>
     * Use PlayerJoinEvent to teleport people into the world, using p.teleport(World.getSpawnLocation());
     * @see #createWorld(String, Long, World.Environment, WorldType)
     * @return Returns the created world, or if a world already exists with the provided name, the existing world.
     */
    public static World createWorld() throws IOException {
        return createWorld(readServerProperties("level-name"), new Random().nextLong());
    }

    /**
     * Deletes a world with the specified world name!
     * @param worldName The name of the world that will be deleted.
     */
    public static void deleteWorld(String worldName) {
        assert worldName != null;
        File oldworld = new File(Bukkit.getWorldContainer().getAbsolutePath() + "/" + worldName);
        do {
            Bukkit.unloadWorld(worldName, false);
            FileUtils.deleteQuietly(oldworld);
        } while (oldworld.exists());
    }

    /**
     * Deletes the world specified in the server properties!
     */
    public static void deleteWorld() throws IOException {
        deleteWorld(readServerProperties("level-name"));
    }

    /**
     * Replace EVERYTHING in a file with a string.
     * Use line terminators if you want to use additional lines.
     * @see lineTerminators
     * @param content The content to write.
     * @param file The file to be written to.
     */
    public static void writeFile(String content, File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }

    /**
     * Line terminators for writing files.
     * @see #writeFile(String, File)
     */
    public enum lineTerminators {
        /**
         * Line terminator for windows.
         */
        Windows("\r\n"),
        /**
         * Line terminator for unix.
         */
        Unix("\n");

        private final String lineTerminators;
        lineTerminators(String str) {lineTerminators = str;}
        public String toString() {return lineTerminators;}
    }

}
