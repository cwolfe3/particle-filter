import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;

import javax.imageio.ImageIO;

public class MapLoader {

	public static int[][] loadMap(String fileName) throws IOException {
        URL url = MapLoader.class.getClassLoader().getResource(fileName);
		
		BufferedImage img = ImageIO.read(url);
		int[][] map = new int[img.getWidth()][img.getHeight()];
		Map<Color, Integer> colorMap = new HashMap<>();
		int nextId = 1;
		
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				Color c = new Color(img.getRGB(i, j));
				if (c.equals(Color.BLACK)) {
					map[i][j] = 0;
				} else {
					if (!colorMap.containsKey(c)) {
						colorMap.put(c, nextId);
						nextId++;
					}
					map[i][j] = colorMap.get(c);
				}
			}
		}
		
		return map;
	}
	
}
