# Containerlocalisation

Usage:

Simply Insert points by clicking into the white space as drawing panel, and use the Buttons on the bottom to run the algorithms. Colormanagement on the right and on the top reset the points or generate a random set of points.

Quick Explanation what the .java files do:

| .java Files  | Usage  |
|---|---|
| Colorbutton  | manage Colors of Points  |
| Comparator X/Y  | to sort a pointlist with their x- or respectively y- Coordinate  |
| CoordPanel  | Panel where points are inserted with Mouseclicks and Containers are drawn  |
| CSETA  | "Color Spanning Equilateral Triangle Annulus" Algorithm [2] |
| CSETAButton  | CSETA Button for real time runtime  |
| CSETAStepButton  | CSETA Step by Step Algorithm Button  |
| CSET  | "Color Spanning Equilateral Triangle" Algorithm [2] |
| CSETButton  |  CSET Button for real time runtime |
| CSETStepButton  | CSET Step by Step Algorithm Button  |
| GeneratePoints  | Button with generate Points Function  |
| Layout  | Panelmanagement file for Applet  |
| Line  | Line-Class as object |
| OptRectangle  | Color Spanning Rectangle Algorithm [1] |
| OptRectangleButton  | CSET Button for real time runtime  |
| OptRectStepButton  | CSRect Step by Step Algorithm Button  |
| Point  | Point-class as object inserted by clicking in CoordPanel  |
| PointMouseListener  | File for Actions that are made by the mouse such as inserting or drag and drop of a point  |
| Rectangle  | Rectangle-Class  |
| Triangle  | Triangle Class  |




References for the algorithms:

[1]  “Smallest Color-Spanning Objects” , Manuel Abellanas, Ferran Hurtado, Christian Icking,  Rolf Klein, Elmar Langetepe, Lihong Ma, Bel´en Palop, Vera Sacristan , April 2001

[2] “Color Spanning Annulus: Square, Rectangle and Equilateral Triangle” by Ankush Acharyya, Subhas C. Nandy and Sasanka Roy, Indian Statistical Institute, Kolkata, India, 2016

