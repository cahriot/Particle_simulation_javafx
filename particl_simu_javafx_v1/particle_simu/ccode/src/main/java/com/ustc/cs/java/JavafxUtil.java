package com.ustc.cs.java;

import java.io.ObjectInputStream.GetField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * JavafxUtil
 */
public class JavafxUtil {
    private static int WIDTH;
    private static int HEIGHT;

    private static Stage stage;

    private static CollisionSystem system;
    
    private static Scene scene;

    private static Group root;

    private static Group buttons;

    private static Color color;

    public static void setStage(Stage stage) {
        JavafxUtil.stage = stage;
    }

    public static void setSystem(CollisionSystem system) {
        JavafxUtil.system = system;
    }

    public static void setCanvasSize(int width, int height) {
        // scene = new Scene(root)
        buttons = new Group();
        root = new Group(buttons);
        WIDTH = width;
        HEIGHT = height;
        scene = new Scene(root, WIDTH + 400, HEIGHT);
    }

    public static void setButton() {
        Button button1 = new Button("Add");
        Button button2 = new Button("Clear One");
        Button button3 = new Button("Clear All");

        // Creating a Grid Pane
        GridPane gridPane = new GridPane();

        // Setting size for the pane
        gridPane.setMinSize(400, 600);

        // Setting the padding
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        // Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        // Setting the Grid alignments
        // gridPane.setAlignment(Pos.TOP_CENTER);

        // Arranging all the nodes in the grid
        gridPane.add(button1, 0, 3);
        gridPane.add(button2, 1, 3);
        gridPane.add(button3, 1, 11);
        gridPane.setLayoutX(600);
        gridPane.setLayoutY(0);

        // Styling nodes
        button1.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        button2.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        gridPane.setStyle("-fx-background-color: BEIGE;");

        // Creating a scene object
        root.getChildren().add(gridPane);

        // Adding scene to the stage
        TextField tfe = new TextField("0.02");
        Label lbe = new Label("radius:     ", tfe);
        lbe.setContentDisplay(ContentDisplay.RIGHT);
        Slider sle = new Slider(0.01, 0.10, 0.02);
        sle.setShowTickLabels(true);

        // add listner
        sle.valueProperty().addListener(ov -> {
            GlobleVa.radius = sle.getValue();
            tfe.setText(String.valueOf(GlobleVa.radius));
        });
        tfe.setOnAction(e -> {
            GlobleVa.radius = Double.valueOf(tfe.getText());
            sle.setValue(GlobleVa.radius);
        });
        gridPane.add(tfe, 1, 7);
        gridPane.add(lbe, 1, 7);
        gridPane.add(sle, 1, 8);
        // Displaying the contents of the stage

        // mass field
        TextField tfm = new TextField("0.5");
        Label lbm = new Label("mass:     ", tfm);
        lbm.setContentDisplay(ContentDisplay.RIGHT);
        Slider slm = new Slider(0, 1.0, 0.5);
        slm.setShowTickLabels(true);

        // add listner
        slm.valueProperty().addListener(ov -> {
            GlobleVa.mass = slm.getValue();
            tfm.setText(String.valueOf(GlobleVa.mass));
        });
        tfm.setOnAction(e -> {
            GlobleVa.mass = Double.valueOf(tfm.getText());
            slm.setValue(GlobleVa.mass);
        });
        gridPane.add(tfm, 1, 9);
        gridPane.add(lbm, 1, 9);
        gridPane.add(slm, 1, 10);
        // vx field
        TextField tfvx = new TextField("0.003");
        Label lbvx = new Label("vx: ", tfvx);
        lbvx.setContentDisplay(ContentDisplay.RIGHT);
        Slider slvx = new Slider(-0.007, 0.007, 0.003);
        slvx.setShowTickLabels(true);

        // add listner
        slvx.valueProperty().addListener(ov -> {
            GlobleVa.vx = slvx.getValue();
            tfvx.setText(String.valueOf(GlobleVa.vx));
        });
        tfvx.setOnAction(e -> {
            GlobleVa.vx = Double.valueOf(tfvx.getText());
            slvx.setValue(GlobleVa.vx);
        });
        gridPane.add(tfvx, 1, 14);
        gridPane.add(lbvx, 1, 14);
        gridPane.add(slvx, 1, 15);

         // vx field
         TextField tfvy = new TextField("0.003");
         Label lbvy = new Label("vy: ", tfvy);
         lbvy.setContentDisplay(ContentDisplay.RIGHT);
         Slider slvy = new Slider(-0.007, 0.007, 0.003);
         slvy.setShowTickLabels(true);
 
         // add listner
         slvy.valueProperty().addListener(ov -> {
             GlobleVa.vy = slvy.getValue();
             tfvy.setText(String.valueOf(GlobleVa.vy));
         });
         tfvx.setOnAction(e -> {
             GlobleVa.vy = Double.valueOf(tfvy.getText());
             slvy.setValue(GlobleVa.vy);
         });
         gridPane.add(tfvy, 1, 16);
         gridPane.add(lbvy, 1, 16);
         gridPane.add(slvy, 1, 17);
 

        final ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.CORAL);

        colorPicker.setOnAction((ActionEvent t) -> {
            GlobleVa.color=colorPicker.getValue();
        });
        gridPane.add(colorPicker,1,12);

        button1.setOnAction(e -> {
            GlobleVa.num++;
            // GlobleVa.gloparticles[GlobleVa.num-1]=new
            // Particle(0.2,0.2,0.0,0.0,0.05,0.5,Color.RED);
            GlobleVa.gloparticles[GlobleVa.num - 1] = new Particle(0.2, 0.2, GlobleVa.vx, GlobleVa.vy, GlobleVa.radius, GlobleVa.mass,
                    GlobleVa.color);
        });
        button2.setOnAction(e -> {
            GlobleVa.num--;
            //system.predictAll();
        });
        button3.setOnAction(e -> {
            GlobleVa.num = 0;
        });
        // Label label1 = new Label();
        // label1.setText("Number: "+ Integer.toString(GlobleVa.num)); 
        // gridPane.add(label1,1,13);
    }

    public static void clear() {
        buttons.getChildren().clear();
    }

    public static void filledCircle(double rx, double ry, double radius) {
        Circle circle = new Circle();
        circle.setCenterX(rx * WIDTH);
        circle.setFill(color);
        // circle.setC
        circle.setCenterY(ry * HEIGHT);
        circle.setRadius(radius * WIDTH);
        buttons.getChildren().add(circle);
    }

    public static void show() {
        // root.getChildren().add(buttons);
        stage.setScene(scene);
        stage.setTitle("Simulation");
        stage.show();
    }

    public static void pause(int t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
    }

    public static void setPenColor(Color c) {
        color = c;
    }
}