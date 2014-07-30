ChEngine
========

Quick-start Java application skeleton for simulation and gaming. ChEngine is driven by a runtime loop, making use of one method for behind-the-scenes program logic and another for rendering images to the screen. 


Usage
-----
The core of ChEngine is _ChApplication_, an abstract class that can be extended directly or indirectly via _ChApplicationAdapter_. The adapter class is a child of _ChApplication_ and implements all the required methods with an empty definiton, acknowledging that not all of the capabilities offered by ChEngine are practical in every program solution.

The current abstract methods in _ChApplication_ are _init_, _update_, _render_, and _draw_.

- _void init()_
Allows the execution of code before the main thread is started (via _start()_). Typical usage is to load resources, add additional components to the frame via _addToFrame(Component...components)_ in _ChApplication_, and to set the application visibility (default is invisible, or _ChApplication.Attribute.visible_ = false).

- _void update()_
Often called _tick_ in other projects, this method is called a given number of times per second (updates per second / ups) and handles the logic of the program.

- _void render(ChGraphics g)_
Description missing. (for now) :-/

- _void draw(Graphics p)_
Description missing. (for now) :-)

_Summary_ :
- To use ChEngine, extend either _ChApplication_ or _ChApplicationAdapter_ located in _cmurphy.ch.engine.core_.
- Five methods define the program lifecycle, _init_, _update_, _render_, _draw_, and _stop_. (actually not _stop_ yet!!)
- _ChApplcation_ has various static fields in the inner classes _Attribute_ and _Flag_ which are directly configurable.
- To start the application, instantiate a child of ChApplication and call _start()_. See the _Launcher.java_ and _ChEngineDemo.java_ below.

Launcher.java
-------------
When ready to launch the program, a launcher class is recommended. Below is an example of a launcher class.
```
import cmurphy.ch.engine.core.ChApplication;

public class Launcher {
    public static void main(String[] args) {
        ChApplication.Flag.verbose = true; // prints additional information to the console
        ChApplication demo = new ChEngineDemo(); // where ChEngineDemo is a child of ChApplication
        demo.start();
    }
}
```
ChEngineDemo.java
-----------------
Description missing. (for now) :-)
```
import cmurphy.ch.engine.core.*;
import cmurphy.ch.engine.util.ChGraphics;

public class ChEngineDemo extends ChApplicationAdapter {
    
    private static final long serialVersionUID = 1L;

    protected void init() {
        Flag.visible = true;
    }

    protected void update() {
        if(System.nanotime() - time() > 5e9)
            stop();
    }
    
    protected void render(ChGraphics g) {
        drawGradient(g, 0x70F4E5, 0xF4707F);
    }
    
    private void drawGradient(ChGraphics g, int col1, int col2) {
        int c1r = col1 / 0x10000;
        int c1g = col1 / 0x100 % 0x100;
        int c1b = col1 % 0x100;
        
        int c2r = col2 / 0x10000;
        int c2g = col2 / 0x100 % 0x100;
        int c2b = col2 % 0x100;

        int rd = (c2r - c1r);
        int gd = (c2g - c1g);
        int bd = (c2b - c1b);
        
        for (int i = 0; i < g.pixels.length; i++) {
            int rt = col1 + (int)(rd * (i / g.width) / (double)g.height);
            int gt = col1 + (int)(gd * (i / g.width) / (double)g.height);
            int bt = col1 + (int)(bd * (i / g.width) / (double)g.height);
            
            int coltemp = rt * 0x10000 + gt * 0x100 + bt;
            
            g.pixels[i] = coltemp;
        }
    }
}
```
