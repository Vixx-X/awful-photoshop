/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject;

public class ImagePortion {
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    
    public ImagePortion(int x, int y, int width, int height){
        this.x1 = x;
        this.y1 = y;
        this.x2= x+width;
        this.y2 = y+height;
    }

}
