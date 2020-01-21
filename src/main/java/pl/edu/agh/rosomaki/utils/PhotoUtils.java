package pl.edu.agh.rosomaki.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import pl.edu.agh.rosomaki.model.Photo;
import pl.edu.agh.rosomaki.model.Tag;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PhotoUtils {
    /**
     * funkcja ładująca obraz z pliku o podanej ścieżce
     */
    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            // todo wyświetlić użytkownikowi komunikat o błędzie IO
            throw new RuntimeException(e);
        }
    }

    /**
     * funkcja konwertująca obiekt Photo (nasz model) na Image (javafx)
     */
    public static Image photoToFXImage(Photo photo) {
        BufferedImage image = photo.getImage();
        return SwingFXUtils.toFXImage(image, null);
    }

    /**
     * funkcja tworząca zbiór tagów z jednego łańcucha
     */
    public static Set<Tag> createTags(String tags) {
        return Arrays.stream(tags.split(", ")).map(Tag::new).collect(Collectors.toSet());
    }

    /**
     * funkcja odpakowująca zbiór tagów do zbioru stringów
     */
    public static Set<String> unpackTags(Set<Tag> tags) {
        return tags.stream().map(Tag::getName).collect(Collectors.toSet());
    }

}
