package asw1013.ui;

import java.awt.Image;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 * Download and keep in memory an image until there is unused space in the JVM heap
 * 
 * This class is a singleton (only one instance per JVM is allowed).
 * 
 * @author mattia
 */
public class SoftReferenceImageCache {
    
    private volatile static SoftReferenceImageCache instance = null;
    
    private Map<URL,SoftReference<Image>> imagesMap = new HashMap<URL,SoftReference<Image>>();
    
    
    public static SoftReferenceImageCache getInstance(){
        if(instance == null){
            synchronized(SoftReferenceImageCache.class){
                if(instance == null){
                    instance = new SoftReferenceImageCache();
                }
            }
        }
        return instance;
    }
    
    private SoftReferenceImageCache(){
        
    }
    
    /**
     * Get an image from the cache or download it if it's not already present 
     */
    public Image getImage(URL imageUri){
        SoftReference<Image> imgRef = imagesMap.get(imageUri);
        Image img = null;
        if(imgRef == null || (img = imgRef.get()) == null){
            img = downloadImage(imageUri);
            imgRef = new SoftReference<Image>(img);
            imagesMap.put(imageUri, imgRef);
        }
        return img;
    }
    
    private Image downloadImage(URL imageUrl){
        ImageIcon imgIcon = new ImageIcon(imageUrl);
        Image img = imgIcon.getImage();
        return img;
    }
}
