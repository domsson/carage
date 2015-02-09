carage
======
This is the result of a university assignment (computer graphics course).
We¹ decided to go for a lot more than what was asked of us. Although we
did not implement all the things we put on our list, we still ended up with
what you could call a very limited 3D engine on top of LWJGL/OpenGL.
![carage screen](http://i.imgur.com/7tPYgi3.png "Yep, that's Carage")

Controls
--------
- `W` `S` `A` `D`: Spin / Steer Wheels (Car won't move though)
- `F`: Toggle Light **F**lickering
- `O`: Toggle Camera **O**verlay
- `L`: Toggle **L**amp (Turn light on/off)
- `P`: Toggle Camera Auto-**P**anning
- `+` `-`: Increase / Decrease Light Intensity
- `←` `→`: Pan Camera left/right (when Auto-Panning is off)
- `↑` `↓`: Pitch Camera up/down

Original tasks (as assigned by the Professor)
----------------------------------------------
- ~~At least two different 3D-Objects~~ **✓**
- ~~Use of transformations (translate, rotate, scale) for a time-based animation~~ **✓**
- ~~Interactivity via keyboard or mouse~~ **✓**
- ~~At least one textured face~~ **✓**
- ~~Vertex and fragment shader for Phong shading~~ **✓**
- ~~Vertex and fragment shader for a simple procedural texture~~ **✓**
- ~~Use of a custom matrix class for the shaders (the OpenGL 3 way)~~ **✓**
- ~~Implementation of other OpenGL 3 techniques (models as VAO/VBO, GLSL from version 1.3+)~~ **✓**

Our own (additional) goals
--------------------------
- ~~Use git to collaborate on the project~~  **✓**
- ~~Simple Wavefront .OBJ loader~~ **✓**
- ~~OpenGL 3+ features only~~ **✓**
- ~~VAO/VBO with IBO (Index buffer object)~~ **✓**
- ~~Re-use textures, shaders and geometry wherever possible~~ **✓**
- ~~Have a light source with customizable intensity~~ **✓**
- ~~Make it possible to move the light source~~ **✓**
- ~~Have a camera object that eases the pseudo camera movement~~ **✓**
- ~~Simple materials to set amb./diff./spec. reflectivity per object~~ **✓**
- ~~A central Renderer instead of objects that draw themselves~~ **✓**
- Throw exceptions instead of silently tolerating broken data
- Get rid of `AbstractSimpleBase`; write own versions of the `lenz` code
- Ability to have more than one light source
- Light attenuation with distance, maybe
- Ability to have more than one texture / material per asset
- Proper asset groups (A scene graph, probably?)
- Switching car parts (car configurator)
- Actual game mode where you can drive the car!

Notes on engine design
----------------------
- Models are expected to be in OBJ format (v, vt, vn and f will be read)
- The axle height of a car body should be aligned with the Y-axis
- An object's texture has to go by the same name as the object
- A car has a `.properties` file with the same name as its object
- Every object can only have one single texture / material :(
- The Renderer always assumes the VAO/VBO data to hold Tris
- The Renderer can handle pure VAOs as well as VAOs with IBOs
- The Renderer assumes that every Asset is unwrapped and textured

Copyright / License
-------------------
You may use all code from within the `carage` package in any way you want.
All of this code has been written by us¹ or is from sources that freely
distribute their code as well (such code should have a link to the source
in the preceding comment). Code in the `lenz` package has been written by
our professor, hence you would need to contact him to ask him about the
license details if you were to use or distribute this code. You may **not**
use or redistribute The included 3D models or textures, other than for
testing this application on your machine.

Some useful resources
---------------------
- [LWJGL Wiki / Tutorials](http://wiki.lwjgl.org/wiki/Main_Page)
- [OpenGL Basic Tutorials](http://www.opengl-tutorial.org/beginners-tutorials/)
- [OpenGL Intermediate Tutorials](http://www.opengl-tutorial.org/intermediate-tutorials/)
- [Anton's OpenGL 4 Tutorials](http://antongerdelan.net/opengl/)
- [OpenGL Introduction](https://open.gl/)
- [Phong Shader](https://www.opengl.org/sdk/docs/tutorials/ClockworkCoders/lighting.php)
- [Basics of GLSL 4 shaders](http://www.gamedev.net/page/resources/_/technical/opengl/the-basics-of-glsl-40-shaders-r2861)
- [songho: Projection Matrix Details](http://www.songho.ca/opengl/gl_projectionmatrix.html)

¹) we/us = Julien [`domsson`](https://github.com/domsson) Dau and [`robertkoerber`](https://github.com/robertkoerber)
