carage
======
Our simple car 'game' for the computer graphics course at HTW.

Controls
--------
- `W`, `S`: Spin Wheels (Car won't move though)
- `A`, `D`: Steer Wheels
- `O`: Toggle Camera *O*verlay
- `L`: Toggle *L*amp (Turn light on/off)
- `P`: Toggle Camera Auto-*P*anning
- `+`, `-`: Increase / Decrease Light Intensity
- `←`, `→`: Pan Camera left/right (when Auto-Panning is off)
- `↑`, `↓`: Pitch camera up/down

Notes on engine design
----------------------
- Models are expected to be in OBJ format (v, vt, vn and f will be read)
- When exporting with Blender, you might need to set scale to 0.1
- The axle height of a car body should be aligned with the Y-axis
- An object's Texture has to go by the same name as the object
- A car has a .properties file with the same name as its object
- Every object can only have one single Texture / Material
- The Renderer always assumes the VAO/VBO data to hold Tris
- The Renderer can handle pure VAOs as well as VAOs with IBOs
- The Renderer assumes that every Asset is unwrapped and textured

Tasks / must have
-----------------
- ~~Zwei verschiedene "volldimensionale" 3D-Objekte~~
- ~~Verwendung von Transformationen (translate, rotate, scale) für eine zeitgesteuerte Animation~~
- ~~Interaktionsmöglichkeit über Tastatur oder Maus~~ (½)
- ~~Oberfläche mit Textur (Bilddatei im Projekt enthalten)~~ (½)
- ~~Vertex- und Fragment-Shader für Phong-Schattierung~~
- ~~Vertex- und Fragment-Shader für eine einfache prozedurale Textur~~
- ~~Verwendung einer "eigenen" Matrix-Klasse (z.B. Matrix4f) in Shadern a la OpenGL 3~~
- ~~Nutzung weiterer OpenGL 3-Techniken (Modelle als VAO/VBO, GLSL ab Version 1.5)~~

½: Will only give half as much points as the other tasks

Nice to have
------------
- Ability to have more than one Light Source
- Ability to have more than one Texture / Material per Asset
- Proper Asset groups (A scene graph, probably?)
- Switching Car parts (Car Configurator)
- Send pre-multiplied ModelView matrix to Shader
- Light attenuation with distance
- Discuss: per-vertex or per-fragment lighting?
- Actual game mode where you can drive the car

Resources
---------
- [LWJGL Wiki / Tutorials](http://wiki.lwjgl.org/wiki/Main_Page)
- [OpenGL Basic Tutorials](http://www.opengl-tutorial.org/beginners-tutorials/)
- [OpenGL Intermediate Tutorials](http://www.opengl-tutorial.org/intermediate-tutorials/)
- [Anton's OpenGL 4 Tutorials](http://antongerdelan.net/opengl/)
- [OpenGL Introduction](https://open.gl/)
- [Phong Shader](https://www.opengl.org/sdk/docs/tutorials/ClockworkCoders/lighting.php)
- [Basics of GLSL 4 shaders](http://www.gamedev.net/page/resources/_/technical/opengl/the-basics-of-glsl-40-shaders-r2861)
- [songho: Projection Matrix Details](http://www.songho.ca/opengl/gl_projectionmatrix.html)
