ChEngine
========

Quick-start Java application skeleton for simulation and gaming. ChEngine is driven by a runtime loop, making use of one method for behind-the-scenes program logic and another for rendering images to the screen. 

Get the latest build [here](https://drive.google.com/folderview?id=0B1V0LqDDmha_UUhjNEpTczlaNnM&usp=drive_web).


Usage
-----
The core of ChEngine is _ChApplication_, an abstract class that can be extended directly or indirectly via _ChApplicationAdapter_. The adapter class is a child of _ChApplication_ and implements all the required methods with an empty definiton, acknowledging that not all of the capabilities offered by ChEngine are practical in every program solution.

The current abstract methods in _ChApplication_ are _init_, _update_, _render_, and _draw_.

- _void init()_
Allows the execution of code before the main thread is started (via _start()_). Typical usage is to load resources, add additional components to the frame via _addToFrame(Component...components)_ in _ChApplication_, and to set the application visibility (default is invisible, or _ChApplication.Attribute.visible_ = false).

- _void update()_
Often called _tick_ in other projects, this method is called a given number of times per second (updates per second / ups) and handles the logic of the program.

- _void render(ChGraphics g)_
Draw graphics using the custom ChGraphics context g. Some standard methods in ChGraphics include clearing the screen and setting a background gradient.

- _void draw(Graphics g)_
Draw graphics using the standard java.awt.Graphics context g.

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
Creates an animated background gradient and then automatically closes the program after five seconds.
```
import cmurphy.ch.engine.core.*;
import cmurphy.ch.engine.util.ChGraphics;

public class ChEngineDemo extends ChApplicationAdapter {
    
    private static final long serialVersionUID = 1L;

    protected void init() {
        Flag.visible = true;
    }

    protected void update() {
        if(System.nanoTime() - time() > 5e9)
            stop();
    }
    
    protected void render(ChGraphics g) {
        g.gradient(java.awt.Point(0, 0),
                   java.awt.Point(ChApplication.Attribute.width, ChApplication.Attribute.height),
                   0x70F4E5,
                   0xF4707F);
    }
}
```
