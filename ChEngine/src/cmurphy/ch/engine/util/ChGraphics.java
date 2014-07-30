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

    public void gradientY(int col1, int col2) {
        int c1r = col1 / 0x10000;
        int c1g = col1 / 0x100 % 0x100;
        int c1b = col1 % 0x100;
        
        int c2r = col2 / 0x10000;
        int c2g = col2 / 0x100 % 0x100;
        int c2b = col2 % 0x100;

        int rd = (c2r - c1r);
        int gd = (c2g - c1g);
        int bd = (c2b - c1b);
        
        for (int i = 0; i < pixels.length; i++) {
            int rt = col1 + (int)(rd * (i / width) / (double)height);
            int gt = col1 + (int)(gd * (i / width) / (double)height);
            int bt = col1 + (int)(bd * (i / width) / (double)height);
            
            int coltemp = rt * 0x10000 + gt * 0x100 + bt;
            
            pixels[i] = coltemp;
        }
    }
    
    public void gradientX(int col1, int col2) {
        int c1r = col1 / 0x10000;
        int c1g = col1 / 0x100 % 0x100;
        int c1b = col1 % 0x100;
        
        int c2r = col2 / 0x10000;
        int c2g = col2 / 0x100 % 0x100;
        int c2b = col2 % 0x100;

        int rd = (c2r - c1r);
        int gd = (c2g - c1g);
        int bd = (c2b - c1b);
        
        for (int i = 0; i < pixels.length; i++) {
            int rt = col1 + (int)(rd * (i % width) / (double)width);
            int gt = col1 + (int)(gd * (i % width) / (double)width);
            int bt = col1 + (int)(bd * (i % width) / (double)width);
            
            int coltemp = rt * 0x10000 + gt * 0x100 + bt;
            
            pixels[i] = coltemp;
        }
    }

    public void gradientNXY(int col1, int col2) {
        // TODO incorrect algorithm
        int c1r = col1 / 0x10000;
        int c1g = col1 / 0x100 % 0x100;
        int c1b = col1 % 0x100;
        
        int c2r = col2 / 0x10000;
        int c2g = col2 / 0x100 % 0x100;
        int c2b = col2 % 0x100;

        int rd = (c2r - c1r);
        int gd = (c2g - c1g);
        int bd = (c2b - c1b);
        
        for (int i = 0; i < pixels.length; i++) {
            int rt = col1 + (int)(rd * (i % width + i / (double)width));
            int gt = col1 + (int)(gd * (i % width + i / (double)width));
            int bt = col1 + (int)(bd * (i % width + i / (double)width));
            
            int coltemp = rt * 0x10000 + gt * 0x100 + bt;
            
            pixels[i] = coltemp;
        }
    }
    
    public void gradientXY(int col1, int col2) {
        // TODO not implemented
        int c1r = col1 / 0x10000;
        int c1g = col1 / 0x100 % 0x100;
        int c1b = col1 % 0x100;
        
        int c2r = col2 / 0x10000;
        int c2g = col2 / 0x100 % 0x100;
        int c2b = col2 % 0x100;

        int rd = (c2r - c1r);
        int gd = (c2g - c1g);
        int bd = (c2b - c1b);
        
        for (int i = 0; i < pixels.length; i++) {
            int rt = col1 + (int)(rd * (i % width) / (double)width);
            int gt = col1 + (int)(gd * (i % width) / (double)width);
            int bt = col1 + (int)(bd * (i % width) / (double)width);
            
            int coltemp = rt * 0x10000 + gt * 0x100 + bt;
            
            pixels[i] = coltemp;
        }
    }
}
