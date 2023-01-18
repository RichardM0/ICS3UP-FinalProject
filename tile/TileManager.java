package tile;

import java.io.IOException;

import main.GamePanel;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;

public class TileManager {
    
    GamePanel gp;
    Tile[] tile;

    

    public TileManager(GamePanel gp){

        this.gp = gp;

        tile = new Tile[10];

        getTileImage();
    }

    public void getTileImage() {

        try {
                tile[0] = new Tile();
                tile[0].image = ImageIO.read(getClass().getResourceAsStream("/main/sky.png"));
                tile[1] = new Tile();
                tile[1].image = ImageIO.read(getClass().getResourceAsStream("/main/earth.png"));
        }
        catch(IOException e) {
            e.printStackTrace();

        }
    }
    //method to draw background
    public void draw(Graphics2D g2) {

        int tilesWidth = (int)gp.maxWidth/gp.tileSize;
        int tilesHeight = (int)gp.maxHeight/gp.tileSize;
        for(int i=0;i<tilesWidth;i++){
            for(int k=0;k<tilesHeight-2;k++){
                g2.drawImage(tile[0].image, 0 + i*gp.tileSize, (0 + k*gp.tileSize), gp.tileSize, gp.tileSize, null);
            }
        }

    }
}
