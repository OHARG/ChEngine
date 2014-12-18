package cmurphy.ch.engine.core;

/**
 * The main body of the application, requires a driver class to start
 * @author Christopher Murphy
 */

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import cmurphy.ch.engine.util.ChGraphics;

public abstract class ChApplication extends Canvas implements Runnable {
    
    private static final long serialVersionUID = 1L;
    
    public static class Attribute {
        public static String title = "ChEngine";
        public static int width = 320;
        public static int height = (int) (width / 16.0 * 9); // 180
        public static int updatesPerSecond = 30;
        public static double scale = 2; // default scale, 640x360
        
        public static int backgroundColor = 0;
        public static int bufferStrategy = 3;
    }
    
    public static class Flag {
        public static boolean verbose = false;
        public static boolean clearScreenOnRefresh = true;
        public static boolean visible = false;
    }
    
    private Thread thread;
    private JFrame frame;
    private ChGraphics chGraphics;
    private boolean running;
    private long nanoTime;
    
    // create a drawable surface (Canvas uses Buffered image)
    private BufferedImage image = new BufferedImage(Attribute.width, Attribute.height, BufferedImage.TYPE_INT_RGB);
    // convert the image object into a raster (drawable)
    private int[] bufferPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    
    // constructor    
    protected ChApplication() {
        nanoTime = System.nanoTime();
        System.out.println("Initializing..." + Attribute.title);
        
        if(Flag.verbose) {
            System.out.println(Attribute.width + " x " + Attribute.height + " x " + 
                    Attribute.updatesPerSecond + ", scale: " + Attribute.scale);
        }
        Dimension size = new Dimension((int)(Attribute.width * Attribute.scale),
                                       (int)(Attribute.height * Attribute.scale));
        setPreferredSize(size);
        frame = new JFrame();
        chGraphics = new ChGraphics(Attribute.width, Attribute.height, Attribute.backgroundColor);
        chInitWindow();
        if(Flag.verbose) {
            System.out.println("Initialized! (" +
                    (int)((System.nanoTime() - nanoTime) / 1e3) / 1e3 +
                    " ms)");
        }
    }
    
    public synchronized void start() {
        running = true;
        init();
        thread = new Thread(this, Attribute.title + " (ChEngine)");
        thread.start();
    }

    public synchronized void stop() {
        System.out.println("Stopping...");
        running = false;
        frame.dispose();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() { // called by thread.start()
        long lastTime = System.nanoTime(); // first system time
        long now; // current system time
        double delta = 0; // time passed
        long timer = System.currentTimeMillis(); // 1 second timer for UPS counter

        int frames = 0; // how many calls to render()
        int updates = 0; // count how many calls to update()
        
        requestFocus();
        
        // print formated startup time: "Started! (000.000 ms)"
        System.out.println("Started! (" + (int)((System.nanoTime() - nanoTime) / 1e3) / 1e3 + " ms)");

        if(Flag.visible)
            frame.setVisible(true);
        
        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) * Attribute.updatesPerSecond / 1e9;
            lastTime = now;
            while (delta >= 1) {
                chUpdate();
                updates++;
                delta--;
            }
            chRender();
            frames++;
            
            // display ups and fps
            if (Flag.verbose && System.currentTimeMillis() - timer >= 1000) {
                timer += 1000; // increment second
                System.out.println(updates + " ups, " + frames + " fps");
                frame.setTitle(Attribute.title + " | " + updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
        }
        stop();
    }
    
    private void chUpdate() {
        update();
    }
    
    private void chRender() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(Attribute.bufferStrategy);
            return;
        }
        
        if(Flag.clearScreenOnRefresh)
            chGraphics.clearScreen();
        
        render(chGraphics);
        
        // get next frame
        for (int i = 0; i < chGraphics.pixels.length; i++) {
            bufferPixels[i] = chGraphics.pixels[i];
        }
        
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        draw(g);
        g.dispose();
        bs.show();
    }
    
    private void chInitWindow() {
        frame.setResizable(false);
        frame.setTitle(Attribute.title);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // centered
    }

    public long time() { return nanoTime; }
    
    protected void addToFrame(Component...components) {
        for(int i=0; i < components.length; i++) {
            frame.add(components[i]);
        }
        frame.pack();
    }
    
    /**
     * ChApplication.init() is called immediately after ChApplication.start() and may be
     * used to set ChApplication.Flag.visible to true (the field is false by default) or
     * load resources. The main application thread is started after this method returns.
     */
    protected abstract void init();
    
    protected abstract void update();
    protected abstract void render(ChGraphics g);
    protected abstract void draw(Graphics g);
}
