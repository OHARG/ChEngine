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

        double phi = Math.atan((p1.y - p2.y) / (double)(p2.x - p1.x));
        double cosPhi = Math.cos(phi);
        double sinPhi = Math.sin(phi);
        
        int numCols = (int) ((p2.x - p1.x) * cosPhi - (p2.y - p1.y) * sinPhi);
        double rd = (col2 / 0x10000 - col1 / 0x10000) / (double)numCols;
        double gd = (col2 / 0x100 % 0x100 - col1 / 0x100 % 0x100) / (double)numCols;
        double bd = (col2 % 0x100 - col1 % 0x100) / (double)numCols;
        
        for (int i = 0; i < pixels.length; i++) {
            int x = i % width - p1.x;
            int y = i / width - p1.y;
            double xx = x * cosPhi - y * sinPhi;
            if(xx < 0) xx = 0;
            if(xx > numCols) xx = numCols;
            int rt = col1 + (int)(rd * xx);
            int gt = col1 + (int)(gd * xx);
            int bt = col1 + (int)(bd * xx);
            
            pixels[i] = rt * 0x10000 + gt * 0x100 + bt;
        }
    }
}
