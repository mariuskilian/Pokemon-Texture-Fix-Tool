package Default;

import java.util.*;
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PTFT {

    public static void runMainProgram() {
        final Map<String, List<String>> pngListMap = seperatePngLists(getPngList());
        for (final String key : pngListMap.keySet()) {
            final List<String> pngList = pngListMap.get(key);
            final String mergedPath = (Main.createEyeMerged) ? createMergedTexture(pngList) : "";
            if (mergedPath != "")
                pngList.add(mergedPath);
            fixTextures(pngList);
        }
    }

    private static void fixTextures(final List<String> pngList) {
        for (int i = 0; i < pngList.size(); i++) {
            String old_path = pngList.get(i);

            final String[] splitPath = old_path.split("\\\\");
            String filename = splitPath[splitPath.length - 1];
            if (!filename.startsWith(Main.prefix)) continue;
            String[] filenameending = formatFileName(filename);
            String path = old_path.replace(splitPath[splitPath.length - 1], "");
            String path_fixed = path;
            path += filenameending[0] + filenameending[1];

            if (Main.locationFixed == 0) path_fixed += "Fixed_";
            path_fixed += filenameending[0];
            if (Main.locationFixed == 1) path_fixed += "_Fixed";
            path_fixed += filenameending[1];

            final BufferedImage texture = readTexture(old_path);
            if (texture != null) {
                final BufferedImage texture_fixed = fixUpTexture(texture);
                switch (Main.formatFileName) {
                    case 0:
                        break;
                    case 1:
                        rename(old_path, path);
                        break;
                    case 2:
                        writeNewTexture(texture, Main.ending, path);
                        if (Main.deleteOldFiles) deleteTexture(old_path);
                        break;
                    default:
                        break;
                }
                writeNewTexture(texture_fixed, Main.ending, path_fixed);
            }
        }
    }

    private static void rename(String path_old, String path_new) {
        File file_old = new File(path_old);
        File file_new = new File(path_new);
        file_old.renameTo(file_new);
    }

    private static String createMergedTexture(final List<String> pngList) {
        String eyePath = "";
        String irisPath = "";
        String normalPath = "";
        String alphaPath = "";

        for (final String path : pngList) {
            if (path.contains("Eye_Merged"))
                return "";
            if (path.contains("Eye1"))
                eyePath = path;
            if (path.contains("Iris") && irisPath == "")
                irisPath = path;
            if (path.contains("EyeNor"))
                normalPath = path;
            if (path.contains("EyeAlpha"))
                alphaPath = path;
        }

        if (eyePath == "" || irisPath == "" || (eyePath == "" && alphaPath == ""))
            return "";
        final String maskPath = (alphaPath != "") ? alphaPath : normalPath;

        final BufferedImage eye = readTexture(eyePath);
        final BufferedImage iris = readTexture(irisPath);
        final BufferedImage mask = readTexture(maskPath);

        final int maskOffset = mask.getWidth() / 4;

        final BufferedImage merged = new BufferedImage(eye.getWidth(), eye.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < merged.getWidth(); x++) {
            final int irisX = (x < merged.getWidth() / 2) ? iris.getWidth() - 1 - x : x % iris.getWidth();
            for (int y = 0; y < merged.getHeight(); y++) {
                final int irisY = y % iris.getHeight();
                final Color c_eye = new Color(eye.getRGB(x, y), true);
                final Color c_iris = new Color(iris.getRGB(irisX, irisY), true);
                final Color c_mask = new Color(mask.getRGB(x + maskOffset, y), true);

                float factor = ((float) c_iris.getAlpha() / 255f) * ((float) c_mask.getAlpha() / 255f);
                int red = (int) ((factor * c_iris.getRed()) + ((1-factor) * c_eye.getRed()));
                int green = (int) ((factor * c_iris.getGreen()) + ((1-factor) * c_eye.getGreen()));
                int blue = (int) ((factor * c_iris.getBlue()) + ((1-factor) * c_eye.getBlue()));
                int alpha = Math.max(c_eye.getAlpha(), c_iris.getAlpha());
                
                Color c_merged = new Color(red, green, blue, alpha);
                merged.setRGB(x, y, c_merged.getRGB());
            }
        }

        final String mergedPath = eyePath.replace("Eye1", "Eye1_Merged");
        writeNewTexture(merged, "png", mergedPath);
        return mergedPath;
    }

    private static void writeNewTexture(final BufferedImage texture, final String imagetype, final String path) {
        final File file = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (final FileNotFoundException e) {
        }

        try {
            ImageIO.write(texture, imagetype, file);
            fos.close();
        } catch (final IOException e) {
        }
    }

    private static void deleteTexture(final String path) {
        final File file = new File(path);
        file.delete();
    }

    private static BufferedImage readTexture(final String path) {
        final File file = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (final FileNotFoundException e) {
        }

        BufferedImage texture = null;
        try {
            texture = ImageIO.read(file);
            fis.close();
        } catch (final IOException e) {
        }

        return texture;
    }

    private static BufferedImage fixUpTexture(final BufferedImage texture) {
        final BufferedImage texture_fixed = new BufferedImage(texture.getWidth() * 2, texture.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < texture.getWidth(); x++) {
            for (int y = 0; y < texture_fixed.getHeight(); y++) {
                texture_fixed.setRGB(x, y, texture.getRGB(x, y));
                texture_fixed.setRGB(texture_fixed.getWidth() - 1 - x, y, texture.getRGB(x, y));
            }
        }
        return texture_fixed;
    }

    private static String[] formatFileName(String filename) {
        final Map<String, String> pokemonnames = Pokedex.makePokemonNamesMap();
        final Map<String, String> texturetypes = Pokedex.makeTextureTypeMap();
        final Map<String, String> variants = Pokedex.makeVariantsMap();

        final String pokecode = filename.substring(3, 6);

        final int fileendingIndex = filename.indexOf(".tga") + 4;
        String fileending = ".tga";
        if (fileendingIndex < filename.length()) {
            fileending = filename.substring(fileendingIndex);
        }
        filename = filename.substring(0, fileendingIndex);

        if (Main.formatFileName != 0) {

            for (final Object key : texturetypes.keySet()) {
                final String key_str = key.toString();
                if (filename.contains(key_str + ".tga")) {
                    if (filename.contains("Iris")) {
                        filename = filename.replace(".tga", "");
                    } else {
                        filename = filename.replace(key_str + ".tga", "_" + texturetypes.get(key_str));
                    }
                }
            }

            filename = (pokemonnames.containsKey(pokecode)) ? filename.replace("pm0" + pokecode, pokecode + "_" + pokemonnames.get(pokecode))
                : filename.replace("pm0" + pokecode, pokecode);
            for (String key : variants.keySet()) {
                filename = filename.replace("_" + key + "_", "_" + variants.get(key) + "_");
            }
        }

        return new String[]{filename, fileending};
    }

    private static Map<String, List<String>> seperatePngLists(final List<String> pngList) {
        final Map<String, List<String>> pngListMap = new HashMap<>();
        for (final String path : pngList) {
            String[] split_path = path.split("\\\\");
            String filename = split_path[split_path.length - 1];
            final String key = (filename.length() >= 10) ? path.replace(filename, filename.substring(0, 10)) : "misc";
            if (pngListMap.containsKey(key)) {
                pngListMap.get(key).add(path);
            } else {
                final List<String> list = new ArrayList<>();
                list.add(path);
                pngListMap.put(key, list);
            }
        }
        return pngListMap;
    }

    private static List<String> getPngList() {
        final File folder = new File(System.getProperty("user.dir"));
        final List<String> result = new ArrayList<>();
        String pattern = ".*\\";
        pattern += (Main.ending.contains(".")) ? Main.ending : "." + Main.ending;
        search(pattern, folder, result);
        return result;
    }

    public static void search(final String pattern, final File folder, final List<String> result) {
        for (final File f : folder.listFiles()) {
            if (f.isDirectory() && Main.affectSubfolders) {
                search(pattern, f, result);
            }
            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    result.add(f.getAbsolutePath());
                }
            }
        }
    }
}