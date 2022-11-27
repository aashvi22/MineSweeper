package org.cis120.minesweeper;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class MSGameTest {

    MineSweeper ms = new MineSweeper(8,8,18);

    public boolean checkEquals(String filestr1, String filestr2) {
        boolean result = true;
        File file1 = new File(filestr1);
        File file2 = new File(filestr2);
        try {
            Scanner reader1 = new Scanner(file1);
            Scanner reader2 = new Scanner(file2);
            boolean first = true;
            while (reader1.hasNext() && reader2.hasNext()) {
                if (first) {
                    String [] parts1 = reader1.nextLine().split(" ");
                    String [] parts2 = reader2.nextLine().split(" ");
                    result = result && parts1[0].equals(parts2[0]);
                } else {
                    String str1 = reader1.nextLine();
                    String str2 = reader2.nextLine();
                    result = result && str1.equals(str2);
                }
                first = false;
            }
            reader1.close();
            reader2.close();
        } catch (IOException e) {
            System.out.println("check equals: " + e);
        }
        return result;
    }



    @Test
    public void testFirstClickRecursion() {
        ms.loadFilePath("testfirstrec");
        ms.playTurn(6,1);
        ms.saveBoardToFile();

        assertTrue(checkEquals("GameBoardText", "firstrec2"));
    }

    @Test
    public void testCheckWinner() {
        ms.loadFilePath("testwinnerText");
        ms.playTurn(10, 3);
        if (ms.checkWinner()) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
        assertTrue(ms.checkWinner());
    }

    @Test
    public void testStillPlaying() {

        ms.loadFilePath("teststillplayingtext");
        assertFalse(ms.checkWinner());
        assertFalse(ms.getHasLost());
    }
    @Test
    public void testLost() {

        ms.loadFilePath("testlostText");
        ms.playTurn(1,0);
        assertFalse(ms.checkWinner());
        assertTrue(ms.getHasLost());
    }

    @Test
    public void testLoadFileNotFound() {
        ms.loadFilePath("testfirstrec");
        ms.loadFilePath("this file doesnt exist");
        ms.playTurn(6,1);
        ms.saveBoardToFile();

        assertTrue(checkEquals("GameBoardText", "firstrec2"));
    }

}
