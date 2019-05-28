/**
 * Robbie Sollie - Steganography.java - EGR226 - CBU - 9/29/18
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Steganography {
    public static void main(String[] args) throws IOException {
        System.out.println("Would you like to: ");
        System.out.println("1: Encode a message in an image");
        System.out.println("2: Decode a message from an image");
        System.out.print("> ");
        Scanner scn = new Scanner(System.in);
        switch (scn.nextInt()) {
            case 1:
                encode();
                break;
            case 2:
                decode();
                break;
        }
    }

    private static void encode() throws IOException {
        Scanner userInput = new Scanner(System.in);
        System.out.print("Enter your secret message: ");
        String secretMessage = userInput.nextLine();

        int[] message = new int[secretMessage.length() * 8 + 8];
        int iSecret = 0;
        for (int i = 0; i < secretMessage.length(); i++) {
            char c = secretMessage.charAt(iSecret);
            for (int j = 0; j < 8; j++) {
                message[(i * 8) + j] = c % 2;
                c /= 2;
            }
            iSecret++;
        }
        iSecret = 0;

        System.out.print("Enter the original image file name: ");
        BufferedImage testImage = ImageIO.read(new File(userInput.nextLine()));

        for (int i = 0; i < testImage.getWidth(); i++) {
            for (int j = 0; j < testImage.getHeight(); j++) {
                Color color = new Color(testImage.getRGB(i, j));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                red -= red % 2;
                green -= green % 2;
                blue -= blue % 2;
                red += message[(iSecret++) % message.length];
                green += message[(iSecret++) % message.length];
                blue += message[(iSecret++) % message.length];
                Color newColor = new Color(red, green, blue);
                testImage.setRGB(i, j, newColor.getRGB());
            }
        }
        System.out.print("Enter the file name for the output image: ");
        ImageIO.write(testImage, "png", new File(userInput.nextLine()));
    }

    private static void decode() throws IOException {
        System.out.print("Enter an image file name to read: ");
        Scanner userInput = new Scanner(System.in);
        BufferedImage testImage;
        testImage = ImageIO.read(new File(userInput.nextLine()));

        int[] decoded = new int[testImage.getHeight() * testImage.getWidth() * 3];
        int idx = 0;
        for (int i = 0; i < testImage.getWidth(); i++) {
            for (int j = 0; j < testImage.getHeight(); j++) {
                Color color = new Color(testImage.getRGB(i, j));
                decoded[idx] = color.getRed() % 2;
                decoded[idx + 1] = color.getGreen() % 2;
                decoded[idx + 2] = color.getBlue() % 2;
                idx += 3;
            }
        }
        System.out.println();
        for (int i = 0; i < decoded.length; i += 8) {
            int character = 0;
            for (int j = i + 7; j >= i; j--) {
                character += decoded[j];
                character *= 2;
            }
            if (character == 0)
                break;
            System.out.print((char) (character / 2));
        }
    }
}
