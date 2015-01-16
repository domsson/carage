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
