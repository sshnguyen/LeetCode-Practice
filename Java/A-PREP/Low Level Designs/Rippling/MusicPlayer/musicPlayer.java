import java.util.*;
import java.util.Map.Entry;

class MusicPlayerAnalytics {

    private int songCounter; //song counter use for unique id for new songs
    private Map<Integer, Set<Integer>> userFavorites; // userID -> set of favorited songIDs
    private Map<Integer, String> songMap; // songID -> title
    private Map<Integer, Set<Integer>> songListeners; // Maps song ID to unique user listeners (that's why we used set)
    private Map<Integer, LinkedHashSet<Integer>> userPlayedSongs; // Maps user to songid played, linked hashset to preserve order and uniqueness of songs



    public MusicPlayerAnalytics() {
        this.songCounter = 1;
        this.songMap = new HashMap<>();
        this.songListeners = new HashMap<>();
        this.userPlayedSongs = new HashMap<>();
        this.userFavorites = new HashMap<>();
    }

    /* Adds a song and returns its unique ID,  A song is given an incremental integer ID when it's added, starting with 1.
        *CLARIFYING QUESTIONS: WHAT HAPPENS WHEN A SONG OF THE SAME TITLE IS ADDED? ALLOW DUPLICATE?
            *IF DUPLICATE IS NOT ALLOW WE NEED ANOTHER MAP FROM TITLE -> SONG_ID FOR QUICK LOOKUP OR traverse all SongMap to look for duplicate O(n)
    */

    public int addSong(String title) {
        int songID = songCounter++;
        songMap.put(songID, title);
        songListeners.put(songID, new HashSet<>());
        return songID;
    }

    /*  Assume any user ID is valid, and that the given song ID will have been added.
        * CLARIFYING QUESTION: CAN WE ASSUME USERID IS VALID?
        * CLARIFYING QUESTION: CAN WE ASSUME SONGID IS VALID? WE CAN CHECK songMAP for easy error checking.
    */
    
    public void playSong(int songId, int userId) {
        //check song exist;
        if (!songMap.containsKey(songId)) {
            System.out.println("Error: Song ID " + songId + " does not exist.");
            return;
        }
        //add user list of users that has played this song
        songListeners.get(songId).add(userId);

        // Ensure that the user has a list of played songs
        userPlayedSongs.putIfAbsent(userId, new LinkedHashSet<>());

        //update order of user played songs
        LinkedHashSet<Integer> playedSongs = userPlayedSongs.get(userId);
        // If song is already in the set, remove it to re-insert it as most recent
        playedSongs.remove(songId);
        // Add the song to the set (this ensures uniqueness and preserves the order)
        playedSongs.add(songId);

    }
    
    public void starSong(int userId, int songId) {
    if (!songMap.containsKey(songId)) {
        System.out.println("Error: Song ID " + songId + " does not exist.");
        return;
    }
    userFavorites.putIfAbsent(userId, new HashSet<>());
    userFavorites.get(userId).add(songId);
}

    public void unstarSong(int userId, int songId) {
        if (!userFavorites.containsKey(userId)) {
            System.out.println("User " + userId + " has no starred songs.");
            return;
        }
        userFavorites.get(userId).remove(songId);
    }

    /*The summary should be sorted (descending) by the number of unique users who have played each song.
       The summary should include the song titles, and the number of unique users, but the formatting does not matter.

       *CLARIFYING QUESTION: SORTING TIE-BREAKER? IF THE NUMBER OF UNIQUE LISTENERS ARE SAME, SORT BY SONG NAME ALPHABETICAL (ASC)
    */
    public void print_analytics_summary(){
        
        List<int[]> songStats = new ArrayList<>(); // int array [songId, number of unique listeners]
        for (Entry<Integer, Set<Integer>> entry : songListeners.entrySet()) {
            int songID = entry.getKey();
            int uniqueListeners = entry.getValue().size();
            songStats.add(new int[]{songID, uniqueListeners});
        }

        songStats.sort((a, b) -> {
            if (b[1] != a[1]) return b[1] - a[1]; // Sort by unique listeners (desc)
            return songMap.get(a[0]).compareTo(songMap.get(b[0])); // Sort by title (asc)
        });
        System.out.println("print_analytics_summary()");
        for (int[] stat : songStats) {
            System.out.println(songMap.get(stat[0]) + " (" + stat[1] + " unique listeners)");
        }
    }
    
    //EXTENSION: only the k most listened songs?
    //USE PRIORITYQUEUE MINHEAP -> O(n log k)
    public void print_analytics_summary(int k) {
    PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> {
        if (a[1] != b[1]) return a[1] - b[1]; // Sort by unique listeners (asc)
        return songMap.get(b[0]).compareTo(songMap.get(a[0])); // Sort by title (desc for tie-breaker)
    });

    for (Entry<Integer, Set<Integer>> entry : songListeners.entrySet()) {
        int songID = entry.getKey();
        int uniqueListeners = entry.getValue().size();
        
        minHeap.offer(new int[]{songID, uniqueListeners});
        
        // If heap size exceeds k, remove the smallest element
        if (minHeap.size() > k) {
            minHeap.poll();
        }
    }

    // Extract top k songs in descending order
    List<int[]> topSongs = new ArrayList<>();
    while (!minHeap.isEmpty()) {
        topSongs.add(minHeap.poll());
    }
    Collections.reverse(topSongs); // Reverse to get highest first

    // Print results
    System.out.println("print_analytics_summary(" +k +")");
    for (int[] stat : topSongs) {
        System.out.println(songMap.get(stat[0]) + " (" + stat[1] + " unique listeners)");
    }
}

    // Print the last k played songs of the user
    public void lastKPlayedSongTitles(int userID, int k) {
        LinkedHashSet<Integer> playedSongs = userPlayedSongs.get(userID);

        if (playedSongs != null && !playedSongs.isEmpty()) {
            System.out.println("User " + userID + "'s last " + k +" unique played songs:");

            // Convert to list to get last 3 elements, 
            List<Integer> songList = new ArrayList<>(playedSongs);
            int start = Math.max(songList.size() - k, 0); // Start index for last k songs

            for (int i = songList.size() - 1; i >= start; i--) {
                System.out.println(songMap.get(songList.get(i)));
            }
        } else {
            System.out.println("User " + userID + " has not played any songs.");
        }
    }
    
    // Print the k favourited songs of the user
    public void lastKFavoritePlayedSongs(int userId, int k) {
        LinkedHashSet<Integer> playedSongs = userPlayedSongs.get(userId);
        Set<Integer> favorites = userFavorites.getOrDefault(userId, Collections.emptySet());

        List<Integer> lastNFavorites = new ArrayList<>();
        List<Integer> playedList = new ArrayList<>(playedSongs);

        for (int i = playedList.size() - 1; i >= 0 && lastNFavorites.size() < k; i--) {
            int songId = playedList.get(i);
            if (favorites.contains(songId)) {
                lastNFavorites.add(songId);
            }
        }

        if (lastNFavorites.isEmpty()) {
            System.out.println("User " + userId + " has no favorite songs in their played history.");
            return;
        }

        System.out.println("User " + userId + "'s last " + k + " favorite songs played:");
        for (int songId : lastNFavorites) {
            System.out.println(songMap.get(songId));
        }
    }
}

public class Solution {

    public static void main(String[] args) {
        MusicPlayerAnalytics player = new MusicPlayerAnalytics();
        player.addSong("Song A");
        player.addSong("Song B");
        player.addSong("Song C");
        player.addSong("Song D");

        // Simulate plays
        player.playSong(1, 1111);
        player.playSong(2, 1111);
        player.playSong(3, 1111);
        player.playSong(4, 1111);
        player.playSong(1, 2222);
        player.playSong(2, 2222);
        player.playSong(1, 3333);
        player.playSong(3, 3333);

        // Print analytics summary
        player.print_analytics_summary();

        // Print the last three songs for User 1111
        player.lastKPlayedSongTitles(1111, 3);

        // Print top 2 most listened songs
        player.print_analytics_summary(2);

        // Star some songs
        player.starSong(1111, 1); // Song A
        player.starSong(1111, 3); // Song C

        // Print last 2 favorite played songs
        player.lastKFavoritePlayedSongs(1111, 2);

        // Unstar a song
        player.unstarSong(1111, 1); // Unstar Song A

        // Print again after unstarring
        player.lastKFavoritePlayedSongs(1111, 2);
    }
}
