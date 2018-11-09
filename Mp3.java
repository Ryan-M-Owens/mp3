/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package mp3;

//import java.nio.file.DirectoryStream;
//import java.nio.file.Paths;
import java.util.*;
import java.nio.file.*;

//import java.nio.file.Files;
//import java.nio.file.Path;

/**
 *
 * @author rmowens
 */
public class Mp3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        
        if(args.length != 1)
        {
            throw new IllegalArgumentException("you need to specifiy a vaild mp3 directory");
        }
        
        String directory = args[0];
        Path mp3Directory = Paths.get(directory);
        
        if(!Files.exists(mp3Directory))
        {
            throw new IllegalArgumentException("specified directory does not exist:" + mp3Directory);
        }
        
        List<Path> mp3Paths = new ArrayList<>();
        
        try(DirectoryStream<Path> paths = Files.newDirectoryStream(mp3Directory, "*.mp3"))
        {
            paths.forEach(p -> {
                System.out.println("Found: " + p.getFileName().toString());
                mp3Paths.add(p);              
            });
        }
        
        List<Song> songs = mp3Paths.stream().map(path -> {
            try {
                Mp3File mp3file = new Mp3File(path);
                ID3v2 id3 = mp3file.getId3v2Tag();
                return new Song(id3.getArtist(), id3.getYear(), id3.getAblum(), id3.getTitle());
            }catch(IOException | UnsuportedTagException | InvalidDataException e)
            {
                throw new IllegalStateException(e);
            }
        });
}
    
    public static class Song {
        
        private final String artist;
        private final String year;
        private final String album;
        private final String title;
        
        public Song(String artist, String year, String album, String title) {
            this.artist = artist;
            this.year = year;
            this.title = title;
            this.album = album;
        }
        
        public String getArtist()
        {
            return artist;
        }
        
        public String getYear()
        {
            return year;
        }
        
        public String getTitle()
        {
            return title;
        }
        
        public String getAlbum()
        {
            return album;
        }
    }
    
}
