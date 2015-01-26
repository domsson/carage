carage
======

Our simple car 'game' for the computer graphics course at HTW.


Notes on engine design
----------------------

- Model format is OBJ (Wavefront)
- Normals can and should be exported, too
- An Object's front should face towards negative X
- For cars, the Y-Axis should mark the height of its axles
- Other objects should be build around 0,0,0 (origin)
- You might need to set scale to 0.1 when exporting with Blender
- Every object can only have one single texture
- An object's texture has to go by the same name as the object
- A car has a .properties file with the same name as its object
- The OBJ Importer can handle Tris and Quads, nothing else
- The Renderer always assumes the VAO/VBO data to hold Tris
- The Renderer can handle pure VAOs as well as VAOs with IBOs


Task Checklist
--------------
- Zwei verschiedene "volldimensionale" 3D-Objekte **CHECK**
- Verwendung von Transformationen (translate, rotate, scale) für eine zeitgesteuerte Animation (z.B. fortlaufende Transformation) **CHECK**
- Interaktionsmöglichkeit über Tastatur oder Maus (1/2) **CHECK**
- Oberfläche mit Textur (Bilddatei im Projekt enthalten) (1/2) **CHECK**
- Vertex- und Fragment-Shader für Phong-Schattierung **CHECK**
- Vertex- und Fragment-Shader für eine einfache prozedurale Textur **TODO**
- Verwendung einer "eigenen" Matrix-Klasse (z.B. Matrix4f) in Shadern a la OpenGL 3. **CHECK**
- Nutzung weiterer OpenGL 3-Techniken (Modelle als VAO/VBO, GLSL ab Version 1.5) **CHECK**

Die mit (1/2) gekennzeichneten Teile zählen halb so viel wie die anderen.


To Do (Have to)
---------------
- Camera orbits around car ("zeitgesteuerte Animation")
- Light parameters from Java instead of hardcoded in the shader **DONE**
- Procedural texture (new fragment/vertex shader); Ideas: TV screen asset or surveillance cam overlay
- "Material" class for assets (to save/select ShaderProgram and "hardness") **DONE**

Nice to have (later)
--------------------
- More than one light source?
- Switching parts of the car?
- More assets (nicer Garage)
- Driving car, bitches!
- Send pre-multiplied ModelView matrix to shader (instead of both separately)
- Light attenuation with distance
- Discuss: per-vertex or per-fragment lighting?

Resources
---------

- [LWJGL Wiki / Tutorials](http://wiki.lwjgl.org/wiki/Main_Page)
- [OpenGL Basic Tutorials](http://www.opengl-tutorial.org/beginners-tutorials/)
- [OpenGL Intermediate Tutorials](http://www.opengl-tutorial.org/intermediate-tutorials/)
- [Anton's OpenGL 4 Tutorials](http://antongerdelan.net/opengl/)
- [Phong Shader](https://www.opengl.org/sdk/docs/tutorials/ClockworkCoders/lighting.php)
- [Basics of GLSL 4 shaders](http://www.gamedev.net/page/resources/_/technical/opengl/the-basics-of-glsl-40-shaders-r2861)
- [songho: Projection Matrix Details](http://www.songho.ca/opengl/gl_projectionmatrix.html)
