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
- Vertex- und Fragment-Shader für Phong-Schattierung *AT WORK*
- Vertex- und Fragment-Shader für eine einfache prozedurale Textur _TODO_
- Verwendung einer "eigenen" Matrix-Klasse (z.B. Matrix4f) in Shadern a la OpenGL 3. **CHECK**
- Nutzung weiterer OpenGL 3-Techniken (Modelle als VAO/VBO, GLSL ab Version 1.5) **CHECK**

Die mit (1/2) gekennzeichneten Teile zählen halb so viel wie die anderen.
