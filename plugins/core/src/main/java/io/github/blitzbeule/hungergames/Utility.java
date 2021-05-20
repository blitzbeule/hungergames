package io.github.blitzbeule.hungergames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Calendar;
import java.util.Random;

public class Utility {

    public static void shutdownServer(JavaPlugin plugin) {
        Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not load state");
        Bukkit.getServer().getLogger().severe(ChatColor.RED + "Going to shutdown server.");
        Bukkit.getServer().broadcastMessage(ChatColor.RED + "Internal error occurred. The server will shut down in 10s");
        Bukkit.getScheduler().runTaskLater((Plugin) plugin, () -> Bukkit.shutdown(), 200L);
    }

    public static class NameGenerator {
        private static final int diffBetweenAtoZ = 25;
        private static final int charValueOfa = 97;
        private String lastGeneratedName = "";
        int length;

        char[] vowels = {
                'a', 'e', 'i', 'o', 'u'
        };

        public NameGenerator(int lengthOfName) {
            if (lengthOfName < 5 || lengthOfName > 10) {
                System.out.println("Setting default length to 7");
                lengthOfName = 7;
            }

            this.length = lengthOfName;
        }

        public String getName() {
            for (;;) {
                Random randomNumberGenerator = new Random(Calendar.getInstance()
                        .getTimeInMillis());

                char[] nameInCharArray = new char[length];

                for (int i = 0; i < length; i++) {
                    if (positionIsOdd(i)) {
                        nameInCharArray[i] = getVowel(randomNumberGenerator);
                    } else {
                        nameInCharArray[i] = getConsonant(randomNumberGenerator);
                    }
                }
                nameInCharArray[0] = (char) Character
                        .toUpperCase(nameInCharArray[0]);

                String currentGeneratedName = new String(nameInCharArray);

                if (!currentGeneratedName.equals(lastGeneratedName)) {
                    lastGeneratedName = currentGeneratedName;
                    return currentGeneratedName;
                }

            }

        }

        private boolean positionIsOdd(int i) {
            return i % 2 == 0;
        }

        private char getConsonant(Random randomNumberGenerator) {
            for (;;) {
                char currentCharacter = (char) (randomNumberGenerator
                        .nextInt(diffBetweenAtoZ) + charValueOfa);
                if (currentCharacter == 'a' || currentCharacter == 'e'
                        || currentCharacter == 'i' || currentCharacter == 'o'
                        || currentCharacter == 'u')
                    continue;
                else
                    return currentCharacter;
            }

        }

        private char getVowel(Random randomNumberGenerator) {
            return vowels[randomNumberGenerator.nextInt(vowels.length)];
        }
    }

}
