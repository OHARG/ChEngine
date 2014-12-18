package cmurphy.ch.engine.util;

public class ChGraphics {
    private int width;
    private int height;
    private int defaultColor;
    
    public int[] pixels;
    
    public ChGraphics(int width, int height, int defaultColor) {
        this.width = width;
        this.height = height;
        this.defaultColor = defaultColor;
        pixels = new int[width * height];
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = defaultColor;
        }
    }

    public void clearScreen() {
        for (int i = 0; i < pixels.length; i++)
            pixels[i] = defaultColor;
    }
    
    public void clearScreen(int color) {
        for (int i = 0; i < pixels.length; i++)
            pixels[i] = color;
    }
    
    public void gradient(java.awt.Point p1, java.awt.Point p2, int col1, int col2) {
        if(p1.x > p2.x) {
            java.awt.Point tempPoint = p1;
            p1 = p2;
            p2 = tempPoint;
        }
        
        if(col1 > col2) {
            int tempCol = col1;
            col1 = col2;
            col2 = tempCol;
        }
        
        double phi = Math.atan((p1.y - p2.y) / (double)(p2.x - p1.x));
        double cosPhi = Math.cos(phi);
        double sinPhi = Math.sin(phi);
        
        int numCols = (int) ((p2.x - p1.x) * cosPhi - (p2.y - p1.y) * sinPhi);
        double rd = (col2 / 0x10000 - col1 / 0x10000) / (double)numCols;
        double gd = (col2 / 0x100 % 0x100 - col1 / 0x100 % 0x100) / (double)numCols;
        double bd = (col2 % 0x100 - col1 % 0x100) / (double)numCols;
        
        int x = 0, y = 0;
        double xx;
        int rt = 0, gt = 0, bt = 0;
        for (int i = 0; i < pixels.length; i++) {
            x = i % width - p1.x;
            y = i / width - p1.y;
            xx = x * cosPhi - y * sinPhi;
            
            if(xx < 0) {
                rt = col1;
                gt = col1;
                bt = col1;
            }
            else if(xx > numCols) {
                rt = col1 + (int)(rd * numCols);
                gt = col1 + (int)(gd * numCols);
                bt = col1 + (int)(bd * numCols);
            }
            else {
                rt = col1 + (int)(rd * xx);
                gt = col1 + (int)(gd * xx);
                bt = col1 + (int)(bd * xx);
            }
            
            pixels[i] = rt * 0x10000 + gt * 0x100 + bt;
        }
    }
}
