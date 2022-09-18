package com.marcelhomsak.transparentsubtitles;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class FirstWindowController implements Initializable {
    /* ---------------------------------- */
    @FXML
    private Label subtitle;
    @FXML
    private AnchorPane ap;
    @FXML
    private Label labelCounter;

    private static int index;
    private static ArrayList<String> arrayListSubtitles = new ArrayList<>();
    /* ---------------------------------- */

    public void readSubtitles(File file) {
        index = 0;
        arrayListSubtitles = new ArrayList<>();

        try (Scanner sc = new Scanner(new FileInputStream(file), "Windows-1250")) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.contains("-->")) {  // universal time separator for subtitles files
                    line = sc.nextLine();
                    StringBuilder paragraph = new StringBuilder();
                    while (!line.equals("")) {
                        paragraph.append(line).append("\n");
                        try {
                            line = sc.nextLine();
                        }
                        catch (Exception e) {
                            System.out.println("Error inside: " + e);
                            break;
                        }
                    }
                    arrayListSubtitles.add(paragraph.toString());
                }
            }
            if (arrayListSubtitles.size() > 0) {
                subtitle.setText(arrayListSubtitles.get(0));
                updateLabelCounter();
            }
            else {
                subtitle.setText("Illegal File");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void updateLabelCounter() {
        labelCounter.setText(String.format("%d / %d", index + 1, arrayListSubtitles.size()));
    }

    public void onKeyPressed(KeyEvent event) {
        KeyCode kc = event.getCode();
        switch (kc) {
            case O: {
                try {
                    FileChooser fc = new FileChooser();
                    fc.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("SRT", "*.srt")
                    );
                    File file = fc.showOpenDialog(ap.getScene().getWindow());
                    if (file != null)
                        readSubtitles(file);
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            }

            case LEFT: {
                try {
                    if (index > 0) {
                        index--;
                        subtitle.setText(arrayListSubtitles.get(index));
                        updateLabelCounter();
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            }

            case RIGHT: {
                try {
                    if (index < arrayListSubtitles.size() - 1) {
                        index++;
                        subtitle.setText(arrayListSubtitles.get(index));
                        updateLabelCounter();
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            }

            case PLUS: {
                try {
                    subtitle.setFont(new Font(subtitle.getFont().getSize() + 1));
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            }

            case MINUS: {
                try {
                    subtitle.setFont(new Font(subtitle.getFont().getSize() - 1));
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            }

            case SHIFT: {
                try {
                    if (ap.getOpacity() <= 0.9)
                        ap.setOpacity(ap.getOpacity() + 0.1);
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            }

            case CONTROL: {
                try {
                    if (ap.getOpacity() >= 0.1)
                        ap.setOpacity(ap.getOpacity() - 0.1);
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            }
        }
    }

    public void exit() {
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            subtitle.setFocusTraversable(true);
            subtitle.setText("Press O for selecting subtitles file\n" +
                             "Press + or - to increase or decrease font size\n" +
                             "Press Left or Right (Arrow Keys) for previous or next subtitle\n" +
                             "Press Shift or CTRL to increase or decrease opacity");
            ap.setOnKeyPressed(this::onKeyPressed);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}