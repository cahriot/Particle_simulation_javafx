// /******************************************************************************
//  *  Compilation:  javac CollisionSystem.java
//  *  Execution:    java CollisionSystem n               (n random GlobleVa.particles)
//  *                java CollisionSystem < input.txt     (from a file) 
//  *  Dependencies: javafxUtil.java Particle.java MinPQ.java
//  *  Creates n random GlobleVa.particles and simulates their motion according
//  *  to the laws of elastic collisions.
//  *
//  ******************************************************************************/
package com.ustc.cs.java;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.NamedArg;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The {@code CollisionSystem} class represents a collection of GlobleVa.particles moving
 * in the unit box, according to the laws of elastic collision. This event-based
 * simulation relies on a priority queue.
 */
public class CollisionSystem extends Application {
    private static final double HZ = 0.5; // number of redraw events per clock tick

    private MinPQ<Event> priorityQueue; // the priority queue
    public AnimationTimer animationTimer;
    private double t = 0.0; // simulation clock time
    private Particle[] particles; // the array of GlobleVa.particles
    public int limit = 10000;

    /**
     * Initializes a system with the specified collection of GlobleVa.particles. The
     * individual GlobleVa.particles will be mutated during the simulation.
     *
     * @param GlobleVa.particles the array of GlobleVa.particles
     */
    public CollisionSystem(Particle[] particles) {
        this.particles = GlobleVa.gloparticles.clone(); // defensive copy
    }

    public CollisionSystem() {
        this.particles = GlobleVa.gloparticles.clone(); // defensive copy
    }

    // updates priority queue with all new events for particle a
    public void predict(Particle a, double limit) {
        if (a == null)
            return;

        // particle-particle collisions
        for (int i = 0; i < GlobleVa.num; i++) {
            double dt = a.timeToHit(GlobleVa.gloparticles[i]);
            if (t + dt <= limit)
                priorityQueue.insert(new Event(t + dt, a, GlobleVa.gloparticles[i]));
        }

        // particle-wall collisions
        double dtX = a.timeToHitVerticalWall();
        double dtY = a.timeToHitHorizontalWall();
        if (t + dtX <= limit)
            priorityQueue.insert(new Event(t + dtX, a, null));
        if (t + dtY <= limit)
            priorityQueue.insert(new Event(t + dtY, null, a));
    }

    // redraw all GlobleVa.particles
    private void redraw(double limit) {
        JavafxUtil.clear();
        for (int i = 0; i < GlobleVa.num; i++) {
            GlobleVa.gloparticles[i].draw();
        }
        JavafxUtil.show();
        JavafxUtil.pause(20);
        if (t < limit) {
            priorityQueue.insert(new Event(t + 1.0 / HZ, null, null));
        }
    }

    
    synchronized public void predictAll() {
        // initialize PQ with collision events and redraw event
        priorityQueue = new MinPQ<Event>();
        for (int i = 0; i < GlobleVa.num; i++) {
            predict(GlobleVa.gloparticles[i], limit);
        }
        priorityQueue.insert(new Event(0, null, null)); // redraw event
    }

    /**
     * Simulates the system of GlobleVa.particles for the specified amount of time.
     *
     * @param limit the amount of time
     */
    public void simulate() {

        predictAll();
        // animationTimer = new AnimationTimer();

        new AnimationTimer(){
        
            @Override
            public void handle(long arg0) {
                // TODO Auto-generated method stub
            // get impending event, discard if invalidated
            Event e = priorityQueue.delMin();
            if (!e.isValid())
                return;
            Particle a = e.a;
            Particle b = e.b;

            // physical collision, so update positions, and then simulation clock
            for (int i = 0; i < GlobleVa.num; i++)
                GlobleVa.gloparticles[i].move(e.time - t);
            t = e.time;

            // process event
            if (a != null && b != null)
                a.bounceOff(b); // particle-particle collision
            else if (a != null && b == null)
                a.bounceOffVerticalWall(); // particle-wall collision
            else if (a == null && b != null)
                b.bounceOffHorizontalWall(); // particle-wall collision
            else if (a == null && b == null)
                redraw(limit); // redraw event

            // update the priority queue with new collisions involving a or b
            predict(a, limit);
            predict(b, limit);

            }
        }.start();
    }


    /***************************************************************************
     * An event during a particle collision simulation. Each event contains the time
     * at which it will occur (assuming no supervening actions) and the GlobleVa.particles a
     * and b involved.
     *
     * - a and b both null: redraw event - a null, b not null: collision with
     * vertical wall - a not null, b null: collision with horizontal wall - a and b
     * both not null: binary collision between a and b
     *
     ***************************************************************************/
    private static class Event implements Comparable<Event> {
        private final double time; // time that event is scheduled to occur
        private final Particle a, b; // GlobleVa.particles involved in event, possibly null
        private final int countA, countB; // collision counts at event creation

        // create a new event to occur at time t involving a and b
        public Event(double t, Particle a, Particle b) {
            this.time = t;
            this.a = a;
            this.b = b;
            if (a != null)
                countA = a.count();
            else
                countA = -1;
            if (b != null)
                countB = b.count();
            else
                countB = -1;
        }

        // compare times when two events will occur
        public int compareTo(Event that) {
            return Double.compare(this.time, that.time);
        }

        // has any collision occurred between when event was created and now?
        public boolean isValid() {
            if (a != null && a.count() != countA)
                return false;
            if (b != null && b.count() != countB)
                return false;
            return true;
        }

    }

    @Override
    public void start(Stage stage) throws Exception {

        JavafxUtil.setStage(stage);
        //JavafxUtil.setPenColor(Color.CYAN);
        JavafxUtil.setCanvasSize(600, 600);
        JavafxUtil.filledCircle(0.2, 0.2, 0.2);
        JavafxUtil.setButton();
        JavafxUtil.show();

        GlobleVa.num=10;
        //GlobleVa.gloparticles = new Particle[100];
        for (int i = 0; i < GlobleVa.num; i++)
            GlobleVa.gloparticles[i] = new Particle();
        //GlobleVa.gloparticles[GlobleVa.num-1]=new Particle(0.2,0.2,0.0,0.0,0.05,0.5,Color.RED);
        CollisionSystem system = new CollisionSystem(GlobleVa.gloparticles);

        system.simulate();
        SecondStage();
    }
    /**
     * Unit tests the {@code CollisionSystem} data type. Reads in the particle
     * collision system from a standard input (or generates {@code N} random
     * GlobleVa.particles if a command-line integer is specified); simulates the system.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        launch(args);
    }
    
    public void SecondStage(){
        TextArea ta = new TextArea();
        ta.setText("Change the parameter .\n Then\nClick \""
                + "Add\" button to add a particle.\n"
                + "Click \"Clear One\" button to clear the button you just add");
        
        ta.setPrefColumnCount(40);
        ta.setPrefRowCount(8);
        ta.setWrapText(true);
        ta.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 20));
        ta.setEditable(false);
        Pane pane2 = new Pane(ta);
        Scene scene2= new Scene(pane2);
        
        Stage stage2 = new Stage();
        stage2.setScene(scene2);
        stage2.setTitle("Instructions");
        stage2.setResizable(false);
        stage2.show();
    }

}
