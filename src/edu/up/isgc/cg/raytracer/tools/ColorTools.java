package edu.up.isgc.cg.raytracer.tools;

import java.awt.*;

public class ColorTools {
    private ColorTools(){}
    public static Color addColor(Color original, Color otherColor) {
        float red = (float) Math.clamp((original.getRed() / 255.0) + (otherColor.getRed() / 255.0), 0.0, 1.0);
        float green = (float) Math.clamp((original.getGreen() / 255.0) + (otherColor.getGreen() / 255.0), 0.0, 1.0);
        float blue = (float) Math.clamp((original.getBlue() / 255.0) + (otherColor.getBlue() / 255.0), 0.0, 1.0);
        return new Color(red, green, blue);
    }

    public static Color addWeightedColor(Color color1, Color color2, double weight){
        double invWeight = 1 - weight;
        Color weightedColor1 = new Color((int) (color1.getRed() * weight),
                (int) (color1.getGreen() * weight),
                (int) (color1.getBlue() * weight));
        Color weightedColor2 = new Color((int) (color2.getRed() * invWeight),
                (int) (color2.getGreen() * invWeight),
                (int) (color2.getBlue() * invWeight));
        return addColor(weightedColor1, weightedColor2);
    }

    public static Color getComplementaryColor(Color color) {
        int red = 255 - color.getRed();
        int green = 255 - color.getGreen();
        int blue = 255 - color.getBlue();
        return new Color(red, green, blue);
    }

    public static Color scaleColor(Color color, double scale) {
        int r = (int) Math.clamp(color.getRed() * scale, 0, 255);
        int g = (int) Math.clamp(color.getGreen() * scale, 0, 255);
        int b = (int) Math.clamp(color.getBlue() * scale, 0, 255);
        return new Color(r, g, b);
    }

    public static Color blendColors(Color color1, Color color2) {
        int r = (color1.getRed() + color2.getRed()) / 2;
        int g = (color1.getGreen() + color2.getGreen()) / 2;
        int b = (color1.getBlue() + color2.getBlue()) / 2;
        return new Color(r, g, b);
    }
}
